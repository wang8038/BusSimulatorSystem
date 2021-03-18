package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;

public class BusInbound extends BusColorDecorator {
  Bus bus;
  private Color color;

  public BusInbound() {
    super();
    this.color = new Color(255, 215, 0, 255);
  }

  public Color getBusColor() {
    return color;
  }
}
