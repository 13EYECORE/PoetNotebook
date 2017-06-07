package eyecore.com.poetnotebook.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.string.PreferenceString;

public class AuthorInfoFragment extends Fragment
{
    TextView text_view_name;
    TextView text_view_author_name;
    TextView text_view_id;
    TextView text_view_author_id;

    Typeface typeface;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.author_info, container, false);

        typeface = MyFont.setDefaultFont(getContext());

        sharedPreferences = getContext().getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, Context.MODE_PRIVATE);

        text_view_name = (TextView)view.findViewById(R.id.info_name);
        text_view_name.setTypeface(typeface);

        text_view_author_name = (TextView)view.findViewById(R.id.info_author_name);
        text_view_author_name.setTypeface(typeface);
        text_view_id = (TextView)view.findViewById(R.id.info_id);
        text_view_id.setTypeface(typeface);

        text_view_author_id = (TextView)view.findViewById(R.id.info_author_id);
        text_view_author_id.setTypeface(typeface);

        text_view_author_name.setText(Author.loadAuthorName(sharedPreferences));
        text_view_author_id.setText(Author.loadAuthorID(sharedPreferences));

        return view;
    }
}
