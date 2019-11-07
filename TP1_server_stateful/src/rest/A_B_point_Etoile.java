package rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("automate")
@Singleton
public class A_B_point_Etoile implements Automate {

	private int numeroSession;
	private Map<Session, Etat> executions;

	public A_B_point_Etoile() {
		System.out.println("D�ploiement de " + this.getClass());
		numeroSession = 0;
		executions = new HashMap<Session, Etat>(25);
	}

	@Override
	public Session initier() {
		ImplemSession s = new ImplemSession();
		s.setNumero(numeroSession);
		numeroSession++;
		executions.put(s, Etat.UN);
		return s;
	}

	@Override
	public Resultat accepter(char x, Session id) {
		Etat e = executions.get(id);
		ImplemResultat res = new ImplemResultat();
		res.setId(id);
		if (e == null) {
			res.setValide(false);
			return res;
		}
		if (e.equals(Etat.UN) && (x == 'a')) {
			executions.put(id, Etat.DEUX);
			res.setValide(true);
			return res;
		}
		if (e.equals(Etat.DEUX) && (x == 'b')) {
			executions.put(id, Etat.UN);
			res.setValide(true);
			return res;
		}
		res.setValide(false);
		return res;
	}

	@Override
	public void clore(Session id) {
		executions.remove(id);
	}
}
