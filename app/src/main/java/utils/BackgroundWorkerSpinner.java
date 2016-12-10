package utils;

import android.content.Context;
import android.os.AsyncTask;

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
import techlytik.techlytik.MonitorListActivity;

/**
 * Created by alex on 2016-06-28.
 */
public class BackgroundWorkerSpinner  extends AsyncTask<String, Void, String>{

    private Context context;
    private MonitorListActivity activity;
    private String company, data;

    public BackgroundWorkerSpinner(Context ctx, MonitorListActivity act) {
        this.context = ctx;
        this.activity = act;
    }


    @Override
    protected String doInBackground(String... params) {

        company = params[0];

        try {
            URL url = new URL(Constants.SPINNER_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("comp", "UTF-8") +"="+company;
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
        activity.parseRuns(s);
        activity.populateSpinner();
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
