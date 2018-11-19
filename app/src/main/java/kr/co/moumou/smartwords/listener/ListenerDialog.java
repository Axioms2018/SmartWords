package kr.co.moumou.smartwords.listener;

import android.app.Dialog;

/**
 * Created by 김민정 on 2018-04-23.
 */

public interface ListenerDialog {
    int DIALOG_BTN_FIRST = 1;
    int DIALOG_BTN_SECOND = 2;
    int DIALOG_BTN_THIRD = 3;

    void onClick(Dialog dialog, int result, Object object);
}

