package edu.pdx.konstan2.trimetlive;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by kmacarenco on 7/27/15.
 */
public class Route {
    String description;
    Long route;
    String type;
    Long direction;
    String dirDescription;
    public Route (Element v) {
        route =   Long.parseLong(v.getAttribute("route"));
        type =   v.getAttribute("type");
        description = v.getAttribute("desc");
        NodeList routesList = v.getElementsByTagName("dir");
        for (int count = 0; count < routesList.getLength(); count++) {
            Node node1 = routesList.item(count);
            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                Element dir = (Element) node1;
                dirDescription = dir.getAttribute("desc");
                direction = Long.parseLong(dir.getAttribute("dir"));
            }
        }
    }

    public String asString() {
        if (dirDescription != null) {
            return description +"\n  "+dirDescription;
        }else {
            return description;
        }
    }
}
