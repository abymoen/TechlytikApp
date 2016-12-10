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
import techlytik.techlytik.NewUserActivity;

/**
 * Created by alex on 2016-06-11.
 */
public class BackgroundWorkerRegister extends AsyncTask<String, Void, String> {

    private NewUserActivity act;
    private Context context;

    public BackgroundWorkerRegister(Context ctx, NewUserActivity activity) {
        context = ctx;
        act = activity;
    }


    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String pass = params[1];
        String company = params[2];

        try {
            URL url = new URL(Constants.RESGISTER_URL);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") +"="+username+"&"+
                        URLEncoder.encode("pass", "UTF-8") +"="+pass+"&"+
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
                Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(context,"Bad URL",Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        act.registerActivity(result);
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
