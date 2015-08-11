/*
 * Copyright (c) 2015. Konstantin Macarenco
 *
 * [This program is licensed under the GPL version 3 or later.]
 *
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 *
 */

package edu.pdx.konstan2.trimetlive;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class mapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        connector test = new connector();
        try {
            test.test2();
        } catch (Exception e) {
            textView.setText(e.toString());
            setContentView(textView);// Set the text view as the activity layout
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showM(String m) {
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setText(m);
        setContentView(textView);
    }
    class connector {
//        public String test() throws Exception {
//            URL yahoo = new URL("http://developer.trimet.org/ws/v2/vehicles");
//            HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(
//                            yc.getInputStream()));
//            String inputLine;
//            String res = new String();
//            while ((inputLine = in.readLine()) != null)
//                res += " " + inputLine;
//            in.close();
//            return res;
//        }
        public String test2() throws Exception {
            String res = "";
            req requester = new req();
            requester.execute("http://developer.trimet.org/ws/v2/vehicles?appID=EEC7240AC3168C424AC5A98E1");
            return res;
        }
    }

    class req extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                URL url= new URL(urls[0]);
                URL yahoo = url;
                String res = new String();

                String appId = "EEC7240AC3168C424AC5A98E1";

                HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
//                yc.setRequestProperty("appID", appId);

//                return Integer.toString(yc.getResponseCode());
//                return yc.toString();
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
                this.exception = e;
                return e.toString();
            }
        }

        protected void onPostExecute(String feed) {
            showM(feed);
        }
    }
}
