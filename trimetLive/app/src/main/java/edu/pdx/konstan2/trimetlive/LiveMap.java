package edu.pdx.konstan2.trimetlive;

import android.content.Context;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LiveMap extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<Long, vehicle> vehicleMap;
    private HashMap<LatLng, Stop> stopMap;
    private responseParserFactory responseParser;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        responseParser = new responseParserFactory();
        vehicleMap = new HashMap<Long, vehicle>();
        stopMap = new HashMap<LatLng, Stop>();
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Create the text view
        setContentView(R.layout.activity_live_map);
        setUpMapIfNeeded();
        Log.d("On create", "Creating trimet connector.");
        final connector test = new connector();

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                new showStops().execute(displayStops(bounds));
//                displayStops(bounds);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isVisible() && !marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                    Log.d("onInfoWindowClick", "hahahahahahahah info is shown");
                }
                Log.d("onInfoWindowClick", "hahahahahahahah");
                return true;
            }
        });

        mMap.setInfoWindowAdapter(
            new InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker arg0) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout displayPlace = (LinearLayout) findViewById(R.id.arrivalsDispay);
                    View custom = inflater.inflate(R.layout.insertable, null);
                    LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
//                    TextView tv = (TextView) custom.findViewById(R.id.text);

                    TextView tv = new TextView(this);
                    tv.setText(((Arrival) pair.getValue()).asString());
                    insertPoint.addView(tv);

                    return custom;
                }
            }
        );

        mMap.clear();
        try {
            test.test2(message);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }



//        timed stops = new timed();
//        stops.execute("test");
//        new Timer().schedule(new TimerTask()
//        {
//
//            @Override
//            public void run()
//            {
//                try {
//                    mMap.clear();
//                    test.test2();
//                } catch (Exception e) {
//                    Log.d("Exception", e.toString());
//                }
//                // Start the home screen
//            }
//        }, 5000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map. if (mMap != null) {
                setUpMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
//        CircleOptions circleOptions = new CircleOptions().center(new LatLng(37.4, -122.1)).radius(1000); // In meters
        // mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        // latitude and longitude
//        double latitude = 17.385044;
//        double longitude = 78.486671;
//
//        // create marker
//        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps");
//
//        // Changing marker icon
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.t2));
//
//        // adding marker
//        mMap.addMarker(marker);
        centerMapOnMyLocation();

    }


    private void centerMapOnMyLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(location.getLatitude(), location.getLongitude()), 13)); CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(18)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }

    }

    private LatLng getMyLocation() {
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            return new LatLng(location.getLatitude(), location.getLongitude());

        }
        return null;
    }

    private LatLngBounds getCurrentBounds() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        return bounds;
    }
//    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//        @Override
//        public void onMyLocationChange(Location location) {
//            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//            Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
//            if(mMap != null){
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//            }
//        }
//    };
    class connector {
        public String test2(String url) throws Exception {
            String res = "";
            req requester = new req();
            requester.execute(url);
            return res;
        }
    }

    public String request(String urlString) {
        try {
            URL url= new URL(urlString);
            URL yahoo = url;


            HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            Log.d("exception in asyntask", e.toString());
            return null;
        }

    }
    class req extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            return request(urls[0]);
        }

        protected void onPostExecute(String feed) {
            responseParser.parse(feed, vehicleMap);
            Iterator it = vehicleMap.entrySet().iterator();
            mMap.clear();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                MarkerOptions markerOptions = new MarkerOptions();
                vehicle v =  (vehicle) pair.getValue();
                markerOptions.position(new LatLng(v.latitude, v.longitude));
                mMap.addMarker(markerOptions);
            }
        }
    }



    public String displayStops(LatLngBounds inBounds) {
        LatLngBounds b = inBounds;
        LatLng northeast = b.northeast;
        LatLng southwest = b.southwest;
        Double latUp = northeast.latitude;
        Double lonUp = northeast.longitude;
        Double latDown = southwest.latitude;
        Double lonDown = southwest.longitude;
        String box = lonDown.toString()+","+latDown.toString()+","+lonUp.toString()+","+latUp.toString();
        Log.d("bounds", box);
        String request = "http://developer.trimet.org/ws/V1/stops?appID=EEC7240AC3168C424AC5A98E1&json=true&bbox="+box;
        return request;

    }

    class showStops extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            return request(urls[0]);
        }

        protected void onPostExecute(String feed) {
            mMap.clear();
            responseParser.parseStops(feed, stopMap);
            Iterator it = stopMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Stop v =  (Stop) pair.getValue();
                mMap.addMarker(new MarkerOptions().position(new LatLng(v.latitude, v.longitude)));
            }
        }
    }

}
