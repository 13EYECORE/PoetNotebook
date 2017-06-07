package eyecore.com.poetnotebook.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;

import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.main.VerseType;
import eyecore.com.poetnotebook.string.IntentString;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.main.Verse;
import eyecore.com.poetnotebook.string.PreferenceString;


public class EditVerseActivity extends AppCompatActivity
{
    boolean isNewVerse;
    int verseID;

    String verseName;
    String authorID;
    String authorName;

    Verse verse;

    EditText edittext_enter_text;

    EditText edittext_enter_name;
    TextView textview_name;

    View addVerseNameDialogView;

    Toolbar toolbar;

    MyVersesDataBaseHelper dbHelper;

    SharedPreferences sharedPref;

    Typeface typeface;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_verse_activity);
        this.setTitle(getString(R.string.new_verse));

        Intent intent = getIntent();

        typeface = MyFont.setDefaultFont(getApplicationContext());

        sharedPref = getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, MODE_PRIVATE);

        authorID = Author.loadAuthorID(sharedPref);
        authorName = Author.loadAuthorName(sharedPref);

        isNewVerse = intent.getBooleanExtra(IntentString.IS_NEW_VERSE, false);

        verseID = intent.getIntExtra(IntentString.VERSE_ID, -1);

        dbHelper = new MyVersesDataBaseHelper(getApplicationContext(), authorID);

        edittext_enter_text = (EditText) findViewById(R.id.edittext_enter_text);
        edittext_enter_text.setTypeface(typeface);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        addVerseNameDialogView = layoutInflater.inflate(R.layout.input_dialog, null);

        edittext_enter_name = (EditText) addVerseNameDialogView.findViewById(R.id.edittext_enter_name);
        edittext_enter_name.setTypeface(typeface);

        textview_name = (TextView) addVerseNameDialogView.findViewById(R.id.dialogTitle);
        textview_name.setTypeface(typeface);

        toolbar = (Toolbar) findViewById(R.id.edit_verse_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyFont.applyFontForToolbarTitle(EditVerseActivity.this, toolbar, typeface);

        if (!isNewVerse)
        {
            this.setTitle(getString(R.string.verse_editing));

            if (verseID != -1)
            {
                verse = dbHelper.getVerse(verseID, VerseType.MY_VERSE);
                edittext_enter_text.setText(verse.getVerseText());
                edittext_enter_name.setText(verse.getVerseName());
            }
        }
        else
        {
            this.setTitle(getString(R.string.new_verse));
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(EditVerseActivity.this, MainActivity.class);
        startActivity(intent);
        EditVerseActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int item_id = item.getItemId();

        switch (item_id)
        {
            case R.id.action_save_verse:

                showAddVerseNameDialog();
                return true;

            case android.R.id.home:
                Intent intent = new Intent(EditVerseActivity.this, MainActivity.class);
                startActivity(intent);
                EditVerseActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_edit_verse, menu);
        return true;
    }

    private void showSaveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditVerseActivity.this, R.style.DialogTheme);
        builder.setTitle("Сохранить стих?");

        String positiveText = "Да";
        String negativeText = "Нет";

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                saveVerse();
            }
        });

        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView title = (TextView)dialog.findViewById(R.id.alertTitle);
        Button positive = (Button)dialog.findViewById(android.R.id.button1);
        positive.setTypeface(typeface);
        Button negative = (Button)dialog.findViewById(android.R.id.button2);
        negative.setTypeface(typeface);
        title.setTypeface(typeface);
    }

    private void showAddVerseNameDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditVerseActivity.this, R.style.DialogTheme);
        builder.setView(addVerseNameDialogView);

        String positiveText = "Сохранить";
        String negativeText = "Отмена";

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (isNewVerse)
                {
                    verseName = edittext_enter_name.getText().toString();

                    if (verseName.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.name_not_filled), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        dialog.dismiss();
                        showSaveDialog();
                    }
                }
                else
                {
                    if (edittext_enter_name.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.name_not_filled), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        verse.setVerseName(edittext_enter_name.getText().toString());
                        dialog.dismiss();
                        showSaveDialog();
                    }
                }
            }
        });

        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                ViewGroup viewGroup = (ViewGroup)addVerseNameDialogView.getParent();
                viewGroup.removeView(addVerseNameDialogView);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        Button positive = (Button)alertDialog.findViewById(android.R.id.button1);
        positive.setTypeface(typeface);
        Button negative = (Button)alertDialog.findViewById(android.R.id.button2);
        negative.setTypeface(typeface);
    }

    private void saveVerse()
    {
        if (isNewVerse)
        {
            verse = new Verse(verseName, edittext_enter_text.getText().toString());
            verse.setVerseAuthor(authorName);
            verse.setAuthorID(authorID);
            dbHelper.addVerse(verse, VerseType.MY_VERSE);
            Toast.makeText(getApplicationContext(), getString(R.string.verse_is_saved), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditVerseActivity.this, MainActivity.class);
            startActivity(intent);
            EditVerseActivity.this.finish();
        }
        else
        {
            verse.setVerseName(edittext_enter_name.getText().toString());
            verse.setVerseText(edittext_enter_text.getText().toString());
            verse.setVerseDate(LocalDate.now());
            dbHelper.updateVerse(verse);
            dbHelper.close();
            Toast.makeText(getApplicationContext(), getString(R.string.verse_is_saved), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditVerseActivity.this, MainActivity.class);
            startActivity(intent);
            EditVerseActivity.this.finish();
        }
    }
}

