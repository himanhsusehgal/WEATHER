package com.example.vishal.weather;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityname;
    TextView resulttextview;
    TextView textView;

    public void findweather(View view) throws UnsupportedEncodingException {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityname.getWindowToken(),0);
        String apikey = "https://openweathermap.org/data/2.5/weather?q="+cityname.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22";


      try {

          String encoderUrl =  URLEncoder.encode(cityname.getText().toString(),"UTF-8");

          Log.i("encoded URL",encoderUrl);
          DownloadTask task = new DownloadTask();
          task.execute(apikey);

      }
      catch (Exception e){
          e.printStackTrace();
          Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
      }




    }

    public class  DownloadTask extends AsyncTask<String ,Void,String>{


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1){

                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                Log.i("content",result);
                return result;
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i =0; i<arr.length();i++){
                    JSONObject jpart = arr.getJSONObject(i);
                    String main = "";
                    String description = "";
                    main = jpart.getString("main");
                    description = jpart.getString("description");
                    Log.i(main,description);
                    if(main.length() != 0 & description.length() !=0) {
                        textView.setText(cityname.getText().toString());
                        resulttextview.setText(main + " : " + description);
                    }
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname = (EditText) findViewById(R.id.editText);
        resulttextview = (TextView) findViewById(R.id.resulttextview);
        textView = (TextView) findViewById(R.id.textView3);

    }
}
