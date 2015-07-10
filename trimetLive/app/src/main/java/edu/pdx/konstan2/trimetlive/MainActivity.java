package edu.pdx.konstan2.trimetlive;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public Intent intent = null;

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, mapActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void callMap(View view) {
        intent = new Intent(this, LiveMap.class);
        parameters params = new parameters();
        EditText editText = (EditText) findViewById(R.id.edit_message);
        intent.putExtra(EXTRA_MESSAGE,params.showStops());
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


class parameters {
    //TODO Update when possile
    // v1 Task: Create request string.

    //********************* Vehicle Location **********************

    // Base HTTP URL: http://developer.trimet.org/ws/v2/vehicles
    // Base HTTPS URL: https://developer.trimet.org/ws/v2/vehicles

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


    //********************* Arrivals  ***********************

    // Base HTTP URL: http://developer.trimet.org/ws/v2/arrivals
    // Base HTTPS URL: https://developer.trimet.org/ws/v2/arrivals

/*    locIDs	       // comma delimited list of location IDs (required)	The location IDs to report arrivals. Arrivals are reported for each unique route and direction that services each stop identified by their location ID. Up to 10 location IDs can be reported at once.
    appID	        //string (required)	Your appID received during registration.
    json	       // boolean (optional) "true" (default) or "false"	If false results will be returned in XML format rather than the default json format.
    callback	   // string (optional)	If present returns the json result in a jsonp callback function. Only used if json is set to true.
    showPosition   // boolean (optional) "true" or "false" (default)	If true arrival elements will include blockPosition elements when available.
    minutes	       *//** integer (optional) default is 20	Arrivals for each route and direction served at the stops in locIDs will be returned up to the first that is further than minutes away. Maximum is 60.
                    *For example: If minutes is set to 20 (the default) results may include arrivals 10 minutes and 23 minutes away. If minutes where instead set to 40 results would include the first two arrivals at 10 and 23 minutes and an additional arrival 45 minutes away.
                   **//*
    arrivals	   *//** integer (optional) default is 2	At least this many arrivals for each route and direction served at the stops in locIDs will be returned. An arrival after an hour terminates this criteria.
                   * For example: If arrivals is set to 2 (the default) results may include arrivals 10 minutes and 23 minutes away. If arrivals where instead set to 4 results would include the first two arrivals at 10 and 23 minutes and an additional arrival 45 and a scheduled arrival 65 minutes away.
                   **//*
    begin	       // date time (optional)	Seconds since epoch, or string in 'yyyy-MM-ddTHH:mm:ss' format. If present arrivals after this time will be included. If set to a value prior to the current time, the current time will be used instead. All arrivals will be returned between begin and end parameters. Any arrival that can be estimated (up to an hour away from the current time) will be included. Otherwise all arrivals will be scheduled. Currently does not apply to streetcar arrivals.
    end	           // date time (optional)	Seconds since epoch, or string in 'yyyy-MM-ddTHH:mm:ss' format. If present arrivals before this time will be included. Requires begin parameter. If end is omitted while begin is present end will default to one hour after begin. Maximum time between begin and end is one day.*/


    //********************* Detours ****************************

    // Base HTTP URL: http://developer.trimet.org/ws/V1/detours
    // Base HTTPS URL: https://developer.trimet.org/ws/V1/detours

/*    routes	        //comma delimited list of route numbers (optional)	If present results will contain only detours applicable for the route numbers provided. If ommitted every detour in effect will be returned.
    route	        //alias for routes (optional)	Same as routes.
    appID	        //string (required)	Your appID received during registration.
    json	        //boolean (optional) "true" or "false" (default)	If true results will be returned in json format rather than the default xml format.
    includeFuture	//boolean (optional) "true" or "false" (default)	If present returns detours with begin dates set in the future.
    callback	    //string (optional)	If present returns the json result in a jsonp callback function. Only used if json is set to true.*/


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


    public String showStops() {

    }
    // ******************* Stop Location **********************

    // Base HTTP URL: http://developer.trimet.org/ws/V1/stops
    // Base HTTPS URL: https://developer.trimet.org/ws/V1/stops

/*    bbox	        //comma delimited list of longitude and latitude values (optional)	bbox arguments are lonmin, latmin, lonmax, latmax in decimal degrees. These define the lower left and upper right corners of the bounding box.
    ll	            //comma delimited long-lat pair (optional)	Defines center of search radius in decimal degrees.
    feet	        //number (optional)	Use with ll to define search radius in feet.
    meters	        //number (optional)	Use with ll to define search radius in meters.
    showRoutes	    //boolean (optional)	location elements will include a list of routes that service the stop(s) if this is set to 'true'.
    showRouteDirs	//boolean (optional)	route elements will include a list of 'dir' elements for each route direction that service the stop(s) if this is set to 'true'. Setting showRoutes is to 'true' is unnessisary if this is set to 'true'.
    appID	        //string (required)	Your appID received during registration.
    json	        //boolean (optional) "true" or "false" (default)	If true results will be returned in json format rather than the default xml format.
    callback	    //string (optional)	If present returns the json result in a jsonp callback function. Only used if json is set to true.*/

    //******************* Trip Planner ************************

    // Base HTTP URL: http://developer.trimet.org/ws/V1/trips/tripplanner
    // Base HTTPS URL: https://developer.trimet.org/ws/V1/trips/tripplanner

/*    fromPlace	     //string (optional)	fromPlace=pdx a successful request requires a fromPlace and/or fromCoord. if the request lacks a fromCoord, fromPlace will geocode as the origin of the trip
    fromCoord	     //string (optional)	fromCoord=-122.72,45.51 if fromPlace exists along with fromCoord, then fromPlace will not engage the geocoder, thus making fromPlace simply a place label
    toPlace	         //string (optional)	toPlace=zoo a successful request requires a toPlace and/or toCoord. if the request lacks a toCoord, toPlace will first be geocoded as the destination of the trip
    toCoord	         //string (optional)	toCoord=-122.72,45.51 if toPlace exists along with toCoord, then toPlace will not engage the geocoder, thus making toPlace simply a place label
    Date	         //string (optional)	Date=9-9-2009 Date of planned trip. If no date is given, then current date (from a server in Portland, OR) is used.
    Time	         //string (optional)	Time=10:22 am Time of planned trip. If no time is given, then current time (from a server in Portland, OR) is used. NOTE: as of now (June 2009), you will need to provide a valid time parameter/value. In a future release of this service, time will be an optional field.
    Min	             //string (optional)	Arr=T -or- Arr=X -or- Arr=W Choose itineraries that offer the Quickest Trip (T); Fewest Transfers (X); or Shortest Walk (W) -- T is the default (either T or X are good options for a default in an app). Be careful with W, as it can give some round-about trips to shave off a few steps of walking (eg: I've seen it suggest taking a bus down and back, with 60 minutes extra transit time, just to avoid crossing a street).
    Arr	             //string (optional)	Arr=D -or- Arr=A Depart by time / Arrive by time
    Walk	         //float (optional)	Walk=0.01 -to- Walk=0.999 (in miles)
    Mode	         //string (optional)	Mode=A -or- Mode=B -or- Mode=T (All Modes / Bus Only / Train Only)
    Format	         //string (optional)	Format=XML -- The only format currently supported is XML.
    MaxIntineraries	 //string (optional)	MaxIntineraries=1 -- The number of different itineraries to return. Default is 3 (maximum is 6).
    appID	         //string (required)	Your appID received during registration.*/

}

