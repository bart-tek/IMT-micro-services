package modele;

import static configuration.JAXRS.CHEMIN_BIBLIO;
import static configuration.JAXRS.TYPE_MEDIA;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.annotations.ReponsesGETNull404;
import infrastructure.jaxrs.annotations.ReponsesPOSTCreated;

@Path(CHEMIN_BIBLIO)
public interface Archive {

	Livre sousRessource(IdentifiantLivre id);

	@GET
	@Path("{id}")
	@Produces(TYPE_MEDIA)
	@ReponsesGETNull404
	Livre getRepresentation(@PathParam("id") IdentifiantLivre id);

	@POST
	@Consumes(TYPE_MEDIA)
	@Produces(TYPE_MEDIA)
	@ReponsesPOSTCreated
	HyperLien<Livre> ajouter(Livre l);
}
