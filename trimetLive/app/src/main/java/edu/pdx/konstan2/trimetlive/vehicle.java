package edu.pdx.konstan2.trimetlive;

/**
 * Created by konstantin on 7/5/15.
 */
public class vehicle {
    routes            //comma delimited list of route numbers (optional)    If included, only vehicles traveling on these routes will be included.
    blocks            //comma delimited list of block numbers (optional)    If included, only vehicles serving these blocks included.
    ids               //comma delimited list of vehicle numbers (optional)  If included, only vehicles with these numbers will be included.
    appID             //string (required)   Your appID received during registration.
    since             //time in milliseconds since epoch (optional)     If included, only vehicle locations refreshed after the epoch time be returned. Defaults to midnight of service day.
    xml               //boolean (optional) "true" or "false" (default is false)     If true results will be returned in xml format rather than the default json.
    callback          //string (optional)   If present returns the json result in a jsonp callback function.
    bbox              //comma delimited list of longitude and latitude values (optional)    bbox arguments are lonmin, latmin, lonmax, latmax in decimal degrees. These define the lower left and upper right corners of the bounding box. Only    vehicles inside the bounding box will be selected.
    showNonRevenue    //boolean (optional) "true" or "false" (default is false)     If true results include vehicles on dead head routes.
    onRouteOnly       //boolean (optional) "true" or "false" (default is true)  If true (the default) results will include only vehicles that are servicing a route.
    showStale         //boolean (optional) "true" or "false" (default is false)     If true results will include entries that have expired. The query time is greater than vehicle[@expires].
}

