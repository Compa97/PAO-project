package it.univpm.progettoSpringBootApp.service;
import java.awt.List;
import java.util.ArrayList;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import it.univpm.progettoSpringBootApp.utilities.*;
import it.univpm.progettoSpringBootApp.model.*;


@Service
public class ServiceImpl implements Servizio{
	//numero di farmacie nel file = 1736
	private static ArrayList<Farmacia> datas =new ArrayList<Farmacia>(1736);

	public ServiceImpl() {
		try {
		     Connection connect = new Connection();
		     connect.startConnection();
		     Parsing parser=new Parsing(connect.getData());
		     parser.createJSON();
		     CreaStruct struct = new CreaStruct();
	   	     struct.read();
	   	     datas = struct.getDati();
/* 
		     CreaStruct dati=new CreaStruct();
		     dati.read();
*/
		}catch(Exception e) {
			System.out.println("Si è verificato un errore");
		}
	}

	public JSONArray getFarmacie(){
		//classe per trasformazione string -> JSONObject
		Gson g = new Gson();
		String str = new String();
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		for(Farmacia farm:datas)
		{
			//Farmacia to String
			str = g.toJson(farm);
			//creo JSONObject
			try {
					obj = (JSONObject) parser.parse(str);
			}
			catch(ParseException e)
			{}
			//aggiungo oggetto a JSONArray
			arr.add(obj);
		}
		//ritorniamo JSONArray
		return arr;
		
	}
	
	public boolean deleteFarmacia(JSONObject filter) {
		boolean trov = false;
		Gson g = new Gson();
		String str = new String();
		ArrayList<String> listaChiavi = new ArrayList<String>(filter.keySet());
		String str1 = (String) filter.get(listaChiavi.get(0)); 
		JSONParser parser = new JSONParser();
		JSONObject farm1 = new JSONObject();
		for(Farmacia farm : datas)
		{
			str = g.toJson(farm);
			try {
					farm1 = (JSONObject) parser.parse(str);
			}
			catch(ParseException e)
			{}
			str = (String) farm1.get(listaChiavi.get(0));
			//implementando un vettore di metadati poi basterà scorrerlo
			if(str1.equals(str)) {
				trov = true;
				datas.remove(farm);
			}
		}
		return trov;
	}
	
	
	/*
	public boolean deleteFarmacia(JSONObject filter) {
		boolean trov = false;
		Gson g = new Gson();
		String str = new String();
		String str1 = (String) filter.get("codId"); 
		JSONParser parser = new JSONParser();
		JSONObject farm1 = new JSONObject();
		for(Farmacia farm : datas)
		{
			str = g.toJson(farm);
			try {
					farm1 = (JSONObject) parser.parse(str);
			}
			catch(ParseException e)
			{}
			str = (String) farm1.get("codId");
			//implementando un vettore di metadati poi basterà scorrerlo
			if(str1.equals(str)) {
				trov = true;
				datas.remove(farm);
			}
		}
		return trov;
	}
*/
	public JSONArray getMetadata(){
		Gson g = new Gson();
		String str = new String();
		JSONParser parser = new JSONParser();
		JSONObject farm1 = new JSONObject();
		JSONObject stampa1 = new JSONObject();
		JSONObject app = new JSONObject();
		sourcefield app1= new sourcefield();
		JSONArray array = new JSONArray();
		Farmacia farm2 = datas.get(1);
		str = g.toJson(farm2);
		try {
			farm1 = (JSONObject) parser.parse(str);
		}
		catch(ParseException e)
		{}
		ArrayList<String> listaChiavi = new ArrayList<String>(farm1.keySet());
		metadati[] stampa = new metadati[17];
	    for(int i=0 ;i<17;i++) {
	    	stampa[i] = new metadati(app1.sourceFields[i], "String", listaChiavi.get(i));
	    	str = g.toJson(stampa[i]);
			try {
					stampa1 = (JSONObject) parser.parse(str);
			}
			catch(ParseException e)
			{}
			array.add(stampa1);
	    }
	    return array;
	}
	
	public void getStats(JSONObject filter) {
		ArrayList<String> listaChiavi = new ArrayList<String>(filter.keySet());
		String str = new String();
		Gson g = new Gson();
		JSONObject farm1 = new JSONObject();
		JSONParser parser = new JSONParser();
		int somma = 0, avg = 0, max = 0, min = 0;
		boolean primo = false;
		for(Farmacia farm : datas)
		{
			str = g.toJson(farm);
			try {
					farm1 = (JSONObject) parser.parse(str);
			}
			catch(ParseException e)
			{}	
			str = (String) farm1.get(listaChiavi.get(0));
			int filtro = Integer.parseInt(str);
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
		System.out.println("media:" + avg);
		System.out.println("sum: " + somma);
		System.out.println("min:" + min);
		System.out.println("max:" + max);
		int somma1 =0;
		for(Farmacia farm : datas)
		{
			str = g.toJson(farm);
			try {
					farm1 = (JSONObject) parser.parse(str);
			}
			catch(ParseException e)
			{}	
			str = (String) farm1.get(listaChiavi.get(0));
			int filtro = Integer.parseInt(str);
			somma1+= Math.pow(filtro - avg, 2);
		}
		double devstd = Math.pow((somma1 / datas.size()), 1/2);
		System.out.println("devstd:" + devstd);
/*	
	//all'interno di find è presente una farmacia
	public JSONArray getFarmacie(String find){
		
	}
	
	public void deleteFarmacia(Integer id) {
		
	}
*/
	}	
}	


