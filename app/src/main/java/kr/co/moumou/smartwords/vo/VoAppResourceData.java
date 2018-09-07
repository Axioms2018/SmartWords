package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-10-02.
 */
public class VoAppResourceData {
    private int MAX_VER;
    private ArrayList<ResourceData> RES_LIST = new ArrayList<ResourceData>();

    public int getMAX_VER() {
        return MAX_VER;
    }

    public void setMAX_VER(int MAX_VER) {
        this.MAX_VER = MAX_VER;
    }

    public ArrayList<ResourceData> getRES_LIST() {
        return RES_LIST;
    }

    public void setRES_LIST(ArrayList<ResourceData> RES_LIST) {
        this.RES_LIST = RES_LIST;
    }

    public class ResourceData{
        private int FILE_VER;
        private String FILE_PATH;
        private String FILE_PATH_REAL;
        private String FILE_NAME;
        private String FILE_DATE;
        private String FILE_STATE;

        public int getFILE_VER() {
            return FILE_VER;
        }

        public void setFILE_VER(int FILE_VER) {
            this.FILE_VER = FILE_VER;
        }

        public String getFILE_PATH() {
            return FILE_PATH;
        }

        public void setFILE_PATH(String FILE_PATH) {
            this.FILE_PATH = FILE_PATH;
        }

        public String getFILE_PATH_REAL() {
            return FILE_PATH_REAL;
        }

        public void setFILE_PATH_REAL(String FILE_PATH_REAL) {
            this.FILE_PATH_REAL = FILE_PATH_REAL;
        }

        public String getFILE_NAME() {
            return FILE_NAME;
        }

        public void setFILE_NAME(String FILE_NAME) {
            this.FILE_NAME = FILE_NAME;
        }

        public String getFILE_DATE() {
            return FILE_DATE;
        }

        public void setFILE_DATE(String FILE_DATE) {
            this.FILE_DATE = FILE_DATE;
        }

        public String getFILE_STATE() {
            return FILE_STATE;
        }

        public void setFILE_STATE(String FILE_STATE) {
            this.FILE_STATE = FILE_STATE;
        }
    }

}
