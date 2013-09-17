package com.mk.data;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Route {

    private String startTown;
    private String endTown;
    private int weight;

    public Route(String startTown, String endTown, int weight) {
        this.startTown = startTown;
        this.endTown = endTown;
        this.weight = weight;
    }

    public String getStartTown() {
        return startTown;
    }

    public String getEndTown() {
        return endTown;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (!endTown.equals(route.endTown)) return false;
        if (!startTown.equals(route.startTown)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startTown.hashCode();
        result = 31 * result + endTown.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "startTown='" + startTown + '\'' +
                ", endTown='" + endTown + '\'' +
                ", weight=" + weight +
                '}';
    }
}
