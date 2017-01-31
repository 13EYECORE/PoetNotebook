package eyecore.com.poetnotebook.activity;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import eyecore.com.poetnotebook.string.IntentString;
import eyecore.com.poetnotebook.app.AppSettings;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.Verse;


public class EditVerseActivity extends AppCompatActivity implements ISettingsChangeable
{
    boolean isNewVerse;
    int verseID;
    String verseAuthor;
    String verseName;
    Verse verse;

    RelativeLayout layout;

    Button button_save;

    EditText edittext_enterName;
    EditText edittext_enterText;

    TextView textview_verseName;
    TextView textview_verseText;

    MyVersesDataBaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.edit_verse);

        Intent intent = getIntent();

        isNewVerse = intent.getBooleanExtra(IntentString.IS_NEW_VERSE, false);
        verseID = intent.getIntExtra(IntentString.VERSE_ID, -1);
        verseAuthor = intent.getStringExtra(IntentString.VERSE_AUTHOR);

        dbHelper = new MyVersesDataBaseHelper(this);

        Typeface typeface = AppSettings.getSettings(getApplicationContext()).setDefaultFont();

        layout = (RelativeLayout)findViewById(R.id.layout_editverse);

        edittext_enterName = (EditText) findViewById(R.id.edittext_enter_verse_name);
        edittext_enterName.setTypeface(typeface);

        edittext_enterText = (EditText) findViewById(R.id.edittext_enter_text);
        edittext_enterText.setTypeface(typeface);

        textview_verseName = (TextView) findViewById(R.id.textview_verse_name);
        textview_verseName.setTypeface(typeface, Typeface.BOLD);

        textview_verseText = (TextView) findViewById(R.id.textview_verse_text);
        textview_verseText.setTypeface(typeface, Typeface.BOLD);

        button_save = (Button) findViewById(R.id.button_save);
        button_save.setTypeface(typeface);


        if (!isNewVerse)
        {
            if (verseID != -1)
            {
                verse = dbHelper.getVerse(verseID);
                edittext_enterName.setText(verse.getVerseName());
                edittext_enterText.setText(verse.getVerseText());
            }
        }

        button_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isNewVerse)
                {
                    if (edittext_enterName.getText().toString().isEmpty())
                    {
                        verseName = getString(R.string.rough_draft);
                    }
                    else
                    {
                        verseName = edittext_enterName.getText().toString();
                    }

                    verse = new Verse(verseName, verseAuthor,
                            edittext_enterText.getText().toString());
                    dbHelper.AddVerse(verse);
                    Intent intent = new Intent(EditVerseActivity.this, MyVersesActivity.class);
                    startActivity(intent);
                }
                else
                {
                    verse.setVerseName(edittext_enterName.getText().toString());
                    verse.setVerseText(edittext_enterText.getText().toString());
                    dbHelper.updateVerse(verse);
                    dbHelper.close();
                    Intent intent = new Intent(EditVerseActivity.this, MyVersesActivity.class);
                    startActivity(intent);
                }

                Toast.makeText(getApplicationContext(), R.string.verse_is_saved, Toast.LENGTH_SHORT).show();
            }
        });

        AppSettings.getSettings(getApplicationContext()).loadSettings();
        setSettings();
    }

    @Override
    public void onBackPressed()
    {
        if (isNewVerse)
        {
            verse = new Verse(edittext_enterName.getText().toString() + getString(R.string.rough_draft), verseAuthor,
                    edittext_enterText.getText().toString());
            dbHelper.AddVerse(verse);
            super.onBackPressed();
        }
        else
        {
            verse.setVerseName(edittext_enterName.getText().toString());
            verse.setVerseText(edittext_enterText.getText().toString());
            dbHelper.updateVerse(verse);
            dbHelper.close();
            Intent intent = new Intent(EditVerseActivity.this, MyVersesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void setSettings()
    {
        layout.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground1Color());

        button_save.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground2Color(),
                PorterDuff.Mode.SRC_IN);
        button_save.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_save.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_verseName.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_verseName.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        edittext_enterName.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        edittext_enterName.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());
        edittext_enterName.setHintTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        edittext_enterName.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);

        textview_verseText.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_verseText.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        edittext_enterText.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        edittext_enterText.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());
        edittext_enterText.setHintTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        edittext_enterText.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
    }
}

