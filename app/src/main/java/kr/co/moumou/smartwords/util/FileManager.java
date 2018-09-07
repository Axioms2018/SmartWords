package kr.co.moumou.smartwords.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 파일관련 함수 모음.
 */
public class FileManager implements IConstants{

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE_CROP = 3;
    public static final int MEDIA_TYPE_IMAGE_MERGE = 4;
    public static final int MEDIA_TYPE_RECORD = 5;
    public static final int MEDIA_TYPE_CONVERT = 6;
    public static final int MEDIA_TYPE_RECORD_TEST = 7;
    public static final int MEDIA_TYPE_RECORD_TEST_CON = 8;

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * 로컬서버 홈 폴더 및 파일을 저장할 루트폴더 경로 가져오기.
     * @return
     */
    public static String getFileDir(){
        File mediaStorageDir = new File(DATA_PATH);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                ////Log.d(TAG,"failed to create directory");
                return null;
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }
    
    /**
     * 다운로드 파일을 저장할 로컬 경로. 서버에서 지정하는 경로가 없다면 여기서 리턴하는 경로에 저장하도록 되어 있다.
     * @return
     */
    public static String getFTPDir(){
        File mediaStorageDir = new File(DATA_PATH,FTP_PATH);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //Log.d(TAG,"failed to create directory");
                return null;
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }
    
    /**
     * 사용자 폴더 경로. 녹음파일, 에러신고 스크린샷등을 저장하는 경로를 가져온다.
     * @return
     */
    public static String getUserDir(){
        File mediaStorageDir = new File(DATA_PATH,MEDIA_PATH);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //Log.d(TAG,"failed to create directory");
                return null;
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }



    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * 미디어파일 경로 가져오기. 타입과 파일 이름 지정.
     * @param type
     * @param name
     * @return
     */
    public static File getOutputMediaFile(int type, String name){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
//	    File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(DATA_PATH, MEDIA_PATH);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //Log.d(TAG,"failed to create directory");
                return null;
            }
        }
        //Log.d(TAG,"getExternalStoragePublicDirectory == " + DATA_PATH);
        //Log.d(TAG,"mediaStorageDir.getPath() == " + mediaStorageDir.getPath());

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        File mediaFile = null;

        if (name.indexOf(".wav") > -1)
            name.replaceAll(".wav", "");

        if (name.indexOf(".mp3") > -1)
            name.replaceAll(".mp3", "");

        try {
            if (type == MEDIA_TYPE_IMAGE)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_SNAPSHOT + timeStamp, ".jpg", mediaStorageDir);
            else if(type == MEDIA_TYPE_VIDEO)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_VIDEO + timeStamp, ".mp4", mediaStorageDir);
            else if (type == MEDIA_TYPE_IMAGE_CROP)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_CROP + timeStamp, ".jpg", mediaStorageDir);
            else if (type == MEDIA_TYPE_IMAGE_MERGE)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_MERGE + timeStamp, ".jpg", mediaStorageDir);
            else if (type == MEDIA_TYPE_RECORD)
                mediaFile = File.createTempFile(name, ".raw", mediaStorageDir);
            else if (type == MEDIA_TYPE_CONVERT)
                mediaFile = File.createTempFile(name, ".wav", mediaStorageDir);
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //Log.d(TAG,"mediaFile.getAbsolutePath() == " +mediaFile.getAbsolutePath());
        addMediaDB("file:" + mediaFile.getAbsolutePath());

        return mediaFile;
    }


    /**
     * 미디어파일 경로 가져오기. 타입만 지정.
     * @param type
     * @return
     */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
