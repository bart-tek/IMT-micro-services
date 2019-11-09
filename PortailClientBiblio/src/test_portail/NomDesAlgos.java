package test_portail;

public enum NomDesAlgos {

	SYNC_SEQ("recherche sync seq"), SYNC_MULTI("recherche sync multi"), SYNC_STREAM("recherche sync stream 8"),
	SYNC_RX("recherche sync stream rx"), ASYNC_SEQ("recherche async seq"), ASYNC_MULTI("recherche async multi"),
	ASYNC_STREAM("recherche async stream 8"), ASYNC_RX("recherche async stream rx");

	private String name = "";

	NomDesAlgos(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
