package eyecore.com.poetnotebook;

import android.app.Activity;
import android.graphics.Typeface;

public class MyFont
{
    public static final String AppFontPath = "fonts/Flow.ttf";

    public static Typeface setAppFont(Activity activity)
    {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), AppFontPath);
        return typeface;
    }

    public static Typeface setMyFont(Activity activity, String fontName)
    {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/" + fontName);
        return typeface;
    }
}
