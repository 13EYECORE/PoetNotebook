package eyecore.com.poetnotebook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eyecore.com.poetnotebook.adapter.MyDialogActionsArrayAdapter;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.adapter.MyVersesArrayAdapter;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.Verse;

public class MyVersesActivity extends AppCompatActivity
{
    MyVersesDataBaseHelper dbHelper;

    ListView listview_myVerses;
    View dialogView;

    List<Verse> myVerses = new ArrayList<>();
    List<String> myActions = new ArrayList<>();

    int verse_pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myverses_list);

        dbHelper = new MyVersesDataBaseHelper(this);

        myVerses = dbHelper.getAllVerses();
        Collections.reverse(myVerses);
        myActions = Arrays.asList(getResources().getStringArray(R.array.actions_array));

        dialogView = LayoutInflater.from(this).inflate(R.layout.myverses_actions_list, null);

        listview_myVerses = (ListView) findViewById(R.id.listview_myverses);

        final MyVersesArrayAdapter myAdapter = new MyVersesArrayAdapter(this, myVerses);
        myAdapter.setNotifyOnChange(true);

        final MyDialogActionsArrayAdapter myDialogAdapter = new MyDialogActionsArrayAdapter(this, myActions);

        listview_myVerses.setAdapter(myAdapter);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(MyVersesActivity.this, R.style.Dialog_No_Border);
        dialog.setAdapter(myDialogAdapter, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                switch (i)
                {
                    case 0:
                        Intent intent_view = new Intent(MyVersesActivity.this, VerseViewActivity.class);
                        intent_view.putExtra("VERSE_ID", myVerses.get(verse_pos).getVerseID());
                        startActivity(intent_view);
                        break;
                    case 1:
                        Intent intent_edit = new Intent(MyVersesActivity.this, EditVerseActivity.class);
                        intent_edit.putExtra("VERSE_ID", myVerses.get(verse_pos).getVerseID());
                        startActivity(intent_edit);
                        break;
                    case 2:
                        dbHelper.deleteVerse(myVerses.get(verse_pos));
                        myAdapter.remove(myVerses.get(verse_pos));
                        break;
                }
            }
        });

        listview_myVerses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                verse_pos = i;
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(MyVersesActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}
