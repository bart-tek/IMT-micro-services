package infrastructure.jaxrs;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import infrastructure.entreesSorties.Flots;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class Cacher implements ClientResponseFilter {

	private Cache cache;
	private ErreurPrecondition412 gestionnaireErreur;

	@Inject
	public Cacher(Cache c, ErreurPrecondition412 g) {
		gestionnaireErreur = g;
		cache = c;
		System.out.println("Filtre initialis� " + this + " : " + this.getClass());
		System.out.println("- Cache partag� : " + this.cache);
		System.out.println("- Gestionnaire 412 partag� : " + this.gestionnaireErreur);
	}

	@Override
	/*
	 * Filtre / r�ponse
	 * 
	 * On utilise le statut de la r�ponse pour discriminer les r�ponses. 200 (OK) :
	 * GET ou PUT, 304 (NOT_MODIFIED) : GET, 412 (PRECONDITION_FAILED) : PUT. On
	 * peut aussi utiliser le param�tre requete pour r�cup�rer des informations
	 * concernant la requ�te, par exemple la m�thode Http (PUT ou GET).
	 */
	public synchronized void filter(ClientRequestContext requete, ClientResponseContext reponse) throws IOException {
		int statut = reponse.getStatus();
		// * Statut OK (200) - GET ou PUT
		if (statut == Response.Status.OK.getStatusCode()) {
			// Initialiser le cache avec la r�ponse :
			// - type m�dia (variable typeContenu),
			// - version stock�e dans le champ EntityTag (variable version),
			// - contenu obtenu par un appel à Flots.convertirFlotEnOctets
			// (variable contenu).
			cache.typeContenu = reponse.getMediaType();
			cache.version = reponse.getEntityTag();
			cache.contenu = Flots.convertirFlotEnOctets(reponse.getEntityStream());
			// Attention : comme la fonction Flots.convertirFlotEnOctets vide le
			// flux entrant,
			// il est n�cessaire de r�initialiser le flux EntityStream de la
			// r�ponse
			// en utilisant un ByteArrayInputStream.
			reponse.setEntityStream(new ByteArrayInputStream(cache.contenu));
			return;
		}
		// * Statut NOT_MODIFIED (304)
		if (statut == Response.Status.NOT_MODIFIED.getStatusCode()) {
			// Compl�ter la r�ponse en utilisant le cache.
			// - Initialiser le flux entrant EntityStream en utilisant un
			// ByteArrayInputStream construit à partir de cache.contenu.
			// - Initialiser le statut à OK.
			// - Initialiser les en-t�tes suivants :
			// - HttpHeaders.CONTENT_LENGTH
			// - HttpHeaders.CONTENT_TYPE
			reponse.setEntityStream(new ByteArrayInputStream(cache.contenu));
			reponse.setStatus(Response.Status.OK.getStatusCode());
			MultivaluedMap<String, String> entetes = reponse.getHeaders();
			entetes.putSingle(HttpHeaders.CONTENT_LENGTH, Integer.toString(cache.contenu.length));
			entetes.putSingle(HttpHeaders.CONTENT_TYPE, cache.typeContenu.toString());
			return;
		}
		// Cas d'une erreur 412 (PUT)
		if (statut == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
			// Initialiser le cache avec la r�ponse :
			// - type m�dia (variable typeContenu),
			// - version stock�e dans le champ EntityTag (variable version),
			// - contenu obtenu par un appel à Flots.convertirFlotEnOctets
			// (variable contenu).
			cache.typeContenu = reponse.getMediaType();
			cache.version = reponse.getEntityTag();
			cache.contenu = Flots.convertirFlotEnOctets(reponse.getEntityStream());
			// Attention : comme la fonction Flots.convertirFlotEnOctets vide le
			// flux entrant,
			// il est n�cessaire de r�initialiser le flux EntityStream de la
			// r�ponse
			// en utilisant un ByteArrayInputStream.
			reponse.setEntityStream(new ByteArrayInputStream(cache.contenu));
			// Changer le statut de la r�ponse à OK.
			// Lever une erreur 412 dans le gestionnaire d'erreur.
			reponse.setStatus(Response.Status.OK.getStatusCode());
			gestionnaireErreur.leverErreur412();
			return;
		}

	}
}
