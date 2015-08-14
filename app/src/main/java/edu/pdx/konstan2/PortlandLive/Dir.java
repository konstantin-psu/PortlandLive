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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by konstantin on 8/1/15.
 *
 * Class description:
 *  Direction container.
 *
 *  It is a bit convoluted due to the fact that some API calls can return
 *  Directions that contain Stops, and Stops that contain Direction, or none of the above
 *
 *  To avoid circular parsing, parse stops only if "includeStops" flag is set to true
 *
 *  Trimet Direction Structure:
 *
 *  1. Direction description such as: To Hillsboro
 *  2. Direction id (unique within a stop): 1, 2, 3 etc
 *
 */
public class Dir {

    private ArrayList<Stop> stops = new ArrayList<>();
    public String description;
    public Long direction;

    /**
     *
     * @param dir XML representation of the direction
     * @param includeStops This parameter is used to avoid circular parsing
     */
    public Dir(Element dir, Boolean includeStops) {
        description = dir.getAttribute("desc");
        direction =  Long.parseLong(dir.getAttribute("dir"));

        if (includeStops) {
            NodeList stopsList = dir.getElementsByTagName("stop");
            for (int count = 0; count < stopsList.getLength(); count++) {
                Node node1 = stopsList.item(count);
                if (node1.getNodeType() == node1.ELEMENT_NODE) {
                    Element stopElement = (Element) node1;
                    stops.add(new Stop(stopElement, true));
                }
            }
        }
    }

    public Iterator iterator() {
        return stops.iterator();
    }
}
