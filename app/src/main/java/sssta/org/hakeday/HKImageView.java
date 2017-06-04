package sssta.org.hakeday;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mac on 2017/6/4.
 */

public class HKImageView extends AppCompatImageView {
    private int state;
    public HKImageView(Context context) {
        this(context, null);
    }

    public HKImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HKImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HKImageView, defStyleAttr, 0);
        state = typedArray.getInt(R.styleable.HKImageView_state, 0);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
