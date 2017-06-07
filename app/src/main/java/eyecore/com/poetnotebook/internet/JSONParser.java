package eyecore.com.poetnotebook.internet;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class JSONParser
{
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";

    public  JSONParser()
    {

    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        try
        {
            if (method.equals("POST"))
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity  = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
            }
            else if (method.equals("GET"))
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                String paramString = URLEncodedUtils.format(params, "UTF-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("", e.toString());
        }
        catch (ClientProtocolException e)
        {
            Log.e("", e.toString());
        }
        catch (IOException e)
        {
            Log.e("", e.toString());
        }

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }

            inputStream.close();
            json = stringBuilder.toString();
        }
        catch (Exception e)
        {
            Log.e("", e.toString());
        }

        try
        {
            jsonObject = new JSONObject(json);
        }
        catch (JSONException e)
        {
            Log.e("", e.toString());
        }

        return  jsonObject;
    }
}
