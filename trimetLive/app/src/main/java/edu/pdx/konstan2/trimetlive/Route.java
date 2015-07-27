package edu.pdx.konstan2.trimetlive;

import org.w3c.dom.Element;

/**
 * Created by kmacarenco on 7/27/15.
 */
public class Route {
    String description;
    Long route;
    String type;
    public Route (Element v) {
        route =   Long.parseLong(v.getAttribute("route"));
        type =   v.getAttribute("type");
        description = v.getAttribute("desc");
    }

    public String asString() {
        return description;
    }
}
