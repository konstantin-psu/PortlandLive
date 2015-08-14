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

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by kmacarenco on 7/30/15.
 */
public class VehiclesLocationFactory implements AsyncJob {
    String url;
    String response;
    HashMap<LatLng, Vehicle> vehiclesMap;
    VehiclesLocationUrlStringBuilder vehiclesLocationBuilder = new VehiclesLocationUrlStringBuilder();
    MasterTask master;
    public final static String command = "addVehiclesLocations";

    public VehiclesLocationFactory(MasterTask master) {
        vehiclesMap = new HashMap<>();
        this.master = master;
    }

    public String url() {
        return  url;
    }

    public void setResponse(String resp) {
        response = resp;
    }
    public void execute() {
        new responseParserFactory().parseVehiclesLocationJSON(response, vehiclesMap);
        master.run(command);

    }

    public void getVehiclesOnRoutes(String [] routes) {
        url = vehiclesLocationBuilder.request(routes);
    }
}


