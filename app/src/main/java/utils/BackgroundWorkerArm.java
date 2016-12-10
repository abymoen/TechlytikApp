package utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import data.Constants;
import techlytik.techlytik.MonitorDetailsActivity;

/**
 * Created by alex on 2016-06-27.
 */
public class BackgroundWorkerArm extends AsyncTask<String, Void, String> {

    private Context context;
    private MonitorDetailsActivity activity;
    private String armedState, username, name;

    public BackgroundWorkerArm(Context ctx, MonitorDetailsActivity act) {
        this.context = ctx;
        this.activity = act;
    }


    @Override
    protected String doInBackground(String... params) {

        armedState = params[0];
        username = params[1];
        name = params[2];

        try {
            URL url = new URL(Constants.ARM_URL);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") +"="+username+"&"+
                        URLEncoder.encode("armed", "UTF-8") +"="+armedState+"&"+
                        URLEncoder.encode("name", "UTF-8") +"="+name;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();

                return result;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(context,"Bad URL",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("armed success")) {
            activity.refreshPage();
        } else {
            Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
