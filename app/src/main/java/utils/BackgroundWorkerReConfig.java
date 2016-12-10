package utils;

import android.content.Context;
import android.os.AsyncTask;
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
import techlytik.techlytik.ConfigureMonitorActivity;

/**
 * Created by alex on 2016-07-04.
 */
public class BackgroundWorkerReConfig extends AsyncTask<String, Void, String>{


    private Context context;
    private ConfigureMonitorActivity activity;
    public BackgroundWorkerReConfig(Context ctx, ConfigureMonitorActivity act) {
        context = ctx;
        activity = act;
    }


    @Override
    protected String doInBackground(String... params) {

        String username = params[0];
        String name = params[1];
        String field = params[2];
        String run = params[3];
        String surfaceLocation = params[4];
        String drumholeLocation = params[5];
        String company = params[10];
        String oldname = params[11];

        try {
            URL url = new URL(Constants.RECONFIG_URL);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") +"="+username+"&"+
                        URLEncoder.encode("name", "UTF-8") +"="+name+"&"+
                        URLEncoder.encode("field", "UTF-8") +"="+field+"&"+
                        URLEncoder.encode("armed", "UTF-8") +"=no"+"&"+
                        URLEncoder.encode("surfacelocation", "UTF-8") +"="+surfaceLocation+"&"+
                        URLEncoder.encode("drumholelocation", "UTF-8") +"="+drumholeLocation+"&"+
                        URLEncoder.encode("company", "UTF-8")+"="+company+"&"+
                        URLEncoder.encode("oldname", "UTF-8")+"="+oldname+"&"+
                        URLEncoder.encode("run", "UTF-8") +"="+run;
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
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("Entry success")) {
            activity.startNextActivity();
        } else if(s.equals("Name copy")) {
            Toast.makeText(context,"You've used that name",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Connection Error",Toast.LENGTH_SHORT).show();
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

