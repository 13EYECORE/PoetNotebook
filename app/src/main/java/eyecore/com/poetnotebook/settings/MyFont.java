package eyecore.com.poetnotebook.settings;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class MyFont
{
    private static final String DEFAULT_FONT_PATH = "fonts/Gabriela.ttf";

    public static Typeface setDefaultFont(Context context)
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),DEFAULT_FONT_PATH);
        return typeface;
    }

    public static Typeface setFont(Context context, String fontPath)
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        return typeface;
    }

    public static void applyFontForToolbarTitle(Activity context, Toolbar toolbar, Typeface typeface)
    {
        for(int i = 0; i < toolbar.getChildCount(); i++)
        {
            View view = toolbar.getChildAt(i);

            if(view instanceof TextView)
            {
                TextView tv = (TextView) view;
                if(tv.getText().equals(toolbar.getTitle()))
                {
                    tv.setTypeface(typeface);
                    break;
                }
            }
        }
    }
}
