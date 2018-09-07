package kr.co.moumou.smartwords;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import kr.co.moumou.smartwords.activity.ActivityBase;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.Preferences;

public class MainActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout)findViewById(R.id.lay_background);
        Preferences.setScreenWidth(this, DisplayUtil.getScreenWidth(this));
        Preferences.setScreenHeight(this, DisplayUtil.getScreenHeight(this));
        Preferences.setIsTable(this, DisplayUtil.isTablet(this));
        layout.post(new Runnable() {
            @Override
            public void run() {
                getStatusBarSize();
            }
        });

        Button str = (Button) findViewById(R.id.startbutton);
        str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abc = new Intent(MainActivity.this, ActivityWordTestMain.class);
                startActivity(abc);
            }
        });


    }

    @Override
    public ViewTopMenu getTopManu() {
        return null;
    }

    @Override
    public int getTopMenuPos() {
        return 0;
    }


    private void getStatusBarSize(){
        Rect rectgle= new Rect();
        Window window= getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int StatusBarHeight= rectgle.top;
        int contentViewTop=
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int TitleBarHeight= contentViewTop - StatusBarHeight;

        Preferences.setStatusBarHeight(this, StatusBarHeight);
    }
}


