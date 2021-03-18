package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import org.junit.jupiter.api.Test;



public class BusOutboundTest {

  @Test
  public void testBusOutbound() {
    BusOutbound busOut = new BusOutbound();
    Color color = new Color(128, 0, 0, 255);
    assertEquals(color, busOut.getBusColor());
  }
}
