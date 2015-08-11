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
 *
 * Class description:
 *  Main purpose is to perform request to Web Api and return response as string.
 *
 *  Expects AsyncJob as the argument. AsyncJob
 *     public interface AsyncJob {
 *         public String url();
 *         public void setResponse(String response);
 *         public void execute();
 *     }
 *  Where  - url() is url to perform request on.
 *         - setResponse(String response) sets response to the AsyncJob response global variable
 *         - execute() is a main callback method that is triggered upon request completion
 *              usually AsyncJob is expected to do some action with the received information
 *              AsyncJob must do it only if/when request succeeds.
 */
public class HtmlRequester extends AsyncTask<AsyncJob , Void, ArrayList<AsyncJob>> {


    // *** Perform request on every AsyncJob, this method is called it multithreaded fashion
    protected ArrayList<AsyncJob> doInBackground(AsyncJob... urls) {
        ArrayList<AsyncJob> tasks = new ArrayList<>();
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


    // *** Execute request, and read response if any...
    public String request(String urlString) {
        try {
            URL url = new URL(urlString);


            HttpURLConnection yc = (HttpURLConnection) url.openConnection();
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
            Log.d("Perform request", e.toString());
            return null;
        }
    }

}
