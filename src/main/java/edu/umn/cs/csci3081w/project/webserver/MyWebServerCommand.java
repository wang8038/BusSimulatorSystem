package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;
import java.io.IOException;

public abstract class MyWebServerCommand {
  public abstract void execute(MyWebServerSession session, JsonObject command,
                               MyWebServerSessionState state) throws IOException;
}
