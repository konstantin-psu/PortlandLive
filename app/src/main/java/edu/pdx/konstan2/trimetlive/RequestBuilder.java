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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by konstantin on 7/5/15.
 */
public class RequestBuilder {
    Map<String, String> options = new HashMap<String, String>();
    String separator = "/";
    public RequestBuilder() {
        options.put("base", "developer.trimet.org/ws/");
        options.put("secure", "https://");
        options.put("regular", "http://");
        options.put("v1", "V1/");
        options.put("v2", "v2/");
        options.put("opts", "/");
        options.put("appID", "appID/EEC7240AC3168C424AC5A98E1");
        // arrivals
        // detours
        // routeConfig
        // stops
        // trips/tripplanner

        // arrivals
        // detours
        // routeConfig
        // stops
        // trips/tripplanner
    }
    public String simple() {
        return options.get("regular")
                + basic();
    }
    public String secure() {
        return options.get("secure")
                + basic();
    }
    public String basic() {
        return options.get("base");
    }

    public String addOption(String url, String option) {
        return url+separator+option;
    }
}

class VehiclesLocationBuilder extends RequestBuilder {
    String version = "v2";
    String command = "routes";
    String name = "vehicles";
    ArrayList<String> routes    = new ArrayList<String>();
    ArrayList<String> blocks = new ArrayList<String>();
    ArrayList<String> vIds = new ArrayList<String>();
    ArrayList<String> bbox = new ArrayList<String>();

    public VehiclesLocationBuilder() {
        super();
        options.put("command", "vehicles");
        options.put("showNonRevenue", "false");
        options.put("onRouteOnly",    "false");
        options.put("shoStale",    "false");

/*    routes         //  comma delimited list of route numbers (optional)    If included, only vehicles traveling on these routes will be included.
    blocks         //  comma delimited list of block numbers (optional)    If included, only vehicles serving these blocks included.
    ids            //  comma delimited list of vehicle numbers (optional)  If included, only vehicles with these numbers will be included.
    appID          //  string (required)   Your appID received during registration.
    since          //  time in milliseconds since epoch (optional)     If included, only vehicle locations refreshed after the epoch time be returned. Defaults to midnight of service day.
    xml            //  boolean (optional) "true" or "false" (default is false)     If true results will be returned in xml format rather than the default json.
    callback       //  string (optional)   If present returns the json result in a jsonp callback function.
    bbox           //  comma delimited list of longitude and latitude values (optional)    bbox arguments are lonmin, latmin, lonmax, latmax in decimal degrees. These define the lower left and upper right corners of the bounding box. Only     vehicles inside the bounding box will be selected.
    showNonRevenue //  boolean (optional) "true" or "false" (default is false)     If true results include vehicles on dead head routes.
    onRouteOnly    //  boolean (optional) "true" or "false" (default is true)  If true (the default) results will include only vehicles that are servicing a route.
    showStale      //  boolean (optional) "true" or "false" (default is false)     If true results will include entries that have expired. The query time is greater than vehicle[@expires].*/
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

}
class ArrivalsBuilder extends RequestBuilder {
    String version = "v2";
    String command = "locIDs";
    String name = "arrivals";
    ArrayList<String> locids    = new ArrayList<String>();
    ArrayList<String> blocks = new ArrayList<String>();

