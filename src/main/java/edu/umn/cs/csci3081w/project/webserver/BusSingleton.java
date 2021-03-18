package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.Position;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

public class BusSingleton {
  //  private BusData data;
  private static BusSingleton uniqueInstance;


  private BusSingleton() {
  }

  /**
   * Bus singleton Class.
   * @return unique instance
   */
  public static BusSingleton getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new BusSingleton();
    }
    return uniqueInstance;
  }

  /**
   * Writing information to csv file.
   * @param csvWriter FileWriter
   * @param simulationTimeElapsed Simulation time
   * @param busId id of bus
   * @param x x position
   * @param y y position
   * @param num number
   * @param cap capacity
   * @throws IOException when not accepted
   */
  public void writeToFile(FileWriter csvWriter, int simulationTimeElapsed,
                          int busId, double x, double y, int num, int cap) throws IOException {
    csvWriter.append("BUS, ");
    csvWriter.append(String.valueOf(simulationTimeElapsed) + ", ");
    csvWriter.append(String.valueOf(busId) + ", ");
    csvWriter.append(String.valueOf(x) + ", ");
    csvWriter.append(String.valueOf(x) + ", ");
    csvWriter.append(String.valueOf(num) + ", ");
    csvWriter.append(String.valueOf(cap + "\n"));
    csvWriter.flush();

  }

}
