package kr.co.moumou.smartwords.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SnowView extends View {
    private static final int NUM_SNOWFLAKES = 120;
    private static final int DELAY = 5;
 
    private SnowFlake[] snowflakes;
 
    public SnowView(Context context) {
        super(context);
    }
 
    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
 
    protected void resize(int width, int height) {
        
        //paint.setAlpha(50);
        //int alpha = ;
        //LogUtil.i("alpha :: " + alpha);
        //paint.setAlpha(alpha);
        //
        snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
        	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            //paint.setStyle(Paint.Style.FILL);
        	int alpha = (int) new Random().getRandom(70, 200);
        	paint.setAlpha(alpha);
            snowflakes[i] = SnowFlake.create(width, height, paint);
        }
    }
 
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            resize(w, h);
        }
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SnowFlake snowFlake : snowflakes) {
            snowFlake.draw(canvas);
        }
        getHandler().postDelayed(runnable, DELAY);
    }
 
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}