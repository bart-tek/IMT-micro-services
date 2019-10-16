package essai;

import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("fr")
@Singleton // (chargement unique au premier usage)
public class SalutFr implements Salut {
	public SalutFr() {
		System.out.println("Chargement de " + this.getClass());
	}

	@Override
	public String dire(String x) {
		return "Salut de " + x;
	}
}