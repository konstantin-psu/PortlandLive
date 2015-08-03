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

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kmacarenco on 7/21/15.
 */
public class HtmlRequestor extends AsyncTask<AsyncJob , Void, ArrayList<AsyncJob>> {
    private Exception exception;

    protected ArrayList<AsyncJob> doInBackground(AsyncJob... urls) {
        ArrayList<AsyncJob> tasks = new ArrayList<AsyncJob>();
        for (AsyncJob j: urls) {
            j.setResponse(request(j.url()));
            tasks.add(j);
        }
        return tasks;
    }

    protected void onPostExecute(ArrayList<AsyncJob> feed) {
        Iterator<AsyncJob> it = feed.iterator();
        while(it.hasNext()) {
            AsyncJob j = it.next();
            j.execute();
            it.remove();
        }
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
