package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.PassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.TestUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Integer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class VisualizationSimulatorTest {
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
   * Test constructor.
   */
  @Test
  public void testConstructorNormal() {
    MyWebServer myWS = new MyWebServer();
    ConfigManager myCM = new ConfigManager();
    MyWebServerSession myWss = new MyWebServerSession();
    VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
    assertTrue(myVS instanceof VisualizationSimulator);
  }

  /**
   * Test for setMethodTest.
   */
  @Test
  public void testSetConcreteBusCreator() {
    MyWebServer myWS = new MyWebServer();
    ConfigManager myCM = new ConfigManager();
    MyWebServerSession myWss = new MyWebServerSession();
    VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
    LocalDateTime time = LocalDateTime.now();
    int day = time.getDayOfMonth();
    myVS.setConcreteBusCreator(time);

  }

  /**
   * Test start method in VS.
   */
  @Test
  public void testStart() {
    MyWebServer myWS = new MyWebServer();
    ConfigManager myCM = new ConfigManager();
    MyWebServerSession myWss = new MyWebServerSession();
    VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
    List<Integer> busStartTimings = new ArrayList<Integer>();
    busStartTimings.add(1);
    busStartTimings.add(3);
    busStartTimings.add(6);
    int timeStps = 3;
    myVS.start(busStartTimings, timeStps);
    assertTrue(myVS instanceof VisualizationSimulator);
  }

  /**
   * Test for togglePause.
   */
  @Test
  public void testTogglePause() {
    MyWebServer myWS = new MyWebServer();
    ConfigManager myCM = new ConfigManager();
    MyWebServerSession myWss = new MyWebServerSession();
    VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
    myVS.togglePause();
    List<Integer> busStartTimings = new ArrayList<Integer>();
    busStartTimings.add(1);
    busStartTimings.add(3);
    busStartTimings.add(6);
    int timeStps = 3;
    myVS.start(busStartTimings, timeStps);
    myVS.togglePause();
  }

  /**
   * Test if trip complete.
   */
  @Test
  public void testIsTripComplete() throws IOException {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConfigManager myCM = new ConfigManager();
    MyWebServer myWS = new MyWebServer();
    VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWebServerSessionSpy);
    List<Integer> busStartTime = new ArrayList<Integer>();
    myCM.readConfig(getClass().getClassLoader().getResource("config.txt").getFile());
    myVS.setConcreteBusCreator(LocalDateTime.now());
    busStartTime.add(3);
    myVS.start(busStartTime, 3);
    Bus bus = TestUtils.createBus();
    myVS.update();
    myVS.togglePause();
    assertFalse(bus.isTripComplete());
  }

  /**
   * Test for addBusObserver(String).
   */
  @Test
  public void testAddBusObserver() {
    try {
      Stop stop1 = new Stop(0, 44.972392, -93.243774);
      Stop stop2 = new Stop(1, 44.973580, -93.235071);
      Stop stop3 = new Stop(2, 44.975392, -93.226632);
      List<Stop> stopsIn = new ArrayList<Stop>();
      stopsIn.add(stop1);
      stopsIn.add(stop2);
      stopsIn.add(stop3);
      List<Double> distancesIn = new ArrayList<Double>();
      distancesIn.add(0.008784);
      distancesIn.add(0.008631);
      List<Double> probabilitiesIn = new ArrayList<Double>();
      probabilitiesIn.add(.15);
      probabilitiesIn.add(0.3);
      probabilitiesIn.add(.025);
      PassengerGenerator generatorIn = new RandomPassengerGenerator(probabilitiesIn, stopsIn);
      Route testRouteIn = new Route("testRouteIn", stopsIn, distancesIn, 3, generatorIn);
      List<Stop> stopsOut = new ArrayList<>();
      stopsOut.add(stop3);
      stopsOut.add(stop2);
      stopsOut.add(stop1);
      List<Double> distancesOut = new ArrayList<>();
      distancesOut.add(0.008631);
      distancesOut.add(0.008784);
      List<Double> probabilitiesOut = new ArrayList<>();
      probabilitiesOut.add(.025);
      probabilitiesOut.add(0.3);
      probabilitiesOut.add(.15);
      PassengerGenerator generatorOut = new RandomPassengerGenerator(probabilitiesOut, stopsOut);
      Route testRouteOut = new Route("testRouteIn", stopsOut, distancesOut, 3, generatorOut);
      Bus bus = new Bus("0", testRouteOut, testRouteIn, 5, 1);
      Bus bus1 = new Bus("1", testRouteOut, testRouteIn, 5, 1);

      MyWebServer myWS = new MyWebServer();
      Position pos = new Position();
      BusData data = new BusData("0", pos, 2, 3);
      myWS.updateBus(data, false);
      ConfigManager myCM = new ConfigManager();
      MyWebServerSession myWss = new MyWebServerSession();
      Session sessionDummy = mock(Session.class);
      myWss.onOpen(sessionDummy);
      VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
      List<Bus> list = new ArrayList<Bus>(2);
      //List<Bus> list=new Bus(3)  ;
      list.add(bus);
      list.add(bus1);
      myVS.setBusses(list);
      //LocalDateTime time=LocalDateTime.now();
      //myVS.setConcreteBusCreator(time);
      //myVS.getSimulationConcreteBusCreator().createBus("0",testRouteOut,testRouteIn,3);
      //myVS.getSimulationConcreteBusCreator().createBus("1",testRouteOut,testRouteIn,3);

      myVS.update();
      //System.out.println(myVS.getBusses().get(0).getBusData().getId());

      myVS.addBusObserver("1");

      assertTrue(myVS.getBusSubject().getBusObservers().contains(bus1));
      myVS.addBusObserver("0");
      assertTrue(myVS.getBusSubject().getBusObservers().contains(bus));

      // assertTrue(myVS.getBusSubject().equals(bus));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Test for addStopObserver(String id).
   */
  @Test
  public void testAddStopObserver() {
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774);
      Stop stop2 = new Stop(1, 44.973580, -93.235071);
      Stop stop3 = new Stop(2, 44.975392, -93.226632);
      List<Stop> stops = new ArrayList<>(); //for route
      stops.add(stop);
      stops.add(stop2);
      stops.add(stop3);
      List<Double> distances = new ArrayList<>();
      distances.add(0.9712663713083954);
      distances.add(0.961379387775189);
      List<Double> probabilities = new ArrayList<>();
      probabilities.add(.15);
      probabilities.add(0.3);
      probabilities.add(.0);
      PassengerGenerator generator = new RandomPassengerGenerator(probabilities, stops);
      Route myRoute = new Route("TestRoute", stops, distances, 3, generator);
      MyWebServer myWS = new MyWebServer();
      ConfigManager myCM = new ConfigManager();
      MyWebServerSession myWss = new MyWebServerSession();
      Session sessionDummy = mock(Session.class);
      myWss.onOpen(sessionDummy);
      VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
      List<Route> list = new ArrayList<Route>(2);
      //List<Bus> list=new Bus(3)  ;
      list.add(myRoute);
      //LocalDateTime time=LocalDateTime.now();
      //myVS.setConcreteBusCreator(time);
      //myVS.getSimulationConcreteBusCreator().createBus("0",testRouteOut,testRouteIn,3);
      //myVS.getSimulationConcreteBusCreator().createBus("1",testRouteOut,testRouteIn,3);
      myVS.setPrototypeRoutes(list);
      myVS.update();
      //System.out.println(myVS.getBusses().get(0).getBusData().getId());
      myVS.addStopObserver("0");
      //myVS.addBusObserver("1");
      assertTrue(myVS.getStopSubject().getStopObservers().contains(stop));
      myVS.addStopObserver("1");
      assertTrue(myVS.getStopSubject().getStopObservers().contains(stop2));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test for update.
   */
  @Test
  public void testUpdate() {
    String data = "~~~~The simulation time is now"
        + "at time step " + 0 + "~~~~";
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774);
      Stop stop2 = new Stop(1, 44.973580, -93.235071);
      Stop stop3 = new Stop(2, 44.975392, -93.226632);
      List<Stop> stops = new ArrayList<>(); //for route
      stops.add(stop);
      stops.add(stop2);
      stops.add(stop3);
      List<Double> distances = new ArrayList<>();
      distances.add(0.9712663713083954);
      distances.add(0.961379387775189);
      List<Double> probabilities = new ArrayList<>();
      probabilities.add(.15);
      probabilities.add(0.3);
      probabilities.add(.0);
      PassengerGenerator generator = new RandomPassengerGenerator(probabilities, stops);
      Route myRoute = new Route("TestRoute", stops, distances, 3, generator);
      //List<Stop> stops2 = new ArrayList<>(); //for route
      //stops2.add(stop);
      //stops2.add(stop2);
      //stops2.add(stop3);
      //PassengerGenerator generator1 = new RandomPassengerGenerator(probabilities, stops2);
      List<Stop> stops1 = new ArrayList<>();
      stops1.add(stop3);
      stops1.add(stop2);
      stops1.add(stop);
      Route myRoute2 = new Route("TestRoute1", stops1, distances, 3, generator);
      //myRoute2.update();
      //myRoute.update();
      MyWebServer myWS = new MyWebServer();
      ConfigManager myCM = new ConfigManager();
      MyWebServerSession myWss = new MyWebServerSession();
      Session sessionDummy = mock(Session.class);
      myWss.onOpen(sessionDummy);
      VisualizationSimulator myVS = new VisualizationSimulator(myWS, myCM, myWss);
      List<Route> list = new ArrayList<Route>(4);
      //List<Bus> list=new Bus(3)  ;
      list.add(myRoute);
      list.add(myRoute2);
      //list.add(myRoute);
      //list.add(myRoute2);
      LocalDateTime time = LocalDateTime.now();
      myVS.setConcreteBusCreator(time);
      myVS.getSimulationConcreteBusCreator().createBus("0", myRoute, myRoute2, 3);
      myVS.getSimulationConcreteBusCreator().createBus("1", myRoute, myRoute2, 3);
      myVS.setPrototypeRoutes(list);
      List<Integer> timeSince = new ArrayList<Integer>();
      timeSince.add(0);
      timeSince.add(1);
      myVS.setTimeSinceLastBus(timeSince);
      //myVS.start(timeSince,1);
      List<Integer> busStartTiming = new ArrayList<>(2);
      busStartTiming.add(0);
      busStartTiming.add(0);
      busStartTiming.add(0);
      myVS.setBusStartTimings(busStartTiming);


      myVS.update();

      myVS.getBusses().get(0).move();

      //assertTrue();
      myVS.setPaused(true);
      myVS.update();
      //System.out.println(myVS.getBusses().get(0).getBusData().getId());
      //myVS.addStopObserver("0");
      //myVS.addBusObserver("1");
      // FileWriter csvWriter = new FileWriter("new.csv");

      FileReader reader1 = new FileReader("/Users/albert/Desktop/"
          + "SHENQINGCODE/CSCI3081w/project_iteration_3-Team13/new.csv");
      char []c = new char[40];


      BufferedReader reader = new BufferedReader(new FileReader("/Users/albert/Desktop/"
          + "SHENQINGCODE/CSCI3081w/project_iteration_3-Team13/new.csv"));

      //assertEquals(reader.readLine(),"BUS, 1, 1001, 44.972392, 44.972392, 0, 90");
      reader.readLine();
      assertEquals(reader.readLine(), "STOP, 1, 0, 44.972392, 44.972392, 1");
      assertEquals(reader.readLine(), "STOP, 1, 1, 44.97358, 44.97358, 1");
      assertEquals(reader.readLine(), "STOP, 1, 2, 44.975392, 44.975392, 0");
      assertEquals(reader.readLine(), "STOP, 1, 2, 44.975392, 44.975392, 0");
      assertEquals(reader.readLine(), "STOP, 1, 1, 44.97358, 44.97358, 2");
      assertEquals(reader.readLine(), "STOP, 1, 0, 44.972392, 44.972392, 2");

      System.out.println(reader.readLine());

    } catch (NullPointerException | IOException e) {
      e.printStackTrace();
    }

  }

}