package edu.pdx.konstan2.trimetlive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LiveMap extends FragmentActivity implements MasterTask {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<LatLng, Stop> stopMap;
    private HashMap<LatLng, View> viewCache;
    private HashMap<String, Route> Routes;
    private final ArrayList itemsSelected;
    final StopsFactory stops;
    final RoutesFactory routesFactory;
    private ArrayList<Long> selectedRoutes;
    LiveMap thisPointer = this;
    private htmlRequestor req;


    public LiveMap() {
        stops = new StopsFactory(this);
        viewCache = new HashMap<>();
        itemsSelected = new ArrayList();
        routesFactory = new RoutesFactory(this);
        selectedRoutes = new ArrayList<>();
    }

    public void run(String command) {
        if (command.equals("addStops")) {
            mMap.clear();
            stopMap = stops.stopsMap;
            Iterator it = stops.stopsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Stop v = (Stop) pair.getValue();
                if (itemsSelected.size() == 0) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(v.latitude, v.longitude)));
                } else {
                    Iterator rit = v.routes.iterator();
                    while(rit.hasNext()) {
                        Route cr = (Route) rit.next();
                        if (routesFactory.routesMap.containsKey(cr.route) && selectedRoutes.contains(cr.route)) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(v.latitude, v.longitude)));
                            break;
                        }
                    }

                }

            }
        } else if (command.equals("addRoutes")){
            createMulticheckboxDialog(routesFactory.stringArray());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopMap = new HashMap<>();

        setContentView(R.layout.activity_live_map);
        setUpMapIfNeeded();

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                req = new htmlRequestor();
                stops.getStopsAtBounds(new Bbox(bounds));
                req.execute(stops);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isVisible() && !marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                }
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        LatLng position = marker.getPosition();
                        Stop currentStop = stopMap.get(position);

                        Intent intent = new Intent(thisPointer, LiveArrivals.class);
                        intent.putExtra("stopJsonEncoded", currentStop.toEncodedString());
                        startActivity(intent);
                    }
                }
        );
        mMap.setInfoWindowAdapter(
                new InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0) {
                        LatLng stopPosition = arg0.getPosition();
                        if (viewCache.containsKey(stopPosition)) {
                            return viewCache.get(stopPosition);
                        }
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout displayPlace = (LinearLayout) findViewById(R.id.arrivalsDispay);
                        View custom = inflater.inflate(R.layout.insertable, null);
                        LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
                        Stop stop = stopMap.get(stopPosition);
                        Iterator<Route> it = stop.routesIterator();
                        while (it.hasNext()) {
                            Route r = it.next();
                            TextView tv = new TextView(getApplicationContext());
                            tv.setPadding(10, 5, 10, 5);
                            tv.setText(r.asString());
                            insertPoint.addView(tv);
                        }
                        viewCache.put(stopPosition, custom);

                        return custom;
                    }
                }
        );
        htmlRequestor tempReq = new htmlRequestor();
        routesFactory.getAllRoutes();
        tempReq.execute(routesFactory);
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
        mMap.setMyLocationEnabled(true);
        centerMapOnMyLocation();
    }


    private void centerMapOnMyLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(18)                   // Sets the zoom
                    .bearing(0)                 // Sets the orientation of the camera to east
                    .tilt(0)                    // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }

    }

    private void createMulticheckboxDialog (String [] items) {
        final Dialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List arrivals for routes: ");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                            selectedRoutes.add(routesFactory.routes.get(selectedItemId).route);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                            selectedRoutes.remove(routesFactory.routes.get(selectedItemId).route);
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                });
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
        dialog = builder.create();
//        dialog.show();
        Button btn = (Button) findViewById(R.id.routes_filter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
}
