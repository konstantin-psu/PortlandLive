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

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by konstantin on 7/31/15.
 */
public class VehicleInfoWindowBuilder {


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

        return vehicleInfoWindow;


    }
}
