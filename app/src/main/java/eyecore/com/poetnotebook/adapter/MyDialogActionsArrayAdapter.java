package eyecore.com.poetnotebook.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eyecore.com.poetnotebook.MyFont;
import eyecore.com.poetnotebook.R;

public class MyDialogActionsArrayAdapter extends ArrayAdapter<String>
{

    public MyDialogActionsArrayAdapter(Context context, List<String> myActions)
    {
        super(context, R.layout.myverses_actions_list, myActions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        AssetManager asset = getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(asset, MyFont.AppFontPath);

        String action = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myverses_actions_item, null);
        }

        TextView textview_actions = (TextView)convertView.findViewById(R.id.textview_action);
        textview_actions.setText(action);
        textview_actions.setTypeface(typeface);

        return convertView;
    }
}
