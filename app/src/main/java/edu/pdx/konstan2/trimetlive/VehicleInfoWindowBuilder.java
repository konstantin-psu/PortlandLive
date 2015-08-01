package edu.pdx.konstan2.trimetlive;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by konstantin on 7/31/15.
 */
public class VehicleInfoWindowBuilder {

/*    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    LinearLayout displayPlace = (LinearLayout) findViewById(R.id.arrivalsDispay);
    View custom = inflater.inflate(R.layout.insertable, null);
    LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
    Stop stop = stopMap.get(stopPosition);
    Iterator<Route> it = stop.routesIterator();
    while (it.hasNext()) {
        Route r = it.next();
        TextView tv = new TextView(getApplicationContext());
        tv.setPadding(10, 5, 10, 5);
        tv.setText(r.asString());
        insertPoint.addView(tv);
    }*/

    public View buildView(Vehicle v, LayoutInflater inflater) {

        View vehicleInfoWindow = inflater.inflate(R.layout.vehicle, null);

        TextView sign = (TextView) vehicleInfoWindow.findViewById(R.id.Sign);
        TextView type = (TextView) vehicleInfoWindow.findViewById(R.id.Type);

        sign.setTextColor(Color.LTGRAY);
        type.setTextColor(Color.LTGRAY);

        sign.setText(v.signMessageLong);
        type.setText(v.type);

        sign.setPadding(10, 5, 10, 5);
        type.setPadding(10, 5, 10, 5);
//                            TextView tv = new TextView(getApplicationContext());
//                            tv.setPadding(10, 5, 10, 5);
//                            tv.setText(r.asString());
//                            insertPoint.addView(tv);

        return vehicleInfoWindow;


    }
}
