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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by konstantin on 7/5/15.
 *
 * Class description
 *
 * Intention of this class is to provide interface for building urls for the Trimet API
 *
 * Each builder will return request string like http://developer.trimet.org/ws/V1/routeConfig
 *
 * For the  Trimet API reference go to http://developer.trimet.org/ws_docs/
 */

public class UrlRequestStringBuilder {
    protected Map<String, String> options = new HashMap<>();
    protected String separator = "/";
    public UrlRequestStringBuilder() {
        options.put("base", "developer.trimet.org/ws/");
        options.put("secure", "https://");
        options.put("regular", "http://");
        options.put("v1", "V1/");
        options.put("v2", "v2/");
        options.put("opts", "/");
        options.put("appID", "appID/EEC7240AC3168C424AC5A98E1");
    }
    public String simple() {
        return options.get("regular") + basic();
    }

    // https protocol basic url builder
    public String secure() {
        return options.get("secure") + basic();
    }

    // http basic url builder
    public String basic() {
        return options.get("base");
    }

    public String addOption(String url, String option) {
        return url+separator+option;
    }
}


class VehiclesLocationUrlStringBuilder extends UrlRequestStringBuilder {
    protected String version = "v2";
    protected String command = "routes";
    protected String name = "vehicles";
    protected ArrayList<String> routes    = new ArrayList<String>();
    protected ArrayList<String> blocks = new ArrayList<String>();
    protected ArrayList<String> vIds = new ArrayList<String>();
    protected ArrayList<String> bbox = new ArrayList<String>();

    public VehiclesLocationUrlStringBuilder() {
        super();
        options.put("command", "vehicles");
        options.put("showNonRevenue", "false");
        options.put("onRouteOnly",    "false");
        options.put("shoStale",    "false");
    }
    public String base() {
        return simple()+version+separator+name+separator+options.get("appID");
    }

    public String request(String [] locations) {
        String basic = base()+separator+command;
        String sep = separator;
        for (String s: locations) {
            basic += sep+s;
            sep = ",";
        }
        return basic;
    }
} class ArrivalsUrlStringBuilder extends UrlRequestStringBuilder {
    protected String version = "v2";
    protected String command = "locIDs";
    protected String name = "arrivals";
    protected ArrayList<String> locids    = new ArrayList<String>();
    protected ArrayList<String> blocks = new ArrayList<String>();

    public ArrivalsUrlStringBuilder() {
        super();
        options.put("json", "false");
        options.put("arrivals", "2");
        options.put("minutes", "20");
        options.put("showPosition", "false");
    }
    public String request(String [] locations) {
        String basic = base()+separator+command;
        for (String s: locations) {
            basic += separator+s;
        }
        return basic;
    }

    public String base() {
        return simple()+version+separator+name+separator+options.get("appID");
    }
}

class RoutesUrlStringBuilder extends UrlRequestStringBuilder {
    protected String version = "v1";
    protected String command = "routes";
    protected String name = "routeConfig";
    protected ArrayList<String> routes    = new ArrayList<String>();

    public RoutesUrlStringBuilder() {
        super();
        options.put("json", "false");
        options.put("stops", "stops/true");
        options.put("dir", "dir/true");
    }
    public String request(String [] locations) {
        String basic = base()+separator+command;
        for (String s: locations) {
            basic += separator+s;
        }
        return basic;
    }

    public String request(String [] locations, Boolean includeStops) {

        String basic = request(locations);
        basic = addOption(basic, options.get("dir"));
        if (includeStops) {
            basic = addOption(basic, options.get("stops"));
        }

        return basic;
    }

    public String request() {
        String basic = base();
        return basic;
    }
    public String request(Boolean stops) {
        String basic = base();
        basic = addOption(basic, options.get("dir"));
        if (stops) {
            basic = addOption(basic, options.get("stops"));
        }
        return basic;
    }


    public String base() {
        return simple()+version+separator+name+separator+options.get("appID");
    }
}


class StopsUrlStringBuilder extends UrlRequestStringBuilder {
    private String version = "V1";
    private String command = "bbox";
    private String name = "stops";
    private ArrayList<String> locids    = new ArrayList<String>();
    private ArrayList<String> blocks = new ArrayList<String>();

    public StopsUrlStringBuilder() {
        super();
        options.put("json", "false");
        options.put("showRoutes", "true");
    }
    public String request(String bounds) {
        String basic = base()+separator+command+separator+bounds;
        return basic;
    }

    public String base() {
        return simple()+version+separator+name+separator+options.get("appID")+separator+"showRoutes"+separator+"true"+separator+"showRouteDirs"+separator+"true";
    }
}
