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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class description:
 *  Should display arrivals by stop location ID
 *  TODO: fix color scheme
 */


public class ArrivalsActivity extends ActionBarActivity implements MasterTask {

    TextView tw;
    ArrivalsFactory arrivalsFactory;



    public void run(String command) {
        hideSoftKeyboard(ArrivalsActivity.this);
        mapToString(arrivalsFactory);
    }

    public void mapToString(ArrivalsFactory arrivalsFactory) {
        String result = new String();
        Iterator it = arrivalsFactory.iterator();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout displayPlace = (LinearLayout) findViewById(R.id.arrivalsDispay);
        Long epoch = System.currentTimeMillis();
        View custom = inflater.inflate(R.layout.insertable, null);
        LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
////            TextView tv = (TextView) custom.findViewById(R.id.text);
//            TextView tv = new TextView(this);
//            tv.setText(((Arrival) pair.getValue()).asString());
//            insertPoint.addView(tv);
//            it.remove(); // avoids a ConcurrentModificationException
//        }
        while (it.hasNext()) {
            View arrivalView = inflater.inflate(R.layout.arrival, null);
            Map.Entry pair = (Map.Entry)it.next();
            TextView tv = new TextView(this);

            TextView sign = (TextView) arrivalView.findViewById(R.id.Sign);
            TextView estimated = (TextView) arrivalView.findViewById(R.id.Estimated);
            TextView scheduled = (TextView) arrivalView.findViewById(R.id.Scheduled);

            sign.setTextColor(Color.LTGRAY);
            estimated.setTextColor(Color.LTGRAY);
            scheduled.setTextColor(Color.LTGRAY);

            Arrival arrival = (Arrival) pair.getValue();
            sign.setText(arrival.fullsign);
            estimated.setText(((arrival.estimated == null) ? "N/A": ((arrival.estimated-epoch)/(60*1000))+" min"));
            scheduled.setText(((arrival.scheduled == null) ? "N/A":(arrival.df.format(new Date(arrival.scheduled)))));

            tv.setText(((Arrival) pair.getValue()).asString());
            tv.setTextColor(Color.LTGRAY);
            tv.setLineSpacing(5,1);
            insertPoint.addView(arrivalView);
            it.remove(); // avoids a ConcurrentModificationException
        }
        displayPlace.addView(custom);
    }
    public void getArrivalsForId(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setText(message);
        ArrivalsUrlStringBuilder arrivals = new ArrivalsUrlStringBuilder();

        TextView tw = (TextView) findViewById(R.id.displayArrivalsView);
        tw.setTextSize(20);
        String [] stops = message.split(",");

        arrivalsFactory = new ArrivalsFactory(this);
        arrivalsFactory.getArrivalsAt(stops);
        HtmlRequester req = new HtmlRequester();
        req.execute(arrivalsFactory);

//        tw.setText(url);


//        setContentView(textView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivals);
        tw = (TextView) findViewById(R.id.displayArrivalsView);
        tw.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrivals, menu);
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
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
