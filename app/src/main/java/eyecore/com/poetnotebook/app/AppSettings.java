package eyecore.com.poetnotebook.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.string.PreferenceString;

@SuppressWarnings("deprecation")
public class AppSettings extends Application
{
    private static AppSettings appSettings;

    private static final String PREFERENCES_NAME = "SETTINGS";
    private static final String DEFAULT_FONT_PATH = "fonts/Flow.ttf";

    private int _textSize;
    private int _textColor;
    private int _background1Color;
    private int _background2Color;

    private SharedPreferences _sharedPref;

    private Context _context;

    private AppSettings(Context context)
    {
        _context = context;
    }

    public static AppSettings getSettings(Context context)
    {
        if (appSettings == null)
        {
            appSettings = new AppSettings(context);
        }

        return appSettings;
    }


    public AppSettings defaultSettings()
    {
        appSettings.setTextSize(24);
        appSettings.setTextColor(AppSettings.getSettings(_context)._context.getResources().getColor(R.color.black));
        appSettings.setBackground1Color(AppSettings.getSettings(_context)._context.getResources().getColor(R.color.spring_green));
        appSettings.setBackground2Color(AppSettings.getSettings(_context)._context.getResources().getColor(R.color.bright_green));
        return appSettings;
    }

    public AppSettings loadSettings()
    {
        _sharedPref = AppSettings.getSettings(_context)._context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        _textSize = _sharedPref.getInt(PreferenceString.TEXT_SIZE, 24);
        _textColor = _sharedPref.getInt(PreferenceString.TEXT_COLOR,
                AppSettings.getSettings(_context)._context.getResources().getColor(R.color.black));
        _background1Color = _sharedPref.getInt(PreferenceString.BACKGROUND1_COLOR,
                AppSettings.getSettings(_context)._context.getResources().getColor(R.color.spring_green));
        _background2Color = _sharedPref.getInt(PreferenceString.BACKGROUND2_COLOR,
                AppSettings.getSettings(_context)._context.getResources().getColor(R.color.bright_green));
        return appSettings;
    }

    public void saveSettings()
    {
        _sharedPref = AppSettings.getSettings(_context)._context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = _sharedPref.edit();
        editor.putInt(PreferenceString.TEXT_SIZE, getTextSize());
        editor.putInt(PreferenceString.TEXT_COLOR, getTextColor());
        editor.putInt(PreferenceString.BACKGROUND1_COLOR, getBackground1Color());
        editor.putInt(PreferenceString.BACKGROUND2_COLOR, getBackground2Color());
        editor.apply();
    }

    public Typeface setDefaultFont()
    {
        Typeface typeface = Typeface.createFromAsset(_context.getAssets(),DEFAULT_FONT_PATH);
        return typeface;
    }

    public Typeface setFont(String fontPath)
    {
        Typeface typeface = Typeface.createFromAsset(_context.getAssets(), fontPath);
        return typeface;
    }



    public int getTextColor() {
        return _textColor;
    }

    public void setTextColor(int textColor) {
        _textColor = textColor;
    }

    public int getTextSize() {
        return _textSize;
    }

    public void setTextSize(int textSize) {
        _textSize = textSize;
    }

    public int getBackground1Color() {
        return _background1Color;
    }

    public void setBackground1Color(int background1Color) {
        _background1Color = background1Color;
    }

    public int getBackground2Color() {
        return _background2Color;
    }

    public void setBackground2Color(int background2Color) {
        _background2Color = background2Color;
    }
}
