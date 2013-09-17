package com.mk.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 8:58 AM
 */
public class Town implements Comparable<Town>,Cloneable{

    private String name;
    private List<Route> neighbors = new ArrayList<Route>();

    public Town(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Route> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Route neighbor) {
        neighbors.add(neighbor);
    }

    public int getNeighborDistance(String neighborName){
        for(Route neighbor: neighbors){
            if(neighbor.getEndTown().equals(neighborName)){
                return neighbor.getWeight();
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Town town = (Town) o;

        if (!name.equals(town.name)) return false;

        return true;
    }

    @Override
    public int compareTo(Town o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Town{" +
                "name='" + name +
                '}';
    }
}
