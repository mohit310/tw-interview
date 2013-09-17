package com.mk;

import com.mk.data.Route;
import com.mk.data.Town;
import com.mk.data.TownMap;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 9:29 AM
 * This class is an utility class to help in making the TownMap.
 * @see TownMap
 */
public class Input {

    public static TownMap constructTownAndEdges(String info) {
        TownMap map = new TownMap();
        if (null == info || info.isEmpty()) return map;
        String[] routes = info.split(",");
        for (String route : routes) {
            if (null != route && route.trim().length() >= 3) {
                route = route.trim();
                String startTown = String.valueOf(route.substring(0, 1));
                String endTown = String.valueOf(route.substring(1, 2));
                int weight = Integer.valueOf(route.substring(2, route.length()));
                Town sTown = map.getTown(startTown);
                sTown = (sTown == null) ? sTown = new Town(startTown) : sTown;
                Route townRoute = new Route(startTown, endTown, weight);
                sTown.addNeighbor(townRoute);
                map.addTown(sTown);
                Town eTown = map.getTown(endTown);
                eTown = (eTown == null) ? eTown = new Town(endTown) : eTown;
                map.addTown(eTown);
            }
        }
        return map;
    }

}
