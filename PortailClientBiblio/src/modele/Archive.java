package modele;

import static configuration.JAXRS.TYPE_MEDIA;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import infrastructure.jaxrs.HyperLien;

public interface Archive {

	@Path("{id}")
	@Produces(TYPE_MEDIA)
	Livre sousRessource(@PathParam("id") IdentifiantLivre id);

	@GET
	@Path("{id}")
	@Produces(TYPE_MEDIA)
	Livre getRepresentation(@PathParam("id") IdentifiantLivre id);

	@POST
	@Consumes(TYPE_MEDIA)
	@Produces(TYPE_MEDIA)
	HyperLien<Livre> ajouter(Livre l);

}
