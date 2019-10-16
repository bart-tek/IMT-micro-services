package essai;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

public class AppliCliente {
	public static Client clientJAXRS() {
		ClientConfig config = new ClientConfig();
		return ClientBuilder.newClient(config);
	}

	public static void main(String[] args) {
		WebTarget cible = clientJAXRS().target("http://localhost:8080/testJersey/fr");
		Salut salutation = WebResourceFactory.newResource(Salut.class, cible);

		System.out.println("*************");
		System.out.println(salutation.dire("coco"));

		cible = clientJAXRS().target("http://localhost:8080/testJersey/en");
		salutation = WebResourceFactory.newResource(Salut.class, cible);

		System.out.println("*************");
		System.out.println(salutation.dire("coco"));
	}
}