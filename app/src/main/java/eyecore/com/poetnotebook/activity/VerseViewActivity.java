package eyecore.com.poetnotebook.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.main.Verse;
import eyecore.com.poetnotebook.main.VerseType;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.string.IntentString;
import eyecore.com.poetnotebook.string.PreferenceString;

public class VerseViewActivity extends AppCompatActivity
{
    ScrollView layout;

    TextView textview_verseName;
    TextView textview_verseText;
    TextView textview_verseAuthor;
    TextView textview_verseDate;

    MyVersesDataBaseHelper dbHelper;

    SharedPreferences sharedPref;

    Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verse_view);

        sharedPref = getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, MODE_PRIVATE);

        dbHelper = new MyVersesDataBaseHelper(getApplicationContext(), Author.loadAuthorID(sharedPref));

        typeface = MyFont.setDefaultFont(getApplicationContext());

        layout = (ScrollView) findViewById(R.id.layout_verseview);
        textview_verseName = (TextView) findViewById(R.id.verse_name_verse_view);
        textview_verseName.setTypeface(typeface);
        textview_verseText = (TextView) findViewById(R.id.verse_text_verse_view);
        textview_verseText.setTypeface(typeface);
        textview_verseAuthor = (TextView) findViewById(R.id.verse_author_verse_view);
        textview_verseAuthor.setTypeface(typeface);
        textview_verseDate = (TextView) findViewById(R.id.verse_date_verse_view);
        textview_verseDate.setTypeface(typeface);

        Intent intent = getIntent();
        int verseType = intent.getIntExtra(IntentString.VERSE_TYPE, VerseType.NONE);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

        int verseID = intent.getIntExtra(IntentString.VERSE_ID, -1);

        switch (verseType)
        {
            case VerseType.MY_VERSE:

                Verse verse;

                if (verseID != -1)
                {
                    verse = dbHelper.getVerse(verseID, VerseType.MY_VERSE);
                    textview_verseName.setText(verse.getVerseName());
                    textview_verseAuthor.setText(" " + verse.getVerseAuthor());
                    textview_verseText.setText(verse.getVerseText());
                    LocalDate date = dtf.parseLocalDate(verse.getVerseDate().toString());
                    textview_verseDate.setText(dtfOut.print(date) + " ");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }

                break;

            case VerseType.FAVOURITE_VERSE:


                if (verseID != -1)
                {
                    verse = dbHelper.getVerse(verseID, VerseType.FAVOURITE_VERSE);
                    textview_verseName.setText(verse.getVerseName());
                    textview_verseAuthor.setText(" " + verse.getVerseAuthor());
                    textview_verseText.setText(verse.getVerseText());
                    LocalDate date = dtf.parseLocalDate(verse.getVerseDate().toString());
                    textview_verseDate.setText(dtfOut.print(date) + " ");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }

                break;

            case VerseType.PUBLISHED_VERSE:

                String verse_name = intent.getStringExtra(IntentString.PUB_VERSE_NAME);
                String verse_text = intent.getStringExtra(IntentString.PUB_VERSE_TEXT);
                String verse_author = intent.getStringExtra(IntentString.PUB_VERSE_AUTHOR);
                String verse_date = intent.getStringExtra(IntentString.PUB_VERSE_DATE);
                String verse_author_id = intent.getStringExtra(IntentString.PUB_VERSE_AUTHOR_ID);

                if (verse_name != null & verse_text != null & verse_author != null &
                        verse_date != null & verse_author_id != null)
                {
                    textview_verseName.setText(verse_name);
                    textview_verseAuthor.setText(" " + verse_author);
                    textview_verseText.setText(verse_text);
                    textview_verseDate.setText(verse_date + " ");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
