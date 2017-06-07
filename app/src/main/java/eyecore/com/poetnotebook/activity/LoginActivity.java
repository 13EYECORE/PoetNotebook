package eyecore.com.poetnotebook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.main.MyToast;
import eyecore.com.poetnotebook.internet.JSONParser;
import eyecore.com.poetnotebook.internet.JSONResponse;
import eyecore.com.poetnotebook.internet.MyURL;
import eyecore.com.poetnotebook.internet.InternetUtils;
import eyecore.com.poetnotebook.main.Author;
import eyecore.com.poetnotebook.string.PreferenceString;

public class LoginActivity extends AppCompatActivity
{
    EditText edit_text_email;
    EditText edit_text_password;
    Button button_enter;
    TextView text_view_registration;
    TextView text_view_email;
    TextView text_view_password;

    String email;
    String password;

    SharedPreferences sharedPref;
    SharedPreferences oldSharedPref;

    Typeface typeface;

    boolean isAuthorLogin = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_login);

        sharedPref = getSharedPreferences(PreferenceString.AUTHOR_PREFERENCES, MODE_PRIVATE);

        typeface = MyFont.setDefaultFont(getApplicationContext());

        oldSharedPref = getSharedPreferences("activity.MainMenuActivity", MODE_PRIVATE);

        text_view_email = (TextView)findViewById(R.id.textview_email_login);
        text_view_email.setTypeface(typeface);

        text_view_password = (TextView)findViewById(R.id.textview_pass_login);
        text_view_password.setTypeface(typeface);

        edit_text_email = (EditText)findViewById(R.id.edittext_enter_email_login);
        edit_text_email.setTypeface(typeface);

        edit_text_password = (EditText)findViewById(R.id.edittext_enter_password_login);
        edit_text_password.setTypeface(typeface);

        button_enter = (Button)findViewById(R.id.button_enter);
        button_enter.setTypeface(typeface);
        button_enter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = edit_text_email.getText().toString();
                password = edit_text_password.getText().toString();


                if (isEmptyFields())
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.fields_not_filled), Toast.LENGTH_SHORT).show();
                }
                else if (!InternetUtils.isEmailValid(email))
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.author_login_email_err), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new LoginActivity.AuthorLogin().execute();
                    new LoginActivity.AuthorInfo().execute();
                }
            }
        });

        text_view_registration = (TextView)findViewById(R.id.textview_registration);
        text_view_registration.setTypeface(typeface);
        text_view_registration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    class AuthorLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        JSONParser jsonParser = new JSONParser();
        String response;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Загрузка...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair("operation", "loginAuthor"));
            parameters.add(new BasicNameValuePair("email", email));
            parameters.add(new BasicNameValuePair("password", password));

            JSONObject jsonObject = jsonParser.makeHttpRequest(MyURL.FOR_AUTHORS, "POST", parameters);

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
                if (response.equals(JSONResponse.LOGIN))
                {
                    Author.saveAuthorLoginState(sharedPref, true);
                    MyToast.makeText(getApplicationContext(), getString(R.string.author_login_suc), Toast.LENGTH_SHORT);
                    isAuthorLogin = true;
                }
                else if (response.equals(JSONResponse.LOGIN_ERROR))
                {
                    MyToast.makeText(getApplicationContext(), getString(R.string.author_login_err), Toast.LENGTH_SHORT);
                }
            }
            else if (!InternetUtils.isOnline(getApplicationContext()))
            {
                MyToast.makeText(getApplicationContext(), getString(R.string.internet_err), Toast.LENGTH_SHORT);
            }
            else
            {
                MyToast.makeText(getApplicationContext(), getString(R.string.internet_err), Toast.LENGTH_SHORT);
            }
        }
    }

    class AuthorInfo extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        JSONParser jsonParser = new JSONParser();
        String responseName;
        String responseID;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Загрузка...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> parameters = new ArrayList<>();
            JSONArray authorInfo;

            parameters.add(new BasicNameValuePair("operation", "sendAuthorInfo"));
            parameters.add(new BasicNameValuePair("email", email));

            JSONObject jsonObject = jsonParser.makeHttpRequest(MyURL.FOR_AUTHORS, "POST", parameters);

            if (jsonObject != null)
            {
                try
                {
                    Log.d("", jsonObject.toString());
                    authorInfo = jsonObject.getJSONArray("authorInfo");
                    JSONObject object = authorInfo.getJSONObject(0);
                    responseName = object.getString("author");
                    responseID = object.getString("author_id");
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

            if (responseName != null & responseID != null & isAuthorLogin)
            {
                Author.saveAuthorLoginState(sharedPref, true);
                Author.saveAuthorInfo(sharedPref, responseName, responseID);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                MyToast.makeText(getApplicationContext(), getString(R.string.author_info_suc), Toast.LENGTH_SHORT);
                LoginActivity.this.finish();
            }
            else if (!InternetUtils.isOnline(getApplicationContext()))
            {
                MyToast.makeText(getApplicationContext(), getString(R.string.internet_err), Toast.LENGTH_SHORT);
            }
            else
            {
                MyToast.makeText(getApplicationContext(), getString(R.string.author_info_err), Toast.LENGTH_SHORT);
            }

        }
    }

    private boolean isEmptyFields()
    {
        if (edit_text_email.getText().toString().isEmpty() ||
            edit_text_password.getText().toString().isEmpty()) return true;
        else return false;
    }
}
