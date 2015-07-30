package edu.pdx.konstan2.trimetlive;

import com.google.android.gms.maps.model.LatLng;

import org.json.simple.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class StopsFactory implements AsyncJob {
    String url;
    String response;
    HashMap<LatLng, Stop> stopsMap = new HashMap<LatLng, Stop>();
    StopsBuilder stopsRequest = new StopsBuilder();
    MasterTask master;

    public StopsFactory(MasterTask master) {
        this.master = master;
    }

    public String url() {
        return  url;
    }

    public void setResponse(String resp) {
        response = resp;
    }
    public void execute() {
        new responseParserFactory().parseStopsXML(response, stopsMap);
        master.run();

    }

    public void getStopsAtBounds(Bbox bounds) {
        url = stopsRequest.request(bounds.asString());
        htmlRequestor req = new htmlRequestor();
        req.execute(this);
    }


}

class Stop {
    public Long locID             ;//  Identifies the vehicle.
    public String direction;
    public Double latitude;
    public Double longitude;
    public String description;
    ArrayList<Route> routes = new ArrayList<Route>();

    public Stop (JSONObject v) {
        locID             = (Long) v.get("locid");
        direction                  = (String) v.get("dir");
        longitude             = (Double) v.get("lng");
        latitude              = (Double) v.get("lat");
        description           = (String) v.get("desc");
//        blockID               = (Long) v.get("blockID");
//        bearing               = (Long) v.get("bearing");
//        serviceDate           = (Long) v.get("serviceDate");
//        locationInScheduleDay = (Long) v.get("locationInScheduleDay");
//        time                  = (Long) v.get("time");
//        expires               = (Long) v.get("expires");
//        longitude             = (Double) v.get("longitude");
//        latitude              = (Double) v.get("latitude");
//        routeNumber           = (Long) v.get("routeNumber");
//        direction             = (Long) v.get("direction");
//        tripID                = (String) v.get("tripID");
//        newTrip               = (Boolean) v.get("newTrip");
//        delay                 = (Long) v.get("delay");
//        messageCode           = (Long) v.get("messageCode");
//        signMessage           = (String) v.get("signMessage");
//        signMessageLong       = (String) v.get("signMessageLong");
//        nextLocID             = (Long) v.get("nextLocID");
//        nextStopSeq           = (Long) v.get("nextStopSeq");
//        lastLocID             = (Long) v.get("lastLocID");
//        lastStopSeq           = (Long) v.get("lastStopSeq");
//        garage                = (String) v.get("garage");
//        extrablockID          = (Long) v.get("extrablockID");
//        offRoute              = (Boolean) v.get("offRoute");
//        inCongestion          = (Boolean) v.get("inCongestion");
//        loadPercentage        = (Long) v.get("loadPercentage");
    }


    public Iterator routesIterator() {
        return routes.iterator();
    }

    public Stop (Element v) {
        locID = Long.parseLong(v.getAttribute("locid"));
        direction                  = v.getAttribute("dir");
        longitude = Double.parseDouble(v.getAttribute("lng"));
        latitude = Double.parseDouble(v.getAttribute("lat"));
        description = v.getAttribute("desc");
//        blockID               = (Long) v.get("blockID");
//        bearing               = (Long) v.get("bearing");

        NodeList routesList = v.getElementsByTagName("route");
        for (int count = 0; count < routesList.getLength(); count++) {
            Node node1 = routesList.item(count);
            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                Element route = (Element) node1;
                routes.add(new Route(route));
            }
        }

//        serviceDate           = (Long) v.get("serviceDate");
//        locationInScheduleDay = (Long) v.get("locationInScheduleDay");
//        time                  = (Long) v.get("time");
//        expires               = (Long) v.get("expires");
//        longitude             = (Double) v.get("longitude");
//        latitude              = (Double) v.get("latitude");
//        routeNumber           = (Long) v.get("routeNumber");
//        direction             = (Long) v.get("direction");
//        tripID                = (String) v.get("tripID");
//        newTrip               = (Boolean) v.get("newTrip");
//        delay                 = (Long) v.get("delay");
//        messageCode           = (Long) v.get("messageCode");
//        signMessage           = (String) v.get("signMessage");
//        signMessageLong       = (String) v.get("signMessageLong");
//        nextLocID             = (Long) v.get("nextLocID");
//        nextStopSeq           = (Long) v.get("nextStopSeq");
//        lastLocID             = (Long) v.get("lastLocID");
//        lastStopSeq           = (Long) v.get("lastStopSeq");
//        garage                = (String) v.get("garage");
//        extrablockID          = (Long) v.get("extrablockID");
//        offRoute              = (Boolean) v.get("offRoute");
//        inCongestion          = (Boolean) v.get("inCongestion");
//        loadPercentage        = (Long) v.get("loadPercentage");
    }
}
