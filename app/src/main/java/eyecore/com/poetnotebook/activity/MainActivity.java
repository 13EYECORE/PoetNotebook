package eyecore.com.poetnotebook.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.internet.InternetUtils;
import eyecore.com.poetnotebook.main.MyToast;
import eyecore.com.poetnotebook.settings.CustomTypefaceSpan;
import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.fragment.AuthorInfoFragment;
import eyecore.com.poetnotebook.fragment.VerseListFragment;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.main.Verse;
import eyecore.com.poetnotebook.main.VerseType;
import eyecore.com.poetnotebook.string.IntentString;
import eyecore.com.poetnotebook.string.PreferenceString;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    SharedPreferences sharedPref;
    SharedPreferences oldSharedPref;

    Typeface typeface;

    boolean oldAuthor;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        typeface = MyFont.setDefaultFont(getApplicationContext());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        for (int i = 0; i < menu.size(); i++)
        {
            MenuItem menuItem = menu.getItem(i);

            SubMenu subMenu = menuItem.getSubMenu();

            if (subMenu != null && subMenu.size() > 0)
            {
                for (int j = 0; j < subMenu.size(); j++)
                {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            applyFontToMenuItem(menuItem);
        }


        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        sharedPref = getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, MODE_PRIVATE);
        oldSharedPref = getSharedPreferences("activity.MainMenuActivity", MODE_PRIVATE);

        oldAuthor = isOldAuthor(oldSharedPref);

        //replace old author verses in new DB
        if (oldAuthor)
        {
            replaceVersesInNewDB();
            SharedPreferences.Editor editor = oldSharedPref.edit();
            editor.remove(PreferenceString.AUTHOR_NAME);
            editor.apply();
        }

        MyFont.applyFontForToolbarTitle(MainActivity.this, toolbar, typeface);

        createVerseListFragment(IntentString.VERSE_TYPE, VerseType.MY_VERSE);
    }


    @Override
    public void onBackPressed()
    {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            showExitDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int item_id = item.getItemId();

        switch (item_id)
        {
            case R.id.nav_new_verse:
                Intent intent = new Intent(this, EditVerseActivity.class);
                intent.putExtra(IntentString.IS_NEW_VERSE, true);
                startActivity(intent);
                MainActivity.this.finish();
                break;

            case R.id.nav_my_verses:
                this.setTitle(R.string.my_verses);
                createVerseListFragment(IntentString.VERSE_TYPE, VerseType.MY_VERSE);
                break;

            case R.id.nav_favourites:
                this.setTitle(R.string.favourites);
                createVerseListFragment(IntentString.VERSE_TYPE, VerseType.FAVOURITE_VERSE);
                break;

            case R.id.nav_published:

                if (!InternetUtils.isOnline(getApplicationContext()))
                {
                   Toast.makeText(getApplicationContext(), getString(R.string.internet_err), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    this.setTitle(R.string.published);
                    createVerseListFragment(IntentString.VERSE_TYPE, VerseType.PUBLISHED_VERSE);
                }
                break;

            case R.id.nav_author:
                this.setTitle(R.string.author);
                AuthorInfoFragment authorInfoFragment = new AuthorInfoFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, authorInfoFragment);
                fragmentTransaction.commit();
            break;

            case R.id.nav_exit:
                showExitDialog();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createVerseListFragment(String key, int verseType)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(key,verseType);
        VerseListFragment verseListFragment = new VerseListFragment();
        verseListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, verseListFragment);
        fragmentTransaction.commit();
    }

    private void showExitDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);
        builder.setTitle("Выйти из приложения?");

        String positiveText = "Да";
        String negativeText = "Нет";

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Author.saveAuthorLoginState(sharedPref, false);
                MainActivity.this.finish();
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

    private boolean isOldAuthor(SharedPreferences sharedPreferences)
    {
        String name = Author.loadOldAuthorName(sharedPreferences);

        if (name.isEmpty())
        {
            return false;
        }
        else
        {
            return  true;
        }
    }

    private void replaceVersesInNewDB()
    {
        MyVersesDataBaseHelper oldDB;
        MyVersesDataBaseHelper newDB;

        oldDB = new MyVersesDataBaseHelper(getApplicationContext());
        newDB = new MyVersesDataBaseHelper(getApplicationContext(), Author.loadAuthorID(sharedPref));

        List<Verse> myVerses = oldDB.getAllVerses(VerseType.MY_VERSE);
        List<Verse> favourites = oldDB.getAllVerses(VerseType.FAVOURITE_VERSE);

        String author = Author.loadAuthorName(sharedPref);

        for (int i = 0; i < myVerses.size(); i++)
        {
            myVerses.get(i).setVerseAuthor(author);
            newDB.addVerse(myVerses.get(i), VerseType.MY_VERSE);
        }

        for (int i = 0; i < favourites.size(); i++)
        {
            newDB.addVerse(favourites.get(i), VerseType.FAVOURITE_VERSE);
        }
    }

    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = MyFont.setDefaultFont(getApplicationContext());
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


}