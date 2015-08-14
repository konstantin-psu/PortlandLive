/*
 * Copyright (c) 2015. Konstantin Macarenco
 *
 * [This program is licensed under the GPL version 3 or later.]
 *
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 *
 */

package edu.pdx.konstan2.PortlandLive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;


public class StopsPickerActivity extends ActionBarActivity implements  MasterTask {
    private String thisDirection;
    private StopsPickerActivity thisPointer;
    private RoutesFactory routesFactory;

    public StopsPickerActivity() {
        thisPointer = this;
    }

    public void run(String command) {
        if (command.equals(RoutesFactory.COMMAND)) {
            buildRoutesView(routesFactory, this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_picker);

        Intent intent = getIntent();

        String routeID = intent.getStringExtra("routeJsonEncoded");
        thisDirection = intent.getStringExtra("direction");

        routesFactory =  new RoutesFactory(this);
        routesFactory.getRoutes(routeID.split(" "), true);

        HtmlRequester htmlRequester = new HtmlRequester();
        htmlRequester.execute(routesFactory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stops_picker, menu);
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


    /**
     *
     * @param routesFactory routesHolder
     * @param a activity where view will be added
     */
    public void buildRoutesView(RoutesFactory routesFactory, Activity a) {

        // Should do only if one route is chosen
        if (routesFactory.size() > 1) {
//            if (routesFactory.routes.size() > 1) {
            Toast.makeText(getApplicationContext(), "FAIL: found more than one route", Toast.LENGTH_SHORT).show();
            return;
        }


        //Create insert point view, which is just a linear list
        LayoutInflater inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout displayPlace = (LinearLayout) a.findViewById(R.id.insert_point);

        View insertable = inflater.inflate(R.layout.insertable, null);
        LinearLayout insertPoint = (LinearLayout) insertable.findViewById(R.id.insert_point);


        // Now we need to find a route that will be used to get Arrivals information
        Route thisRoute = routesFactory.get(0);
        Iterator it = thisRoute.directions.iterator();
        Dir thisDir = null;
        while (it.hasNext()) {
            Dir d = (Dir) it.next();
            if (thisDirection.equals(d.description)){
               thisDir = d;
                break;
            }
        }
        assert(thisDir != null);

        // Route is found, create view with all arrivals for this route,
        // Each Arrival should be clickable, by click we are going to open LiveArrivals activity
        it = thisDir.iterator();
        Boolean colorSwitch = false;
        while (it.hasNext()) {
            final Stop stop = (Stop) it.next();
            stop.routes.add(thisRoute);
            View stopView = inflater.inflate(R.layout.stop, null);

            TextView id =      (TextView) stopView.findViewById(R.id.Id);
            TextView sign =      (TextView) stopView.findViewById(R.id.Sign);
            LinearLayout onClickOwner = (LinearLayout) stopView.findViewById(R.id.StopOnClick);
//            if (colorSwitch)
//                onClickOwner.setBackground(getResources().getDrawable(R.drawable.route_blue));
//            else
//                onClickOwner.setBackground(getResources().getDrawable(R.drawable.route_red));
//            colorSwitch = !colorSwitch;

            id.setTextColor(Color.LTGRAY);
            sign.setTextColor(Color.LTGRAY);

            id.setText(stop.locID.toString());
            sign.setText(stop.description);

            insertPoint.addView(stopView);
            onClickOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisPointer, LiveArrivals.class);
                    intent.putExtra("stopJsonEncoded", stop.toEncodedString());
                    startActivity(intent);
                }
            });

        }
        displayPlace.addView(insertable);
    }

}
