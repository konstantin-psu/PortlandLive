package edu.pdx.konstan2.trimetlive;

import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kmacarenco on 7/21/15.
 */
public class ArrivalsFactory implements AsyncJob {
    String url;
    String response;
    HashMap<String, Arrival> arrivalsmap;
    ArrivalsBuilder arrivalsRequest = new ArrivalsBuilder();
    MasterTask master;
    public static final String command = "addArrivals";

    public ArrivalsFactory(MasterTask master) {
        arrivalsmap = new HashMap<>();
        this.master = master;
    }

    public String url() {
        return  url;
    }

    public void setResponse(String resp) {
        response = resp;
    }
    public void execute() {
        new responseParserFactory().parseArrivals(response, arrivalsmap);
        master.run(command);

    }

    public void getArrivalsAt(String [] stopLocations) {
        url = arrivalsRequest.request(stopLocations);
    }


}


class Arrival {
    String id;
    Long blockID;
    Boolean departed;
    Boolean detoured;
    JSONObject detour;
    Long dir;
    Long estimated;
    Long feet;
    String fullsign;
    Boolean inCongestion;
    Long loadPercentage;
    Long locid;
    Long route;
    Long scheduled;
    String shortSign;
    String status;
    String reason;
    String tripID;
    Boolean newTrip;
    Boolean replacedService;
    String piece;
    String vehicleID;
    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    ArrayList<Route> routes = new ArrayList<>();
    JSONObject jsonArrival;
    public String asString() {
        Date scheduled = new Date(this.scheduled);
        Date estimated;
        if (this.estimated != null) {
            estimated = new Date(this.estimated);
            return shortSign + " " + scheduled.toString() + " estimated at " + estimated.toString();
        } else {
            return shortSign + " " + scheduled.toString();
        }
    }
    public Arrival(JSONObject v) {
        jsonArrival = v;
        id              = (String) v.get("id");
        blockID         = (Long) v.get("blockID");
        departed        = (Boolean) v.get("departed");
        detoured        = (Boolean) v.get("detoured");
//        detour          = (JSONObject) v.get("detour"); TODO Add detours later
        dir             = (Long) v.get("dir");
        estimated       = (Long) v.get("estimated");
        feet            = (Long) v.get("feet");
        fullsign        = (String) v.get("fullSign");
        inCongestion    = (Boolean) v.get("inCongestion");
        loadPercentage  = (Long) v.get("loadPercentage");
        locid           = (Long) v.get("locid");
        route           = (Long) v.get("route");
        scheduled       = (Long) v.get("scheduled");
        shortSign       = (String) v.get("shortSign");
        status          = (String) v.get("status");
//        reason          = (String) v.get("reason");
        tripID          = (String) v.get("tripID");
        newTrip         = (Boolean) v.get("newTrip");
//        replacedService = (Boolean) v.get("replacedService");
        piece           = (String) v.get("piece");
        vehicleID       = (String) v.get("vehicleID");
    }
}

