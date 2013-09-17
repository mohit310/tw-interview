import com.mk.Input;
import com.mk.RouteFindingEngine;
import com.mk.data.Route;
import com.mk.data.Town;
import com.mk.data.TownMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: mk
 * Date: 9/14/13
 * Time: 9:01 AM
 * This Junit class is used to test our RouteFindingEngine with different scenarios
 */
@RunWith(JUnit4.class)
public class TestRouteApp {

    RouteFindingEngine engine;

    /**
     * Initialize the MAP here and this will be used to seed for all tests
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        TownMap map = Input.constructTownAndEdges("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7");
        engine = RouteFindingEngine.getEngine(map);
    }

    /**
     * Test to add a new city with connections
     */
    @Test
    public void testAddCity() {
        Town c = new Town("A");
        Route e = new Route("A", "B", 5);
        c.addNeighbor(e);
        assertEquals(c.getName(), "A");
        List<Route> routes = c.getNeighbors();
        assertNotNull(routes);
        assertEquals(routes.get(0), e);
    }

    /**
     * Test to add a new Route. Route is synonym for an edge in graph terminology.
     */
    @Test
    public void testAddRoute() {
        Route e = new Route("A", "B", 5);
        assertEquals(e.getStartTown(), "A");
        assertEquals(e.getEndTown(), "B");
        assertEquals(e.getWeight(), 5);
    }

    /**
     * Test to add a new town and route. This tests makes sure we can build a MAP which represents a GRAPH. TownMap represents our Graph.
     */
    @Test
    public void testTownMap() {
        TownMap map = new TownMap();
        Town sTown = new Town("A");
        Route townRoute = new Route("A", "B", 5);
        sTown.addNeighbor(townRoute);
        map.addTown(sTown);
        Town eTown = new Town("B");
        map.addTown(eTown);
        assertEquals(sTown.getName(), map.getTown("A").getName());
    }

    /**
     * Test to compute distance from A to C via B
     */
    @Test
    public void testABC() {
        assertEquals(9, engine.getDistance("A-B-C"));
    }

    /**
     * Test to compute distance from A to D
     */
    @Test
    public void testAD() {
        assertEquals(5, engine.getDistance("A-D"));
    }

    /**
     * Test to compute distance from A to C via D
     */
    @Test
    public void testADC() {
        assertEquals(13, engine.getDistance("A-D-C"));
    }

    /**
     * Test to compute distance from A to D via E-B-C
     */
    @Test
    public void testAEBCD() {
        assertEquals(22, engine.getDistance("A-E-B-C-D"));
    }

    /**
     * Test to compute distance from non existing path. There is no route available. Total distance should be 0
     */
    @Test
    public void testAED() {
        int distance = engine.getDistance("A-E-D");
        assertEquals(0, distance);
    }

    /**
     * Test to compute empty distance
     */
    @Test
    public void testEmpty() {
        assertEquals(0, engine.getDistance(""));
    }

    /**
     * Test to compute distance from only source where there is no destination.
     */
    @Test
    public void testSingleEntry() {
        assertEquals(0, engine.getDistance("A"));
    }

    /**
     * Test to compute shortest distance from source to destination where source and destination can be same node.
     */
    @Test
    public void testShortestPathCC() {
        assertEquals(9, engine.getShortestPath("C", "C"));
    }

    /**
     * Test to compute shortest distance from source to different destination.
     */
    @Test
    public void testShortestPathAC() {
        assertEquals(9, engine.getShortestPath("A", "C"));
    }

    /**
     * Test to compute number and route of all possible trips with maximum number of hops from source to destination. Destination can be same as source.
     */
    @Test
    public void testNumberOfTripsCC() {
        assertEquals(2, engine.getTotalTripsWithMaxHops("C", "C", 3));
    }

    /**
     * Test to compute number and route of all possible trips with maximum number of hops from source to destination. Destination can be same as source.
     */
    @Test
    public void testNumberOfTripsAC() {
        assertEquals(3, engine.getTotalTripsWithExactHops("A", "C", 4));
    }

    /**
     * Test to compute number and route of all possible trips with maximum distance between source and destination. Destination can be same as source.
     */
    @Test
    public void testNumberOfTripsWithMaxDistanceCC() {
        assertEquals(7, engine.getTotalTripsWithMaxDistance("C", "C", 30));
    }


}
