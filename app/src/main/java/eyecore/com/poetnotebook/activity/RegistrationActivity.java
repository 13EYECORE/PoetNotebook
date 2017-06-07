package eyecore.com.poetnotebook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eyecore.com.poetnotebook.R;
import eyecore.com.poetnotebook.settings.MyFont;
import eyecore.com.poetnotebook.main.MyToast;
import eyecore.com.poetnotebook.internet.InternetUtils;
import eyecore.com.poetnotebook.internet.JSONParser;
import eyecore.com.poetnotebook.internet.JSONResponse;
import eyecore.com.poetnotebook.internet.MyURL;


public class RegistrationActivity extends AppCompatActivity
{
    EditText edit_text_name;
    EditText edit_text_surname;
    EditText edit_text_email;
    EditText edit_text_password;

    TextView text_view_name;
    TextView text_view_surname;
    TextView text_view_email;
    TextView text_view_password;

    Button button_registration;

    Typeface typeface;

    String author;
    String email;
    String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_registration);

        typeface = MyFont.setDefaultFont(getApplicationContext());

        edit_text_name = (EditText) findViewById(R.id.edittext_enter_name);
        edit_text_name.setTypeface(typeface);
        edit_text_surname = (EditText) findViewById(R.id.edittext_enter_surname);
        edit_text_surname.setTypeface(typeface);
        edit_text_email = (EditText) findViewById(R.id.edittext_enter_email_reg);
        edit_text_email.setTypeface(typeface);
        edit_text_password = (EditText) findViewById(R.id.edittext_enter_password_reg);
        edit_text_password.setTypeface(typeface);

        text_view_name = (TextView)findViewById(R.id.textview_name_reg);
        text_view_name.setTypeface(typeface);
        text_view_surname = (TextView)findViewById(R.id.textview_surname_reg);
        text_view_surname.setTypeface(typeface);
        text_view_email = (TextView)findViewById(R.id.textview_email_reg);
        text_view_email.setTypeface(typeface);
        text_view_password = (TextView)findViewById(R.id.textview_pass_reg);
        text_view_password.setTypeface(typeface);

        button_registration = (Button) findViewById(R.id.button_reg);
        button_registration.setTypeface(typeface);
        button_registration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                author = edit_text_name.getText().toString() + " " + edit_text_surname.getText().toString();
                email = edit_text_email.getText().toString();
                password = edit_text_password.getText().toString();

                if (isEmptyFields())
                {
                    MyToast.makeText(getApplicationContext(), getString(R.string.fields_not_filled), Toast.LENGTH_SHORT);
                }
                else if(!InternetUtils.isEmailValid(email) || !InternetUtils.isValidPassword(password))
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.author_reg_email_pass_err), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.author_reg_email_pass_err_2), Toast.LENGTH_LONG).show();
                }
                else
                {
                    new RegistrationActivity.AuthorRegistration().execute();
                }
            }
        });
    }

    class AuthorRegistration extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        JSONParser jsonParser = new JSONParser();
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setMessage("Загрузка...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair("operation", "newAuthor"));
            parameters.add(new BasicNameValuePair("author", author));
            parameters.add(new BasicNameValuePair("email", email));
            parameters.add(new BasicNameValuePair("password", password));

            JSONObject jsonObject = jsonParser.makeHttpRequest(MyURL.FOR_AUTHORS, "POST", parameters);

            if (jsonObject != null) {
                try {
                    Log.d("", jsonObject.toString());
                    response = jsonObject.getString("response");
                } catch (JSONException e) {
                    Log.d("", jsonObject.toString());
                }

                return null;
            } else return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();

            if (response != null)
            {
                switch (response)
                {
                    case JSONResponse.REGISTERED:

                        MyToast.makeText(getApplicationContext(), getString(R.string.author_reg_suc), Toast.LENGTH_SHORT);
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        RegistrationActivity.this.finish();
                        break;

                    case JSONResponse.REGISTRATION_ERROR:

                        MyToast.makeText(getApplicationContext(), getString(R.string.author_reg_err), Toast.LENGTH_SHORT);
                        break;

                    case JSONResponse.AUTHOR_IS_EXIST:

                        MyToast.makeText(getApplicationContext(), getString(R.string.author_reg_email_err), Toast.LENGTH_SHORT);
                        break;
                }
            } else if (!InternetUtils.isOnline(getApplicationContext()))
            {
                MyToast.makeText(getApplicationContext(), getString(R.string.internet_err), Toast.LENGTH_SHORT);
            }
        }
    }

    private boolean isEmptyFields()
    {
        if (edit_text_name.getText().toString().isEmpty() ||
                edit_text_surname.getText().toString().isEmpty() ||
                edit_text_email.getText().toString().isEmpty() ||
                edit_text_password.getText().toString().isEmpty()) return true;
        else return false;
    }
}
