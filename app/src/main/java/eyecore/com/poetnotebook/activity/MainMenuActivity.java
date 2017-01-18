package eyecore.com.poetnotebook.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import eyecore.com.poetnotebook.string.PreferenceString;
import eyecore.com.poetnotebook.string.IntentString;
import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.app.AppSettings;

public class MainMenuActivity extends AppCompatActivity implements ISettingsChangeable
{
    LinearLayout layout;

    Button button_newVerse;
    Button button_myVerses;
    Button button_favourites;
    Button button_published;
    Button button_settings;

    TextView textview_author;
    TextView textview_author_name;
    TextView textview_author_edit;

    Typeface typeface;

    SharedPreferences sharedPref;

    String author;
    boolean isNameless = false;

    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        typeface = AppSettings.getSettings(getApplicationContext()).setDefaultFont();

        layout = (LinearLayout)findViewById(R.id.layout_mainmenu);

        textview_author = (TextView)findViewById(R.id.textview_author);
        textview_author_name = (TextView)findViewById(R.id.textview_author_name);

        textview_author.setTypeface(typeface);
        textview_author_name.setTypeface(typeface);

        textview_author_edit = (TextView) findViewById(R.id.textview_author_edit);
        textview_author_edit.setTypeface(typeface);

        textview_author_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createAuthorDialog();
            }
        });
        button_newVerse = (Button)findViewById(R.id.button_new_verse);
        button_newVerse.setTypeface(typeface);

        button_newVerse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, EditVerseActivity.class);
                intent.putExtra(IntentString.IS_NEW_VERSE, true);
                intent.putExtra(IntentString.VERSE_AUTHOR, author);
                startActivity(intent);
            }
        });

        button_myVerses = (Button) findViewById(R.id.button_my_verses);
        button_myVerses.setTypeface(typeface);

        button_myVerses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainMenuActivity.this, MyVersesActivity.class);
                startActivity(intent);
            }
        });

        button_favourites = (Button) findViewById(R.id.button_favourites);
        button_favourites.setTypeface(typeface);

        button_favourites.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "Временно недоступно", Toast.LENGTH_SHORT).show();
            }
        });

        button_published = (Button) findViewById(R.id.button_published);
        button_published.setTypeface(typeface);

        button_published.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "Временно недоступно", Toast.LENGTH_SHORT).show();
            }
        });

        button_settings = (Button) findViewById(R.id.button_settings);
        button_settings.setTypeface(typeface);

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        AppSettings.getSettings(getApplicationContext()).loadSettings();
        setSettings();

        loadAuthor();
        checkAuthor();

        textview_author_name.setText(author);
    }

    @Override
    public void onBackPressed()
    {

    }

    private void loadAuthor()
    {
        sharedPref = getPreferences(MODE_PRIVATE);
        author = sharedPref.getString(PreferenceString.AUTHOR_NAME, author);
    }

    private void saveAuthor()
    {
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PreferenceString.AUTHOR_NAME, author);
        editor.apply();
    }

    private void checkAuthor()
    {
        if (isNameless)
        {
            button_newVerse.setEnabled(false);
            button_myVerses.setEnabled(false);
            button_favourites.setEnabled(false);
            button_published.setEnabled(false);

            createAuthorDialog();
        }
        else
        {
            button_newVerse.setEnabled(true);
            button_myVerses.setEnabled(true);
        }
    }

    private void createAuthorDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this, R.style.Dialog_No_Border);
        LayoutInflater inflater = LayoutInflater.from(MainMenuActivity.this);

        final View dialogView = inflater.inflate(R.layout.entry_dialog, null);
        builder.setView(dialogView);

        LinearLayout layout = (LinearLayout) dialogView.findViewById(R.id.layout_entry);

        layout.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground2Color(),
                PorterDuff.Mode.SRC_IN);

        Button button_continue = (Button) dialogView.findViewById(R.id.button_continue);
        button_continue.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        button_continue.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_continue.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        final EditText edittext_authorName = (EditText) dialogView.findViewById(R.id.edittext_enter_author_name);
        edittext_authorName.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        edittext_authorName.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        edittext_authorName.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        final EditText edittext_authorSurname = (EditText) dialogView.findViewById(R.id.edittext_enter_author_surname);
        edittext_authorSurname.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        edittext_authorSurname.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        edittext_authorSurname.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        TextView textview_authorName = (TextView) dialogView.findViewById(R.id.textview_author_name);
        textview_authorName.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_authorName.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        TextView textview_authorSurname = (TextView) dialogView.findViewById(R.id.textview_author_surname);
        textview_authorSurname.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_authorSurname.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        TextView textview_introduce = (TextView)dialogView.findViewById(R.id.textview_introduce);
        textview_introduce.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_introduce.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_continue.setTypeface(typeface);
        edittext_authorName.setTypeface(typeface);
        edittext_authorSurname.setTypeface(typeface);
        textview_authorName.setTypeface(typeface);
        textview_authorSurname.setTypeface(typeface);
        textview_introduce.setTypeface(typeface);

        button_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (edittext_authorName.getText().toString().replaceAll(" ", "").isEmpty() ||
                    edittext_authorSurname.getText().toString().replaceAll(" ", "").isEmpty())
                {
                    isNameless = true;
                    Toast.makeText(getApplicationContext(), "Не все поля заполнены", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    author = edittext_authorName.getText().toString().replaceAll(" ", "") + " "
                            + edittext_authorSurname.getText().toString().replaceAll(" ", "");
                    isNameless = false;
                    Toast.makeText(getApplicationContext(), "Автор изменён", Toast.LENGTH_SHORT).show();
                }

                saveAuthor();
                textview_author_name.setText(author);
                dialog.dismiss();
                checkAuthor();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setSettings()
    {
        layout.setBackgroundColor(AppSettings.getSettings(getApplicationContext()).getBackground2Color());

        textview_author.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_author.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_author_name.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_author_name.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        textview_author_edit.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        textview_author_edit.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_newVerse.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        button_newVerse.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_newVerse.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_myVerses.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        button_myVerses.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_myVerses.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_favourites.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        button_favourites.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_favourites.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_published.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        button_published.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_published.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());

        button_settings.getBackground().setColorFilter(AppSettings.getSettings(getApplicationContext()).getBackground1Color(),
                PorterDuff.Mode.SRC_IN);
        button_settings.setTextColor(AppSettings.getSettings(getApplicationContext()).getTextColor());
        button_settings.setTextSize(AppSettings.getSettings(getApplicationContext()).getTextSize());
    }
}

