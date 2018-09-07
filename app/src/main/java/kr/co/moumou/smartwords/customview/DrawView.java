package kr.co.moumou.smartwords.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
	float fromX;
	float toX;
	float fromY;
	float toY;
	
	float fromX2;
	float toX2;
	float fromY2;
	float toY2;
	
	int connectedPosisionLeft = -1;
	int connectedPosisionRight = -1;
	Paint paint = new Paint();
	boolean showCorrectItem;
	boolean isWords = false;
	
	int defaultColorInCorrect = Color.RED;
	int defaultColorCorrect = Color.BLUE;
	int defaultStrokeWidth = 3;

	public DrawView(Context context) {
		super(context);
		paint.setColor(defaultColorCorrect);
		paint.setStrokeWidth(defaultStrokeWidth);
		paint.setAntiAlias(true);      
	}
	
	public DrawView(Context context, boolean isWords) {
		super(context);
		paint.setColor(defaultColorCorrect);
		paint.setStrokeWidth(defaultStrokeWidth);
		paint.setAntiAlias(true); 
		this.isWords = isWords;
	}
	

	public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}



	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void changeColor(int correct, int inCorrect) {
		this.defaultColorCorrect = correct;
		this.defaultColorInCorrect = inCorrect;
	}
	
	public void changeStorekeWidth(int strokeWidth) {
		this.defaultStrokeWidth = strokeWidth;
	}

	public void setRedColor() {
		paint.setColor(defaultColorInCorrect);
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		if(isWords) {
			if (showCorrectItem) {
				paint.setColor(defaultColorInCorrect);
				canvas.drawLine(fromX2, fromY2, toX2, toY2, paint);
			}else{
				paint.setColor(defaultColorCorrect);
				canvas.drawLine(fromX, fromY, toX, toY, paint);
			}
			return;
		}
		
		paint.setColor(Color.BLUE);
		canvas.drawLine(fromX, fromY, toX, toY, paint);

		if (showCorrectItem) {
			paint.setColor(Color.RED);
			canvas.drawLine(fromX2, fromY2, toX2, toY2, paint);
		}
	}
	
	public void initLine() {
		
		showCorrectItem = false;
		
		this.fromX = 0;
		this.toX = 0;
		this.fromY = 0;
		this.toY = 0;
		
		this.fromX2 = 0;
		this.fromX2 = 0;
		this.fromX2 = 0;
		this.fromX2 = 0;
		
		setConnectedLeftPosition(-1);
		setConnectedRightPosition(-1);
		
		invalidate();
	}
	
	public void drawLine(float fromX, float toX, float fromY, float toY) {
		this.fromX = fromX;
		this.toX = toX;
		this.fromY = fromY;
		this.toY = toY;
		
		showCorrectItem = false;
		
		invalidate();
	}
	
	public void drawLine(float fromX, float toX, float fromY, float toY, float fromX2, float toX2, float fromY2, float toY2) {
		this.fromX = fromX;
		this.toX = toX;
		this.fromY = fromY;
		this.toY = toY;
		
		this.fromX2 = fromX2;
		this.toX2 = toX2;
		this.fromY2 = fromY2;
		this.toY2 = toY2;
		
		this.showCorrectItem = true;
		
		invalidate();
	}
	
	public void drawCorrectLine(float fromX, float toX, float fromY, float toY) {
		this.fromX2 = fromX;
		this.toX2 = toX;
		this.fromY2 = fromY;
		this.toY2 = toY;
		
		showCorrectItem = true;
		
		invalidate();
	}
	
	public void setConnectedLeftPosition(int position) {
		connectedPosisionLeft = position;
	}
	
	public Integer getConnectedLeftPosition() {
		return connectedPosisionLeft;
	}
	
	public void setConnectedRightPosition(int position) {
		connectedPosisionRight = position;
	}
	
	public Integer getConnectedRightPosition() {
		return connectedPosisionRight;
	}
}
