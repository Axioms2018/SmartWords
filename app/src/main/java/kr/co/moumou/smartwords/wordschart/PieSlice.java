package kr.co.moumou.smartwords.wordschart;

import android.graphics.Path;
import android.graphics.Region;

public class PieSlice {

    private final Path mPath = new Path();
    private final Region mRegion = new Region();
    private int mColor = 0xFF33B5E5;
    private float mValue;
    private float mOldValue;
    private float mGoalValue;

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = value;
    }

    public float getOldValue() {
        return mOldValue;
    }

    public void setOldValue(float oldValue) {
    	mOldValue = oldValue; 
    }

    public float getGoalValue() {
        return mGoalValue;
    }

    public void setGoalValue(float goalValue) {
    	mGoalValue = goalValue; 
    }

	public Path getPath() {
        return mPath;
    }

    public Region getRegion() {
        return mRegion;
    }
}
