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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;


public class SelectByRoutesActivity extends ActionBarActivity implements MasterTask {

    RoutesFactory routesFactory;
    SelectByRoutesActivity thisPointer;

    public void run(String command) {
        if (command.equals(routesFactory.COMMAND)) {
            //Display all gathered routes
            buildRoutesView(routesFactory, this);

        }

    }

    public SelectByRoutesActivity() {
        thisPointer = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_by_routes);

        routesFactory = new RoutesFactory(this);
        HtmlRequester htmlRequester = new HtmlRequester();
        routesFactory.getAllRoutesWithStops();
        htmlRequester.execute(routesFactory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_by_routes, menu);
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

    public void buildRoutesView(RoutesFactory routesFactory, Activity a) {

        Iterator it = routesFactory.iterator();

        LayoutInflater inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout displayPlace = (LinearLayout) a.findViewById(R.id.insert_point);
        View routesView = inflater.inflate(R.layout.activity_select_by_routes, null);
        LinearLayout inserPoint = (LinearLayout) routesView.findViewById(R.id.insert_point);

        Boolean colorParity = false;
        while (it.hasNext()) {
            Route route = (Route) it.next();
            View routeView = inflater.inflate(R.layout.route, null);
            LinearLayout mainLayout = (LinearLayout) routeView.findViewById(R.id.mainL);
            mainLayout.setBottom(20);
//            if (colorParity)
//                routeView.setBackground(getResources().getDrawable(R.drawable.route_blue));
//            else
//                routeView.setBackground(getResources().getDrawable(R.drawable.route_red));
//            colorParity = !colorParity;
            LinearLayout dir1Onclick = (LinearLayout) routeView.findViewById(R.id.dir1OnclickLocation);
            LinearLayout dir2Onclick = (LinearLayout) routeView.findViewById(R.id.dir2OnclickLocation);

            TextView id =      (TextView) routeView.findViewById(R.id.Id);
            TextView sign =      (TextView) routeView.findViewById(R.id.Sign);
            TextView type = (TextView) routeView.findViewById(R.id.Type);
            TextView dirSign = (TextView) routeView.findViewById(R.id.DirectionSign);
            TextView dirSign1 = (TextView) routeView.findViewById(R.id.DirectionSign1);

            id.setTextColor(Color.LTGRAY);
            sign.setTextColor(Color.LTGRAY);
            type.setTextColor(Color.LTGRAY);
            dirSign.setTextColor(Color.LTGRAY);
            dirSign1.setTextColor(Color.LTGRAY);

            id.setText(route.route.toString());
            sign.setText(route.description);
            type.setText(route.type);

            Dir dir1 = route.directions.get(0);
            //TODO should loop through directions instead of hardcoding -- someday?
            dirSign.setText(dir1.description);
            setupRouteListener(routeView, dirSign, dir1Onclick, route.route);
            if (route.directions.size() > 1) {
                Dir dir2 = route.directions.get(1);
                dirSign1.setText(dir2.description);
                setupRouteListener(routeView, dirSign1, dir2Onclick, route.route);
            } else {
                dirSign1.setVisibility(View.GONE);
            }

//            tv.setLineSpacing(5,1);
            inserPoint.addView(routeView);

        }
        displayPlace.addView(routesView);
    }

    public void setupRouteListener(View routeView, final TextView sign, final LinearLayout dirOnclick, final Long routeID) {
        dirOnclick.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {


//                 routesFactory.
//                 Stop currentStop = stopMap.get(position);
//
                                              Route currentRoute = routesFactory.routesMap.get(routeID);
                                              String message = routeID.toString();
                                              Intent intent = new Intent(thisPointer, StopsPickerActivity.class);
                                              intent.putExtra("routeJsonEncoded", message);
                                              intent.putExtra("direction", sign.getText());
                                              startActivity(intent);

                }
            }
        );
    }
}
