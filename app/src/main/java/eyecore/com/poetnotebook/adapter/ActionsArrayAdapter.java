package eyecore.com.poetnotebook.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.settings.MyFont;

public class ActionsArrayAdapter extends ArrayAdapter<String>
{
    TextView textview_action;
    LinearLayout layout;
    Typeface typeface;

    public ActionsArrayAdapter(Context context, List<String> myActions)
    {
        super(context, R.layout.actions_dialog, myActions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        String action = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actions_item, null);
        }

        typeface = MyFont.setDefaultFont(getContext());
        textview_action = (TextView)convertView.findViewById(R.id.textview_action);
        textview_action.setTypeface(typeface);
        layout = (LinearLayout) convertView.findViewById(R.id.layout_myactions);
        textview_action.setText(action);
        return convertView;
    }
}
