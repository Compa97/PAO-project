package it.univpm.progettoSpringBootApp.service;
import java.util.ArrayList;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import it.univpm.progettoSpringBootApp.utilities.*;
import it.univpm.progettoSpringBootApp.model.*;


@Service
public class ServiceImpl implements Servizio{
	//numero di farmacie nel file = 1736
	private static ArrayList<Farmacia> datas =new ArrayList<Farmacia>(1736);
//creare datas
	public ServiceImpl() {
		try {
		     Connection connect = new Connection();
		     connect.startConnection();
		     Parsing parser=new Parsing(connect.getData());
		     parser.createJSON();
		     CreaStruct struct = new CreaStruct();
	   	     struct.read();
	   	     datas = struct.getDati();
		}catch(Exception e) {
			System.out.println("Si è verificato un errore");
		}
	}

	public JSONArray getFarmacie(){
		//classe per trasformazione string -> JSONObject
		Gson g = new Gson();
		String str = new String();
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		for(Farmacia farm:datas)
		{
			//Farmacia to String
			str = g.toJson(farm);
			//creo JSONObject
			obj = stringToJSONObject(str);
			//aggiungo oggetto a JSONArray
			arr.add(obj);
		}
		//ritorniamo JSONArray
		return arr;
		
	}
	
	public JSONObject getStats(JSONObject filter)  {
		//campo contiene il filtro su cui facciamo la statistica
		ArrayList<String> campo;
		campo = prendiChiavi(filter);
		//trovato va a prendere tutte le farmacie che corrispondono alla ricerca
		JSONArray trovato = this.getFarmacieConditional(filter);
		double somma = 0 , min = 0, max = 0;
		boolean primo = false;
		for(int i=0; i < trovato.size() ; i++)
		{
			//prendo il campo all'interno di ogni elemento del vettore filtrato
			JSONObject appo = (JSONObject) trovato.get(i);
			String str = (String) appo.get(campo.get(0));
			double filtro = Double.parseDouble(str);
			somma+=filtro;
			if(primo == false){
				primo = true;
				max = filtro;
				min = filtro;
			}
			else
			{
				if(filtro > max)
					max = filtro;
				if(filtro < min)
					min = filtro;
			}
		}
		double avg = somma / trovato.size();
		double somma1 =0;
		for(int i=0; i < trovato.size() ; i++)
		{
			JSONObject appo = (JSONObject) trovato.get(i);
			String str = (String) appo.get(campo.get(0));
			double filtro = Double.parseDouble(str);
			somma1+= Math.pow(filtro - avg, 2);
		}
		double devstd = Math.pow((somma1 / trovato.size()), 1/2);
		String statistiche = "{\"media\":"+ "\"" + Double.toString(avg)+ "\"" + ", \"sum\":" + "\"" + Double.toString(somma)+ "\"" + ", \"min\":" + "\"" + Double.toString(min)+ "\"" + ", \"max\":" + "\"" + Double.toString(max)+ "\"" + ", \"Deviazione Standard\":" + "\"" + Double.toString(devstd)+ "\"" + "}";
		JSONObject json= new JSONObject();	
		json = stringToJSONObject(statistiche);
		return json;
	}
	
	public void deleteFarmaciaConditional(JSONObject filter) {
		JSONArray trovato = this.getFarmacieConditional(filter);
		Gson g = new Gson();
		for(int i=0; i < trovato.size() ; i++)
		{
			Farmacia app = g.fromJson(trovato.get(i).toString(),Farmacia.class);
			datas.remove(app);
		}
	}
	
	public void deleteFarmaciaLogical(JSONObject filter) {
		JSONArray trovato = this.getFarmacieLogical(filter);
		Gson g = new Gson();
		for(int i=0; i < trovato.size() ; i++)
		{
			Farmacia app = g.fromJson(trovato.get(i).toString(),Farmacia.class);
			datas.remove(app);
		}
	}
	
	public JSONArray getMetadata(){
		Gson g = new Gson();
		String str = new String();
		JSONObject farm1 = new JSONObject();
		JSONObject stampa1 = new JSONObject();
		sourcefield app1= new sourcefield();
		JSONArray array = new JSONArray();
		Farmacia farm2 = datas.get(1);
		str = g.toJson(farm2);
		farm1 = stringToJSONObject(str);
		ArrayList<String> listaChiavi;
		listaChiavi = prendiChiavi(farm1);
		metadati[] stampa = new metadati[17];
	    for(int i=0 ;i<17;i++) {
	    	stampa[i] = new metadati(app1.sourceFields[i], "String", listaChiavi.get(i));
	    	str = g.toJson(stampa[i]);
			stampa1 = stringToJSONObject(str);
			array.add(stampa1);
	    }
	    return array;
	}
	
