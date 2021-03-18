package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RouteData;
import edu.umn.cs.csci3081w.project.model.StopData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class GetRoutesCommandTest {

  /**
   * Test get empty busses command.
   */
  @Test
  public void getEmptyRoutesCommandTest() {
    try {
      MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
      doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
      Session sessionDummy = mock(Session.class);
      myWebServerSessionSpy.onOpen(sessionDummy);
      JsonObject commandFromClient = new JsonObject();
      //commandFromClient.addProperty("command", "initRoutes");
      commandFromClient.addProperty("command", "getRoutes");
      myWebServerSessionSpy.onMessage(commandFromClient.toString());
      ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
      verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
      JsonObject commandToClient = messageCaptor.getValue();
      assertEquals("[]", commandToClient.get("routes").toString());
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test if GetRoutesCommand sends the right message to client.
   */
  @Test
  public void testExecute() throws IOException {
    //setup
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    MyWebServerSessionState state = new MyWebServerSessionState();
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    MyWebServer myWS = myWebServerSessionSpy.getMyWS();
    JsonObject commandFromClient = new JsonObject();

    RouteData route1 = new RouteData();
    Position p = new Position(44.972392, -93.243774);
    //Stop stop = new Stop(0, 44.972392, -93.243774);
    StopData data = new StopData("1", p, 4);
    List<StopData> a = new ArrayList<StopData>();
    a.add(data);
    route1.setStops(a);
    myWS.routes.add(route1);
    commandFromClient.addProperty("command", "getRoutes");

    try {
      GetRoutesCommand commandObject = new GetRoutesCommand(myWS);
      commandObject.execute(myWebServerSessionSpy, commandFromClient, state);
      ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
      verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
      JsonObject commandToClient = messageCaptor.getValue();
      assertEquals("[{\"id\":\"\",\"stops\":[{\"id\":\"1\",\"numPeople\":4,"
              + "\"position\":{\"x\":44.972392,\"y\":-93.243774}}]}]",
          commandToClient.get("routes").toString());
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }
}

