package eyecore.com.poetnotebook.fragment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.activity.EditVerseActivity;
import eyecore.com.poetnotebook.activity.VerseViewActivity;
import eyecore.com.poetnotebook.adapter.ActionsArrayAdapter;
import eyecore.com.poetnotebook.adapter.VersesArrayAdapter;
import eyecore.com.poetnotebook.adapter.PublishedVersesArrayAdapter;
import eyecore.com.poetnotebook.main.MyToast;
import eyecore.com.poetnotebook.database.MyVersesDataBaseHelper;
import eyecore.com.poetnotebook.internet.JSONParser;
import eyecore.com.poetnotebook.internet.JSONResponse;
import eyecore.com.poetnotebook.internet.MyURL;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.main.Verse;
import eyecore.com.poetnotebook.main.VerseType;
import eyecore.com.poetnotebook.string.IntentString;
import eyecore.com.poetnotebook.string.PreferenceString;

public class VerseListFragment extends Fragment
{
    ListView layout;
    View dialogView;

    MyVersesDataBaseHelper dbHelper;

    List<Verse> myVerses = new ArrayList<>();
    List<Verse> favourites = new ArrayList<>();
    ArrayList<HashMap<String, String>> publishedVerses = new ArrayList<>();

    List<String> myVersesActions = new ArrayList<>();
    List<String>  favouritesActions = new ArrayList<>();
    List<String> publishedActions =  new ArrayList<>();

    int versePosition;
    int verseType;

    String authorID;
    String authorName;

