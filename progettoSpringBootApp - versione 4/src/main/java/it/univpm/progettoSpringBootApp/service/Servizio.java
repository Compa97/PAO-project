package it.univpm.progettoSpringBootApp.service;

import org.json.simple.*;

public interface Servizio{
	public abstract JSONArray getMetadata();
	public abstract JSONArray getFarmacie();
	//public abstract void deleteFarmacia(Integer id);
	//lasciando alcuni campi vuoti fa poi la ricerca solo sui campi effettivamente riempiti
	public abstract void deleteFarmaciaConditional(JSONObject filter);
	public abstract void deleteFarmaciaLogical(JSONObject filter);
	public abstract JSONObject getStats(String filter);
	public abstract JSONObject getStats(JSONObject filter);
	public abstract JSONArray getFarmacieConditional(JSONObject filter);
	public abstract JSONArray getFarmacieLogical(JSONObject filter);
}