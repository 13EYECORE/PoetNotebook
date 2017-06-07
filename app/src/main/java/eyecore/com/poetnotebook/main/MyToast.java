package eyecore.com.poetnotebook.main;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class MyToast
{
    public static void makeText(final Context context, final String text, final int duration)
    {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, text, duration).show();
            }
        });
    }
}
