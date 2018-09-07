package com.dpizarror.autolabel.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.moumou.smartwords.R;

/*
 * Copyright (C) 2015 David Pizarro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AutoLabelUI extends AutoViewGroup {

    private static final String LOG_TAG = AutoLabelUI.class.getSimpleName();

    private final Context mContext;
    private int mTextColor;
    private int mBackgroundResource;
    
    /**
     * Default constructor
     */
    public AutoLabelUI(Context context) {
        this(context, null);
    }

    /**
     * Default constructor
     */
    public AutoLabelUI(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Default constructor
     */
    @SuppressLint("NewApi")
	public AutoLabelUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (!isInEditMode()) {
            getAttributes(attrs);
        }
    }

    /**
     * Retrieve styles attributes
     */
    private void getAttributes(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LabelsView, 0, 0);

        if (typedArray != null) {
            try {
                mTextColor = typedArray.getColor(R.styleable.LabelsView_text_color,
                        getResources().getColor(android.R.color.white));
                mBackgroundResource = typedArray.getResourceId(R.styleable.LabelsView_label_background_res, R.color.default_background_label);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error while creating the view AutoLabelUI: ", e);
            } finally {
                typedArray.recycle();
            }
        }

    }
    
	public boolean addLabel(Label label) {
        label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(label);
        requestLayout();
        return true;
    }
    
    public void clear(){
        removeAllViews();
    }

    public Label getLabel(int position){
        return (Label) getChildAt(position);
    }

    public List<Label> getLabels(){
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0;i < getChildCount();i++){
            labels.add(getLabel(i));
        }
        return labels;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        int newColor;
        try {
            newColor = getResources().getColor(textColor);
        } catch (Resources.NotFoundException e) {
            newColor = textColor;
        }
        this.mTextColor = newColor;
    }

    public int getBackgroundResource() {
        return mBackgroundResource;
    }

    public void setBackgroundResource(int backgroundResource){
        this.mBackgroundResource = backgroundResource;
    }

    /**
     * This method sets the desired functionalities of the labels to make easy.
     *
     * @param autoLabelUISettings Object with all functionalities to make easy.
     */
    public void setSettings(AutoLabelUISettings autoLabelUISettings) {
        setBackgroundResource(autoLabelUISettings.getBackgroundResource());
        setTextColor(autoLabelUISettings.getTextColor());
    }
}