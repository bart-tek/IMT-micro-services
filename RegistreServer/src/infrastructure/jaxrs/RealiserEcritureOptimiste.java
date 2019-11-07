package infrastructure.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import infrastructure.jaxrs.annotations.EcritureOptimiste;
import modele.Versionneur;

@Provider
@EcritureOptimiste
@Priority(Priorities.HEADER_DECORATOR)
public class RealiserEcritureOptimiste implements ContainerRequestFilter {

	private Versionneur ressourceVersionnee;

	@Inject
	public RealiserEcritureOptimiste(Versionneur r) {
		this.ressourceVersionnee = r;
		System.out.println("* Initialisation du filtre " + this + " : " + this.getClass());
		System.out.println("- Partage du versionneur " + this.ressourceVersionnee);
	}

	@Override
	public void filter(ContainerRequestContext requete) throws IOException {
		if (requete.getMethod().equalsIgnoreCase("PUT")) {
			MultivaluedMap<String, String> enTetes = requete.getHeaders();

			String recom = "DOIT contenir l'en-t�te if-match, NE DOIT PAS contenir l'en-t�te if-none-match.";
			String erreur = "erreur " + StatutRFC6585.PRECONDITION_REQUIRED.getCodeStatut();
			String versionClient = enTetes.getFirst(HttpHeaders.IF_NONE_MATCH);
			if (versionClient != null) {
				// Pr�condition if-none-match pr�sente

				String msgErreur = "Requ�te incorrecte - " + erreur + " : pr�-condition requise - " + recom;
				Response rep = Response.status(StatutRFC6585.PRECONDITION_REQUIRED.getCodeStatut())
						.header(HttpHeaders.ETAG, OutilsHttp.etag(ressourceVersionnee.getVersion()))
						.entity(erreur + " - " + recom).build();
				// .entity(ressourceVersionnee.getRessourceMutable()).build();
				requete.abortWith(rep);
				System.out.println("*** " + msgErreur + " ***");
				return;
			}

			versionClient = enTetes.getFirst(HttpHeaders.IF_MATCH);
			if (versionClient == null) {
				// Pas de pr�condition if-match
				String msgErreur = "Requ�te incorrecte - " + erreur + " : pr�-condition requise - " + recom;
				Response rep = Response.status(StatutRFC6585.PRECONDITION_REQUIRED.getCodeStatut())
						.header(HttpHeaders.ETAG, OutilsHttp.etag(ressourceVersionnee.getVersion()))
						.entity(erreur + " - " + recom).build();
				requete.abortWith(rep);
				System.out.println("*** " + msgErreur + " ***");
				return;
			}

			Response.ResponseBuilder rb = requete.getRequest()
					.evaluatePreconditions(OutilsHttp.etag(ressourceVersionnee.getVersion()));
			// Evaluation de la pr�condition / version serveur not�e vs
			// - (if-match (vc1)) pr�sent ? (vc1 = vs) : VRAI
			// - et
			// - (if-none-match (vc2)) pr�sent ? (vc2 != vs) : VRAI
			// Ici : la seconde proposition est n�cessairement vraie, la
			// premi�re proposition se r�duit en (vc1 = vs).

			if (rb != null) {
				// Pr�condition non v�rifi�e :
				String msgErreur = "Transaction à reprendre (" + Response.Status.PRECONDITION_FAILED.getStatusCode()
						+ " - version client : " + versionClient + " - version serveur : "
						+ this.ressourceVersionnee.getVersion() + ")";
				Response rep = rb.entity(ressourceVersionnee.getRessourceMutable()).build();
				requete.abortWith(rep);
				System.out.println("*** " + msgErreur + " ***");
				return;
			}
			ressourceVersionnee.incrementerVersion();
			return;
		}

	}

}
