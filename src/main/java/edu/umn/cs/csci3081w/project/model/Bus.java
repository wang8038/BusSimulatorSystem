package edu.umn.cs.csci3081w.project.model;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Bus implements BusObserver {
  private PassengerUnloader unloader;
  private PassengerLoader loader;
  private List<Passenger> passengers;
  private int passengerMaxCapacity;
  private String name;
  //the speed is in distance over a time unit
  private double speed;
  private Route outgoingRoute;
  private Route incomingRoute;
  private double distanceRemaining;
  private Stop nextStop;
  private BusData busData;
  public static boolean TESTING = false;
  //  private JsonObject testingOutput;
  private ConcreteBusSubject concreteBusSubject;


  /**
   * Constructor for a bus.
   *
   * @param name     name of bus
   * @param out      outbound route
   * @param in       inbound route
   * @param capacity capacity of bus
   * @param speed    speed of bus
   */
  public Bus(String name, Route out, Route in, int capacity, double speed) {
    this.name = name;
    this.outgoingRoute = out;
    this.incomingRoute = in;
    this.passengerMaxCapacity = capacity;
    this.speed = speed;
    this.distanceRemaining = 0;
    this.nextStop = out.getDestinationStop();
    this.unloader = new PassengerUnloader();
    this.loader = new PassengerLoader();
    this.passengers = new ArrayList<Passenger>();
    this.busData = new BusData();

  }

  /**
   * Report statistics for the bus.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Bus Info Start####");
    out.println("Name: " + name);
    out.println("Speed: " + speed);
    out.println("Distance to next stop: " + distanceRemaining);
    out.println("****Passengers Info Start****");
    out.println("Num of passengers: " + passengers.size());
    for (Passenger pass : passengers) {
      pass.report(out);
    }
    out.println("****Passengers Info End****");
    out.println("####Bus Info End####");
  }

  public boolean isTripComplete() {
    return outgoingRoute.isAtEnd() && incomingRoute.isAtEnd();
  }

  public boolean loadPassenger(Passenger newPassenger) {
    return loader.loadPassenger(newPassenger, passengerMaxCapacity, passengers);
  }

  /**
   * Moves the bus on its route.
   *
   * @return if the bus moved
   */
  public boolean move() {
    // update passengers FIRST
    // new passengers will get "updated" when getting on the bus
    for (Passenger passenger : passengers) {
      passenger.pasUpdate();
    }
    //actually move
    double speed = updateDistance();
    if (!isTripComplete() && distanceRemaining <= 0) {
      //load & unload
      int passengersHandled = handleBusStop();
      if (passengersHandled >= 0) {
        // if we spent time unloading/loading
        // we don't get to count excess distance towards next stop
        distanceRemaining = 0;
      }
      //switch to next stop
      toNextStop();
    }
    return (speed > 0);
  }

  public void update() {
    move();
    updateBusData();
  }

  /**
   * Updates bus data with new details.
   */
  public void updateBusData() {
    busData.setId(name);
    // Get the correct route and early exit
    Route currentRoute = outgoingRoute;
    if (outgoingRoute.isAtEnd()) {
      if (incomingRoute.isAtEnd()) {
        return;
      }
      currentRoute = incomingRoute;
    }
    Stop prevStop = currentRoute.prevStop();
    Stop nextStop = currentRoute.getDestinationStop();
    double distanceBetween = currentRoute.getNextStopDistance();
    // the ratio shows us how far from the previous stop are we in a ratio from 0 to 1
    double ratio;
    // check if we are at the first stop
    if (distanceBetween - 0.00001 < 0) {
      ratio = 1;
    } else {
      ratio = distanceRemaining / distanceBetween;
      if (ratio < 0) {
        ratio = 0;
        distanceRemaining = 0;
      }
    }
    Position p = new Position(0.0, 0.0);
    p.setXcoordLoc(nextStop.getLongitude() * (1 - ratio) + prevStop.getLongitude() * ratio);
    p.setYcoordLoc(nextStop.getLatitude() * (1 - ratio) + prevStop.getLatitude() * ratio);
    busData.setPosition(p);
    busData.setNumPassengers(passengers.size());
    busData.setCapacity(passengerMaxCapacity);
  }

  /**
   * Retrives the current bus information and send the information to the visualization module.
   */
  public void displayInfo() {
    JsonObject data = new JsonObject();
    data.addProperty("command", "observeBus");
    String text = "";
    text += "Bus " + busData.getId() + System.lineSeparator();
    text += "-----------------------------" + System.lineSeparator();
    text += "  * Position: (" + busData.getPosition().getXcoordLoc() + ","
        + busData.getPosition().getYcoordLoc()
        + ")" + System.lineSeparator();
    text += "  * Passengers: " + busData.getNumPassengers() + System.lineSeparator();
    text += "  * Capacity: " + busData.getCapacity() + System.lineSeparator();
    data.addProperty("text", text);
    concreteBusSubject.getSession().sendJson(data);
  }

  public BusData getBusData() {
    return busData;
  }

  public String getName() {
    return name;
  }

  public Stop getNextStop() {
    return nextStop;
  }

  public long getNumPassengers() {
    return passengers.size();
  }

  public int getCapacity() {
    return passengerMaxCapacity;
  }

  public Route getOutgoingRoute() {
    return outgoingRoute;
  }

  public Route getIncomingRoute() {
    return incomingRoute;
  }

  public double getSpeed() {
    return speed;
  }


  public void setConcreteBusSubject(
      ConcreteBusSubject concreteBusSubject) {
    this.concreteBusSubject = concreteBusSubject;
  }

  private int unloadPassengers() {
    return unloader.unloadPassengers(passengers, nextStop);
  }

  private int handleBusStop() {
    int passengersHandled = 0;
    passengersHandled += unloadPassengers();
    passengersHandled += nextStop.loadPassengers(this);
    if (passengersHandled != 0) {
      distanceRemaining = 0;
    }
    return passengersHandled;
  }

  private void toNextStop() {
    currentRoute().toNextStop();
    if (!isTripComplete()) {
      nextStop = currentRoute().getDestinationStop();
      distanceRemaining +=
          currentRoute().getNextStopDistance();
    } else {
      nextStop = null;
      distanceRemaining = 999;
    }
  }

  private double updateDistance() {
    // Updates the distance remaining and returns the effective speed of the bus
    // Bus does not move if speed is negative or bus is at end of route
    if (isTripComplete()) {
      return 0;
    }
    if (speed < 0) {
      return 0;
    }
    distanceRemaining -= speed;
    return speed;
  }

  private Route currentRoute() {
    // Figure out if we're on the outgoing or incoming route
    if (!outgoingRoute.isAtEnd()) {
      busData.setBusColor(new Color(128, 0, 0, 255));
      return outgoingRoute;
    }
    busData.setBusColor(new Color(255, 215, 0, 255));
    return incomingRoute;
  }
}
