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
import techlytik.techlytik.ConfigureMonitorActivity;
import techlytik.techlytik.MonitorListActivity;

/**
 * Created by alex on 2016-06-29.
 */
public class BackgroundWorkerCompany extends AsyncTask<String, Void, String> {

    private Context context;
    private MonitorListActivity activity;
    private ConfigureMonitorActivity activityTwo;
    private int state;
    private String data;

    public BackgroundWorkerCompany(Context ctx, MonitorListActivity act) {
        this.context = ctx;
        this.activity = act;
        this.state = 0;
    }

    public BackgroundWorkerCompany(Context ctx, ConfigureMonitorActivity act) {
        this.context = ctx;
        this.activityTwo = act;
        this.state = 1;
    }


    @Override
    protected String doInBackground(String... params) {

        String username = params[0];

        try {
            URL url = new URL(Constants.GETCOMPANY_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + username;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((data = bufferedReader.readLine()) != null) {
                stringBuilder.append(data + "\n");
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
        if(state == 0) {
            activity.parseCompany(s);
            activity.nextParse();
        } else {
            activityTwo.parseCompany(s);
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
