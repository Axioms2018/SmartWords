package kr.co.moumou.smartwords.util;

import android.os.Environment;

import java.io.File;

/**
 * 일반상수 인터페이스
 */
public interface IConstants {
    /** this var is must be false for before release  **/
    boolean IS_DEBUG = false;

    /** this var is must be false for before release  **/
    boolean IS_FTP_TEST = false;

    /** this var is must be false for before release  **/
    boolean IS_REC_TEST = false;
    
    String WEBVIEW_HEADER_KEY = "APP_OS";
    String WEBVIEW_HEADER_VAL = "Android";
    String WEBVIEW_SCRIPT_CLASS = "MouMouAndroid";

    String TAG = "MouMou";

    String DATA_PATH = Environment.getExternalStorageDirectory() + File.separator + "MouMou";
    String OS = "Android";

    String MEDIA_PATH = "userFiles";
    String WORD_PATH = "word";
    String REC_PATH = "userFiles";
    String FTP_PATH = "ftp";

    String FILE_MOUMOU = "moumou_";
    String FILE_SNAPSHOT = "snapshot_";
    String FILE_CROP = "crop_";
    String FILE_MERGE = "merge_";
    String FILE_VIDEO = "cam_";
    String FILE_RECORD = "rec_";
    String FILE_CONVERT = "con_";


    int LOCAL_PORT = 7780;


    /***************************************
     * fragments keys
     */
    String FRAG_KEY_CONTENT = "content";


    /****************************************
     * java script key
     */
    String JAVA_SCRIPT = "javascript:";
    String JAVA_SC_LEFT = "('";
    String JAVA_SC_RIGHT = "')";
}
