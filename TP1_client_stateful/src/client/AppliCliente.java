package client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import rest.Automate;
import rest.Resultat;
import rest.Session;
import rest.jaxb.FournisseurTraduction;

public class AppliCliente {

	public static Client clientJAXRS() {
		ClientConfig config = new ClientConfig();
		config.register(LoggingFeature.class);
		config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
		config.register(FournisseurTraduction.class);
		config.register(JacksonFeature.class);
		return ClientBuilder.newClient(config);
	}

	public static void main(String[] args) {

		String adresse = "http://localhost:8080/TP1_server_stateful/automate";

		System.out.println("*************");

//		WebTarget target = clientJAXRS().target(adresse);
//		Automate automate = WebResourceFactory.newResource(Automate.class, target);
		AutomateProxy automateProxy = new AutomateProxy(adresse, MediaType.APPLICATION_JSON_TYPE);
//		test(automate);
		test(automateProxy);

		System.out.println("*************");

	}

	private static void test(Automate automate) {

		char[] mot = { 'a', 'b', 'a', 'a', 'a', 'b' };
		Resultat res = null;

		Session session = automate.initier();

		for (char c : mot) {
			res = automate.accepter(c, session);
		}

		if (res.isValide()) {
			System.out.println("The word \"" + String.valueOf(mot) + "\" is valid");
		} else {
			System.out.println("The word \"" + String.valueOf(mot) + "\" is invalid");
		}

		automate.clore(session);
	}
}
