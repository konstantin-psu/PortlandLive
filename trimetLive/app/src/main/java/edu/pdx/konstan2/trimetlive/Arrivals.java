package edu.pdx.konstan2.trimetlive;

import org.json.simple.JSONObject;

/**
 * Created by kmacarenco on 7/21/15.
 */
public class Arrivals {
    String id;
    Long blockID;
    Boolean departed;
    Boolean detoured;
    JSONObject detour;
    Long dir;
    Long estimated;
    String feet;
    String fullsign;
    Boolean inCongestion;
    Long loadPercentage;
    Long locid;
    Long route;
    Double scheduled;
    String shortSign;
    String status;
    String reason;
    String tripID;
    Boolean newTrip;
    Boolean replacedService;
    String piece;
    String vehicleID;
    public Arrivals (JSONObject v) {
        id              = (String) v.get("locid");
        blockID         = (Long) v.get("blockID");
        departed        = (Boolean) v.get("departed");
        detoured        = (Boolean) v.get("detoured");
        detour          = (JSONObject) v.get("detour");
        dir             = (Long) v.get("dir");
        estimated       = (Long) v.get("estimated");
        feet            = (String) v.get("feet");
        fullsign        = (String) v.get("fullsign");
        inCongestion    = (Boolean) v.get("inCongestion");
        loadPercentage  = (Long) v.get("loadPercentage");
        locid           = (Long) v.get("locid");
        route           = (Long) v.get("route");
        scheduled       = (Double) v.get("scheduled");
        shortSign       = (String) v.get("shortSign");
        status          = (String) v.get("status");
        reason          = (String) v.get("reason");
        tripID          = (String) v.get("tripID");
        newTrip         = (Boolean) v.get("newTrip");
        replacedService = (Boolean) v.get("replacedService");
        piece           = (String) v.get("piece");
        vehicleID       = (String) v.get("vehicleID");
    }
}
