package kr.co.moumou.smartwords.util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kr.co.moumou.smartwords.common.Constant;

/**
 * @Project  : 스마트알파
 * @FileName : MediaDecibelAudio.java
 * @comment  : 데시벨 계산하는 클래스 - startReader() 를 호출하여 데시벨 계산한다.
 */
public class MediaDecibelAudio {
	/**
	 * LogTraceJong 로그켓 : 해당 클래스 출력
	 */
	private String TagClass = getClass().getSimpleName();
	//LogUtil.d("[onCreate 함수] {onCreate 시작} (Bundle : " + savedInstanceState + ")");

//	private static final float MAX_16_BIT = 32768;
//	private static final float MAX_16_BIT = 24500;
//	private static final float MAX_16_BIT = 18000;
	private static final float MAX_16_BIT = 6000;
//	private static final float MAX_16_BIT = 1536;
//	private static final float MAX_16_BIT = 640;
	private static final float FUDGE = 0.6f;
	
	private static final int RECORDER_BPP = 16;
	public static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
	public static final String AUDIO_RECORDER_FOLDER = Constant.PATH_REC_FILE_FOLDER;
	private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
//	private static final int RECORDER_SAMPLERATE = 44100;
//	private static final int RECORDER_SAMPLERATE = 22050;
	private static final int RECORDER_SAMPLERATE = 16000;
//	private static final int RECORDER_SAMPLERATE = 11025;
//	private static final int RECORDER_SAMPLERATE = 8000;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private AudioRecord recorder = null;
	private int bufferSize = 0;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	
	private Listener listener;
	
	public interface Listener {
		int ERR_OK = 0;
		int ERR_INIT_FAILED = 1;
		int ERR_READ_FAILED = 2;

		void onReadComplete(int decibel);
		void onSaveStart();
		void onSaveEnd();
		void onReadError(int error);
	}

	public int calculatePowerDb(byte[] sdata) {
		double sum = 0;
		double sqsum = 0;
		int length = sdata.length /2;
		
		for (int i = 0; i < (length); i++) {
			final long v = sdata[(i*2)] | (sdata[((i*2 +1))] << 8);
			sum += v;
			sqsum += v * v;
		}
		double power = (sqsum - sum * sum / length) / length;

		power /= MAX_16_BIT * MAX_16_BIT;

		double result = Math.log10(power) * 10f + FUDGE;

		//데시벨 표시를 양수로 바꿈     // 절대값은 int mResult = Math.abs(dB);
		double mResult = result - (-100);

		return (int) mResult;
	}

	public MediaDecibelAudio(){
		try{
			bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	//파일경로
	private String getFilename(){
		String filepath = Constant.DATA_PATH;
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}

		return (file.getAbsolutePath() + "/" + Constant.PATH_REC_FILE + AUDIO_RECORDER_FILE_EXT_WAV);
	}

	private String getTempFilename(){
		String filepath = Constant.DATA_PATH;
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}

		File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);

		if(tempFile.exists())
			tempFile.delete();

		return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
	}

	private boolean isSave = false;

	public void startReader(boolean isSave, Listener listener){
		this.listener = listener;
		this.isSave = isSave;
		
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
							RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

		recorder.startRecording();
		
		isRecording = true;

		recordingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				micInputHandle();
			}
		},"AudioRecorder Thread");

		recordingThread.start();
	}

	private void micInputHandle(){
		int checkDecibelCount = 0;
		
		byte data[] = new byte[bufferSize];
		String filename = getTempFilename();
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(filename);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}

		int read = 0;

		if(null != os){
			while(isRecording){
				read = recorder.read(data, 0, bufferSize);
				
				if (read < 0) {
					readError(Listener.ERR_READ_FAILED);
					isRecording = false;
					break;
				}
				
				if(checkDecibelCount >= 3){
					byte[] readData = data.clone();
					checkDecibelCount = 0;
					if(listener != null){
						listener.onReadComplete(calculatePowerDb(readData));
					}
				}else{
					checkDecibelCount++;
				}
				
				if(isSave){
					if(AudioRecord.ERROR_INVALID_OPERATION != read){
						try {
							os.write(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	public void stopReader(){
		if(null != recorder){
			isRecording = false;

			recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
			if(isSave){
				copyWaveFile(getTempFilename(),getFilename());
				deleteTempFile();
				LogUtil.d("{stopReader}");
				isSave = false;
			}
		}
	}
	
	private void readError(int code) {
		if(listener == null){
			return;
		}
		listener.onReadError(code);
	}

	

	private void deleteTempFile() {
		File file = new File(getTempFilename());

		file.delete();
	}

	private void copyWaveFile(String inFilename, String outFilename){
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = RECORDER_SAMPLERATE;
		int channels = 2;
		long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

		byte[] data = new byte[bufferSize];

		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			
			BufferedInputStream bin = new BufferedInputStream(in);
			BufferedOutputStream bout = new BufferedOutputStream(out);
			
			
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;

			//AppLog.logString("File size: " + totalDataLen);

			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);

			
			byte[] buffer = new byte[1024 * 32];
			
			int bytesRead;
			while ((bytesRead = bin.read(buffer, 0, buffer.length)) != -1) {
				bout.write(buffer, 0, bytesRead);
			}
			
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

		byte[] header = new byte[44];

		header[0] = 'R';  // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';  // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1;  // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8);  // block align
		header[33] = 0;
		header[34] = RECORDER_BPP;  // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		out.write(header, 0, 44);
	}
}