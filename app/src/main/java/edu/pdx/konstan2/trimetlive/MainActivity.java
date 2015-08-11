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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * Class description
 *
 *  Main Activity is created when application started.
 *
 *  Defines onClick methods for the three main buttons:
 *      1. Get arrivals by id
 *      2. Get arrival by map
 *      3. Get arrivals by Routes
 */
public class MainActivity extends Activity {
    public Intent intent = null;

    // 1.
    public void callArrivalsById(View view) {
        Intent intent = new Intent(this, SelectByRoutesActivity.class);
        startActivity(intent);
    }

    // 2.
    public void callMap(View view) {
        intent = new Intent(this, LiveMap.class);
        startActivity(intent);
    }

    // 3.
    public void callArrivalsActivity(View view) {
        startActivity(new Intent(this, ArrivalsActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
