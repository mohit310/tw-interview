package com.mk;

import com.mk.data.Edge;
import com.mk.data.Town;
import com.mk.data.TownMap;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 9:29 AM
 */
public class Input {

    public static void main(String[] args) {
        if (args.length > 0) {
            TownMap map = Input.constructTownAndEdges(args[0]);
            RouteFindingEngine engine = RouteFindingEngine.getEngine(map);
            System.out.println(engine.getShortestPath("A", "D"));

        }
    }

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
                Edge townEdge = new Edge(startTown, endTown, weight);
                sTown.addNeighbor(townEdge);
                map.addTown(sTown);
                Town eTown = map.getTown(endTown);
                eTown = (eTown == null) ? eTown = new Town(endTown) : eTown;
                map.addTown(eTown);
            }
        }
        return map;
    }

}
