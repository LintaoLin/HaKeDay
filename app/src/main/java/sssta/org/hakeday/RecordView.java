package sssta.org.hakeday;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Paint.Style.STROKE;

/**
 * Created by mac on 2017/6/3.
 */

public class RecordView extends View {
    Paint paint;
    Path path;
    Paint backgroudPaint;
    int stock;

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        backgroudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient linearGradient = new LinearGradient(0,0,100,100, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR);
        backgroudPaint.setShader(linearGradient);
        path = new Path();

        DashPathEffect d = new DashPathEffect(new float[]{2,2,2,2}, 2);
        paint.setPathEffect(d);

//        ArcShape shape = new ArcShape();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saved = canvas.getSaveCount();
        paint.setColor(Color.BLACK);
        paint.setStyle(STROKE);
        path.arcTo(getLeft() + 5, 5, getWidth() - 5, getHeight()-5, 0 , 270, true);
        paint.setStrokeWidth(10);
        canvas.drawPath(path, paint);
        canvas.restoreToCount(saved);
        invalidate();
    }
}
