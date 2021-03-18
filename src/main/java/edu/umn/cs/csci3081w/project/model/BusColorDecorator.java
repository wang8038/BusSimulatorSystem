package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;

public abstract class BusColorDecorator extends BusData {
  protected Bus bus;
  protected Color color;

  public BusColorDecorator() {
    super();
  }

  public abstract Color getBusColor();
}
