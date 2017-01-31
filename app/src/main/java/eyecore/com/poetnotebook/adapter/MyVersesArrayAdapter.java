package eyecore.com.poetnotebook.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
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

import eyecore.com.poetnotebook.activity.ISettingsChangeable;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.Verse;
import eyecore.com.poetnotebook.app.AppSettings;

public class MyVersesArrayAdapter extends ArrayAdapter<Verse> implements ISettingsChangeable
{
    DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

    TextView textview_myverse_name;
    TextView textview_myverse_date;
    LinearLayout layout;

    public MyVersesArrayAdapter(Context context, List<Verse> myVerses)
    {
        super(context, R.layout.myverses_list_item, myVerses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Typeface typeface = AppSettings.getSettings(getContext()).setDefaultFont();

        Verse verse = getItem(position);

        AppSettings.getSettings(getContext()).loadSettings();

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myverses_list_item, null);
        }

        layout = (LinearLayout) convertView.findViewById(R.id.layout_myverse);

        textview_myverse_name = (TextView)convertView.findViewById(R.id.textview_myverse_name);
        textview_myverse_name.setTypeface(typeface);

        textview_myverse_date = (TextView)convertView.findViewById(R.id.textview_myverse_date);
        textview_myverse_date.setTypeface(typeface);

        LocalDate date = dtf.parseLocalDate(verse.getVerseDate().toString());

        textview_myverse_name.setText(" "+verse.getVerseName());
        textview_myverse_date.setText(dtfOut.print(date)+" ");

        setSettings();

        return convertView;
    }

    @Override
    public void setSettings()
    {
        layout.getBackground().setColorFilter(AppSettings.getSettings(getContext()).getBackground1Color(), PorterDuff.Mode.SRC_IN);

        textview_myverse_name.setTextColor(AppSettings.getSettings(getContext()).getTextColor());
        textview_myverse_name.setTextSize(AppSettings.getSettings(getContext()).getTextSize());

        textview_myverse_date.setTextColor(AppSettings.getSettings(getContext()).getTextColor());
        textview_myverse_date.setTextSize(AppSettings.getSettings(getContext()).getTextSize());
    }
}
