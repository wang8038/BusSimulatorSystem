package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;


public class BusSingletonTest {
  public BusSingleton busSingleton;

  /**
   * Test for BusSingleton.
   */
  @Test
  public void testBusSingleton() {
    String data = "~~~~The simulation time is now"
        + "at time step " + 0 + "~~~~";
    try {

      FileWriter csvWriter = new FileWriter("new.csv");
      busSingleton = BusSingleton.getInstance();
      busSingleton.writeToFile(csvWriter, 1, 1, 44.972392, -93.243774, 2, 2);
      busSingleton.writeToFile(csvWriter, 2, 2, 44.972395, -93.243774, 2, 2);
      busSingleton.writeToFile(csvWriter, 6, 3, 44.971325, -93.24554, 2, 2);

      FileReader reader1 = new FileReader("new.csv");
      char [] c = new char[40];

      BufferedReader reader = new BufferedReader(new FileReader("new.csv"));
      //assertEquals(reader.readLine(),"BUS, 1, 1001, 44.972392, 44.972392, 0, 90");
      assertEquals(reader.readLine(), "BUS, 1, 1, 44.972392, 44.972392, 2, 2");
      assertEquals(reader.readLine(), "BUS, 2, 2, 44.972395, 44.972395, 2, 2");
      assertEquals(reader.readLine(), "BUS, 6, 3, 44.971325, 44.971325, 2, 2");


      //System.out.println(reader.readLine());

    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (IOException a) {
      a.printStackTrace();
    }

  }
}