//	    File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(DATA_PATH, MEDIA_PATH);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //Log.d(TAG,"failed to create directory");
                return null;
            }
        }
        //Log.d(TAG,"getExternalStoragePublicDirectory == " + DATA_PATH);
        //Log.d(TAG,"mediaStorageDir.getPath() == " + mediaStorageDir.getPath());

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        try {
            if (type == MEDIA_TYPE_IMAGE)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_SNAPSHOT + timeStamp, ".jpg", mediaStorageDir);
            else if(type == MEDIA_TYPE_VIDEO)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_VIDEO + timeStamp, ".mp4", mediaStorageDir);
            else if (type == MEDIA_TYPE_IMAGE_CROP)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_CROP + timeStamp, ".jpg", mediaStorageDir);
            else if (type == MEDIA_TYPE_IMAGE_MERGE)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_MERGE + timeStamp, ".jpg", mediaStorageDir);
            else if (type == MEDIA_TYPE_RECORD)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_RECORD + timeStamp, ".raw", mediaStorageDir);
            else if (type == MEDIA_TYPE_CONVERT)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_CONVERT + timeStamp, ".wav", mediaStorageDir);
            else if (type == MEDIA_TYPE_RECORD_TEST)
                mediaFile = File.createTempFile("user_test", ".raw", mediaStorageDir);
            else if (type == MEDIA_TYPE_RECORD_TEST_CON)
                mediaFile = File.createTempFile("user_test", ".wav", mediaStorageDir);
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //Log.d(TAG,"mediaFile.getAbsolutePath() == " +mediaFile.getAbsolutePath());
        addMediaDB("file:" + mediaFile.getAbsolutePath());

        return mediaFile;
    }


    /**
     * 미디어파일 경로 가져오기. 저장 경로와 타입 지정.
     * @param path
     * @param type
     * @return
     */
    public static File getOutputMediaFile(String path, int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
//	    File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(path);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //Log.d(TAG,"failed to create directory");
                return null;
            }
        }
        //Log.d(TAG,"getExternalStoragePublicDirectory == " + DATA_PATH);
        //Log.d(TAG,"mediaStorageDir.getPath() == " + mediaStorageDir.getPath());

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        try {
            if (type == MEDIA_TYPE_IMAGE)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_SNAPSHOT + timeStamp, ".jpg", mediaStorageDir);
            else if(type == MEDIA_TYPE_VIDEO)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_VIDEO + timeStamp, ".mp4", mediaStorageDir);
            else if (type == MEDIA_TYPE_IMAGE_CROP)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_CROP + timeStamp, ".jpg", mediaStorageDir);
            else if (type == MEDIA_TYPE_IMAGE_MERGE)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_MERGE + timeStamp, ".jpg", mediaStorageDir);
            else if (type == MEDIA_TYPE_RECORD)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_RECORD + timeStamp, ".raw", mediaStorageDir);
            else if (type == MEDIA_TYPE_CONVERT)
                mediaFile = File.createTempFile(FILE_MOUMOU + FILE_CONVERT + timeStamp, ".wav", mediaStorageDir);
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //Log.d(TAG,"mediaFile.getAbsolutePath() == " +mediaFile.getAbsolutePath());
        addMediaDB("file:" + mediaFile.getAbsolutePath());

        return mediaFile;
    }

    private static void addMediaDB(String path){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);

        //Log.d(TAG,"path == " +path);
        //Log.d(TAG,"contentUri == " +contentUri.getPath());
        mediaScanIntent.setData(contentUri);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(DATA_PATH, MEDIA_PATH);

        if (!file.mkdirs()) {
            //Log.d(TAG,"Directory not created");
        }
        return file;
    }

    /**
     * 전체 파일 삭제.
     */
    public static void deleteAllMedia(){
    	//Any//Log.d(TAG, "deleteAllMedia()::Start");
    	
        File mediaStorageDir = new File(DATA_PATH);
        
        deleteDirectory(mediaStorageDir);
        //Any//Log.d(TAG, "deleteAllMedia()::deleteDirectory()::Start");
        
        if (!mediaStorageDir.exists())
            return;

    }

    private static boolean deleteDirectory(File path){

        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
        return path.delete();
    }

    public static void deleteVulkMediaFiles(ArrayList<String> arr){
        for (int i = 0; i < arr.size(); i++) {
            //Log.d(TAG,String.format("vulk delete...at [%d]  result..[%s]", i, delete(arr.get(i))));
        }
    }

    public static boolean deleteMediaFile(String path){
        if (StringUtils.isNull(path))
            return false;

        return delete(path);
    }

    private static boolean delete(String path){
        try {
            File file = new File(path);
            if (!file.exists())
                return false;

            return file.delete();

        } catch (SecurityException e) {
            return false;
        }
    }

    
    /**
     * 기능 : 파일 존재 여부 체크 
     * 파일정보 : 파일 디렉토리 + 파일명
     * 
     * @param filePath 파일정보 
     * @return 파일존재여부 (true :존재 , false : 미존재)
     */
    public static boolean fileExist(String filePath){
		
    	boolean existYN = false;	//파일존재여부
    	
    	try
    	{
	    	File file = new File(filePath);
	    	if(file.exists())
	    	{
	    		existYN = true;
	    	}
	    	
    	} catch(Exception e) {
    		//예외가 발생 하면 리턴값은 false로 처리
    		existYN = false;
    	}
    		return existYN;
    }//end::fileExist()
    
    
}
