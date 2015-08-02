package edu.pdx.konstan2.trimetlive;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by konstantin on 8/1/15.
 */
public class Dir {
    public ArrayList<Stop> stops = new ArrayList<>();
    public String description;
    Long direction;

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

}
