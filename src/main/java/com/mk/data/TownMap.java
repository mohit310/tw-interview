package com.mk.data;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 9:38 AM
 * This class holds the entire graph of Towns and Routes. We chose Map as lookup will be O(1) rather than using List
 * Each Town can contain multiple edges. Town stores respective edges to other Towns as List in Town class.
 */
public class TownMap {

    private final Map<String, Town> towns = new TreeMap<String, Town>();

    public void addTown(Town t) {
        towns.put(t.getName(), t);
    }

    public Town getTown(String townName) {
        return towns.get(townName);
    }

    public int getSize() {
        return towns.size();
    }

    public List<String> getSortedTowns(){
        Set<String> names = towns.keySet();
        return new ArrayList<String>(names);
    }

    @Override
    public String toString() {
        return "TownMap{" +
                "towns=" + towns +
                '}';
    }
}
