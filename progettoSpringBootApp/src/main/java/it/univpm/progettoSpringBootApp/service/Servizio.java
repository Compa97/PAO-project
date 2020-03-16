package it.univpm.progettoSpringBootApp.service;

import org.json.simple.*;

public interface Servizio{
	public abstract JSONArray getMetadata();
	public abstract JSONArray getFarmacie();
	//public abstract void deleteFarmacia(Integer id);
	//lasciando alcuni campi vuoti fa poi la ricerca solo sui campi effettivamente riempiti
	public abstract boolean deleteFarmacia(JSONObject filter);
	public abstract void getStats(JSONObject filter);
	
}