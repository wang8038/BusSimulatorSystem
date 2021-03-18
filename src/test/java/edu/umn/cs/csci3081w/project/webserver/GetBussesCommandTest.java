package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import java.io.IOException;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;



public class GetBussesCommandTest {
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
   * Test get empty busses command.
   */
  @Test
  public void getEmptyBussesCommandTest() {
    try {
      MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
      doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
      Session sessionDummy = mock(Session.class);
      myWebServerSessionSpy.onOpen(sessionDummy);
      JsonObject commandFromClient = new JsonObject();
      //commandFromClient.addProperty("command", "initRoutes");
      commandFromClient.addProperty("command", "getBusses");
      myWebServerSessionSpy.onMessage(commandFromClient.toString());
      ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
      verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
      JsonObject commandToClient = messageCaptor.getValue();
      assertEquals("[]", commandToClient.get("busses").toString());
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void getBussesCommandTest2() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    MyWebServerSessionState state = new MyWebServerSessionState();
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    JsonObject commandFromClient = new JsonObject();

    BusData bus1 = new BusData("0", new Position(44.972392, -93.243774), 0, 30);
    BusData bus2 = new BusData("1", new Position(44.973580, -93.235071), 0, 60);
    myWS.busses.add(bus1);
    myWS.busses.add(bus2);
    commandFromClient.addProperty("command", "updateBusses");

    try {
      GetBussesCommand commandObject = new GetBussesCommand(myWS);
      commandObject.execute(myWebServerSessionSpy, commandFromClient, state);
      ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
      verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
      JsonObject commandToClient = messageCaptor.getValue();
      assertEquals("[{\"id\":\"0\",\"numPassengers\":0.0,\"capacity\":30.0,"
              + "\"position\":{\"x\":44.972392,\"y\":-93.243774},"
              + "\"color\":{\"red\":0,\"green\":0,\"blue\":0,\"alpha\":0}},"
              + "{\"id\":\"1\",\"numPassengers\":0.0,\"capacity\":60.0,"
              + "\"position\":{\"x\":44.97358,\"y\":-93.235071},"
              + "\"color\":{\"red\":0,\"green\":0,\"blue\":0,\"alpha\":0}}]",
          commandToClient.get("busses").toString());

    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }


}


