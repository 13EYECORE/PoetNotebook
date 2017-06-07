package eyecore.com.poetnotebook.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.settings.MyFont;

public class PublishedVersesArrayAdapter extends ArrayAdapter<HashMap<String, String>>
{
    TextView textview_name;
    TextView textview_date;
    TextView textview_author;
    LinearLayout layout;
    Typeface typeface;

    public PublishedVersesArrayAdapter(Context context, ArrayList<HashMap<String, String>> pub_verses)
    {
        super(context, R.layout.verses_list_item, pub_verses);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        HashMap<String, String> pub_verse = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.verses_list_item, null);
        }

        typeface = MyFont.setDefaultFont(getContext());

        layout = (LinearLayout) convertView.findViewById(R.id.layout_myverse);

        textview_name = (TextView)convertView.findViewById(R.id.textview_verse_name);
        textview_date = (TextView) convertView.findViewById(R.id.textview_verse_date);
        textview_name.setTypeface(typeface);
        textview_date.setTypeface(typeface);
        textview_name.setText(" " + pub_verse.get("verse_name"));
        textview_date.setText(pub_verse.get("verse_date") + " ");
        textview_author = (TextView)convertView.findViewById(R.id.textview_verse_author);
        textview_author.setTypeface(typeface);
        textview_author.setText(" " + pub_verse.get("verse_author"));

        return convertView;
    }

}
