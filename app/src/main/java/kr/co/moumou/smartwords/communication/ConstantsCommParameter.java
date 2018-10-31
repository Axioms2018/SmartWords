package kr.co.moumou.smartwords.communication;


public class ConstantsCommParameter {
    public class Keys{
        public static final String COMMAND = "COMMAND";               			//명령어
        public static final String SESSIONID = "SID";                 		//세션아이디
        public static final String USERID = "USERID";             			             //아이디
        public static final String COMPCODE = "COMPCODE";             			             //학습관아이디
        public static final String USERNAME = "USERNAME";           		       //이름
        public static final String USERGB = "USERGB";           		       //회원구분
        public static final String SEX = "SEX";           		       //성별
        public static final String PWD = "PWD";                 			                 //패스워드
        public static final String CLIENT_IP = "CLIENT_IP";         		              //아이피
        public static final String TEACHERID = "TEACHERID";         		       //담당교사아이디
        public static final String TEACHERNAME = "TEACHERNAME";     		 //담당교사명
        public static final String CLIENT_TYPE = "CLIENT_TYPE";     		       //교사(T), 학생(S)
        public static final String AGE = "AGE";                				                 //나이
        public static final String GRADE = "GRADE";              			              //학년
        public static final String PCODE = "PCODE";              			              //교재코드
        public static final String CON_SEQ = "CON_SEQ";               			//교제 코드 번호
        public static final String PNAME = "PNAME";              			              //교재명
        public static final String PSNAME = "PSNAME";              			              //교재약어
        public static final String CHACI = "CHACI";              			                  //차시
        public static final String STDY_GUBN = "STDY_GUBN";         		         //학습구분(0001 학습, 0002 재학습)
        public static final String STDY_GUBN_NM = "STDY_GUBN_NM";         //학습구분명
        public static final String STDY_GUBN_SEQ = "STDY_GUBN_SEQ";      	  //학습 구분 순번(학습은 무조건 1)
        public static final String STDY_WRNG_YN = "STDY_WRNG_YN";      	  //틀린문제 풀기여부
        public static final String BOOK_READING_YN = "BOOK_READING_YN";      	  //리딩북 여부
        public static final String STDY_STGE = "STDY_STGE";          		         //학습단계
        public static final String STDY_STGE_NM = "STDY_STGE_NM";       	   //학습단계명
        public static final String STDY_PGSS_STAT = "STDY_PGSS_STAT";     	//학습진행상태
        public static final String STDY_PGSS_STAT_NM = "STDY_PGSS_STAT_NM"; //학습진행상태명
        public static final String NUM = "NUM";                				                //문항번호
        public static final String STRT_TM = "STRT_TM";            			             //시작시간
        public static final String END_TM = "END_TM";             			             //종료시간
        public static final String LOGN_TM = "LOGN_TM";            			          //로그인시간
        public static final String LOGN_OUT_TM = "LOGN_OUT_TM";        		//로그아웃시간
        public static final String FILE_NAME = "FILE_NAME";          		          //파일명
        public static final String FILE_SIZE = "FILE_SIZE";          		                 //파일 사이즈
        public static final String LAP_TM = "LAP_TM";             			                  //다운로드 시간
        public static final String RES_CODE = "RES_CODE";       	  //명령실행결과
        public static final String RES_MSG = "RES_MSG";          		         //에러메세지
        public static final String RANDOM_NUMBER = "RANDOM_NUMBER";    //난수(접속된 소켓의 고유값)
        public static final String MEMNO = "MEMNO";      	                      //UserNname과 같은데 삭제 예정.
        public static final String MEMNAME = "MEMNAME";      	                      //UserNname과 같은데 삭제 예정.
        public static final String NRML_TM = "NRML_TM";
        public static final String QUST_CNT = "QUST_CNT";
        public static final String STDY_GUDE = "STDY_GUDE";
        public static final String STDY_NUM = "STDY_NUM";
        public static final String TCHR_CHCK_YN = "TCHR_CHCK_YN";
        public static final String BACK_YN = "BACK_YN";
        public static final String QUST = "QUST";
        public static final String DATA = "Data";
        public static final String DATE = "Date";
        public static final String RUN_TM = "RUN_TM";
        public static final String CALL_TM = "CALL_TM";
        public static final String ACT_AVG_DEC = "ACT_AVG_DEC";
        public static final String IMG_PATH = "IMG_PATH";
        public static final String DOWNLOAD_URLS = "DOWNLOAD_URLS";
        public static final String INFO = "info";
        public static final String REG_DT = "REG_DT";
        public static final String SRVR_SEND_YN = "SRVR_SEND_YN";
        public static final String SRVR_SEND_TM = "SRVR_SEND_TM";
        public static final String COMP_INOUT_GUBN = "COMP_INOUT_GUBN";     //학습관 내부 외부(0001:내부, 0002외부)
        public static final String VRSN = "VRSN";                           //VRSN (1:타블렛, 2:릴레이 서버, 3:무무서버)
        public static final String CLOS_YN = "CLOS_YN";                     //학습종료유무
        public static final String CNFM_YN = "CNFM_YN";                     //교사확인유무(Y,N) - 테블릿은 무조건 'N'
        public static final String RESTDY_NUM = "RESTDY_NUM";               //유창성 재학습 Index
        public static final String RESTDY_YN = "RESTDY_YN";               //재학습인지
        public static final String REC_YN = "REC_YN";               //녹음 학습인지
        public static final String REC_URL = "REC_URL";               //녹음 파일 듣기 URL
        public static final String REC_FILES = "REC_FILES";               //파일리스트, 구분자  ';'
        public static final String BOOK_TYPE = "BOOK_TYPE";      	  //북 타입
        
