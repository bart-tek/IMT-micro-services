package essai;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface Salut {

	@Path("salut")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String dire(@QueryParam("de") String x);
}