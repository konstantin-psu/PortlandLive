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

import com.google.android.gms.maps.model.LatLng;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
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
    StopsUrlStringBuilder stopsRequest = new StopsUrlStringBuilder();
    MasterTask master;
    public static final String command = "addStops";

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
        master.run(command);

    }

    public void getStopsAtBounds(Bbox bounds) {
        url = stopsRequest.request(bounds.asString());
    }


}

class Stop {
    public Long locID;//  Identifies the vehicle.
    public String direction;
    public Double latitude;
    public Double longitude;
    public String description;
    ArrayList<Route> routes = new ArrayList<Route>();
    JSONObject jsonRepresntation;
    Element xmlRepresentation;

    public String toJsonString() {
        StringBuilder str = new StringBuilder("{");
        str.append("\"locid\":"+locID+",");
        str.append("\"dir\":"+"\""+direction+"\""+",");
        str.append("\"lng\":"+longitude+",");
        str.append("\"lat\":"+latitude+",");
        str.append("\"desc\":"+"\""+description+"\"");
        String prefix = "";
        if (routes.size() != 0) {
            str.append(",\"routes\":"+"[");
            for (Route r: routes) {
                str.append(prefix);
                str.append(r.toJsonString()+",");
            }
            str.append("]");
        }
        str.append("}");
        return str.toString();
    }


    public Stop(String jsonString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(jsonString);
            jsonRepresntation = jobj;
            parse(jsonRepresntation);
        } catch (Exception e) {
        }

    }
    public String[] routesIDs () {
        ArrayList<String> result = new ArrayList<>();
        for (Route r: routes) {
            result.add(r.route.toString());
        }
        return result.toArray(new String[result.size()]);
    }
    public String[] listRoutes() {
        ArrayList<String> result = new ArrayList<>();
        for (Route r: routes) {
            result.add(r.description);
        }
        return result.toArray(new String[result.size()]);
    }

    public void parse (JSONObject v) {
        locID             = (Long) v.get("locid");
        direction                  = (String) v.get("dir");
        longitude             = (Double) v.get("lng");
        latitude              = (Double) v.get("lat");
        description           = (String) v.get("desc");
        JSONArray arr = (JSONArray) v.get("routes");
        Iterator<JSONObject> iter = arr.iterator();
        while(iter.hasNext()) {
            Route r = new Route(iter.next());
            routes.add(r);
        }
    }
    public Stop (JSONObject v) {
        jsonRepresntation = v;
        parse(v);
    }


    public Iterator routesIterator() {
        return routes.iterator();

    }

    public String toEncodedString() {
        return toJsonString();
    }
    public Stop (Element v) {
        xmlRepresentation = v;
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
    }

    public Stop (Element v, Boolean skipRoutes) {
        xmlRepresentation = v;
        locID = Long.parseLong(v.getAttribute("locid"));
        direction                  = v.getAttribute("dir");
        longitude = Double.parseDouble(v.getAttribute("lng"));
        latitude = Double.parseDouble(v.getAttribute("lat"));
        description = v.getAttribute("desc");
//        blockID               = (Long) v.get("blockID");
//        bearing               = (Long) v.get("bearing");

        if (!skipRoutes) {
            NodeList routesList = v.getElementsByTagName("route");
            for (int count = 0; count < routesList.getLength(); count++) {
                Node node1 = routesList.item(count);
                if (node1.getNodeType() == node1.ELEMENT_NODE) {
                    Element route = (Element) node1;
                    routes.add(new Route(route));
                }
            }
        }
    }

}
