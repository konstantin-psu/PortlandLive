package edu.pdx.konstan2.trimetlive;

import android.content.Intent;
import android.graphics.Canvas;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLEngineResult;

public class LiveMap extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<Long, vehicle> vehicleMap = new HashMap<Long, vehicle>();
    private jsonParser responseParser = new jsonParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the text view
        setContentView(R.layout.activity_live_map);
        setUpMapIfNeeded();
        connector test = new connector();
        try {
            test.test2();
        } catch (Exception e) {
        }

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
        CircleOptions circleOptions = new CircleOptions().center(new LatLng(37.4, -122.1)).radius(1000); // In meters
//        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

// latitude and longitude
        double latitude = 17.385044;
        double longitude = 78.486671;

// create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps");

// Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.t2));

// adding marker
        mMap.addMarker(marker);

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
    //        public String test() throws Exception {
//            URL yahoo = new URL("http://developer.trimet.org/ws/v2/vehicles");
//            HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(
//                            yc.getInputStream()));
//            String inputLine;
//            String res = new String();
//            while ((inputLine = in.readLine()) != null)
//                res += " " + inputLine;
//            in.close();
//            return res;
//        }
    public String test2() throws Exception {
        String res = "";
        req requester = new req();
        requester.execute("http://developer.trimet.org/ws/v2/vehicles?appID=EEC7240AC3168C424AC5A98E1");
        return res;
    }
}

    class req extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                URL url= new URL(urls[0]);
                URL yahoo = url;
                String res = new String();

                String appId = "EEC7240AC3168C424AC5A98E1";

                HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
//                yc.setRequestProperty("appID", appId);

//                return Integer.toString(yc.getResponseCode());
//                return yc.toString();
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
                this.exception = e;
                return e.toString();
            }
        }

        protected void onPostExecute(String feed) {
            responseParser.parse(feed, vehicleMap);
            Iterator it = vehicleMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                vehicle v =  (vehicle) pair.getValue();
                mMap.addMarker(new MarkerOptions().position(new LatLng(v.latitude, v.longitude)).title(v.tripID)
                .snippet("Type "+ v.type+"\n"+
                         "Route number" + v.routeNumber+"\n"+
                         "Sign" +v.signMessage));
            }
        }
    }
}
