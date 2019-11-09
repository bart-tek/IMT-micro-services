package test_portail;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.junit.Assert;

import configuration.JAXRS;
import configuration.Orchestrateur;
import infrastructure.jaxrs.HyperLien;
import modele.ImplemLivre;
import modele.ImplemNomAlgorithme;
import modele.Livre;
import modele.Portail;

public class TestPortail {

	private static final String ADRESSE = JAXRS.ADRESSE_PORTAIL + "/" + JAXRS.CHEMIN_PORTAIL;

	public static void main(String[] args) {

		System.out.println("*************");

		WebTarget cible = Orchestrateur.clientJAXRS().target(ADRESSE);

		Portail portail = WebResourceFactory.newResource(Portail.class, cible);

		HyperLien<Livre> expected = new HyperLien<Livre>("http://localhost:8095/bib5/bibliotheque/6");

		// creer un livre à chercher
		Livre l1 = new ImplemLivre("Services5.6");

		for (NomDesAlgos nom : NomDesAlgos.values()) {

			// changer l'algo
			portail.changerAlgorithmeRecherche(new ImplemNomAlgorithme(nom.toString()));

			// chercher sync
			Instant startSync = Instant.now();
			Optional<HyperLien<Livre>> lienRecupSync = portail.chercher(l1);
			Instant stopSync = Instant.now();

//			// chercher async
//			Instant startAsync = Instant.now();
//			Future<Optional<HyperLien<Livre>>> lienRecupAsync = portail.chercherAsynchrone(l1,
//					new AsyncResponder());
//			Instant stopAsync = Instant.now();
//
			Assert.assertEquals(expected.getUri(), lienRecupSync.get().getUri());
//			try {
//				Assert.assertEquals(expected.getUri(), lienRecupAsync.get().get().getUri());
//			} catch (InterruptedException | ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			System.out.println("Nom de l'algo : " + nom.toString());
			System.out.println(
					"Temps de recherche en synchrone : " + Duration.between(startSync, stopSync).toMillis() + "\n\n\n");
//			System.out.println("Temps de recherche en asynchrone : "
//					+ Duration.between(startAsync, stopAsync).toMillis() + "\n\n\n");

		}

		System.exit(0);
	}

}
