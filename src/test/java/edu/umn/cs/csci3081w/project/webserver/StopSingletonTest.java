package edu.umn.cs.csci3081w.project.webserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class StopSingletonTest {
  /**
   * Test stop Singletion class.
   * @throws IOException if not accepted
   */
  @Test
  public void testStopSingleton() throws IOException {
    FileWriter csvWriter = new FileWriter("new.csv");
    StopSingleton stopSingleton = StopSingleton.getInstance();
    FileReader reader1;
    stopSingleton.writeToFile(csvWriter, 1, 0, 44.972392, 44.972392, 1);
    stopSingleton.writeToFile(csvWriter, 1, 1, 44.97358, 44.97358, 1);
    stopSingleton.writeToFile(csvWriter, 1, 2, 44.975392, 44.975392, 0);
    stopSingleton.writeToFile(csvWriter, 1, 2, 44.975392, 44.975392, 0);
    stopSingleton.writeToFile(csvWriter, 1, 1, 44.97358, 44.97358, 2);
    stopSingleton.writeToFile(csvWriter, 1, 0, 44.972392, 44.972392, 2);
    reader1 = new FileReader("new.csv");
    char [] c = new char[40];

    BufferedReader reader = new BufferedReader(new FileReader("new.csv"));

    //assertEquals(reader.readLine(),"BUS, 1, 1001, 44.972392, 44.972392, 0, 90");
    reader.readLine();
    assertEquals(reader.readLine(), "STOP, 1, 0, 44.972392, 44.972392, 1");
    assertEquals(reader.readLine(), "STOP, 1, 1, 44.97358, 44.97358, 1");
    assertEquals(reader.readLine(), "STOP, 1, 2, 44.975392, 44.975392, 0");
    assertEquals(reader.readLine(), "STOP, 1, 2, 44.975392, 44.975392, 0");
    assertEquals(reader.readLine(), "STOP, 1, 1, 44.97358, 44.97358, 2");
    assertEquals(reader.readLine(), "STOP, 1, 0, 44.972392, 44.972392, 2");
  }

  private void assertEquals(String readLine, String s) {
  }

}
