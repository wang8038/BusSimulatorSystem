package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;

public class BusOutbound extends BusColorDecorator {
  Bus bus;
  private Color color;

  public BusOutbound() {
    super();
    this.color = new Color(128, 0, 0, 255);
  }

  public Color getBusColor() {
    return color;
  }
}