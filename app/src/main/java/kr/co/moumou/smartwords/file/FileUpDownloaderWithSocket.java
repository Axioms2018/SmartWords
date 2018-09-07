package kr.co.moumou.smartwords.file;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.listener.SimpleFileConrollerProgressListener;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoMyInfo;

public class FileUpDownloaderWithSocket implements FileUpDownloader{

	private Socket socket;
	private FileControllerProgress listener = new SimpleFileConrollerProgressListener();
	private boolean isCancel = false;

	public FileUpDownloaderWithSocket() {
	}

	@Override
	public void setListener(FileControllerProgress listener){
		this.listener = listener;
	}

	public boolean connect(){
		if(StringUtil.isNull(ApplicationPool.SOCKET_IP)){
			return false;
		}

		if(socket != null){
			if(socket.isConnected()){
				return true;
			}
		}

		SocketAddress socketAddress = new InetSocketAddress(ApplicationPool.SOCKET_IP, ApplicationPool.SOCKET_PORT_UPLOAD);
		socket = new Socket();
		try {
			LogUtil.d(" Socket Adress IP [" + socketAddress + "]" );
			socket.connect(socketAddress, ApplicationPool.SOCKET_TIME_OUT);
		} catch (IOException e) {
			LogUtil.w("SOCKET CANNOT CONNECTED");
			listener.onException(e.toString());
			return false;
		}
		return true;
	}

	public boolean isConnected(){
		if(socket == null){
			return false;
		}
		return socket.isConnected();
	}

	private String getFileName(String path){
		return path.substring((path.lastIndexOf("/")+1), path.length());
	}
	
	@Override
	public void uploadStart(final String filePath, long fileSize) {
		new FileUploader(socket, filePath, fileSize).start();
		
	}

	@Override
	public void downloadStart(Context context, String url, String sessionID, final String fileName, long fileSize, String chaci, final String pCode){
	}

	@Override
	public void cancel() {
		isCancel = true;
	}


	class FileUploader extends Thread {
		private DataInputStream dis;
		private Socket socket;
		private String filePath;
		private long fileSize;

//		File testFile = new File("/mnt/sdcard/temp/Release_20.mp3");

		public FileUploader(Socket socket, String filePath, long fileSize) {
			this.socket = socket;
			this.filePath = filePath;
			PrintWriter sendWriter = null;
			try {
				sendWriter = new PrintWriter(socket.getOutputStream(), true);      // 출력용 스트림
			} catch (IOException e) {
				listener.onException(e.toString());
				LogUtil.e("Upload File Exception : " + e.toString());
				return;
			}
			kr.co.moumou.smartwords.util.LogUtil.e("File Size : " + fileSize);

			HashMap<String, String> data = new HashMap<String, String>();
			data.put(ConstantsCommParameter.Keys.COMMAND, ConstantsCommCommand.COMMAND_105_TRANFER_SEND_FILE);
			data.put(ConstantsCommParameter.Keys.SESSIONID, VoMyInfo.getInstance().getSESSIONID());
			data.put(ConstantsCommParameter.Keys.TEACHERID, VoMyInfo.getInstance().getTEACHERID());
			data.put(ConstantsCommParameter.Keys.FILE_NAME, getFileName(filePath));


			data.put(ConstantsCommParameter.Keys.FILE_SIZE, fileSize+"");
//			data.put(ConstantsCommParameter.Keys.FILE_SIZE, testFile.length()+"");

			
//			CommToSocekt.getInstance().sendBehind("105", ApplicationPool.getGson().toJson(data));
			sendWriter.println(ApplicationPool.getGson().toJson(data) + ConstantsCommURL.SOCKET_POST_FIX);
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}

		}

		@Override
		public void run() {
			try {
				DataOutputStream dos = null;
				try {
					// 데이터 전송용 스트림 생성
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					listener.onException(e.toString());
					e.printStackTrace();
				}
				File file = new File(filePath);
				if(!file.exists()){
					listener.onException("File not exists. path : " + filePath);
				}
				FileInputStream fis = new FileInputStream(file);
//				FileInputStream fis = new FileInputStream(testFile);

				BufferedInputStream bis = new BufferedInputStream(fis);
				long fileLength = file.length();

				int count = 0;
				int len;
				int size = 1024 * 8;
				byte[] data = new byte[size];
				kr.co.moumou.smartwords.util.LogUtil.e("Send Upload");
				long sendFileSize = 0;
				while ((len = bis.read(data)) != -1) {
					sendFileSize += len;
					dos.write(data, 0, len);

					if(count % 150 == 0) {
						count = 0;
						listener.onUploading(((sendFileSize * 100) / fileLength) + "/100 %");
					}
					count++;
				}
				listener.onUploadComplete();

				kr.co.moumou.smartwords.util.LogUtil.e("End Upload FileSize : " + sendFileSize);
				dos.flush();

				Thread.sleep(1000);

//				dos.close();
				bis.close();
				fis.close();

			} catch (Exception e) {
				LogUtil.e("File Upload Exception // "  + e.toString());
			}

			listener.onComplete();
		}
	}
}