	public JSONObject getStats(String campo)  {
		System.out.println("Campo richiesto" + campo);
		Gson g = new Gson();
		JSONObject farm1 = new JSONObject();
		double somma = 0,avg=0, max = 0, min = 0;
		boolean primo = false;
		for(Farmacia farm : datas)
		{
			String str = g.toJson(farm); //converto farmacia in una stringa json
			farm1 = stringToJSONObject(str);
			str = (String) farm1.get(campo); //prendo il contenuto del campo richiesto
			double filtro = Double.parseDouble(str);
			somma+=filtro;
			if(primo == false)
			{
				primo = true;
				max = filtro;
				min = filtro;
			}
			else {
				if(filtro > max)
					max = filtro;
				if(filtro < min)
					min = filtro;
			}
		}
		avg = somma / datas.size();
		double somma1 =0;
		for(Farmacia farm : datas)
		{
			String str = g.toJson(farm);
			farm1 = stringToJSONObject(str);	
			str = (String) farm1.get(campo);
			double filtro = Double.parseDouble(str);
			somma1+= Math.pow(filtro - avg, 2);
		}
		double devstd = Math.pow((somma1 / datas.size()), 1/2);
		String statistiche = "{\"media\":"+ "\"" + Double.toString(avg)+ "\"" + ", \"sum\":" + "\"" + Double.toString(somma)+ "\"" + ", \"min\":" + "\"" + Double.toString(min)+ "\"" + ", \"max\":" + "\"" + Double.toString(max)+ "\"" + ", \"Deviazione Standard\":" + "\"" + Double.toString(devstd)+ "\"" + "}";
		JSONObject json= new JSONObject();
		json = stringToJSONObject(statistiche);
		//System.out.println(json);
		return json;
	}
	
	public JSONArray getFarmacieConditional(JSONObject filter) {
		Gson g = new Gson();
		String str = new String();
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		JSONObject farm1 = new JSONObject();
		Object str1 = new Object();
		ArrayList<String> listaChiavi = prendiChiavi(filter);
		str1= filter.get(listaChiavi.get(0));//ho il valore associato alla chiave cap
		String prova = g.toJson(str1); //converto il valore in una stringa json
		farm1 = stringToJSONObject(prova);
		ArrayList<String> filter1 = prendiChiavi(farm1);
		String str2 = (String) farm1.get(filter1.get(0));//Qui ho il parametro passato con Postman
		 if(filter1.get(0).equals("$gt")) {
			for(Farmacia farm:datas)
			{
				//Farmacia to String
				str = g.toJson(farm);
				//creo JSONObject
				farm1 = stringToJSONObject(str);
	 			str = (String) farm1.get(listaChiavi.get(0));//ho il valore da inserire in compareTo
				if(str.compareTo(str2)>0) {
					arr.add(farm1);
				}
			}
		 }
		 if(filter1.get(0).equals("$lt")) {
				for(Farmacia farm:datas)
				{
					//Farmacia to String
					str = g.toJson(farm);
					//creo JSONObject
					farm1 = stringToJSONObject(str);
		 			str = (String) farm1.get(listaChiavi.get(0));
					if(str.compareTo(str2)<0) {
						arr.add(farm1);
					}
				}
			}
		return arr;	
	}
	
	public JSONArray getFarmacieLogical(JSONObject filter) {
		Gson g = new Gson();
		String str = new String();
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		JSONArray farm1 = new JSONArray();
		JSONObject farm3 = new JSONObject();
		JSONObject farm2 = new JSONObject();
		Object str1 = new Object();
		ArrayList<String> listaChiavi = prendiChiavi(filter);
		str1= filter.get(listaChiavi.get(0));
		String prova = g.toJson(str1);
		try {
				farm1 = (JSONArray) parser.parse(prova);
		}
		catch(ParseException e)
		{}
		farm3 = (JSONObject) farm1.get(0);
		JSONObject farm4 = (JSONObject) farm1.get(1);
		ArrayList<String> nomeParametro1 = prendiChiavi(farm3);
		ArrayList<String> nomeParametro2 = prendiChiavi(farm4);
		String paramFarm3 = (String) farm3.get(nomeParametro1.get(0));
		String paramFarm4 = (String) farm4.get(nomeParametro2.get(0));
		if(listaChiavi.get(0).equals("$and"))
		{
			for(Farmacia farm:datas)
			{
					//Farmacia to String
					str = g.toJson(farm);
					//creo JSONObject
					farm2 = stringToJSONObject(str);
					if(paramFarm3.compareTo((String) farm2.get(nomeParametro1.get(0))) == 0 && 
							paramFarm4.compareTo((String) farm2.get(nomeParametro2.get(0))) == 0)
						arr.add(farm2);
			}
		}
		else if(listaChiavi.get(0).equals("$or")) {
			for(Farmacia farm:datas)
			{
					//Farmacia to String
					str = g.toJson(farm);
					//creo JSONObject
					farm2 = stringToJSONObject(str);
					if(paramFarm3.compareTo( (String) farm2.get(nomeParametro1.get(0))) == 0 || 
							paramFarm4.compareTo( (String) farm2.get(nomeParametro2.get(0))) == 0)
						arr.add(farm2);
			}
		}
		return arr;	
	}	

	public JSONObject stringToJSONObject (String str) {
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		try {
			obj = (JSONObject) parser.parse(str);
		}
		catch(ParseException e)
		{
			// throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body inserito errato");
		}
		return obj;
	}
	
	public ArrayList<String> prendiChiavi(JSONObject obj)
	{
		ArrayList<String> listaChiavi;
		return listaChiavi = new ArrayList<String>(obj.keySet());
	}
}	


