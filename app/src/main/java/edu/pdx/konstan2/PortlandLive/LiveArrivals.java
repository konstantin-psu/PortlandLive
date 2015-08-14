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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
*
* Intention of this class to create activity that displays:
* 1. Arrivals for a given stop.
* 2. Vehicles location for the stop
*
**/

public class LiveArrivals extends MapsCommon implements OnMapReadyCallback, MasterTask {

    // *** Class global variables ***
    String locId;                                           // This class only serves one stop, this is this stop locId (Which is defined by Trimet)
    ArrivalsFactory arrivalsFactory;                        // Responsible for fetching arrivals information
    VehiclesLocationFactory vehiclesLocationFactory;        // Responsible for fetching vehicles location information

    final ArrayList itemsSelected;                          // Stores Routes That currently selected - empty by default.

    private HashMap<String, String> routesResolver;         // LocId - to Description Map.
    private HtmlRequester req;                              // Responsible for handling Trimet API request

    // *** End global variables section


    // *** Default Constructor
    public LiveArrivals() {
        itemsSelected = new ArrayList();
    }

    // *** Adds arrivals to the activity
    public void addArrivals(ArrivalsFactory arrivals) {
        ArrivalsViewBuilder arrivalsViewBuilder = new ArrivalsViewBuilder();
        arrivalsViewBuilder.buildView(arrivalsFactory, this);

    }

    /**
     * This is callback method that is used my HtmlRequester
     * See purpose description in HtmlRequester.java
    **/
    public void run(String methodName) {
        if (methodName.equals(ArrivalsFactory.command)) {
            addArrivals(arrivalsFactory);
        } else if (methodName.equals(VehiclesLocationFactory.command)) {
            addVehiclesLocations();
        }
    }


    // *** Adds vehicles location to the map fragment.
    private void addVehiclesLocations() {
        Iterator it = vehiclesLocationFactory.vehiclesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Vehicle v = (Vehicle) pair.getValue();
            if (mMap != null) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(v.latitude, v.longitude)));
            } else {
                Toast.makeText(getApplicationContext(), "Error: map is not available", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


    // *** This method is called when activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_arrivals);
        MapFragment mapFragment = (MapFragment) getFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        // *** Initialize globals

        routesResolver = new HashMap<>();

        // *** Expect stop information in JSON format.
        Stop myStop = new Stop(intent.getStringExtra("stopJsonEncoded"));


        // Map:  description -> locId
        for (Route r: myStop.routes) {
            routesResolver.put(r.description, r.route.toString());
        }
        locId = myStop.locID.toString();
        req = new HtmlRequester();
        arrivalsFactory = new ArrivalsFactory(this);
        vehiclesLocationFactory = new VehiclesLocationFactory(this);

        //  get arrivals only for the given location
        arrivalsFactory.getArrivalsAt(locId.split(" "));
        vehiclesLocationFactory.getVehiclesOnRoutes(myStop.routesIDs());

        req.execute(arrivalsFactory, vehiclesLocationFactory);

        createMulticheckboxDialog(myStop);
    }

    // *** Created routes selector dialog that is called by select routes button (Actual button name may differ).
    private void createMulticheckboxDialog (Stop thisStop) {
        final Dialog dialog;

        String [] items = thisStop.listRoutes();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List arrivals for routes: ");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog = builder.create();
        Button btn = (Button) findViewById(R.id.button_dialog);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Map is created do something with it
    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        mMap = map;
        goToDefaultLocation();
        centerMapOnMyLocation();


        /**
         * Create default marker info window
         * Map's job is to display real time vehicle locations, so each markers
         * info window will contain information defined in  vehicle.xml (vehicle layout)
        **/
        mMap.setInfoWindowAdapter(
                new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0) {
                        LatLng stopPosition = arg0.getPosition();
                        Vehicle vehicle = vehiclesLocationFactory.vehiclesMap.get(stopPosition);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                        VehicleInfoWindowBuilder vehicleInfoWindowBuilder =new VehicleInfoWindowBuilder();
                        return  vehicleInfoWindowBuilder.buildView(vehicle, inflater);
                    }
                }
        );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Show info window only if not already shown
                if (marker.isVisible() && !marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
    }
}
