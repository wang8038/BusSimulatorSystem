package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OverallConcreteBuscreatorTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test set current concrete bus creator.
   */
  @Test
  public void testSetCurrentConcreteBusCreator2() {
    OverallConcreteBusCreator overallConcreteBusCreator = new OverallConcreteBusCreator();
    overallConcreteBusCreator.setCurrentConcreteBusCreator(1, 1);
    assertTrue(overallConcreteBusCreator.getCurrentConcreteBusCreator()
        instanceof RandomConcreteBusCreator);
    OverallConcreteBusCreator overallConcreteBusCreator2 = new OverallConcreteBusCreator();
    overallConcreteBusCreator.setCurrentConcreteBusCreator(15, 1);
    assertTrue(overallConcreteBusCreator.getCurrentConcreteBusCreator()
        instanceof RandomConcreteBusCreator);
  }

  /**
   * Test set current concrete bus creator.
   */
  @Test
  public void testSetCurrentConcreteBusCreator() {
    OverallConcreteBusCreator overallConcreteBusCreator = new OverallConcreteBusCreator();
    overallConcreteBusCreator.setCurrentConcreteBusCreator(2, 1);
    assertTrue(overallConcreteBusCreator.getCurrentConcreteBusCreator()
        instanceof TimeOfDayConcreteBusCreator);
  }

  /**
   * Test create bus.
   */
  @Test
  public void testCreateBus() {
    OverallConcreteBusCreator overallConcreteBusCreator = new OverallConcreteBusCreator();
    overallConcreteBusCreator.setCurrentConcreteBusCreator(2, 1);
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    Route testOutRoute = TestUtils.createRouteGivenData(stopsOut, distancesOut, probabilitiesOut);
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    Route testInRoute = TestUtils.createRouteGivenData(stopsIn, distancesIn, probabilitiesIn);
    Bus testBus = overallConcreteBusCreator.createBus("0", testOutRoute, testInRoute, 1);
    assertTrue(testBus instanceof SmallBus);
  }

}
