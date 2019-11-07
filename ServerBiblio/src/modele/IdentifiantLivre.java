package modele;

public interface IdentifiantLivre {
	String getId();

	public static IdentifiantLivre valueOf(String str) {
		return new ImplemIdentifiantLivre(str);
	}
}
