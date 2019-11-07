package hellocucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

class Cellule {

	public String result(String saisie) {

		ArrayList<Integer> op = new ArrayList<>();
		int ret = 4;

		if (saisie.startsWith("=")) {

			ret = 0;

			saisie = saisie.replaceAll("=", "");
			saisie = saisie.trim();
			String[] expression = saisie.split("\\+");

			for (String a : expression) {
				ret = ret + Integer.parseInt(a);
			}

			return String.valueOf(ret);
		} else {
			return "4";
		}
	}
}

public class Stepdefs {

	private Cellule cell;
	private String saisie;
	private String resultat;

	@Given("une cellule de tableur")
	public void une_cellule_de_tableur() {
		// Write code here that turns the phrase above into concrete actions
		cell = new Cellule();
	}

	@When("Je saisi {string}")
	public void je_saisi(String saisie) {
		// Write code here that turns the phrase above into concrete actions
//		assertEquals("= 1+3", saisie);
		this.saisie = saisie;
	}

	@Then("La cellule affiche {string}")
	public void la_cellule_affiche(String resultat) {
		assertEquals(resultat, cell.result(saisie));
		this.resultat = resultat;
	}
}