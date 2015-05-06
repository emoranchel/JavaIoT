package test.iot.server.testiotserver.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

@Path("temperature")
public class TemperatureResource {

  @GET
  @Produces("text/plain")
  public String getText() {
    return "HI!";
  }

  @POST
  @Consumes("text/plain")
  public void putText(String content) {
    System.out.println("REST RECEIVED " + content);
  }
}
