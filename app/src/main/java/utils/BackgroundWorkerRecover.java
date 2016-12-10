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
import techlytik.techlytik.RecoverPasswordActivity;

/**
 * Created by alex on 2016-06-12.
 */
public class BackgroundWorkerRecover extends AsyncTask<String, Void, String>{

    private Context ctx;
    private RecoverPasswordActivity act;

    public BackgroundWorkerRecover(Context context, RecoverPasswordActivity activity) {
        ctx = context;
        act = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String company = params[1];



        try {
            URL url = new URL(Constants.RECOVER_URL);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") +"="+username+"&"+
                        URLEncoder.encode("company", "UTF-8") +"="+company+"&"+
                        URLEncoder.encode("table", "UTF-8") +"=companies";
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
                Toast.makeText(ctx, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(ctx,"Bad URL",Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.v("RECOVERDATA", result);
        act.startChangeActivity(result);
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
