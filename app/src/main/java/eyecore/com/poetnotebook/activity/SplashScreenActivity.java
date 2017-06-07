package eyecore.com.poetnotebook.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.string.PreferenceString;


public class SplashScreenActivity extends AppCompatActivity
{
    int splashScreenTime = 3000;
    boolean isAuthorEntered;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        sharedPref = getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, MODE_PRIVATE);

        isAuthorEntered = Author.isEntered(sharedPref);

        Thread splashTimer = new Thread()
        {
            @Override
            public void run()
            {

                try
                {
                    Intent intent;

                    sleep(splashScreenTime);

                    if (isAuthorEntered)
                    {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    }
                    else
                    {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }

                    startActivity(intent);
                }
                catch (InterruptedException e)
                {
                    Log.d("", "Splash screen timer error");
                }
                finally
                {
                    finish();
                }
            }
        };

        splashTimer.start();
    }
}