    SharedPreferences sharedPref;

    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.verses_list, container, false);
        dialogView = inflater.inflate(R.layout.actions_dialog, container, false);

        layout = (ListView) view.findViewById(R.id.listview_verses);

        bundle = this.getArguments();

        if (bundle != null)
        {
            verseType = bundle.getInt(IntentString.VERSE_TYPE, VerseType.NONE);
        }

        sharedPref = getContext().getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, Context.MODE_PRIVATE);
        authorID = Author.loadAuthorID(sharedPref);
        authorName = Author.loadAuthorName(sharedPref);

        if (authorID.isEmpty())
        {
            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        else
        {
            dbHelper = new MyVersesDataBaseHelper(getContext(), authorID);
        }


        switch (verseType)
        {
            case VerseType.MY_VERSE:

                myVerses = dbHelper.getAllVerses(VerseType.MY_VERSE);
                Collections.reverse(myVerses);

                if (myVerses.isEmpty())
                {
                    Toast.makeText(getContext(), R.string.empty_list, Toast.LENGTH_SHORT).show();
                }

                myVersesActions = Arrays.asList(getResources().getStringArray(R.array.actions_array_mv));

                final VersesArrayAdapter versesArrayAdapter = new VersesArrayAdapter(getContext(), myVerses, VerseType.MY_VERSE);
                versesArrayAdapter.setNotifyOnChange(true);
                layout.setAdapter(versesArrayAdapter);

                ActionsArrayAdapter actionsMyVerses = new ActionsArrayAdapter(getContext(), myVersesActions);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.DialogNoBorder);
                dialog.setAdapter(actionsMyVerses, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            //Open
                            case 0:
                                Intent intent_view = new Intent(getContext(), VerseViewActivity.class);
                                intent_view.putExtra(IntentString.VERSE_ID, myVerses.get(versePosition).getVerseID());
                                intent_view.putExtra(IntentString.VERSE_TYPE, VerseType.MY_VERSE);
                                startActivity(intent_view);
                                break;
                            //Publish
                            case 1:
                                new VerseListFragment.PublishVerse().execute();
                                break;
                            //Edit
                            case 2:
                                Intent intent_edit = new Intent(getContext(), EditVerseActivity.class);
                                intent_edit.putExtra(IntentString.VERSE_ID, myVerses.get(versePosition).getVerseID());
                                startActivity(intent_edit);
                                break;
                            //Copy text
                            case 3:
                                ClipboardManager cb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", myVerses.get(versePosition).getVerseText());
                                cb.setPrimaryClip(clip);
                                Toast.makeText(getContext(), R.string.copied_text, Toast.LENGTH_SHORT).show();
                                break;
                            //Delete
                            case 4:
                                dbHelper.deleteVerse(myVerses.get(versePosition), VerseType.MY_VERSE);
                                versesArrayAdapter.remove(myVerses.get(versePosition));
                                Toast.makeText(getContext(), R.string.deleted_verse, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                layout.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        versePosition = position;
                        dialog.show();
                    }
                });
                break;

            case VerseType.FAVOURITE_VERSE:

                favourites = dbHelper.getAllVerses(VerseType.FAVOURITE_VERSE);
                Collections.reverse(favourites);

                if (favourites.isEmpty())
                {
                    Toast.makeText(getContext(), R.string.empty_list, Toast.LENGTH_SHORT).show();
                }

                favouritesActions = Arrays.asList(getResources().getStringArray(R.array.actions_array_fv));

                final VersesArrayAdapter actionsFavourites = new VersesArrayAdapter(getContext(), favourites, VerseType.FAVOURITE_VERSE);
                actionsFavourites.setNotifyOnChange(true);
                layout.setAdapter(actionsFavourites);

                ActionsArrayAdapter actionsArrayAdapter2 = new ActionsArrayAdapter(getContext(), favouritesActions);

                final AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext(), R.style.DialogNoBorder);
                dialog2.setAdapter(actionsArrayAdapter2, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            //Open
                            case 0:
                                Intent intent_view = new Intent(getContext(), VerseViewActivity.class);
                                intent_view.putExtra(IntentString.VERSE_ID, favourites.get(versePosition).getVerseID());
                                intent_view.putExtra(IntentString.VERSE_TYPE, VerseType.FAVOURITE_VERSE);
                                startActivity(intent_view);
                                break;
                            //Copy text
                            case 1:
                                ClipboardManager cb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", favourites.get(versePosition).getVerseText());
                                cb.setPrimaryClip(clip);
                                Toast.makeText(getContext(), R.string.copied_text, Toast.LENGTH_SHORT).show();
                                break;
                            //Delete
                            case 2:
                                dbHelper.deleteVerse(favourites.get(versePosition), VerseType.FAVOURITE_VERSE);
                                actionsFavourites.remove(favourites.get(versePosition));
                                Toast.makeText(getContext(), R.string.deleted_verse, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                layout.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        versePosition = position;
                        dialog2.show();
                    }
                });
                break;

            case VerseType.PUBLISHED_VERSE:

                new LoadAllVerses().execute();
                publishedActions = Arrays.asList(getResources().getStringArray(R.array.actions_array_3));
                ActionsArrayAdapter actionsPublished = new ActionsArrayAdapter(getContext(), publishedActions);

                final AlertDialog.Builder dialog3 = new AlertDialog.Builder(getContext(), R.style.DialogNoBorder);
                dialog3.setAdapter(actionsPublished, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            //Open
                            case 0:
                                Intent intent_view = new Intent(getContext(), VerseViewActivity.class);
                                intent_view.putExtra(IntentString.VERSE_TYPE, VerseType.PUBLISHED_VERSE);
                                intent_view.putExtra(IntentString.PUB_VERSE_NAME, publishedVerses.get(versePosition).get("verse_name"));
                                intent_view.putExtra(IntentString.PUB_VERSE_TEXT, publishedVerses.get(versePosition).get("verse_text"));
                                intent_view.putExtra(IntentString.PUB_VERSE_AUTHOR, publishedVerses.get(versePosition).get("verse_author"));
                                intent_view.putExtra(IntentString.PUB_VERSE_DATE, publishedVerses.get(versePosition).get("verse_date"));
                                intent_view.putExtra(IntentString.PUB_VERSE_AUTHOR_ID, publishedVerses.get(versePosition).get("author_id"));
                                startActivity(intent_view);
                                break;
                            //Add to favourites
                            case 1:
                                Verse verse = new Verse();
                                verse.setVerseName(publishedVerses.get(versePosition).get("verse_name"));
                                verse.setVerseText(publishedVerses.get(versePosition).get("verse_text"));
                                verse.setVerseAuthor(publishedVerses.get(versePosition).get("verse_author"));
                                DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
                                verse.setVerseDate(LocalDate.parse(publishedVerses.get(versePosition).get("verse_date"), formatter));
                                verse.setAuthorID(publishedVerses.get(versePosition).get("author_id"));
                                dbHelper.addVerse(verse, VerseType.FAVOURITE_VERSE);
                                Toast.makeText(getContext(), "Добавлен в избранное", Toast.LENGTH_SHORT).show();
                                break;
                            //Copy text
                            case 2:
                                ClipboardManager cb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", publishedVerses.get(versePosition).get("verse_text"));
                                cb.setPrimaryClip(clip);
                                Toast.makeText(getContext(), R.string.copied_text, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                layout.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        versePosition = position;
                        dialog3.show();
                    }
                });
                break;

        }
        return view;
    }

    class PublishVerse extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        JSONParser jsonParser = new JSONParser();
        String response;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Загрузка...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair("operation", "add"));
            parameters.add(new BasicNameValuePair("author_id", authorID));
            parameters.add(new BasicNameValuePair("verse_author", authorName));
            parameters.add(new BasicNameValuePair("verse_name", myVerses.get(versePosition).getVerseName()));
            parameters.add(new BasicNameValuePair("verse_date", myVerses.get(versePosition).getVerseDate().toString()));
            parameters.add(new BasicNameValuePair("verse_text", myVerses.get(versePosition).getVerseText()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(MyURL.FOR_VERSES, "POST", parameters);

            if (jsonObject != null)
            {
                try
                {
                    Log.d("", jsonObject.toString());
                    response = jsonObject.getString("response");
                }
                catch (JSONException e)
                {
                    Log.d("", jsonObject.toString());
                }

                return null;
            }
            else return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();

            if (response != null)
            {
                if (response.equals(JSONResponse.PUBLICATION_SUCCESS))
                {
                    MyToast.makeText(getContext(), getString(R.string.verse_published_suc), Toast.LENGTH_SHORT);
                }
                else if (response == null || response.equals(JSONResponse.PUBLICATION_ERROR))
                {
                    MyToast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT);
                }
            }
        }
    }

    class LoadAllVerses extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        JSONParser jsonParser = new JSONParser();
        JSONArray verses = null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM.yyyy");

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Загрузка...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("operation", "allVerses"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(MyURL.FOR_VERSES, "POST", parameters);

            if (jsonObject != null)
            {
                Log.d("", jsonObject.toString());

                try
                {
                    verses = jsonObject.getJSONArray("verses");

                    for (int i = 0; i < verses.length(); i++)
                    {
                        JSONObject object = verses.getJSONObject(i);

                        String verse_id = object.getString("verse_id");
                        String author_id = object.getString("author_id");
                        String verse_author = object.getString("verse_author");
                        String verse_name = object.getString("verse_name");
                        String verse_date = object.getString("verse_date");
                        String verse_text = object.getString("verse_text");

                        LocalDate date = dtf.parseLocalDate(verse_date);
                        verse_date = dtfOut.print(date);

                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put("verse_id", verse_id);
                        hashMap.put("author_id", author_id);
                        hashMap.put("verse_author", verse_author);
                        hashMap.put("verse_name", verse_name);
                        hashMap.put("verse_date", verse_date);
                        hashMap.put("verse_text", verse_text);

                        publishedVerses.add(hashMap);
                    }
                }
                catch (JSONException e)
                {
                    Log.d("", jsonObject.toString());
                }

                return null;
            }
            else
            {
                MyToast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    PublishedVersesArrayAdapter adapter = new PublishedVersesArrayAdapter(getContext(), publishedVerses);
                    layout.setAdapter(adapter);
                }
            });
        }
    }
}
