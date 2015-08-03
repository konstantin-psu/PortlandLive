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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ArrivalsActivity extends ActionBarActivity implements AsyncJob {

    String url;
    String response;
    TextView tw;
    HashMap <String, Arrival> arrivalsmap;
    public String url() {
        return  url;
    }

    public void setResponse(String resp) {
        response = resp;
    }
    public void execute() {
        new responseParserFactory().parseArrivals(response, arrivalsmap);
//        tw.setText(addArrivals(vehiclesMap));
        mapToString(arrivalsmap);
        ArrivalsViewBuilder arrivalsViewBuilder = new ArrivalsViewBuilder();
//        arrivalsViewBuilder.buildView();
        hideSoftKeyboard(ArrivalsActivity.this);

    }
    public void mapToString(Map<String, Arrival> arr) {
        String result = new String();
        Iterator it = arr.entrySet().iterator();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout displayPlace = (LinearLayout) findViewById(R.id.arrivalsDispay);
        View custom = inflater.inflate(R.layout.insertable, null);
        LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
//            TextView tv = (TextView) custom.findViewById(R.id.text);
            TextView tv = new TextView(this);
            tv.setText(((Arrival) pair.getValue()).asString());
            insertPoint.addView(tv);
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
        ArrivalsBuilder arrivals = new ArrivalsBuilder();

        TextView tw = (TextView) findViewById(R.id.displayArrivalsView);
        tw.setTextSize(20);
        url = arrivals.request(message.split(","));

        responseParserFactory parser = new responseParserFactory();
        HtmlRequestor req = new HtmlRequestor();
        req.execute(this);

//        tw.setText(url);


//        setContentView(textView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivals);
        tw = (TextView) findViewById(R.id.displayArrivalsView);
        tw.setMovementMethod(new ScrollingMovementMethod());
        arrivalsmap = new HashMap<String, Arrival>();
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
