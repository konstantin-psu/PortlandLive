package edu.pdx.konstan2.trimetlive;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kmacarenco on 7/21/15.
 */
public class htmlRequestor extends AsyncTask<AsyncJob , Void, AsyncJob> {
    private Exception exception;

    protected AsyncJob doInBackground(AsyncJob... urls) {
        urls[0].setResponse(request(urls[0].url()));
        return urls[0];
    }

    protected void onPostExecute(AsyncJob feed) {
       feed.execute();
    }
    public String request(String urlString) {
        try {
            URL url= new URL(urlString);
            URL yahoo = url;


            HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            Log.d("exception in asyntask", e.toString());
            return null;
        }
    }

}
