package client;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import configuration.JAXRS;
import infrastructure.jaxrs.ClientRessource;
import infrastructure.jaxrs.HyperLien;
import modele.BibliothequeArchive;
import modele.ImplemLivre;
import modele.Livre;

public class TestPerso {

	private static final String ADRESSE = JAXRS.SERVEUR + JAXRS.CHEMIN;

	public static void main(String[] args) {

		System.out.println("*************");

		WebTarget cible = JAXRS.client().target(ADRESSE);
		BibliothequeArchive biblio = WebResourceFactory.newResource(BibliothequeArchive.class, cible);

		System.out.println("Biblio (proxy) : " + Proxy.isProxyClass(biblio.getClass()));

		System.out.println("*** 1. Ajouter des livres ***");

		List<Livre> listeLivre = new ArrayList<>();
		List<HyperLien<Livre>> listeHyperliens = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Livre livre = new ImplemLivre("Livre " + i);
			HyperLien<Livre> hyperLien = null;
			hyperLien = biblio.ajouter(livre); // POST 1
			listeLivre.add(livre);
			listeHyperliens.add(hyperLien);
			System.out.println("POST " + i + ":" + hyperLien.getClass());
			System.out.println("POST " + i + " - uri : " + hyperLien.getUri());
		}

		System.out.println("*** 2. Chercher un livre ***");

		System.out.println("GET 1 - uri : " + biblio.chercher(listeLivre.get(0)));
		System.out.println("GET 2 - livre absent : " + biblio.chercher(new ImplemLivre("absent")));

		System.out.println("*** 3. Récupérer un livre à partir de l'hyperlien ***");

		Livre l3 = ClientRessource.proxy(listeHyperliens.get(0), Livre.class);
		System.out.println("Classe du proxy : " + l3.getClass());
		String titre3 = l3.getTitre();
		System.out.println("GET 3 (titre : 2.0) : " + titre3);
		Livre l3b = ClientRessource.representation(listeHyperliens.get(0), Livre.class, JAXRS.OBJET_TYPE_MEDIA);
		System.out.println("GET 4 (ImplemLivre) : " + l3b.getClass());
		System.out.println("livre (2.0) : " + l3b);

		System.exit(0);
	}

}
