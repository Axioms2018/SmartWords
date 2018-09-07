package kr.co.moumou.smartwords.wordschart;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class PieGraph extends View implements  PieGraphAnimater {

    private ArrayList<PieSlice> mSlices = new ArrayList<PieSlice>();
    private Paint mPaint = new Paint();
//    private boolean mDrawCompleted = false;
    private RectF mRectF = new RectF();
    private Bitmap mBackgroundImage = null;
    private Point mBackgroundImageAnchor = new Point(0,0);
    private boolean mBackgroundImageCenter = false;
    
    private float mLineSize;
    private int mDuration = 2000;
    private Animator.AnimatorListener mAnimationListener;
    private ValueAnimator mValueAnimator;

    public Context mContext;

    public void getContext(Context context) {
    	this.mContext = context;
    }

    public PieGraph(Context context) {
        this(context, null);
    }

    public PieGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieGraph, 0, 0);
    }

    public void onDraw(Canvas canvas) {
        float midX, midY, radius, innerRadius;

        canvas.drawColor(Color.TRANSPARENT);
        mPaint.reset();
        mPaint.setAntiAlias(true);

        if(mBackgroundImage != null) {
            if(mBackgroundImageCenter)
                mBackgroundImageAnchor.set(
                        getWidth() / 2 - mBackgroundImage.getWidth() / 2,
                        getHeight() / 2 - mBackgroundImage.getHeight() / 2
                );
            canvas.drawBitmap(mBackgroundImage, mBackgroundImageAnchor.x, mBackgroundImageAnchor.y, mPaint);
        }

        //분기점 위치
        float currentAngle = 270;
        //각 슬라이스 비율
        float currentSweep = 0;
        //그래프 총 합
        float totalValue = 0;

        //중심 좌표
        midX = getWidth() / 2;
        midY = getHeight() / 2;

        //외경 반지름 구하기
        radius = mBackgroundImage.getHeight() / 2 + mLineSize;

        //내경 반지름
        innerRadius = mBackgroundImage.getWidth() / 2;

        //슬라이스 값의 개수만큼 총 합 계산
        for (PieSlice slice : mSlices) {
            totalValue += slice.getValue();
        }

        for (PieSlice slice : mSlices) {
            Path p = slice.getPath();
            p.reset();

            mPaint.setColor(slice.getColor());

            currentSweep = (slice.getValue() / totalValue) * (360);

            //외부 영역 지정
            mRectF.set(midX - radius, midY - radius, midX + radius, midY + radius);

            createArc(p, mRectF, currentSweep, currentAngle, currentSweep);

          //내부 영역 지정
            mRectF.set(midX - innerRadius, midY - innerRadius, midX + innerRadius, midY + innerRadius);

            createArc(p, mRectF, currentSweep, currentAngle + currentSweep, - currentSweep);

            p.close();

            // Create selection region
            Region r = slice.getRegion();
            r.set((int) (midX - radius),
                    (int) (midY - radius),
                    (int) (midX + radius),
                    (int) (midY + radius));
            canvas.drawPath(p, mPaint);
            currentAngle = currentAngle + currentSweep;
        }
//        mDrawCompleted = true;
    }

    private void createArc(Path p, RectF mRectF, float currentSweep, float startAngle, float sweepAngle) {
        if (currentSweep == 360) {
            p.addArc(mRectF, startAngle, sweepAngle);
        } else {
            p.arcTo(mRectF, startAngle, sweepAngle);
        }
    }

    public Bitmap getBackgroundBitmap() {
        return mBackgroundImage;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap, int pos_x, int pos_y) {
        mBackgroundImage = backgroundBitmap;
        mBackgroundImageAnchor.set(pos_x, pos_y);
        postInvalidate();
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        mBackgroundImageCenter = true;
        mBackgroundImage = backgroundBitmap;
        postInvalidate();
    }

    public ArrayList<PieSlice> getSlices() {
        return mSlices;
    }

    public void setSlices(ArrayList<PieSlice> slices) {
        mSlices = slices;
        postInvalidate();
    }

    public PieSlice getSlice(int index) {
        return mSlices.get(index);
    }

    public void addSlice(PieSlice slice) {
        mSlices.add(slice);
        postInvalidate();
    }

    public void removeSlices() {
        mSlices.clear();
        postInvalidate();
    }

    public float getmLineSize() {
		return mLineSize;
	}

	public void setmLineSize(float mLineSize) {
		this.mLineSize = mLineSize;
	}

	@Override
    public int getDuration() {
        return mDuration;
    }

    @Override
    public void setDuration(int duration) {
    	mDuration = duration;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void animateToGoalValues() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1){
            Log.e("GraphLibrary compatibility error", "Animation not supported on api level 12 and below. Returning without animating.");
            return;
        }
        if (mValueAnimator != null)
            mValueAnimator.cancel();

        for (PieSlice s : mSlices)
            s.setOldValue(s.getValue());

        ValueAnimator va = ValueAnimator.ofFloat(0,1);
        mValueAnimator = va;
        va.setDuration(getDuration());

        if (mAnimationListener != null) va.addListener(mAnimationListener);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = Math.max(animation.getAnimatedFraction(), 0.01f);

                for (PieSlice s : mSlices) {
                    float x = s.getGoalValue() - s.getOldValue();
                    s.setValue(s.getOldValue() + (x * f));
                }
                postInvalidate();
            }});
            va.start();
        }

	@Override
	public void setAnimationListener(AnimatorListener animationListener) {
		mAnimationListener = animationListener;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Animator.AnimatorListener getAnimationListener(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            	Log.d("piefrag","anim start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("piefrag", "anim end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d("piefrag", "anim cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            	Log.d("piefrag","anim repeat");
            }
        };
        else return null;
		
	}
}
