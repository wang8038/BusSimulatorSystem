package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import org.junit.jupiter.api.Test;


public class BusInboundTest {

  @Test
  public void testBusInbound() {
    BusInbound busIn = new BusInbound();
    Color color = new Color(255, 215, 0, 255);
    assertEquals(color, busIn.getBusColor());
  }
}
