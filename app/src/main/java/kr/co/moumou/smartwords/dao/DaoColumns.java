package kr.co.moumou.smartwords.dao;

/**
 * Created by Administrator on 2015-10-12.
 */
public class DaoColumns {
    protected class Columns {
        public static final String INSERT_INDEX = "insert_index";
        public static final String PCODE = "PCODE";
        public static final String CON_SEQ = "CON_SEQ";
        public static final String CHACI = "CHACI";
        public static final String SYNC_VRESION = "sync_version";
        public static final String QUST_LIST = "qust_list";
        public static final String SYNC_DATE = "sync_date";

        public static final String MEMNAME = "MEMNAME";						//회원명
        public static final String USERID = "USERID"; 						//아이디
        public static final String QUST_SEQ = "QUST_SEQ";					//문항SEQ
        public static final String STDY_GUBN = "STDY_GUBN";					//학습구분
        public static final String STDY_GUBN_SEQ = "STDY_GUBN_SEQ";			//학습구분SEQ
        public static final String COMPCODE = "COMPCODE";					//학습관코드
        public static final String TMPT_CD = "TMPT_CD";						//템플릿 코드
        public static final String MEMNO = "MEMNO";							//회원NO
        public static final String VRSN = "VRSN";							//버전(app 내에 생성되는 데이터는 무조건 1)
        public static final String PNAME = "PNAME";							//교재명
        public static final String TEACHERID = "TEACHERID";					//교사아이디
        public static final String TEACHERNAME = "TEACHERNAME";				//교사명
        public static final String GRADE = "GRADE";							//학년
        public static final String LOGN_TM = "LOGN_TM";						//로그인시간
        public static final String STRT_TM = "STRT_TM";						//학습시작시간
        public static final String END_TM = "END_TM";						//종료시간
        public static final String LOG_OUT_TM = "LOG_OUT_TM";				//로그아웃시간
        public static final String SRVR_SEND_YN = "SRVR_SEND_YN";			//서버전송여부
        public static final String SRVR_SEND_TM = "SRVR_SEND_TM";			//서버전송시간.
        public static final String COMP_INOUT_GUBN = "COMP_INOUT_GUBN"; 	//학습관 내외 구분(0001:학습관내, 0002:학습관외)
        public static final String REG_DT = "REG_DT"; 						//생성일자
        public static final String STDY_STGE = "STDY_STGE"; 				//학습단계
        public static final String STDY_STGE_NM = "STDY_STGE_NM"; 			//학습단계명
        public static final String STDY_PGSS_STAT = "STDY_PGSS_STAT"; 		//학습진행상태
        public static final String STDY_PGSS_STAT_NM = "STDY_PGSS_STAT_NM"; //학습진행상태명
        public static final String RUN_TM = "RUN_TM"; 						//학습 진행시간
        public static final String ACT_AVG_DEC = "ACT_AVG_DEC"; 			//평균 데시벨
        public static final String VOC_SEQ = "VOC_SEQ"; 			//음원파일 시퀀스
        public static final String VOC_PATH = "VOC_PATH"; 			//녹음 파일 경로
        public static final String CLOS_YN = "CLOS_YN"; 			//학습완료여부
        public static final String STDY_TYPE = "STDY_TYPE"; 		//학습타입
        public static final String CNFM_YN = "CNFM_YN"; 			//교사확인여부

        public static final String STDY_DT = "STDY_DT";	// 일자
        public static final String STDY_MM = "STDY_MM";	// 년월
        public static final String STDY_YYYY = "STDY_YYYY";	// 년
        public static final String MEM_ANSW = "MEM_ANSW";	// 정답
        public static final String ANSW_RSLT = "ANSW_RSLT";	// 정답결과
        public static final String RETY_CNT = "RETY_CNT";	// 재시도횟수
        public static final String ANSW_GUBN = "ANSW_GUBN";	// 학습, 재학습 구분
        public static final String ACT_SPE_CNT = "ACT_SPE_CNT";	// 큰소리총개수
        public static final String ACT_SPE_PASS_CNT = "ACT_SPE_PASS_CNT";	// 큰소리성공개수
        public static final String SCOR  = "SCOR";	// 큰소리평균점수
        
        public static final String ANSW_USER  = "ANSW_USER";	//학습자정답
        public static final String EVAL_GUBN  = "EVAL_GUBN";	//평가상태
        public static final String END_YN  = "END_YN";			//마지막 문제일때 Y
        
        public static final String STD_GB  = "STD_GB";			//본학습 : S / 재학습 : R
        public static final String STD_W_GB  = "STD_W_GB";		//데일리 : D / 리뷰 : R
        public static final String STD_LEVEL  = "STD_LEVEL";	//학습레벨
        public static final String STD_DAY  = "STD_DAY";		//학습데이
        public static final String WORD_SEQ  = "WORD_SEQ";		//문항_SEQ(고유번호)
        public static final String STD_NUM  = "STD_NUM";		//학습문항번호
        public static final String USER_ANS  = "USER_ANS";		//사용자답안
        public static final String ANSWER  = "ANSWER";			//정답
        public static final String RESULT_YN  = "RESULT_YN";	//정답여부
        public static final String STD_STEP  = "STD_STEP";		//학습영역(Test1, Practice)
        public static final String STD_TIME  = "STD_TIME";		//학습시간
        public static final String STD_TYPE  = "STD_TYPE";		//문항구분(한글객관식, 영어객관식)
        public static final String DOWNLOAD_NM  = "DOWNLOAD_NM";	//다운로드 이름
        public static final String DOWNLOAD_TM  = "DOWNLOAD_TM";	//다운로드 시간
        
        public static final String CR_USERID = "CR_USERID";
        public static final String CR_POPUP_CHECK = "CR_POPUP_CHECK";
        public static final String CR_DATE_CEHCK = "CR_DATE_CHECK";
        
        public static final String WCCODE  = "WCCODE";
        public static final String ESSAYCODE  = "ESSAYCODE";
        public static final String ESSAYSTEP  = "ESSAYSTEP";
        public static final String ESSAYSTEP_NAME  = "ESSAYSTEP_NAME";
        public static final String BRAINSTORMING  = "BRAINSTORMING";
        public static final String DRAFTING  = "DRAFTING";
        public static final String WRITING  = "WRITING";
        public static final String REVISING  = "REVISING";
        public static final String REVISING_CHK  = "REVISING_CHK";
        public static final String EDITING  = "EDITING";
        public static final String EDITING_CHK  = "EDITING_CHK";
        public static final String EVAL_CHK  = "EVAL_CHK";
        public static final String EVAL_SCORE  = "EVAL_SCORE";
        
    }

}
