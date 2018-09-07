package kr.co.moumou.smartwords.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import kr.co.moumou.smartwords.customview.DialogLoding;

public class BaseFragmentActivity extends MediaKeyEventHookFragmentActivity {
	
	private DialogLoding progress;
	private boolean isDestory = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(arg0);
	}
	
	public void showLoadingProgress(String msg){
		if(isDestory) return;
		
		if(progress == null){
			progress = new DialogLoding(this, true, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			progress.show();
			progress.setText(msg);
		}else{
			progress.show();
		}
	}
	
	public void showLoadingProgress(int resID){
		if(progress == null){
			progress = new DialogLoding(this, true, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			progress.show();
			progress.setText(getString(resID));
		}else{
			progress.show();
		}
	}
	
	public void hideProgress(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progress == null){
					return;
				}

				if(progress.isShowing()){
					progress.dismiss();
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		hideProgress();
		progress = null;
		isDestory = true;
	}
	
	public void recycleView(View view) {

		if(view != null) {
			Drawable bg = view.getBackground();
			if(bg != null) {
				bg.setCallback(null);
				((BitmapDrawable)bg).getBitmap().recycle();
				view.setBackgroundDrawable(null);
			}	
		}
	}
}
