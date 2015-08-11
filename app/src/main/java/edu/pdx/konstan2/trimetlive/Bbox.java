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
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by konstantin on 7/26/15.
 *
 * Class description: BBox is a utility class. Bound box container that defines a rectangle on a map, with four points
 */
public class Bbox {
    Double lonDown;
    Double lonUp;
    Double latDown;
    Double latUp;

    public Bbox(Double latDown, Double latUp, Double lonDown, Double lonUp) {
        this.lonDown = lonDown;
        this.lonUp = lonUp;
        this.latDown = latDown;
        this. latUp = latUp;
    }

    public Bbox(String locations) {

    }

    public Bbox (LatLngBounds inBounds) {
        LatLngBounds b = inBounds;
        LatLng northeast = b.northeast;
        LatLng southwest = b.southwest;
        latUp = northeast.latitude;
        lonUp = northeast.longitude;
        latDown = southwest.latitude;
        lonDown = southwest.longitude;
    }

    // Convert to a string expected by the Trimet API
    public String asString() {
        return lonDown.toString()+","+latDown.toString()+","+lonUp.toString()+","+latUp.toString();
    }

    public String [] asStringArray() {
        String bounds = asString();
        return bounds.split(",");
    }
}
