package eyecore.com.poetnotebook.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import eyecore.com.poetnotebook.MyFont;
import eyecore.com.poetnotebook.R;


public class MainMenuActivity extends AppCompatActivity
{
    Button button_newVerse;
    Button button_myVerses;
    Button button_favourites;
    Button button_published;

    TextView textview_author;
    TextView textview_author_name;

    SharedPreferences sharedPref;

    public static final String AUTHOR_NAME = "AUTHOR_NAME";
    String author;

    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Typeface typeface = MyFont.setAppFont(this);

        textview_author = (TextView)findViewById(R.id.textview_author);
        textview_author_name = (TextView)findViewById(R.id.textview_author_name);
        textview_author.setTypeface(typeface);
        textview_author_name.setTypeface(typeface);

        textview_author_name.setOnClickListener(new View.OnClickListener()
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
                intent.putExtra("IS_NEW_VERSE", true);
                intent.putExtra("VERSE_AUTHOR", author);
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
        button_favourites.setEnabled(false);

        button_favourites.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            }
        });

        button_published = (Button) findViewById(R.id.button_published);
        button_published.setTypeface(typeface);
        button_published.setEnabled(false);

        button_published.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            }
        });

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
        author = sharedPref.getString(AUTHOR_NAME, author);
    }

    private void saveAuthor()
    {
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AUTHOR_NAME, author);
        editor.apply();
    }

    private void checkAuthor()
    {
        if (author == null || author.isEmpty())
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
        final View dialogView = inflater.inflate(R.layout.mainmenu_author_dialog, null);
        builder.setView(dialogView);

        Button button_saveAuthor = (Button) dialogView.findViewById(R.id.button_save);
        final EditText edittext_authorName = (EditText) dialogView.findViewById(R.id.edittext_author_name);

        button_saveAuthor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                author = edittext_authorName.getText().toString();
                saveAuthor();
                textview_author_name.setText(author);
                dialog.dismiss();
                checkAuthor();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}

