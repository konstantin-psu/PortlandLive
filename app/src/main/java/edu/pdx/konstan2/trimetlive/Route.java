package edu.pdx.konstan2.trimetlive;

import org.json.simple.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by kmacarenco on 7/27/15.
 */
public class Route {
    String type;
    Long route;
    String description;
    Long direction;
    String dirDescription;
    public String toJsonString() {
        StringBuilder str = new StringBuilder("{");
        str.append("\"type\":"+"\""+type+"\""+",");
        str.append("\"route\":"+route+",");
        str.append("\"desc\":"+"\""+description+"\""+",");
        str.append("\"dir\":"+direction+",");
        str.append("\"dirDesc\":"+"\""+dirDescription+"\"");
        str.append("}");
        return str.toString();
    }
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

    public Route (JSONObject v) {
        route =  (Long) v.get("route");
        type =   (String) v.get("type");
        description = (String) v.get("desc");
        dirDescription = (String) v.get("dirDesc");
        direction = (Long) v.get("dir");

//        NodeList routesList = v.getElementsByTagName("dir");
//        for (int count = 0; count < routesList.getLength(); count++) {
//            Node node1 = routesList.item(count);
//            if (node1.getNodeType() == node1.ELEMENT_NODE) {
//                Element dir = (Element) node1;
//                dirDescription = dir.getAttribute("desc");
//                direction = Long.parseLong(dir.getAttribute("dir"));
//            }
//        }
    }
    public String asString() {
        if (dirDescription != null) {
            return description +"\n  "+dirDescription;
        }else {
            return description;
        }
    }

}
