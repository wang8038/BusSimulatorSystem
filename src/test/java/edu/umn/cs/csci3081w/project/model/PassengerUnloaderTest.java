package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerUnloaderTest {

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
   * Test unloading of passengers.
   */
  @Test
  public void testUnloadPassengerNoPassengers() {
    List<Passenger> passengers = new ArrayList<>();
    Stop stop = new Stop(0, 44.972392, -93.243774);
    PassengerUnloader testPassUnloader = new PassengerUnloader();
    int passUnloaded = testPassUnloader.unloadPassengers(passengers, stop);
    assertEquals(0, passUnloaded);
  }

  /**
   * Test unloading of passengers.
   */
  @Test
  public void testUnloadPassengerNoPassengers2() {
    List<Passenger> passengers = new ArrayList<>();
    Passenger passenger = new Passenger(0, "Goldy");
    Passenger passenger1 = new Passenger(2, "josh");
    passengers.add(passenger);
    passengers.add(passenger1);
    Stop stop = new Stop(0, 44.972392, -93.243774);
    PassengerUnloader testPassUnloader = new PassengerUnloader();
    int passUnloaded = testPassUnloader.unloadPassengers(passengers, stop);
    assertEquals(1, passUnloaded);
  }

}
