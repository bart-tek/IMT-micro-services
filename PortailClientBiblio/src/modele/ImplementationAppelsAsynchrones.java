package modele;

import java.util.Optional;
import java.util.concurrent.Future;

import javax.ws.rs.container.AsyncResponse;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;

public class ImplementationAppelsAsynchrones {

	// Version asynchrone
	public static Future<Optional<HyperLien<Livre>>> rechercheAsynchroneBibliotheque(Bibliotheque bib, Livre l,
			AsyncResponse ar) {
		Outils.afficherInfoTache("recherche asynchrone");
		Optional<HyperLien<Livre>> h = bib.chercher(l);
		ar.resume(h);
		return null; // Le r�sultat n'importe pas mais permet de typer la fonction
						// c�t� serveur de la m�me mani�re que c�t� client.
						// Fonctionnement d'un appel asynchrone
						// - Le client r�alise un appel de la m�thode distante. Cet appel renvoie
						// imm�diatement
						// une promesse de type Future, qui contient le canal de retour sur lequel la
						// r�ponse arrivera.
						// - Le serveur re�oit la requête correspondante et la traite. Il envoie la
						// r�ponse au client
						// via le canal de retour : voir ar.resume.
						// - Le client peut tester si la promesse est r�alis�e ou non. Elle l'est
						// lorsque la r�ponse
						// du serveur parvient au client.
	}
}
