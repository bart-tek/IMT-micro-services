package client;

import java.util.stream.Stream;

import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import rest.Automate;

public class TestConcurrence {

	public static void main(String[] args) {
		String adresse = "http://localhost:8080/TP1_server_stateful/automate/concurrent";
		System.out.println("*************");

		WebTarget cible = AppliCliente.clientJAXRS().target(adresse);
		Automate automateProxyJersey = WebResourceFactory.newResource(Automate.class, cible);

		int REQUETES = 10000;
		final long incrementations = Stream.generate(() -> 0).limit(REQUETES).parallel()
				.map(j -> automateProxyJersey.initier().getNumero()).distinct().count();

		System.out.println("Pertes en �criture : " + (REQUETES - incrementations) + "/ " + REQUETES + " requ�tes.");
		System.out.println("*************");
	}

}