    public ArrivalsBuilder() {
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
/*    locIDs	       // comma delimited list of location IDs (required)	The location IDs to report arrivals. Arrival are reported for each unique route and direction that services each stop identified by their location ID. Up to 10 location IDs can be reported at once.
    appID	        //string (required)	Your appID received during registration.
    json	       // boolean (optional) "true" (default) or "false"	If false results will be returned in XML format rather than the default json format.
    callback	   // string (optional)	If present returns the json result in a jsonp callback function. Only used if json is set to true.
    showPosition   // boolean (optional) "true" or "false" (default)	If true arrival elements will include blockPosition elements when available.
    minutes	       *//** integer (optional) default is 20	Arrival for each route and direction served at the stops in locIDs will be returned up to the first that is further than minutes away. Maximum is 60.
     *For example: If minutes is set to 20 (the default) results may include arrivals 10 minutes and 23 minutes away. If minutes where instead set to 40 results would include the first two arrivals at 10 and 23 minutes and an additional arrival 45 minutes away.
     **//*
    arrivals	   *//** integer (optional) default is 2	At least this many arrivals for each route and direction served at the stops in locIDs will be returned. An arrival after an hour terminates this criteria.
     * For example: If arrivals is set to 2 (the default) results may include arrivals 10 minutes and 23 minutes away. If arrivals where instead set to 4 results would include the first two arrivals at 10 and 23 minutes and an additional arrival 45 and a scheduled arrival 65 minutes away.
     **//*
    begin	       // date time (optional)	Seconds since epoch, or string in 'yyyy-MM-ddTHH:mm:ss' format. If present arrivals after this time will be included. If set to a value prior to the current time, the current time will be used instead. All arrivals will be returned between begin and end parameters. Any arrival that can be estimated (up to an hour away from the current time) will be included. Otherwise all arrivals will be scheduled. Currently does not apply to streetcar arrivals.
    end	           // date time (optional)	Seconds since epoch, or string in 'yyyy-MM-ddTHH:mm:ss' format. If present arrivals before this time will be included. Requires begin parameter. If end is omitted while begin is present end will default to one hour after begin. Maximum time between begin and end is one day.*/

}

class RoutesBuilder extends RequestBuilder {
    String version = "v1";
    String command = "routes";
    String name = "routeConfig";
    ArrayList<String> routes    = new ArrayList<String>();

    public RoutesBuilder() {
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

    //******************** Route config ************************

    // Base HTTP URL: http://developer.trimet.org/ws/V1/routeConfig
    // Base HTTPS URL: https://developer.trimet.org/ws/V1/routeConfig

    /*    routes	    // comma delimited list of route numbers (optional)	If present results will contain only the routes with numbers that where provided in the list. If omitted every route will be returned.
        route	    // alias for routes (optional)	Same as routes.
        dir	        // indicates route directions (optional)	direction elements to include under route number, either 0 (outbound) or 1 (inbound), 'true' or 'yes' for both directions.
        stops	    // any non empty string (optional)	If this argument is present and has any non-empty value stop elements will be included under each route direction element.
        tp	        // any non-empty string (optional)	If this argument is present and has any non-empty value stop elements will be included under each route direction element that are also time points along the route. If this argument is used there is no need for the stops argument.
        startSeq	// integer value (optional)	Only stops with sequence numbers higher or equal to this argument will be included in stop lists.
        endSeq	    // integer value (optional)	Only stops with sequence numbers lower or equal to this argument will be included in stop lists.
        appID	    // string (required)	Your appID received during registration.
        json	    // boolean (optional) "true" or "false" (default)	If true results will be returned in json format rather than the default xml format.
        callback	// string (optional)	If present returns the json result in a jsonp callback function. Only used if json is set to true.*/

}
class StopsBuilder extends RequestBuilder {
    String version = "V1";
    String command = "bbox";
    String name = "stops";
    ArrayList<String> locids    = new ArrayList<String>();
    ArrayList<String> blocks = new ArrayList<String>();

    public StopsBuilder() {
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
    // ******************* Stop Location **********************

    // Base HTTP URL: http://developer.trimet.org/ws/V1/stops
    // Base HTTPS URL: https://developer.trimet.org/ws/V1/stops

/*  bbox	        //comma delimited list of longitude and latitude values (optional)	bbox arguments are lonmin, latmin, lonmax, latmax in decimal degrees. These define the lower left and upper right corners of the bounding box.
    ll	            //comma delimited long-lat pair (optional)	Defines center of search radius in decimal degrees.
    feet	        //number (optional)	Use with ll to define search radius in feet.
    meters	        //number (optional)	Use with ll to define search radius in meters.
    showRoutes	    //boolean (optional)	location elements will include a list of routes that service the stop(s) if this is set to 'true'.
    showRouteDirs	//boolean (optional)	route elements will include a list of 'dir' elements for each route direction that service the stop(s) if this is set to 'true'. Setting showRoutes is to 'true' is unnessisary if this is set to 'true'.
    appID	        //string (required)	Your appID received during registration.
    json	        //boolean (optional) "true" or "false" (default)	If true results will be returned in json format rather than the default xml format.
    callback	    //string (optional)	If present returns the json result in a jsonp callback function. Only used if json is set to true.*/

}
