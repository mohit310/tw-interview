package com.mk;

import com.mk.data.Route;
import com.mk.data.Town;
import com.mk.data.TownMap;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 10:45 AM
 * This class helps in graph traversal. Use the static factory method getEngine to get a new instance.
 */
public class RouteFindingEngine {

    /**
     * Infinity value for distances.
     */
    private static final int INFINITE_DISTANCE = Integer.MAX_VALUE;
    /**
     * The townMap as passed
     */
    private final TownMap townMap;

    /**
     * Constructor is private as getEngine will create a new instance of this class.
     * @param townMap the graph to be passed.
     */
    private RouteFindingEngine(TownMap townMap) {
        this.townMap = townMap;
    }

    /**
     * Computes the distance between towns given a hyphen separated path
     * @param hyphenSeparatedPath The distance to compute between towns passed as hypen separated values
     * @return the distance between towns or 0 if path doesn't exist
     */
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
            if (weight == 0) {
                System.out.println("NO SUCH ROUTE");
                return 0;
            }
            else totalDistance += weight;
            town = townMap.getTown(towns[i]);
        }
        return totalDistance;
    }

    /**
     * Computes the total number of possible paths and returns only paths which are <= maximum hops.
     * @param startTownName the start town
     * @param endTownName the end town
     * @param maxHops the number of hops for which the number of paths need to be found
     * @return total number of possible paths possible based on maxHops passed
     */
    public int getTotalTripsWithMaxHops(String startTownName, String endTownName, int maxHops) {
        Town startTown = townMap.getTown(startTownName);
        Town endTown = townMap.getTown(endTownName);
        if (startTown == null || endTown == null) return 0;
        //We are using a stack as we want LIFO functionality as we are using DFS approach
        Stack<String> unsettledTowns = new Stack<String>();
        List<String> allPaths = new ArrayList<String>();
        find(startTown, endTown, unsettledTowns, allPaths, maxHops, false);
        int trips = 0;
        //The list can contain hops greater than maxhops as find method will find all possible paths
        //The list also contains an additional end TownName and hence we need to always subtract one.
        for (String path : allPaths) {
            if ((path.length() - 1) <= maxHops) {
                trips++;
                System.out.println(path);
            }
        }
        return trips;
    }

    /**
     * Computes the total number of possible paths and returns only paths which are == hops passed
     * @param startTownName the start town
     * @param endTownName the end town
     * @param exactHops the total number of possible paths based on exactHops passed
     * @return total number of possible paths possible based on exactHops passed
     */
    public int getTotalTripsWithExactHops(String startTownName, String endTownName, int exactHops) {
        Town startTown = townMap.getTown(startTownName);
        Town endTown = townMap.getTown(endTownName);
        if (startTown == null || endTown == null) return 0;
        //We are using a stack as we want LIFO functionality as we are using DFS approach
        Stack<String> unsettledTowns = new Stack<String>();
        List<String> allPaths = new ArrayList<String>();
        find(startTown, endTown, unsettledTowns, allPaths, exactHops, true);
        int trips = 0;
        //The list can contain hops greater than maxhops as find method will find all possible paths
        //The list also contains an additional end TownName appened and hence we need to always subtract one.
        for (String path : allPaths) {
            if ((path.length() - 1) <= exactHops) {
                trips++;
                System.out.println(path);
            }
        }
        return trips;
    }

    /**
     * Computes the total number of possible paths and returns only path which <= maxDistance
     * @param startTownName the start town
     * @param endTownName the end town
     * @param maxDistance the maximum distance to travel from different towns
     * @return total number of possible paths based on maxDistance passed
     */
    public int getTotalTripsWithMaxDistance(String startTownName, String endTownName, int maxDistance) {
        Town startTown = townMap.getTown(startTownName);
        Town endTown = townMap.getTown(endTownName);
        if (startTown == null || endTown == null) return 0;
        //We are using a stack as we want LIFO functionality as we are using DFS approach
        Stack<String> unsettledTowns = new Stack<String>();
        //This stack has is used to add distance to compute total distance using DFS approach
        Stack<Integer> unsettledTownsDistance = new Stack<Integer>();
        Map<String, Integer> allPaths = new HashMap<String, Integer>();
        findDistance(startTown, endTown, unsettledTowns, unsettledTownsDistance, allPaths, maxDistance);
        int trips = 0;
        Set<String> allPathsSet = allPaths.keySet();
        for (String path : allPathsSet) {
            trips++;
            System.out.println(path);
        }
        return trips;
    }

    /**
     * This is a recursive method of a modified DFS to find all possible paths creating a spanning tree for all possible routes
     * @param startTown startTown
     * @param endTown endTown
     * @param unsettledTowns Stack of unsettled Towns. Stack used LIFO which is helpful here for tree traversal
     * @param allPaths the computed path which are added as we reach endTown
     * @param count the number of hops we can make
     * @param isEqualStops to find if we are going to stop at count once end Town is found or keep going until we hit count
     */
    private void find(Town startTown, Town endTown, Stack<String> unsettledTowns, List<String> allPaths, int count, boolean isEqualStops) {

        unsettledTowns.push(startTown.getName());

        for (Route route : startTown.getNeighbors()) {
            String edgeTownName = route.getEndTown();
            if (townMap.getTown(edgeTownName).equals(endTown)) {
                //Since we are also taking into consideration the end town. Routes will be number of towns-1 routes
                if (isEqualStops && (unsettledTowns.size() + 1 <= count)) {
                    find(townMap.getTown(edgeTownName), endTown, unsettledTowns, allPaths, count, isEqualStops);
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (String node : unsettledTowns) {
                        builder.append(node);
                    }
                    allPaths.add(builder.toString() + edgeTownName);
                }
            } else {
                find(townMap.getTown(edgeTownName), endTown, unsettledTowns, allPaths, count, isEqualStops);
            }
        }
        unsettledTowns.pop();

    }

    /**
     * This is a recursive method of a modified DFS to find all possible paths creating a spanning tree for all possible routes
     * @param startTown startTown
     * @param endTown endTown
     * @param unsettledTowns Stack of unsettled Towns. Stack used LIFO which is helpful here for tree traversal
     * @param unsettledTownsDistance Stack of distances for all edges from one town to another town
     * @param allPaths the computed path which are added as we reach endTown
     * @param totalDistance the maximum distance we can go to find all possible paths
     */
    private void findDistance(Town startTown, Town endTown, Stack<String> unsettledTowns, Stack<Integer> unsettledTownsDistance, Map<String, Integer> allPaths, int totalDistance) {

        unsettledTowns.push(startTown.getName());
        unsettledTownsDistance.push(0);

        for (Route route : startTown.getNeighbors()) {
            String edgeTownName = route.getEndTown();
            if (townMap.getTown(edgeTownName).equals(endTown)) {
                unsettledTownsDistance.push(route.getWeight());
                StringBuilder builder = new StringBuilder();
                for (String node : unsettledTowns) {
                    builder.append(node);
                }
                int distance = addAllDistance(unsettledTownsDistance);
                if (distance < totalDistance) {
                    allPaths.put(builder.toString() + edgeTownName, distance);
                    findDistance(townMap.getTown(edgeTownName), endTown, unsettledTowns, unsettledTownsDistance, allPaths, totalDistance);
                }
                unsettledTownsDistance.pop();
            } else {
                unsettledTownsDistance.push(route.getWeight());
                findDistance(townMap.getTown(edgeTownName), endTown, unsettledTowns, unsettledTownsDistance, allPaths, totalDistance);
                unsettledTownsDistance.pop();
            }
        }
        unsettledTowns.pop();
        unsettledTownsDistance.pop();
    }

    /**
     * The stack which has all the distances and needs to sum all elements to find total distance
     * @param distances the distances in stack
     * @return the total distance
     */
    private int addAllDistance(Stack<Integer> distances) {
        int sum = 0;
        for (int distance : distances) {
            sum += distance;
        }
        return sum;
    }

    /**
     * implementation of modified Dijkstra algorithm to support same start and end node.
     * We use priority queue which sorts itself based on Comparator so that the run time is reduced
     * @param startTown start Town
     * @param endTown end Town
     * @return the distance of shortest path
     */
    public int getShortestPath(String startTown, String endTown) {
        return getShortestPath(townMap.getTown(startTown), townMap.getTown(endTown));
    }

    /**
     * implementation of modified Dijkstra algorithm to support same start and end node.
     * We use priority queue which sorts itself based on Comparator so that the run time is reduced
     * @param startTown start Town
     * @param endTown end Town
     * @return the distance of shortest path
     */
    public int getShortestPath(Town startTown, Town endTown) {
        //The map which computes the shortest distance for each Town
        final Map<Town, Integer> shortestDistMap = new HashMap<Town, Integer>();
        //The towns which are marked as visitied
        final Set<Town> settledTowns = new HashSet<Town>();
        //The priority queue (FIFO) but sort itself on priority as defined by comparator below. This contains list of all unvisited towns
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

    /**
     * BFS search used in Dijkstra algorithm and called by getShortestPath method to evaluate the neighbor nodes for distance
     * @param townToEvaluate townToEvaluate
     * @param endTown   endTown
     * @param settledTowns   all visited towns
     * @param unsettledTowns all unvisited towns
     * @param shortestDistMap the map of all distances stored as town, distance value
     */
    private void evalNeighbors(Town townToEvaluate, Town endTown, Set<Town> settledTowns, Queue<Town> unsettledTowns, Map<Town, Integer> shortestDistMap) {
        List<Route> routeTowns = townToEvaluate.getNeighbors();
        for (Route routeTown : routeTowns) {
            if (!settledTowns.contains(routeTown)) {
                int edgeDistance = routeTown.getWeight();
                String edgeEndTownName = routeTown.getEndTown();
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

    /**
     * Static factory method to create an instance of RouteFindingEngine which takes TownMap as parameter.
     * @param townMap the graph of Towns and Routes all stored in TownMap class
     * @return a new instance of RouteFindingEngine
     */
    public static RouteFindingEngine getEngine(TownMap townMap) {
        return new RouteFindingEngine(townMap);
    }

}
