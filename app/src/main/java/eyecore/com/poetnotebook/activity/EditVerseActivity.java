package eyecore.com.poetnotebook.activity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import eyecore.com.poetnotebook.MyFont;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.Verse;


public class EditVerseActivity extends AppCompatActivity
{
    boolean isNewVerse;
    int verseID;
    String verseAuthor;
    String verseName;
    Verse verse;

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
        isNewVerse = intent.getBooleanExtra("IS_NEW_VERSE", false);
        verseID = intent.getIntExtra("VERSE_ID", -1);
        verseAuthor = intent.getStringExtra("VERSE_AUTHOR");

        dbHelper = new MyVersesDataBaseHelper(this);

        Typeface typeface = MyFont.setAppFont(this);

        edittext_enterName = (EditText) findViewById(R.id.edittext_enter_name);
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
                        verseName = "(Черновик)";
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
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (isNewVerse)
        {
            verse = new Verse(edittext_enterName.getText().toString() + "(Черновик)", verseAuthor, edittext_enterText.getText().toString());
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
}

