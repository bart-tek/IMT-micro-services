package modele;

import static configuration.JAXRS.SOUSCHEMIN_TITRE;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "livre")
public interface Livre {

	@GET
	@Path(SOUSCHEMIN_TITRE)
	String getTitre();

	public static Livre fromString(String str) {
		return new ImplemLivre(str);
	}
}
