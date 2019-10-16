package client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.Automate;
import rest.Resultat;
import rest.Session;

public class AutomateProxy implements Automate {
	private WebTarget cibleInitier;
	private WebTarget cibleAccepter;
	private WebTarget cibleClore;
	private MediaType typeContenu;

	public AutomateProxy(String uriBase, MediaType typeContenu) {
		WebTarget cible = AppliCliente.clientJAXRS().target(uriBase);
		cibleInitier = cible.path("etat/initial");
		cibleAccepter = cible.path("etat/suivant");
		cibleClore = cible.path("fin");
		this.typeContenu = typeContenu;
	}

	@Override
	public Session initier() {

		Builder builder = cibleInitier.request(typeContenu);
		Response rep = builder.post(Entity.entity(Session.class, typeContenu));
		return rep.readEntity(Session.class);

	}

	@Override
	public Resultat accepter(char x, Session id) {

		WebTarget newTarget = cibleAccepter.path("/" + x);
		Builder builder = newTarget.request(typeContenu);
		Resultat rep = builder.post(Entity.entity(id, typeContenu), Resultat.class);
		return rep;
	}

	@Override
	public void clore(Session id) {
		Builder builder = cibleClore.request(typeContenu);
		builder.put(Entity.entity(id, typeContenu));
	}

}
