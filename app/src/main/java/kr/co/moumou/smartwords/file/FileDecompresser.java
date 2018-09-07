package kr.co.moumou.smartwords.file;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.util.LogUtil;


class FileDecompresser extends AsyncTask<String, Integer, Void> {
	private Calendar startCal;
	private Calendar endCal;

	private FileControllerProgress listener;
	private int zipFileCount = 0;
	private boolean isCancel = false;

	private String pCode;
	private String fileName;
	
	private String fileFullPath;
	private String filePath;
	
	private boolean chant = false;

    public FileDecompresser(String fileName, String pCode){
		this.fileName = fileName;
		this.pCode = pCode;
		String downloadPath = null;
		
		if(FileController.isChant(fileName)){
			filePath = ApplicationPool.PATH_BOOK_RESOURCE + "chant" + File.separator;
			downloadPath = ApplicationPool.PATH_DOWNLOAD_BOOK_RESOURCE  + "chant" + File.separator;
			chant = true;
		}else{
			filePath = ApplicationPool.PATH_BOOK_RESOURCE + pCode + File.separator;
			downloadPath = ApplicationPool.PATH_DOWNLOAD_BOOK_RESOURCE  + pCode + File.separator;
			chant = false;
		}
		fileFullPath = downloadPath + fileName;
		
//		if(fileName.toLowerCase().indexOf("study") > -1 || fileName.toLowerCase().indexOf("work") > -1
//				|| fileName.toLowerCase().indexOf("eval") > -1 || fileName.toLowerCase().indexOf("evla") > -1
//				|| fileName.toLowerCase().indexOf("answer") > -1
//				|| fileName.toLowerCase().indexOf("test") > -1
//				){
//			chant = false;
//		}else{
//			chant = true;
//		}
	}
	
	public void setListener(FileControllerProgress listener) {
		this.listener = listener;
	}

	public int getFileCount() {
		return zipFileCount;
	}

	protected void cancel() {
		isCancel = true;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onPostExecute(Void result) {
		endCal = Calendar.getInstance();
	}

	@Override
	protected Void doInBackground(String... params) {

		try {
			FileInputStream fin = new FileInputStream(fileFullPath);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;

			ZipFile zipFile = new ZipFile(fileFullPath);
			int zipFileTotalCount = zipFile.size();
			int count = 0;

			while (((ze = zin.getNextEntry()) != null) && !isCancel) {
				count++;
				if (listener != null) {
					if(chant){
						listener.onDecompressing((ApplicationPool.PATH_CHANT_RESOURCE + ze.getName()), count, zipFileTotalCount);
					}else{
						listener.onDecompressing((filePath + ze.getName()), count, zipFileTotalCount);
					}
				}
//				LogUtil.d("  Decompress File Path : " + filePath + ze.getName());

				if (ze.isDirectory()) {
					_dirChecker(ze.getName());
				} else {
					zipFileCount++;
					File file = null;
					if(chant){
						file = new File((ApplicationPool.PATH_CHANT_RESOURCE + ze.getName().trim()).replace("/book_1", "").replace("/work_book", ""));
					}else{
						file = new File((filePath + ze.getName().trim()).replace("/book_1", "").replace("/work_book", ""));
					}
//					LogUtil.d(" DeCompress File Path : "+ file.getAbsolutePath());
					if (file.exists()) {
						file.delete();
					}
					if (!file.exists()) {
						checkDir(file);
						file.createNewFile();
					}
					FileOutputStream fout = new FileOutputStream(file);
					BufferedInputStream in = new BufferedInputStream(zin);
					BufferedOutputStream out = new BufferedOutputStream(fout);
					int buffer = 1024 * 10;
					byte b[] = new byte[buffer];
					int n;
					while ((n = in.read(b, 0, buffer)) >= 0) {
						out.write(b, 0, n);
					}
					zin.closeEntry();
					out.close();
					fout.close();
				}
			}

			if (listener != null && !isCancel) {
				File f = new File(fileFullPath);
				f.delete();
				LogUtil.d("  Decompress  END ");
				listener.onDecompressComplete();
			}

			zin.close();
		} catch (Exception e) {
			//Log.e("Decompress", "unzip", e);
			e.printStackTrace();
			if (listener != null) {
				listener.onException(e.toString());
			}
		}

		return null;
	}
	private void checkDir(File file){
		String path = file.getAbsolutePath();
		int lastIndex = path.lastIndexOf("/");
		File dir = new File(path.substring(0, lastIndex));
		if(!dir.exists()){
			dir.mkdirs();
		}
	}
	private void _dirChecker(String dir) {
		File f = new File(ApplicationPool.PATH_BOOK_RESOURCE + pCode + File.separatorChar + dir);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}

}