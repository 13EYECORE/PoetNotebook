package eyecore.com.poetnotebook.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import eyecore.com.poetnotebook.MyFont;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.Verse;

public class MyVersesArrayAdapter extends ArrayAdapter<Verse>
{
    DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

    public MyVersesArrayAdapter(Context context, List<Verse> myVerses)
    {
        super(context, R.layout.myverses_list_item, myVerses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        AssetManager asset = getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(asset, MyFont.AppFontPath);

        Verse verse = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myverses_list_item, null);
        }
        TextView textview_myverse_name = (TextView)convertView.findViewById(R.id.textview_myverse_name);
        textview_myverse_name.setTypeface(typeface);
        TextView textview_myverse_date = (TextView)convertView.findViewById(R.id.textview_myverse_date);
        textview_myverse_date.setTypeface(typeface);
        LocalDate date = dtf.parseLocalDate(verse.getVerseDate().toString());
        textview_myverse_name.setText(verse.getVerseName());
        textview_myverse_date.setText(dtfOut.print(date));

        return convertView;
    }
}
