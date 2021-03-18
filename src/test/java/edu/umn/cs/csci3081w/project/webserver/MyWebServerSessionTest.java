package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class MyWebServerSessionTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test command for initializing the simulation.
   */
  @Test
  public void testSimulationInitialization() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "initRoutes");
    try {
      myWebServerSessionSpy.onMessage(commandFromClient.toString());
      ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
      verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
      JsonObject commandToClient = messageCaptor.getValue();
      assertEquals("4", commandToClient.get("numRoutes").getAsString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test random command.
   */
  @Test
  public void testRandomCommand() throws IOException {
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    //MyWebServerSession session = new MyWebServerSession();
    JsonObject commandFromClient = new JsonObject();
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    commandFromClient.addProperty("command", "noSuchCommand");
    myWebServerSessionSpy.onOpen(sessionDummy);
    final Charset charset = StandardCharsets.UTF_8;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream testStream = new PrintStream(outputStream, true, charset.name());
    System.setOut(new PrintStream(outputStreamCaptor));
    myWebServerSessionSpy.onMessage(commandFromClient.toString());
    testStream.close();
    outputStream.close();
    String strToCompare = "";
    assertEquals(strToCompare, outputStreamCaptor.toString().trim());
  }
}



