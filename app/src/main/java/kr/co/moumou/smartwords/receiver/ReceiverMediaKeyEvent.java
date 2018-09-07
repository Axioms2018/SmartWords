package kr.co.moumou.smartwords.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import kr.co.moumou.smartwords.util.LogUtil;

public class ReceiverMediaKeyEvent extends BroadcastReceiver {

	public ReceiverMediaKeyEvent() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();

		LogUtil.e("intentAction : " + intentAction);

		if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
			return;     // 미디어액션버튼이면 받고 아니면 종료
		}
		KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
		if (event == null) {
			return;
		}
		
		abortBroadcast();
	}

}

