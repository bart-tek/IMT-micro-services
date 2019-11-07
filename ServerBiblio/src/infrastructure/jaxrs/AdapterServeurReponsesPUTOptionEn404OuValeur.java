package infrastructure.jaxrs;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import infrastructure.jaxrs.annotations.ReponsesPUTOption;

@Provider
@ReponsesPUTOption
@Priority(Priorities.HEADER_DECORATOR)
public class AdapterServeurReponsesPUTOptionEn404OuValeur implements ReaderInterceptor, ContainerResponseFilter {

	private static String DESCRIPTION_LIVRE = "descriptionLivre";

	public AdapterServeurReponsesPUTOptionEn404OuValeur() {
		System.out.println("** Initialisation du filtre de type " + this.getClass());
	}

	@Override
	public void filter(ContainerRequestContext requete, ContainerResponseContext reponse) throws IOException {

		// Cas PUT

		if (requete.getMethod().equalsIgnoreCase("PUT")) {
			if (reponse.getStatus() == Response.Status.OK.getStatusCode()) {
				if (reponse.getEntity() instanceof Optional<?>) {
					Optional<?> entite = (Optional<?>) reponse.getEntity();
					if (!entite.isPresent()) {
						convertirEn404(requete, reponse);
						return;
					} else {
						reponse.setEntity(entite.get());
					}
				}
			}
		}
	}

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

		// unmarshalling du corps du message et mémorisation de ce livre dans une
		// propriété du contexte passé en argument
		context.setProperty("Description", context.proceed());

		return context.getProperty("Description");

	}

	private void convertirEn404(ContainerRequestContext requete, ContainerResponseContext reponse) {
		System.out.println("recherche : 404 NOT FOUND !");
		String contenu = requete.getUriInfo().getRequestUri().toString();
		// Utilisation de la propriété pour récupérer la description du livre recherché.
		contenu = contenu + " - " + requete.getProperty(DESCRIPTION_LIVRE);
		reponse.setEntity(contenu, null, MediaType.TEXT_PLAIN_TYPE);
		reponse.setStatus(Response.Status.NOT_FOUND.getStatusCode());
	}

}
