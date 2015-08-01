package edu.pdx.konstan2.trimetlive;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by kmacarenco on 7/30/15.
 */
public class VehiclesLocationFactory implements AsyncJob {
    String url;
    String response;
    HashMap<LatLng, Vehicle> vehiclesMap;
    VehiclesLocationBuilder vehiclesLocationBuilder = new VehiclesLocationBuilder();
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


