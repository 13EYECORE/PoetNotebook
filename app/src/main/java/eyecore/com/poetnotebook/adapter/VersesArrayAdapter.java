package eyecore.com.poetnotebook.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.main.VerseType;
import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.main.Verse;

public class VersesArrayAdapter extends ArrayAdapter<Verse>
{
    DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

    TextView textview_myverse_name;
    TextView textview_myverse_date;
    TextView textview_myverse_author;
    LinearLayout layout;

    Typeface typeface;

    int verseType;


    public VersesArrayAdapter(Context context, List<Verse> myVerses, int verseType)
    {
        super(context, R.layout.verses_list_item, myVerses);
        this.verseType = verseType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Verse verse = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.verses_list_item, null);
        }

        layout = (LinearLayout) convertView.findViewById(R.id.layout_myverse);

        typeface = MyFont.setDefaultFont(getContext());

        if (verseType == VerseType.FAVOURITE_VERSE)
        {
            textview_myverse_author = (TextView)convertView.findViewById(R.id.textview_verse_author);
            textview_myverse_author.setTypeface(typeface);
            textview_myverse_author.setText(" " + verse.getVerseAuthor());
        }

        textview_myverse_name = (TextView)convertView.findViewById(R.id.textview_verse_name);
        textview_myverse_date = (TextView)convertView.findViewById(R.id.textview_verse_date);

        textview_myverse_name.setTypeface(typeface);
        textview_myverse_date.setTypeface(typeface);

        LocalDate date = dtf.parseLocalDate(verse.getVerseDate().toString());

        textview_myverse_name.setText(" "+verse.getVerseName());
        textview_myverse_date.setText(dtfOut.print(date)+" ");


        return convertView;
    }

}
