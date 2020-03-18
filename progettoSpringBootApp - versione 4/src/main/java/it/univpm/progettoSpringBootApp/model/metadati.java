package it.univpm.progettoSpringBootApp.model;


public class metadati {
	private String sourceField;
	private String aliases;
	private String tipo;
	
	public metadati(String s, String t, String a) {
		this.sourceField = s;
		this.tipo = t;
		this.aliases =a;
	}

}
