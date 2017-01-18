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

import java.util.List;

import eyecore.com.poetnotebook.activity.ISettingsChangeable;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.app.AppSettings;

public class MyActionsArrayAdapter extends ArrayAdapter<String> implements ISettingsChangeable
{
    TextView textview_actions;
    LinearLayout layout;

    public MyActionsArrayAdapter(Context context, List<String> myActions)
    {
        super(context, R.layout.myverses_actions_list, myActions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        AppSettings.getSettings(getContext()).loadSettings();

        Typeface typeface = AppSettings.getSettings(getContext()).setDefaultFont();

        String action = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myverses_actions_item, null);
        }

        textview_actions = (TextView)convertView.findViewById(R.id.textview_action);
        layout = (LinearLayout) convertView.findViewById(R.id.layout_myactions);
        textview_actions.setText(action);
        textview_actions.setTypeface(typeface);
        setSettings();

        return convertView;
    }

    @Override
    public void setSettings()
    {
        layout.getBackground().setColorFilter(AppSettings.getSettings(getContext()).getBackground1Color(), PorterDuff.Mode.SRC_IN);
        textview_actions.setTextColor(AppSettings.getSettings(getContext()).getTextColor());
        textview_actions.setTextSize(AppSettings.getSettings(getContext()).getTextSize());
    }
}
