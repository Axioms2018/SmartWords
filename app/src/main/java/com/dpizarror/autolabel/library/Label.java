package com.dpizarror.autolabel.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.CustomEditText;
import kr.co.moumou.smartwords.customview.CustomTextView;
import kr.co.moumou.smartwords.util.DisplayUtil;


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
public class Label extends LinearLayout {

    private CustomTextView mTextView;
    private CustomEditText mEditText;
    private boolean isEditText = false;
    
    public Label(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Label(Context context, boolean isEditText, int editWidth, int textSize, int textColor, int backgroundResource, int padding, KeyboardEventListener keyListener) {
        super(context);
        this.isEditText = isEditText;
        this.keyListener = keyListener;
        init(context, isEditText, editWidth, textSize, textColor, backgroundResource, padding);
    }

    private void init(final Context context, boolean isEditText, int editWidth, int textSize, int textColor, int backgroundResource, int padding) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.label_view, this, true);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llLabel);
        linearLayout.setBackgroundResource(backgroundResource);
        linearLayout.setPadding(padding, padding, padding, padding);

        if(isEditText) {
        	mEditText = (CustomEditText) view.findViewById(R.id.tvLabel2);
        	//mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        	mEditText.setTextSizeCustom(textSize);
        	mEditText.setTextColor(textColor);
        	//mEditText.setWidth(editWidth);
        	mEditText.setVisibility(View.VISIBLE);
        	mEditText.setSingleLine();
        	mEditText.setOnEditorActionListener(editorActionListener);
        	mEditText.setPrivateImeOptions("defaultInputmode=english;");
        	mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        	DisplayUtil.setLayoutWidth((Activity) context, editWidth, mEditText);
        	
        	//최대길이 제한
    		InputFilter[] FilterArray = new InputFilter[1];
    		FilterArray[0] = new InputFilter.LengthFilter(50);
    		mEditText.setFilters(FilterArray);

	        
        }else{
	        mTextView = (CustomTextView) view.findViewById(R.id.tvLabel);
	        //mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	        mTextView.setTextSizeCustom(textSize);
	        mTextView.setTextColor(textColor);
	        mTextView.setVisibility(View.VISIBLE);
        }
    }
    
    public void blockEditText() {
    	if(!isEditText) return;
    	mEditText.setEnabled(false);
    	mEditText.setFocusable(false);
    	mEditText.setFocusableInTouchMode(false);
    }
    
    public EditText getEditText() {
    	if(!isEditText) return null;
    	return mEditText;
    }

    public String getText() {
        if(isEditText) return mEditText.getText().toString();
        else return mTextView.getText().toString();
    }

    public void setText(String text) {
        if(isEditText) mEditText.setText(text);
        else mTextView.setText(text);
    }
    
    public void onRequestFocus() {
    	if(isEditText) mEditText.requestFocus();
    }
    
    @SuppressLint("NewApi")
    OnEditorActionListener editorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			
			if(null != event && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
				if (event.getAction() != KeyEvent.ACTION_UP){
					keyListener.onKeyEvent();
					return true;
				}
			}
			return false;
		}
	};
	
	KeyboardEventListener keyListener = null;
}
