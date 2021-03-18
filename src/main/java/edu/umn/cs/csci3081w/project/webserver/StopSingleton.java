package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.Position;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

public class StopSingleton {
  private static StopSingleton uniqueInstance;

  StopSingleton() {
  }

  /**
   * A stop singleton class.
   * @return unique instance
   */
  public static StopSingleton getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new StopSingleton();
    }
    return uniqueInstance;
  }

  /**
   * Writing information to csv file.
   * @param csvWriter File Writer
   * @param simulationTimeElapsed Simulation Time
   * @param busId bus id
   * @param x x position
   * @param y y position
   * @param num number
   * @throws IOException if not accepted
   */
  public void writeToFile(FileWriter csvWriter, int simulationTimeElapsed,
                          int busId, double x, double y, int num) throws IOException {
    csvWriter.append("STOP, ");
    csvWriter.append(String.valueOf(simulationTimeElapsed) + ", ");
    csvWriter.append(String.valueOf(busId) + ", ");
    csvWriter.append(String.valueOf(x) + ", ");
    csvWriter.append(String.valueOf(x) + ", ");
    csvWriter.append(String.valueOf(num) + "\n");
    csvWriter.flush();
  }
}
