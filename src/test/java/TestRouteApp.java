import com.mk.Input;
import com.mk.RouteFindingEngine;
import com.mk.data.Edge;
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
 */
@RunWith(JUnit4.class)
public class TestRouteApp {

    RouteFindingEngine engine;

    @Before
    public void setUp() throws Exception {
        TownMap map = Input.constructTownAndEdges("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7");
        engine = RouteFindingEngine.getEngine(map);
    }

    @Test
    public void testAddCity() {
        Town c = new Town("A");
        Edge e = new Edge("A", "B", 5);
        c.addNeighbor(e);
        assertEquals(c.getName(), "A");
        List<Edge> edges = c.getNeighbors();
        assertNotNull(edges);
        assertEquals(edges.get(0), e);
    }

    @Test
    public void testAddEdge() {
        Edge e = new Edge("A", "B", 5);
        assertEquals(e.getStartTown(), "A");
        assertEquals(e.getEndTown(), "B");
        assertEquals(e.getWeight(), 5);
    }

    @Test
    public void testTownMap() {
        TownMap map = new TownMap();
        Town sTown = new Town("A");
        Edge townEdge = new Edge("A", "B", 5);
        sTown.addNeighbor(townEdge);
        map.addTown(sTown);
        Town eTown = new Town("B");
        map.addTown(eTown);
        assertEquals(sTown.getName(), map.getTown("A").getName());
    }

    @Test
    public void testABC() {
        assertEquals(9, engine.getDistance("A-B-C"));
    }

    @Test
    public void testAD() {
        assertEquals(5, engine.getDistance("A-D"));
    }

    @Test
    public void testADC() {
        assertEquals(13, engine.getDistance("A-D-C"));
    }

    @Test
    public void testAEBCD() {
        assertEquals(22, engine.getDistance("A-E-B-C-D"));
    }

    @Test
    public void testAED() {
        int distance = engine.getDistance("A-E-D");
        assertEquals(0, distance);
        if (distance == 0) System.out.println("NO SUCH ROUTE");
    }


    @Test
    public void testEmpty() {
        assertEquals(0, engine.getDistance(""));
    }

    @Test
    public void testSingleEntry() {
        assertEquals(0, engine.getDistance("A"));
    }


    @Test
    public void testShortestPathCC() {
        assertEquals(9, engine.getShortestPath("C", "C"));
    }

    @Test
    public void testShortestPathAC() {
        assertEquals(9, engine.getShortestPath("A", "C"));
    }

    @Test
    public void testNumberOfTripsCC() {
        assertEquals(2, engine.getTripsWithMaxStops("C", "C", 3));
    }

    @Test
    public void testNumberOfTripsAC() {
        assertEquals(3, engine.getTripsWithOnlyStops("A", "C", 4));
    }
}
