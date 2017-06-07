package eyecore.com.poetnotebook.main;

import android.content.SharedPreferences;


import eyecore.com.poetnotebook.string.PreferenceString;

public class Author
{
    public static boolean isEntered(SharedPreferences sharedPreferences)
    {
        return sharedPreferences.getBoolean(PreferenceString.AUTHOR_LOGIN, false);
    }

    public static void saveAuthorLoginState(SharedPreferences sharedPreferences, boolean isEntered)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PreferenceString.AUTHOR_LOGIN, isEntered);
        editor.apply();
    }

    public static void saveAuthorInfo(SharedPreferences sharedPreferences, String authorName, String authorID)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceString.AUTHOR_NAME, authorName);
        editor.putString(PreferenceString.AUTHOR_ID, authorID);
        editor.apply();
    }

    public static String loadAuthorName(SharedPreferences sharedPreferences)
    {
        return sharedPreferences.getString(PreferenceString.AUTHOR_NAME, "");
    }


    public static String loadAuthorID(SharedPreferences sharedPreferences)
    {
        return  sharedPreferences.getString(PreferenceString.AUTHOR_ID, "");
    }

    //for app ver. 1.0.2
    public static String loadOldAuthorName(SharedPreferences sharedPreferences)
    {
        return  sharedPreferences.getString(PreferenceString.AUTHOR_NAME, "");
    }

}
