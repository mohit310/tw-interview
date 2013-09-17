package com.mk;

import com.mk.data.Edge;
import com.mk.data.Town;
import com.mk.data.TownMap;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 10:45 AM
 */
public class RouteFindingEngine {

    /**
     * Infinity value for distances.
     */
    private static final int INFINITE_DISTANCE = Integer.MAX_VALUE;
    private final TownMap townMap;

    private RouteFindingEngine(TownMap townMap) {
        this.townMap = townMap;
    }

    public int getDistance(String hyphenSeparatedPath) {
        int totalDistance = 0;
        if (hyphenSeparatedPath == null || hyphenSeparatedPath.trim().length() == 0) return totalDistance;
        String[] towns = hyphenSeparatedPath.split("-");
        Town town = null;
        for (int i = 0; i < towns.length; i++) {
            if (i == 0) {
                town = townMap.getTown(towns[i]);
                if (null == town) return totalDistance;
                continue;
            }
            int weight = town.getNeighborDistance(towns[i]);
            if (weight == 0) return 0;
            else totalDistance += weight;
            town = townMap.getTown(towns[i]);
        }
        return totalDistance;
    }

    public int getTripsWithMaxStops(String startTownName, String endTownName, int count) {
        Town startTown = townMap.getTown(startTownName);
        Town endTown = townMap.getTown(endTownName);
        if (startTown == null || endTown == null) return 0;
        Set<String> previousTowns = new HashSet<String>();
        Stack<String> unsettledTowns = new Stack<String>();
        List<String> allPaths = new ArrayList<String>();
        find(startTown, endTown, previousTowns, unsettledTowns, allPaths, count, false);
        int trips = 0;
        for (String path : allPaths) {
            if ((path.length() - 1) <= count) {
                trips++;
                System.out.println(path);
            }
        }
        return trips;
    }

    public int getTripsWithOnlyStops(String startTownName, String endTownName, int count) {
        Town startTown = townMap.getTown(startTownName);
        Town endTown = townMap.getTown(endTownName);
        if (startTown == null || endTown == null) return 0;
        Set<String> previousTowns = new HashSet<String>();
        Stack<String> unsettledTowns = new Stack<String>();
        List<String> allPaths = new ArrayList<String>();
        find(startTown, endTown, previousTowns, unsettledTowns, allPaths, count, true);
        int trips = 0;
        for (String path : allPaths) {
            if ((path.length() - 1) <= count) {
                trips++;
                System.out.println(path);
            }
        }
        return trips;
    }

    private void find(Town startTown, Town endTown, Set<String> previousTowns, Stack<String> unsettledTowns, List<String> allPaths, int count, boolean isEqualStops) {

        unsettledTowns.push(startTown.getName());
        previousTowns.add(startTown.getName());

        for (Edge edge : startTown.getNeighbors()) {
            String edgeTownName = edge.getEndTown();
            if (townMap.getTown(edgeTownName).equals(endTown)) {
                if (isEqualStops && (unsettledTowns.size()+1 <= count)) {
                    find(townMap.getTown(edgeTownName), endTown, previousTowns, unsettledTowns, allPaths, count, isEqualStops);
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (String node : unsettledTowns) {
                        builder.append(node);
                    }
                    allPaths.add(builder.toString() + edgeTownName);
                }
            } else {
                find(townMap.getTown(edgeTownName), endTown, previousTowns, unsettledTowns, allPaths, count, isEqualStops);
            }
        }
        unsettledTowns.pop();
        previousTowns.remove(startTown);
    }

    public int getShortestPath(String startTown, String endTown) {
        return getShortestPath(townMap.getTown(startTown), townMap.getTown(endTown));
    }

    public int getShortestPath(Town startTown, Town endTown) {

        final Map<Town, Integer> shortestDistMap = new HashMap<Town, Integer>();
        final Set<Town> settledTowns = new HashSet<Town>();
        final PriorityQueue<Town> unsettledTowns = new PriorityQueue<Town>(16, new Comparator<Town>() {
            @Override
            public int compare(Town o1, Town o2) {
                int result = shortestDistMap.get(o1) - shortestDistMap.get(o2);
                return (result == 0) ? o1.compareTo(o2) : result;
            }
        });

        unsettledTowns.add(startTown);
        shortestDistMap.put(startTown, 0);

        while (unsettledTowns.size() != 0) {
            Town townToEvaluate = unsettledTowns.poll();
            evalNeighbors(townToEvaluate, endTown, settledTowns, unsettledTowns, shortestDistMap);
            settledTowns.add(townToEvaluate);
        }

        return shortestDistMap.get(endTown);

    }

    private void evalNeighbors(Town townToEvaluate, Town endTown, Set<Town> settledTowns, Queue<Town> unsettledTowns, Map<Town, Integer> shortestDistMap) {
        List<Edge> edgeTowns = townToEvaluate.getNeighbors();
        for (Edge edgeTown : edgeTowns) {
            if (!settledTowns.contains(edgeTown)) {
                int edgeDistance = edgeTown.getWeight();
                String edgeEndTownName = edgeTown.getEndTown();
                Town edgeEndTown = townMap.getTown(edgeEndTownName);
                int distance = shortestDistMap.get(townToEvaluate) + edgeDistance;
                Integer edgeEndTownDistance = shortestDistMap.get(edgeEndTown);
                edgeEndTownDistance = (edgeEndTownDistance == null) ? INFINITE_DISTANCE : edgeEndTownDistance;
                if (edgeEndTownDistance > distance) {
                    shortestDistMap.put(edgeEndTown, distance);
                    unsettledTowns.add(edgeEndTown);
                }
                if (edgeEndTown.equals(endTown)) {
                    shortestDistMap.put(edgeEndTown, distance);
                    settledTowns.add(edgeEndTown);
                    unsettledTowns.clear();
                }
            }
        }
    }

    public static RouteFindingEngine getEngine(TownMap townMap) {
        return new RouteFindingEngine(townMap);
    }

}
