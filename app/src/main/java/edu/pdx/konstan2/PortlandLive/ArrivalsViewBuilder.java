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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kmacarenco on 7/31/15.
 *
 * Class Description:
 *  Class single responsibility is to build View For the Arrivals information
 *
 *  View structure is defined in arrival.xml, and gathered in insertable, which is just
 *  a linear list
 *
 *
 */
public class ArrivalsViewBuilder {
    public void buildView(ArrivalsFactory arrivalsFactory, Activity a) {

        Long epoch = System.currentTimeMillis();

        Iterator it = arrivalsFactory.iterator();

        LayoutInflater inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout displayPlace = (LinearLayout) a.findViewById(R.id.view_insert_point);

        View custom = inflater.inflate(R.layout.insertable, null);
        LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
        while (it.hasNext()) {
            View arrivalView = inflater.inflate(R.layout.arrival, null);
            Map.Entry pair = (Map.Entry)it.next();
            TextView tv = new TextView(a);

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
}
