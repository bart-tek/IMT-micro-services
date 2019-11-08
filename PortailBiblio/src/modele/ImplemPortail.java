package modele;

import static configuration.JAXRS.CHEMIN_PORTAIL;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.container.AsyncResponse;

import configuration.Initialisation;
import configuration.Orchestrateur;
import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.HyperLiens;
import infrastructure.jaxrs.LienVersRessource;

@Singleton
@Path(CHEMIN_PORTAIL)
public class ImplemPortail implements Portail {

	private ConcurrentMap<NomAlgorithme, AlgorithmeRecherche> table;
	private Client client;
	private AlgorithmeRecherche algo;
	private List<HyperLien<BibliothequeArchive>> bibliotheques;

	public ImplemPortail() {

		table = new ConcurrentHashMap<NomAlgorithme, AlgorithmeRecherche>();
		client = Orchestrateur.clientJAXRS();
		algo = null;
		bibliotheques = Initialisation.bibliotheques();

		AlgorithmeRecherche algo1 = new RechercheSynchroneSequentielle();
		table.put(algo1.nom(), algo1);

		AlgorithmeRecherche algo2 = new RechercheSynchroneMultiTaches();
		table.put(algo2.nom(), algo2);

		AlgorithmeRecherche algo3 = new RechercheSynchroneStreamParallele();
		table.put(algo3.nom(), algo3);

		AlgorithmeRecherche algo4 = new RechercheSynchroneStreamRx();
		table.put(algo4.nom(), algo4);

		AlgorithmeRecherche algo5 = new RechercheAsynchroneSequentielle();
		table.put(algo5.nom(), algo5);

		AlgorithmeRecherche algo6 = new RechercheAsynchroneMultiTaches();
		table.put(algo6.nom(), algo6);

		AlgorithmeRecherche algo7 = new RechercheAsynchroneStreamParallele();
		table.put(algo7.nom(), algo7);

		AlgorithmeRecherche algo8 = new RechercheAsynchroneStreamRx();
		table.put(algo8.nom(), algo8);

	}

	@Override
	public Future<Optional<HyperLien<Livre>>> chercherAsynchrone(Livre l, AsyncResponse ar) {

		for (HyperLien<BibliothequeArchive> lien : bibliotheques) {
			BibliothequeArchive bib = LienVersRessource.proxy(client, lien, BibliothequeArchive.class);
			ImplementationAppelsAsynchrones.rechercheAsynchroneBibliotheque(bib, l, ar);
		}

		return null;
	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre l) {
		return algo.chercher(l, bibliotheques, client);
	}

	@Override
	public HyperLiens<Livre> repertorier() {

		Stream<HyperLien<BibliothequeArchive>> stream = bibliotheques.stream();

		Stream<BibliothequeArchive> streamBiblio = stream
				.map(item -> LienVersRessource.proxy(client, item, BibliothequeArchive.class));
		Stream<List<HyperLien<Livre>>> streamListeHyperlienLivre = streamBiblio
				.map(item -> item.repertorier().getLiens());

		List<HyperLien<Livre>> listeHyperlienLivre = streamListeHyperlienLivre
				.reduce((x, y) -> Stream.concat(x.stream(), y.stream()).collect(Collectors.toList())).get();

		return new HyperLiens<Livre>(listeHyperlienLivre);

	}

	@Override
	public void changerAlgorithmeRecherche(NomAlgorithme nom) {
		if (table.containsKey(nom)) {
			this.algo = table.get(nom);
		}
	}
}
