package eyecore.com.poetnotebook.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.app.AppSettings;
import yuku.ambilwarna.AmbilWarnaDialog;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatActivity implements ISettingsChangeable
{
    ScrollView layout;

    View view_text_color;
    View view_background1_color;
    View view_background2_color;

    TextView textview_text_size;
    TextView textview_current_size;
    TextView textview_text_color;
    TextView textview_background1_color;
    TextView textview_background2_color;

    Button button_default_settings;
    Button button_save_settings;

    ImageButton imagebutton_left_arrow;
    ImageButton imagebutton_right_arrow;

    Typeface typeface;

    int current_text_size;
    int text_size_min = 12;
    int text_size_max = 72;

    int color_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        typeface = AppSettings.getSettings(getApplicationContext()).setDefaultFont();

        layout = (ScrollView) findViewById(R.id.layout_settings);

        view_text_color = findViewById(R.id.color_text_rectangle);
        view_background1_color = findViewById(R.id.color_background1_rectangle);
        view_background2_color = findViewById(R.id.color_background2_rectangle);

        textview_text_size = (TextView) findViewById(R.id.textview_text_size);
        textview_current_size = (TextView) findViewById(R.id.textview_current_size);
        textview_text_color = (TextView) findViewById(R.id.textview_text_color);
        textview_background1_color = (TextView) findViewById(R.id.textview_background1_color);
        textview_background2_color = (TextView) findViewById(R.id.textview_background2_color);

        textview_text_size.setTypeface(typeface);
        textview_current_size.setTypeface(typeface);
        textview_text_color.setTypeface(typeface);
        textview_background1_color.setTypeface(typeface);
        textview_background2_color.setTypeface(typeface);

        button_default_settings = (Button) findViewById(R.id.button_default_settings);
        button_save_settings = (Button) findViewById(R.id.button_save_settings);

        button_default_settings.setTypeface(typeface);
        button_save_settings.setTypeface(typeface);

        imagebutton_left_arrow = (ImageButton) findViewById(R.id.imagebutton_left_arrow);
        imagebutton_right_arrow = (ImageButton) findViewById(R.id.imagebutton_right_arrow);

        AppSettings.getSettings(getApplicationContext()).loadSettings();
        setSettings();

        view_text_color.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                color_id = 0;
                createColorPickerDialog(AppSettings.getSettings(getApplicationContext()).getTextColor(), color_id);
            }
        });

        view_background1_color.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                color_id = 1;
                createColorPickerDialog(AppSettings.getSettings(getApplicationContext()).getBackground1Color(), color_id);
            }
        });

        view_background2_color.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                color_id = 2;
                createColorPickerDialog(AppSettings.getSettings(getApplicationContext()).getBackground2Color(), color_id);
            }
        });

        button_default_settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppSettings.getSettings(getApplicationContext()).defaultSettings();
                textview_current_size.setText(String.valueOf(current_text_size));
                view_text_color.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
                view_background1_color.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground1Color());
                view_background2_color.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground2Color());
                Toast.makeText(getApplicationContext(), R.string.settings_reset, Toast.LENGTH_SHORT).show();
                setSettings();
            }
        });

        button_save_settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppSettings.getSettings(getApplicationContext()).setTextSize(current_text_size);
                AppSettings.getSettings(getApplicationContext()).saveSettings();
                setSettings();
                Toast.makeText(getApplicationContext(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        imagebutton_left_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (current_text_size != text_size_min) {
                    current_text_size--;
                    textview_current_size.setText(String.valueOf(current_text_size));
                }
            }
        });

        imagebutton_right_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (current_text_size != text_size_max) {
                    current_text_size++;
                    textview_current_size.setText(String.valueOf(current_text_size));
                }
            }
        });

    }

    private void createColorPickerDialog(int color, final int color_id)
    {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, color, new AmbilWarnaDialog.OnAmbilWarnaListener()
        {
            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog)
            {

            }

            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i)
            {
                switch(color_id)
                {
                    case (0):
                        AppSettings.getSettings(getApplicationContext()).setTextColor(i);
                        view_text_color.setBackgroundColor(i);
                        break;
                    case (1):
                        AppSettings.getSettings(getApplicationContext()).setBackground1Color(i);
                        view_background1_color.setBackgroundColor(i);
                        break;
                    case (2):
                        AppSettings.getSettings(getApplicationContext()).setBackground2Color(i);
                        view_background2_color.setBackgroundColor(i);
                        break;
                }

            }
        });
        dialog.show();
    }

    @Override
    public void setSettings() {

        view_text_color.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getTextColor());

        view_background1_color.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground1Color());
        view_background2_color.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground2Color());

        current_text_size = AppSettings.getSettings(getApplicationContext()).getTextSize();

        layout.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground2Color());

        textview_text_size.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_text_size.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_current_size.setText(String.valueOf(current_text_size));
        textview_current_size.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_current_size.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_text_color.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_text_color.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_background1_color.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_background1_color.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_background2_color.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_background2_color.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_save_settings.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_save_settings.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());
        button_save_settings.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);

        button_default_settings.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_default_settings.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());
        button_default_settings.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);

        imagebutton_left_arrow.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground2Color());
        imagebutton_right_arrow.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground2Color());
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}
