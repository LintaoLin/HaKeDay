package sssta.org.hakeday;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mac on 2017/6/3.
 */

public class CCView extends View {
    public CCView(Context context) {
        super(context);
    }

    public CCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CCView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.getSaveCount();

        super.onDraw(canvas);
    }
}
