package sssta.org.hakeday;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mac on 2017/6/3.
 */

public class RecordLayout extends FrameLayout {

    @BindView(R.id.outer_image)
    ImageView outerImage;
    @BindView(R.id.inner_image)
    ImageView innerImage;
    @BindView(R.id.record_text)
    TextView recordText;

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    private AnimatorSet outerAnim, innerAnim;
    public RecordLayout(@NonNull Context context) {
        this(context, null);
    }

    public RecordLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.record_layout, this);
        ButterKnife.bind(this);
        initAnimation();
    }

    public void initAnimation() {
        outerAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.record_outer);
        outerAnim.setInterpolator(new LinearInterpolator());
        innerAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.record_inner);
        innerAnim.setInterpolator(new LinearInterpolator());
    }
    public void startAnimation() {
        if (!outerAnim.isRunning() || outerAnim.isPaused()) {
            if (outerAnim.isPaused()) {
                outerAnim.resume();
                innerAnim.resume();
            } else {
                outerAnim.setTarget(outerImage);
                outerAnim.start();
                innerAnim.setTarget(innerImage);
                innerAnim.start();
            }
            Log.d(RecordLayout.class.getSimpleName(), "startAnimation: ");
        }
    }

    public void stopAnimation() {
        if (outerAnim.isRunning()) {
            outerAnim.pause();
            innerAnim.pause();
            Log.d(RecordLayout.class.getSimpleName(), "stopAnimation: ");
        }
    }

    public void cancelAnimation() {
        if (outerAnim.isRunning()) {
            outerAnim.cancel();
            innerAnim.cancel();
        }
    }

    public boolean isAnimationAlive() {
        return outerAnim.isRunning() && !outerAnim.isPaused();
    }


}
