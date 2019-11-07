package infrastructure.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class AjouterPrecondition implements ClientRequestFilter {

	
	private Cache cache;

	@Inject
	public AjouterPrecondition(Cache c) {
		cache = c;
		System.out.println("Filtre initialis� " + this + " : " + this.getClass());
		System.out.println("- Cache partag� : " + this.cache);
	}

	@Override
	/*
	 * Filtre / requ�te.
	 */
	public synchronized void filter(ClientRequestContext requete)
			throws IOException {

		// Premier cas : requ�te PUT
		if (requete.getMethod().equalsIgnoreCase("PUT")) {
			// Si la version est d�finie dans le cache, l'affecter au champ
			// HttpHeaders.IF_MATCH
			// de l'en-t�te de la requ�te.
			// Cf. ClientRequestContext.getHeaders().
			if (cache.version != null) {
				MultivaluedMap<String, Object> enTetes = requete.getHeaders();
				enTetes.putSingle(HttpHeaders.IF_MATCH,
						cache.version);
			}
			return;
		}
		// Second cas : requ�te GET
		if (requete.getMethod().equalsIgnoreCase("GET")) {
			// Si la version est d�finie dans le cache, l'affecter au champ
			// HttpHeaders.IF_NONE_MATCH
			// de l'en-t�te de la requ�te.
			// Cf. ClientRequestContext.getHeaders().
			if (cache.version != null) {
				MultivaluedMap<String, Object> enTetes = requete.getHeaders();
				enTetes.putSingle(HttpHeaders.IF_NONE_MATCH,
						cache.version);
			}
			return;
		}
	}

}
