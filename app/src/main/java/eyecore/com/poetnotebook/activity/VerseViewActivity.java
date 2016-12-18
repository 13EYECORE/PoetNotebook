package eyecore.com.poetnotebook.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import eyecore.com.poetnotebook.MyFont;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.Verse;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;

public class VerseViewActivity extends AppCompatActivity
{
    TextView textview_verseName;
    TextView textview_verseText;
    TextView textview_verseAuthor;
    TextView textview_verseDate;
    Verse verse;
    MyVersesDataBaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verse_view);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

        dbHelper = new MyVersesDataBaseHelper(this);

        textview_verseName = (TextView) findViewById(R.id.verse_name_verse_view);
        textview_verseText = (TextView) findViewById(R.id.verse_text_verse_view);
        textview_verseAuthor = (TextView) findViewById(R.id.verse_author_verse_view);
        textview_verseDate = (TextView) findViewById(R.id.verse_date_verse_view);

        Typeface typeface = MyFont.setAppFont(this);

        textview_verseName.setTypeface(typeface, Typeface.BOLD);
        textview_verseText.setTypeface(typeface);
        textview_verseAuthor.setTypeface(typeface, Typeface.BOLD);
        textview_verseDate.setTypeface(typeface, Typeface.BOLD);

        Intent intent = getIntent();
        int verseID = intent.getIntExtra("VERSE_ID", -1);

        if (verseID != -1)
        {
            verse = dbHelper.getVerse(verseID);
            textview_verseName.setText(verse.getVerseName());
            textview_verseAuthor.setText(verse.getVerseAuthor());
            textview_verseText.setText(verse.getVerseText());
            LocalDate date = dtf.parseLocalDate(verse.getVerseDate().toString());
            textview_verseDate.setText(dtfOut.print(date));
        }
        else
        {
            textview_verseText.setText("Произошла ошибка");
        }
    }
}
