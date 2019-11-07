package modele;

public interface IdentifiantLivre {
	String getId();

	public static IdentifiantLivre fromString(String str) {
		return new ImplemIdentifiantLivre(str);
	}
}
