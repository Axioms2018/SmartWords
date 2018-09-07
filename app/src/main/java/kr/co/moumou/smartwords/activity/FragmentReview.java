package kr.co.moumou.smartwords.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.DrawView;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.util.StringUtils;

public class FragmentReview extends BaseFragmentWordTest {

	VoWordQuest[] wordQuestList = new VoWordQuest[FragmentActivityWordTest.reviewTestQuizCnt];
	String[] wordQuizMeanList = new String[FragmentActivityWordTest.reviewTestQuizCnt];
	
	private int bogiHeight = 76;
	private int bogiWidth= 260;
	private int bogiTopMar = 22;
	private int lineWidth = 340;
	
	LinearLayout ll_exam_left, ll_exam_right;
	RelativeLayout ll_draw_line;
	
	private View leftExamViews[] = new View[FragmentActivityWordTest.reviewTestQuizCnt];
	private View rightExamViews[] = new View[FragmentActivityWordTest.reviewTestQuizCnt];
	private DrawView drawViews[] = new DrawView[FragmentActivityWordTest.reviewTestQuizCnt];
	private DrawView correctQuizDrawView[] = new DrawView[FragmentActivityWordTest.reviewTestQuizCnt];
	
	private int selectedLeftIdx = -1;
	private int selectedRightIdx = -1;
	
