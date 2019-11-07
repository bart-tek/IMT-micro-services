package infrastructure.jaxrs;


import infrastructure.jaxrs.annotations.CacheClient;
import modele.Versionneur;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider 
@CacheClient 
@Priority(Priorities.HEADER_DECORATOR - 10)
public class Cacher implements ContainerRequestFilter {

	private Versionneur ressourceVersionnee;

	@Inject
	public Cacher(Versionneur r) {
		this.ressourceVersionnee = r;
		System.out
				.println("* Initialisation du filtre " + this + " : " + this.getClass());
		System.out
		.println("- Partage du versionneur " + this.ressourceVersionnee);
	}

	@Override
	public void filter(ContainerRequestContext requete) throws IOException {
		if (requete.getMethod().equalsIgnoreCase("GET")) {
			
			MultivaluedMap<String, String> enTetes = requete.getHeaders();
			String versionClient = enTetes.getFirst(HttpHeaders.IF_MATCH);
			if(versionClient != null){
				// Pr�condition if-match pr�sente au lieu de if-none-match
				String recom = "NE DOIT PAS contenir l'en-t�te if-match, DEVRAIT contenir l'en-t�te if-none-match.";
				String msgErreur = 
						"Requ�te incorrecte - erreur 428 : pr�-condition requise - " + recom;
				Response rep = Response
						.status(StatutRFC6585.PRECONDITION_REQUIRED.getCodeStatut())
						.header(HttpHeaders.ETAG, OutilsHttp.etag(ressourceVersionnee.getVersion()))
						.entity("erreur 428 - " + recom).build();
						//.entity(ressourceVersionnee.getRessourceMutable()).build(); // Une alternative
				requete.abortWith(rep);
				System.out.println("*** " + msgErreur + " ***");
				return;
			}
			
			// Evaluation de la pr�condition / version serveur not�e vs
			//   - (if-match (vc1)) pr�sent ? (vc1 = vs) : VRAI
			//   - et
			//   - (if-none-match (vc2)) pr�sent ? (vc2 != vs) : VRAI
			// Ici : la premi�re proposition est n�cessairement vraie, la seconde proposition donne donc le r�sultat.
			// En l'absence de if-none-match, la requ�te est trait�e normalement, en oubliant le cache.		    
			EntityTag e = OutilsHttp.etag(ressourceVersionnee.getVersion());
			Response.ResponseBuilder rb = requete	
					.getRequest().evaluatePreconditions(e);
			if (rb != null){
				// pr�condition non v�rifi�e : 
				Response rep = rb.build();
				requete.abortWith(rep);
				System.out.println("*** R�ponse en cache côt� client ***");
				return;
			}
			// pr�condition v�rifi�e
			return;
		}
	}

}
