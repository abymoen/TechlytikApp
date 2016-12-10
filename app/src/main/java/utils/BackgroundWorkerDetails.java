package utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by alex on 2016-06-21.
 */
public class BackgroundWorkerDetails extends AsyncTask<String, Void, String>{

    private MonitorDetailsActivity activity;
    private Context context;
    private String company, name, data;

    public BackgroundWorkerDetails(Context ctx, MonitorDetailsActivity act) {
        context = ctx;
        activity = act;
    }

    @Override
    protected String doInBackground(String... params) {

        company = params[0];
        name = params[1];

        try {
            URL url = new URL(Constants.GETDETAILS_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("company", "UTF-8") +"="+company+"&"+
                    URLEncoder.encode("monitor", "UTF-8") +"="+name;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while((data = bufferedReader.readLine()) != null) {
                stringBuilder.append(data+"\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        activity.parseJSON(s);
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
