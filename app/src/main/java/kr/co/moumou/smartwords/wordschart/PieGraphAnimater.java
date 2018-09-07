package kr.co.moumou.smartwords.wordschart;

import android.animation.Animator;

public interface PieGraphAnimater {

    /*final int ANIMATE_NORMAL = 0;
    final int ANIMATE_INSERT = 1;
    final int ANIMATE_DELETE = 2;*/
    int getDuration();
    void setDuration(int duration);

    void animateToGoalValues();
    void setAnimationListener(Animator.AnimatorListener animationListener);
}
