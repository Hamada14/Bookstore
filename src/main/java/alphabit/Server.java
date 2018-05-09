package alphabit;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello")
public class Server {
private String name="";

    /**
     * Default constructor. 
     */
    public Server() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieves representation of an instance of 
     * Hello
     * @return an instance of String
     */
    @GET
    @Produces("text/plain")
    public String sayHello() {
        // TODO return proper representation object
    return "Hello World"+name;
    }

    /**
     * PUT method for updating or creating an
     * instance of Hello
     * @param content representation for the resource
     * @return an HTTP response with content of the 
     * updated or created resource.
     */
    @PUT
    @Consumes("text/plain")
    public void putText(String content) {
     name=content; 
    }
}