        public static final String STDY_PGSS_OVER_YN = "STDY_PGSS_OVER_YN";               //시간 초과 인지 아닌지
        
        public static final String APP_ID = "APP_ID";

        public static final String MOU_EXE_YN = "MOU_EXE_YN";               //무무앱 실행 중인지
        public static final String EXE_NM = "EXE_NM";                       //현 실행 중인 앱 이름

        public static final String IS_STUDY_FINISH_CHECK = "STUDY_FINISH_CHECK";
        public static final String DATA_DEL_YN = "DATA_DEL_YN";             //데이터 삭제 유무

        public static final String GET_PICTURE_TYPE = "GET_PICTURE_TYPE";   //사진 Gallary, camera 구분
        public static final String LOUD_LEVEL= "LEVL";   //큰소리 레벨
        public static final String HAND_GUBN = "HAND_GUBN";   //큰소리 레벨 (0001 왼손, 0002오른손)


        public static final String SYSGB = "sysgb";   //MouMou app bg(101001 통합형 온라인, 101002:스마트 알파, 101003;스마트 무무, 101004:무무 맘)
        public static final String APPVER = "appver";   //앱 버전
        public static final String ID = "id";   //앱 버전
        public static final String MODEL = "model";   //디바이스 모델
        public static final String CPU = "cpu";   //디바이스 CPU
        public static final String ANDROIDVER = "ANDROIDVER";   //안드로이드 버전
        public static final String ERRMSG = "ERRMSG";   //에러메시지
        public static final String ERRLOG = "ERRLOG";   //에러로그
        public static final String EBCODE = "EBCODE";
        public static final String EBLIKE = "EBLIKE";
        public static final String EBREVIEW = "EBREVIEW";
        public static final String STDY_TYPE = "STDY_TYPE";               //D: 데일리, M 먼쓸리
        public static final String MONTHLY_TEST = "MONTHLY_TEST";
        public static final String RESOURCE_VER = "ver";   //리소스 버전

        public static final String EVAL_GUBN = "EVAL_GUBN";   //먼슬리 테스트
        public static final String WRONG = "WRONG";  	 //틀린문제구분 1: 전부, 2: 틀린
        public static final String TEST_YN = "TEST_YN";  	 //Y : 본평가때만 사용자 답 재학습일때는 null;, N : 무조건 사용자 답이 있는 것
        
        public static final String ESSAYCODE = "ESSAYCODE";   // WC
        public static final String WCCODE = "WCCODE";
        public static final String ESSAYSTEP = "ESSAYSTEP";
        public static final String ESSAYSTEP_NAME = "ESSAYSTEP_NAME";
        public static final String BRAINSTORMING = "BRAINSTORMING";
        public static final String DRAFTING = "DRAFTING";
        public static final String WRITING = "WRITING";
        public static final String REVISING = "REVISING";
        public static final String REVISING_CHK = "REVISING_CHK";
        public static final String EDITING = "EDITING";
        public static final String EDITING_CHK = "EDITING_CHK";
        public static final String EVAL_CHK = "EVAL_CHK";
        public static final String EVAL_SCORE = "EVAL_SCORE";
        public static final String ESSAY_NO = "ESSAY_NO";
        
        public static final String JSON_DATA = "JSON_DATA";
        
        public static final String IDX_NO = "IDX_NO";
        
        public static final String WORDTEST_LEVEL = "std_level";
        public static final String WORDTEST_DAY = "std_day";
        public static final String WORDTEST_STD_GB = "std_gb";
        public static final String WORDTEST_DELETE = "delete";
        
        public static final String FINAL_TEACHER_CHECK = "teacher_check";
        public static final String FINAL_TEACHER_CHECK_TYPE = "type";

        public static final String MAC = "MAC";   //디바이스 CPU
        public static final String COM_INOUT = "COMP_INOUT";   //디바이스 CPU
        
    }

    public class Values{
        public static final String SYSBG_MOUMOU = "101003";
        public static final String SYSBG_MOUMOU_WORDS = "101009";

        public static final String STDY_GUBN_SEQ_IN = "0001";
        public static final String STDY_GUBN_SEQ_OUT = "0002";

        public static final String STDY_GUBN_STUDY = "0001";
        public static final String STDY_GUBN_RESTUDY = "0002";

        
        public static final String CLIENT_TYPE_TABLE = "TBL";

        public static final int VRSN_IN_TABLE = 1;
        public static final int VRSN_IN_TEACHER = 2;
        public static final int VRSN_IN_SERVER = 3;

        public static final int GET_PICTURE_GALLERY = 1;
        public static final int GET_PICTURE_CAMERA = 2;
        
        public static final String STUDY_TYPE_DAILY = "D";
        public static final String STUDY_TYPE_TEST = "T";
        public static final String STUDY_TYPE_REVIEW = "R";
        
        
    }
}
