package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import data.Constants;
import techlytik.techlytik.LoginActivity;

/**
 * Created by alex on 2016-06-10.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {

    private LoginActivity act;
    private Context context;


    public BackgroundWorker(Context ctx, LoginActivity activity) {
        context = ctx;
        act = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String username = params[1];
        String pass = params[2];
        if(type.equals("login")) {
            try {
                URL url = new URL(Constants.LOGIN_URL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") +"="+username+"&"+
                        URLEncoder.encode("pass", "UTF-8") +"="+pass+"&"+
                            URLEncoder.encode("table", "UTF-8") +"=users";
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
                    Toast.makeText(context,"Connection Failed",Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(context,"Bad URL",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Wrong type",Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        act.setVerify(result);
        act.loginActivity(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}


