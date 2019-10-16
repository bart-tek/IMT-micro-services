package essai;

import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("en")
@Singleton // (chargement unique au premier usage)
public class SalutEn implements Salut {
	public SalutEn() {
		System.out.println("Chargement de " + this.getClass());
	}

	@Override
	public String dire(String x) {
		return "Hello from " + x;
	}
}