	private boolean isSelecting = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_words_review, container, false);
		setLayoutSetting(v);
		return v;
	}
	
	@Override
	protected void setLayoutSetting(View v) {
		ll_exam_left = (LinearLayout) v.findViewById(R.id.ll_exam_left);
		ll_draw_line = (RelativeLayout) v.findViewById(R.id.ll_draw_line);
//		ll_correct_line = (RelativeLayout) v.findViewById(R.id.ll_correct_line);
		ll_exam_right = (LinearLayout) v.findViewById(R.id.ll_exam_right);
//		DisplayUtil.setLayout(getActivity(), 308, 72 * FragmentActivityWordTest.reviewTestQuizCnt, ll_draw_line);
//		DisplayUtil.setLayout(getActivity(), 308, 72 * FragmentActivityWordTest.reviewTestQuizCnt, ll_correct_line);
		
		int totalHeight = (bogiHeight * FragmentActivityWordTest.reviewTestQuizCnt) + ((FragmentActivityWordTest.reviewTestQuizCnt - 1) * bogiTopMar);
		DisplayUtil.setLayout(getActivity(), lineWidth, totalHeight, ll_draw_line);
	//	DisplayUtil.setLayout(getActivity(), 308, totalHeight, ll_correct_line);
		
		addExamView();	//문제정보에 맞게 화면 그리기
		super.setLayoutSetting(v);
	}
	
	@Override
	protected void resetQuizInfo() {
		answerInputCnt = 0;
		selectedLeftIdx = -1;
		selectedRightIdx = -1;
		setExamList();	//문제 정보 담기
		setQuizData();	//정보에 맞게 퀴즈 담기
		resetBogi();
		worngNumArray.clear();
	}

	
	

	@Override
	protected void showCheckAnswer(boolean isCorrect) {
		LogTraceMin.I("showCheckAnswer isCorrect :: " + isCorrect);
	}

	@Override
	protected void cantAnswerInput() {
		LogTraceMin.I("cantAnswerInput :: ");
		for(int i = 0; i < FragmentActivityWordTest.reviewTestQuizCnt; i++) {
			leftExamViews[i].setClickable(false);
			leftExamViews[i].setEnabled(false);
			rightExamViews[i].setClickable(false);
			rightExamViews[i].setEnabled(false);
		}
	}

	@Override
	protected void onKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 다시 클릭 가능하게
	 */
	private void resetBogi() {
		for(int i = 0; i < FragmentActivityWordTest.reviewTestQuizCnt; i++) {
			leftExamViews[i].setClickable(true);
			leftExamViews[i].setEnabled(true);
			rightExamViews[i].setClickable(true);
			rightExamViews[i].setEnabled(true);
			leftExamViews[i].findViewById(R.id.layoutItem).setEnabled(true);
			rightExamViews[i].findViewById(R.id.layoutItem).setEnabled(true);
		}
	}
	
	/**
	 * 문제 만들기
	 */
	private void setExamList() {
		int current  = (wordTestAcitivity.getCurrentQuizNum() - 1) * FragmentActivityWordTest.reviewTestQuizCnt;
		int total = current + FragmentActivityWordTest.reviewTestQuizCnt;
		
		LogTraceMin.I("list : " + current + " ~ " + (total - 1));
		
		int idx = 0;
		for(int i = current; i < total; i++) {
			wordsTestInfo.getSTD_LIST().get(i).setUSER_ANS(null);
			wordQuestList[idx] = wordsTestInfo.getSTD_LIST().get(i);
			wordQuizMeanList[idx] = wordsTestInfo.getSTD_LIST().get(i).getMEAN();
			LogTraceMin.I("wordquest word : " + wordQuestList[idx].getSTD_NUM() + " / " + wordQuestList[idx].getWORD());
			idx++;
		}
		shuffleArray(wordQuestList);
		shuffleArray(wordQuizMeanList);
	}
	
	/**
	 * 문제 랜덤
	 * @param arrayList
	 */
	@SuppressLint("NewApi")
	private void shuffleArray(VoWordQuest[] arrayList) {
		Random rnd = ThreadLocalRandom.current();
	    for (int i = arrayList.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      VoWordQuest str = arrayList[index];
	      arrayList[index] = arrayList[i];
	      arrayList[i] = str;
	    }
	}
	
	/**
	 * 문제 랜덤
	 * @param arrayList
	 */
	@SuppressLint("NewApi")
	private void shuffleArray(String[] arrayList) {
		Random rnd = ThreadLocalRandom.current();
	    for (int i = arrayList.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      String str = arrayList[index];
	      arrayList[index] = arrayList[i];
	      arrayList[i] = str;
	    }
	}
	
	/**
	 * 문제정보에 맞게 화면 그리기
	 */
	private void addExamView() {
		
		for(int i = 0; i < FragmentActivityWordTest.reviewTestQuizCnt; i++) {
//			addLeftExamView(wordQuestList[i], i);
//			addRightExamView(wordQuizMeanList[i], i);
			addLeftExamView(i);
			addRightExamView(i);
			drawViews[i] = new DrawView(wordTestAcitivity, true);
			drawViews[i].changeColor(R.color.words_review_line_blue, R.color.words_review_line_red);
			drawViews[i].changeStorekeWidth(2);
			ll_draw_line.addView(drawViews[i]);
			correctQuizDrawView[i] = new DrawView(wordTestAcitivity, true);
			correctQuizDrawView[i].changeStorekeWidth(2);
			ll_draw_line.addView(correctQuizDrawView[i]);
//			ll_correct_line.addView(correctQuizDrawView[i]);
		}
		
//		int totalHeight = (72 * FragmentActivityWordTest.reviewTestQuizCnt) + ((FragmentActivityWordTest.reviewTestQuizCnt - 1) * 12);
//		DisplayUtil.setLayout(getActivity(), 308, totalHeight, ll_draw_line);
	}
	
	/**
	 * 데이터 담기
	 * @param info
	 * @param idx
	 */
	private void setQuizData() {
		
		for(int i = 0; i < FragmentActivityWordTest.reviewTestQuizCnt; i++) {
		
			leftExamViews[i].setSelected(false);
			rightExamViews[i].setSelected(false);
			
			//왼쪽 보기
			TextView tvExam = (TextView) leftExamViews[i].findViewById(R.id.tvExam);
			tvExam.setText(wordQuestList[i].getWORD());
			//tvExam.setText("straightforward");
			
			LinearLayout layoutItem = (LinearLayout) leftExamViews[i].findViewById(R.id.layoutItem);
			
			leftExamViews[i].setTag(wordQuestList[i]);
			layoutItem.setTag(i);
			leftExamViews[i].setOnClickListener(leftOnClickListener);
			
			//오른쪽 보기
			tvExam = (TextView) rightExamViews[i].findViewById(R.id.tvExam);
			tvExam.setText(wordQuizMeanList[i]);
			//tvExam.setText("(둘 중) 어느 것도 ~ 아니다");
			
			layoutItem = (LinearLayout) rightExamViews[i].findViewById(R.id.layoutItem);
			
			rightExamViews[i].setTag(wordQuizMeanList[i]);
			layoutItem.setTag(i);
			rightExamViews[i].setOnClickListener(rigthOnClickListener);
			
			drawViews[i].initLine();
			correctQuizDrawView[i].initLine();
		}
	}
	
	
	/**
	 * 왼쪽에 보기 문제 붙이기
	 */
	private void addLeftExamView(int idx) {
		
		View view = LayoutInflater.from(wordTestAcitivity).inflate(R.layout.item_words_bogi, null);
		
//		TextView tvExam = (TextView) view.findViewById(R.id.tvExam);
//		tvExam.setText(info.getWORD());
		
		View layoutParent = view.findViewById(R.id.layoutParent);
		LinearLayout layoutItem = (LinearLayout) view.findViewById(R.id.layoutItem);
		
		DisplayUtil.setLayoutPadding(getActivity(), 0, bogiTopMar, 0, 0, layoutParent);
		DisplayUtil.setLayout(getActivity(), bogiWidth, bogiHeight, layoutItem);
		DisplayUtil.setLayoutPadding(getActivity(), 10, 0, 10, 0, layoutItem);
		
//		view.setTag(info);
//		layoutItem.setTag(idx);
//		view.setOnClickListener(leftOnClickListener);
		
		leftExamViews[idx] = view;
		ll_exam_left.addView(view);
	}
	
	OnClickListener leftOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			LogTraceMin.I("leftOnClickListener click");
			
			if(isSelecting) return;
			isSelecting = true;
			
			LinearLayout layoutItem = (LinearLayout) v.findViewById(R.id.layoutItem);
			
			//VoWordQuest info = (VoWordQuest) v.getTag();
			int idx = (int) layoutItem.getTag();
			
			//LogTraceMin.I("idx :: " + idx);
			//LogTraceMin.I("info :: " + info.getWORD());
			
			if(v.isSelected()) {
				
				//선이 그려져 있는 것을 클릭 했을때
				if(selectedLeftIdx == -1 && selectedRightIdx == -1) {
					removeUserAnswer(idx);
					isSelecting = false;
					return;
				}
				
				//선택되어 있는 내 자신을 다시 선택 했을 경우
				if(selectedLeftIdx == idx) {
					v.setSelected(false);
					selectedLeftIdx = -1;
					isSelecting = false;
				}else{ //선택된 자신과 다른거 선택 했을 경우
				
					//1. 다른거 선택 후 선이 그려져 있는 것을 클릭했을때
					if(selectedLeftIdx > -1) {
						leftExamViews[selectedLeftIdx].setSelected(false);
						removeUserAnswer(idx);
						isSelecting = false;
						return;
					}
					
					//2. 오른쪽이 선택되어 있는데 선 있는 왼쪽을 선택 했을때
					if(selectedRightIdx > -1) {
						isSelecting = false;
						return;
					}
	
					//선이 있지 않은 것을 클릭 했을때
					//리셋 시켜준다.
					selectedRightIdx = -1;
					v.setSelected(false);
					isSelecting = false;
				}	
				
			}else{
				
				//오른쪽이 이미 선택되어 있을 경우
				if(selectedRightIdx > -1) {
					
					selectedLeftIdx = idx;
					
					//선 연결
					putUserAnswer();
					v.setSelected(true);
					isSelecting = false;
					return;
					
				}
				
				//왼쪽이 이미 선택되어 있을 경우
				if(selectedLeftIdx > -1) {
					leftExamViews[selectedLeftIdx].setSelected(false);
				}
				
				//혼자 선택 됨
				selectedLeftIdx = idx;
				v.setSelected(true);
				isSelecting = false;
			}
			
		}
	};
	
	private void addRightExamView(int idx) {
		View view = LayoutInflater.from(wordTestAcitivity).inflate(R.layout.item_words_bogi, null);
		
//		TextView tvExam = (TextView) view.findViewById(R.id.tvExam);
//		tvExam.setText(mean);
		
		View layoutParent = view.findViewById(R.id.layoutParent);
		LinearLayout layoutItem = (LinearLayout) view.findViewById(R.id.layoutItem);
		
		DisplayUtil.setLayoutPadding(getActivity(), 0, bogiTopMar, 0, 0, layoutParent);
		DisplayUtil.setLayout(getActivity(), bogiWidth, bogiHeight, layoutItem);
		DisplayUtil.setLayoutPadding(getActivity(), 10, 0, 0, 10, layoutItem);
		
//		view.setTag(mean);
//		layoutItem.setTag(idx);
//		view.setOnClickListener(rigthOnClickListener);
		
		rightExamViews[idx] = view;
		ll_exam_right.addView(view);
	}
	
	OnClickListener rigthOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			LogTraceMin.I("rigthOnClickListener click");
			
			if(isSelecting) return;
			isSelecting = true;
			
			LinearLayout layoutItem = (LinearLayout) v.findViewById(R.id.layoutItem);
			
			String mean = (String) v.getTag();
			int idx = (int) layoutItem.getTag();
			
			/*LogTraceMin.I("idx :: " + idx);
			LogTraceMin.I("mean :: " + mean);
			
			LogTraceMin.I("selectedLeftIdx :: " + selectedLeftIdx);
			LogTraceMin.I("selectedRightIdx :: " + selectedRightIdx);*/
			
			if(v.isSelected()) {
				
				//선이 그려져 있는 것을 클릭 했을때
				if(selectedLeftIdx == -1 && selectedRightIdx == -1) {
					
					int leftIdx = -1;
					
					//Left가 선택되어 있지 않음
					int i = 0;
					
					for(VoWordQuest word : wordQuestList) {
						if(mean.equals(word.getUSER_ANS())) {
							leftIdx = i;
							break;
						}
						i++;
					}
					removeUserAnswer(leftIdx);
					isSelecting = false;
					return;
				}
				
				//선택되어 있는 내 자신을 다시 선택 했을 경우
				if(selectedRightIdx == idx) {
					v.setSelected(false);
					selectedRightIdx = -1;
					isSelecting = false;
				}
				else{ //선택된 자신과 다른거 선택 했을 경우
				
					//1. 다른거 선택 후 선이 그려져 있는 것을 클릭했을때
					if(selectedRightIdx > -1) {
						
						int leftIdx = -1;
						
						//Left가 선택되어 있지 않음
						int i = 0;
						
						for(VoWordQuest word : wordQuestList) {
							if(mean.equals(word.getUSER_ANS())) {
								leftIdx = i;
								break;
							}
							i++;
						}
						
						rightExamViews[selectedRightIdx].setSelected(false);
						removeUserAnswer(leftIdx);
						isSelecting = false;
						return;
					}
					
					//2. 왼쪽이 선택되어 있는데 선 있는 오른을 선택 했을때
					if(selectedLeftIdx > -1) {
						isSelecting = false;
						return;
					}
	
					//선이 있지 않은 것을 클릭 했을때
					//리셋 시켜준다.
					selectedRightIdx = -1;
					v.setSelected(false);
					isSelecting = false;
				}
				
			}else{
				
				//오른쪽이 이미 선택되어 있을 경우
				if(selectedLeftIdx > -1) {
					
					selectedRightIdx = idx;
					
					//선 연결
					putUserAnswer();
					v.setSelected(true);
					isSelecting = false;
					return;
					
				}
				
				//왼쪽이 이미 선택되어 있을 경우
				if(selectedRightIdx > -1) {
					rightExamViews[selectedRightIdx].setSelected(false);
				}
				
				//혼자 선택 됨
				selectedRightIdx = idx;
				v.setSelected(true);
				isSelecting = false;

			}
		}
	};
	
	/**
	 * 정답 담기
	 */
	private void putUserAnswer() {
		
		int left = -1, right = -1;
		
		left = selectedLeftIdx;
		right = selectedRightIdx;
		
		wordQuestList[selectedLeftIdx].setUSER_ANS(wordQuizMeanList[selectedRightIdx]);
		
		//TextView tvExam = (TextView) leftExamViews[selectedLeftIdx].findViewById(R.id.tvExam);
		//tvExam.append(" / " + wordQuizMeanList[selectedRightIdx]);
		
		LogTraceMin.I("putUserAnswer :: " + wordQuestList[selectedLeftIdx].getUSER_ANS());
		
		selectedLeftIdx = -1;
		selectedRightIdx = -1;
		
		if(left > -1 && right > -1) {
			try {
				connectionView(left, right);
			} catch (Exception e) {
				LogTraceMin.E(e.toString());
			}
			
		}
	}
	
	/**
	 *  선 연결
	 */
	private void connectionView(int left, int right) throws Exception {
		
		LogTraceMin.I("connectionView :: left : " + left + " / right : " + right );
		
		int[] img_coordinates_left = new int[2];
		leftExamViews[left].getLocationOnScreen(img_coordinates_left);

		int[] img_coordinates_test = new int[2];
		leftExamViews[0].getLocationOnScreen(img_coordinates_test);
		
		float fromY = img_coordinates_left[1] - img_coordinates_test[1] + leftExamViews[left].getHeight() / 2;
		
//		LogTraceMin.I("connectionView :: img_coordinates_left[1] : " + img_coordinates_left[1] + 
//				" / img_coordinates_test[1] : " + img_coordinates_test[1] + " / leftExamViews[left].getHeight() : " + leftExamViews[left].getHeight() );
		
		int[] img_coordinates_test2 = new int[2];
		rightExamViews[0].getLocationOnScreen(img_coordinates_test2);
		
		int[] img_coordinates_right = new int[2];
		rightExamViews[right].getLocationOnScreen(img_coordinates_right);
		
		float toY = img_coordinates_right[1] - img_coordinates_test2[1] + rightExamViews[right].getHeight() / 2;
		float toX = drawViews[left].getWidth();
		
		LogTraceMin.I("connectionView :: toX : " + toX + " / fromY : " + fromY + " / toY : " + toY );
		
		drawViews[left].drawLine(0, toX, fromY, toY);
		//drawViews[left].setConnectedLeftPosition(left);
		//drawViews[right].setConnectedRightPosition(right);
		
		checkCorrectQuiz();
	}
	
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			
//			boolean isCorrect = (boolean) msg.obj;
//			wordTestAcitivity.nextQuiz("", isCorrect);
//			if(!isCorrect) showCorrectLine();
//		};
//	};
	
	/**
	 * 정답 체크하기
	 */
	//private ArrayList<DrawView> wrongQuizDrawView = new ArrayList<DrawView>();	//틀린문제를 위해서 틀릴때 만들어 준다.
	private ArrayList<Integer> worngNumArray = new ArrayList<Integer>();
	
	private void showCorrectLine() {
		
		//ll_draw_line.removeAllViews();
		
		for(int i = 0; i < worngNumArray.size(); i++) {
			
			String answer = wordQuestList[worngNumArray.get(i)].getANSWER();
			
			LogTraceMin.I("showCorrectLine number :: " + worngNumArray.get(i));
			LogTraceMin.I("showCorrectLine answer :: " + answer);
			
			for(int j = 0; j < wordQuizMeanList.length; j++) {
				
				if(answer.equals(wordQuizMeanList[j])) {
					
					try {
						connectionCorrectView(worngNumArray.get(i), j);
					} catch (Exception e) {
						LogTraceMin.E(e.toString());
					}
					break;
				}
			}
		}
	}
	
	/**
	 *  정답 연결
	 */
	private void connectionCorrectView(int left, int right) throws Exception {
		
		LogTraceMin.I("connectionCorrectView :: left : " + left + " / right : " + right );
		
		int[] img_coordinates_left = new int[2];
		leftExamViews[left].getLocationOnScreen(img_coordinates_left);

		int[] img_coordinates_test = new int[2];
		leftExamViews[0].getLocationOnScreen(img_coordinates_test);
		
		float fromY = img_coordinates_left[1] - img_coordinates_test[1] + leftExamViews[left].getHeight() / 2;
				
		int[] img_coordinates_test2 = new int[2];
		rightExamViews[0].getLocationOnScreen(img_coordinates_test2);
		
		int[] img_coordinates_right = new int[2];
		rightExamViews[right].getLocationOnScreen(img_coordinates_right);
		
		float toY = img_coordinates_right[1] - img_coordinates_test2[1] + rightExamViews[right].getHeight() / 2;
		float toX = correctQuizDrawView[left].getWidth();
		
		LogTraceMin.I("connectionView :: toX : " + toX + " / fromY : " + fromY + " / toY : " + toY );
		
		correctQuizDrawView[left].drawCorrectLine(0, toX, fromY, toY);
		
		leftExamViews[left].findViewById(R.id.layoutItem).setEnabled(false);
		rightExamViews[right].findViewById(R.id.layoutItem).setEnabled(false);
	}
	
	/**
	 * 정답 지우기
	 */
	private void removeUserAnswer(int idx) {
		
		try {
			LogTraceMin.I("removeUserAnswer :: " + idx);
			
			String userAnswer = wordQuestList[idx].getUSER_ANS();
			LogTraceMin.I("userAnswer :: " + userAnswer);
			
			TextView tvExam = (TextView) leftExamViews[idx].findViewById(R.id.tvExam);
			tvExam.setText(wordQuestList[idx].getWORD());
			
			wordQuestList[idx].setUSER_ANS(null);
	
			int notSelected = -1;
			int i = 0;
			
			for(String mean : wordQuizMeanList) {
				if(userAnswer.equals(mean)) {
					LogTraceMin.I("removeUserAnswer mean :: " + mean);
					notSelected = i;
					break;
				}
				i++;
			}
			
			//선택된 부분 리셋
			if(notSelected > -1)
				rightExamViews[notSelected].setSelected(false);
			leftExamViews[idx].setSelected(false);
			
			selectedLeftIdx = -1;
			selectedRightIdx = -1;
			
			removeLine(idx);
			answerInputCnt--;
		
		} catch (Exception e) {
			LogTraceMin.E(e.toString());
		}
	}
	
	/**
	 * 선 삭제
	 * @param idx
	 */
	private void removeLine(int idx) {
		LogTraceMin.I("removeLine :: " + idx);
		drawViews[idx].initLine();
	}
	
	/**
	 * 정답체크
	 */
	int answerInputCnt = 0;
	boolean isCorrect = true;
	
	
	private void checkCorrectQuiz() {
		
		answerInputCnt++;
		LogTraceMin.I("answerInputCnt : " + answerInputCnt);
		
		//모든 정답이 다 입력되었을 때
		if(answerInputCnt < FragmentActivityWordTest.reviewTestQuizCnt) return;
		wordTestAcitivity.nextQuiz(checkAnswerNextQuiz());
	}
	
	/**
	 * 타임 End시, 혹은 정답 입력시 정답 체크
	 */
	@Override
	protected boolean checkAnswerNextQuiz() {
		
		LogTraceMin.I("checkAnswerNextQuiz --------------------------------------------------------------- ");
		boolean isCorrect = true;
		
		for(int i = 0; i < wordQuestList.length; i++) {
			
			VoWordQuest wordQuest = wordQuestList[i];
			
			String userAnswer = wordQuest.getUSER_ANS();
			String answer = wordQuest.getANSWER();
			
			if(StringUtils.isNull(answer)) {
				continue;
			}
			
			if(!answer.equals(userAnswer)) {
				worngNumArray.add(i);
				isCorrect = false;
			}
			
			wordQuest.setRESULT_YN(answer.equals(userAnswer) ? "Y" : "N");
			LogTraceMin.I("userAnswer : " + userAnswer + " / answer : " + answer + " >> " + isCorrect);
		}
		
		LogTraceMin.I("문제 완료 !! ");
		LogTraceMin.I("정답체크 : " + (isCorrect ? "맞음" : "틀림"));
		
		cantAnswerInput();
		stopTimerAni();
		
		if(!isCorrect) showCorrectLine();
		return isCorrect;
	}

}
