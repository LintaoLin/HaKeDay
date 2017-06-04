package sssta.org.hakeday;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by mac on 2017/6/3.
 */

public class HKApplication extends Application {

    private static HKApplication hkApplication;

    public Typeface getZaoziTypeFace() {
        if (zaoziTypeFace == null) {
            zaoziTypeFace =Typeface.createFromAsset(getAssets(),"fonts/zaozi.otf");
        }
        return zaoziTypeFace;
    }

    private Typeface zaoziTypeFace;

    public Typeface getOCRAStdTypeFace() {
        if (OCRAStdTypeFace == null)
            OCRAStdTypeFace = Typeface.createFromAsset(getAssets(), "fonts/OCRAStd.otf");
        return OCRAStdTypeFace;
    }

    private Typeface OCRAStdTypeFace;

    public static HKApplication getInstance() {
        return hkApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        hkApplication = this;
    }
}
