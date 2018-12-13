package kr.co.moumou.smartwords.stt;

import android.os.StatFs;

import com.Diotek.STT.EduEng.IDioSttEduENG;
import com.Diotek.STT.MediaLib.Mp3util;
import com.Diotek.STT.SDK.DioSDKProduct;
import com.Diotek.STT.common.ALOG;
import com.Diotek.STT.common.CommonFunc;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public final class DioSTTEduENG4MOUMOU {

	private static IDioSttEduENG dioSttEduENG = null;

	public static IDioSttEduENG getDioSttEduENG(){
		ALOG.d("getDioSttEduENG");
		if(dioSttEduENG==null){
			try {

				dioSttEduENG = DioSDKProduct.getAPI(IDioSttEduENG.class, "20150529_DioSTT_Edu_ENG_Android");
			} catch (Exception e) {
				dioSttEduENG = null;
				e.printStackTrace();
			} 

			ALOG.d("getDioSttEduENG : " + dioSttEduENG);
		}
		return dioSttEduENG;
	}
	

	
	/**
	 * mp3 파일을 이용하여 Batch를 수행한다. <br>
	 * 
	 * {@link com.Diotek.STT.EduEng.IDioSttEduENG#RecognitionBatch}
	 * 함수를 이용하며, HciFile가 있을 경우에 전사툴의 결과를 반영하여 Batch를 수행한다.
	 * @param HciFile
	 * @param Mp3File
	 * @return
	 * @throws IOException
	 */
	public static int RecognitionBatchFromMp3WithHCIFormat(File HciFile, File Mp3File) throws IOException {
		IDioSttEduENG stt = getDioSttEduENG();
		if(stt==null) return -100;
		if(Mp3File==null|| !Mp3File.exists()) return -101;
		
		Mp3util mp3util = new Mp3util();		
		short buff[] = mp3util.getDecodePCMshort(Mp3File);
		if(buff==null||buff.length<=0) return -102;
		
		int sendSize=buff.length*2;
		
		boolean isHciFormat = false;
		byte hciBuff[] = new byte[0];
		if(HciFile!=null&& HciFile.exists() && HciFile.length()>0){
			hciBuff = CommonFunc.readFilebyte(HciFile.getAbsolutePath());
			if(hciBuff!=null && hciBuff.length>0) isHciFormat = true;
			sendSize +=hciBuff.length;
		}
		
		ByteBuffer sendBuff = ByteBuffer.allocate(sendSize);
		sendBuff.order(ByteOrder.nativeOrder());
		if(isHciFormat) sendBuff.put(hciBuff);
		for (int i = 0; i < buff.length; i++) {
			sendBuff.putShort(buff[i]);
		}
		
		return stt.RecognitionBatch(buff, isHciFormat?1:0);
	}
	
	/**
	 * PCM 버퍼를 인코딩 하여 파일로 쓴다
	 * @param pcmbuff PCM 버퍼
	 * @param targetPath 저장될 파일 경로
	 * @return : >0 인코딩 성공 <br> 그외 에러코드 반환 
	 */
	public static int encodeMp3BufferToFile(short[] pcmbuff, String targetPath){
		Mp3util mp3util = new Mp3util();		
		return mp3util.encodeBufferToFile(pcmbuff, targetPath);		
	}
		
	/**
	 * mp3 파일을 이용하여 Batch를 수행한다.
	 * {@link com.Diotek.STT.EduEng.IDioSttEduENG#RecognitionBatch}
	 * @param file mp3 파일
	 * @return 
	 */
	public static int RecognitionBatchFromMp3(File file){
		IDioSttEduENG stt = getDioSttEduENG();
		Mp3util mp3util = new Mp3util();
		short buff[] = mp3util.getDecodePCMshort(file);
		
		return stt.RecognitionBatch(buff, 0);
	}
	

	
	/**
	 * mp3 파일을 pcm 형식으로 decode 한다.
	 * 16000k 기준 1chanel, 1분 이내의 음성 파일
	 * @param file mp3 파일
	 * @return 
	 */
	public static short[] getDecodePCMshort(File file){
		Mp3util mp3util = new Mp3util();
		short buff[] = mp3util.getDecodePCMshort(file);
		return buff;
	}
	

	/**
	 * 외장 메모리 영역이 존제 하는지 검사한다.
	 * @return
	 */
	public static boolean IsExternalMemoryAvailable(){
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 외장 메모리의 사용 가능한 공간을 확인하여 크기를 반환 한다. (Byte 단위)
	 * @return -1: 사용 불가능, >0 : 현 사용 가능한 공간 Byte 크기
	 */
	public static long GetAvailableExternalMemorySize(){
		if(IsExternalMemoryAvailable() == true){
			File path = android.os.Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();

			return availableBlocks * blockSize;
		} else{
			return -1;
		}
	}

}
