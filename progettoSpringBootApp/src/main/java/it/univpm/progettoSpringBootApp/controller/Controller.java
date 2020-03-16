package it.univpm.progettoSpringBootApp.controller;

import org.json.simple.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.univpm.progettoSpringBootApp.service.ServiceImpl;
import it.univpm.progettoSpringBootApp.service.Servizio;





@RestController
public class Controller {
	@Autowired
	Servizio servizio;
	

	@GetMapping("/dati")
	public JSONArray metodo1() {
		return servizio.getFarmacie();
	}

	@RequestMapping(value = "/dati", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteFarmacia(@RequestBody JSONObject filter)
	{
		boolean trov;
		trov = servizio.deleteFarmacia(filter);
		if(trov == false)
			return new ResponseEntity<>("Il prodotto non è stato eliminato", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<>("Il prodotto è stato eliminato", HttpStatus.OK);
	}
	
	@GetMapping("/metadati")
	public JSONArray metodo2() {
		return servizio.getMetadata();
	}
	
	@GetMapping("/stats")
	public void metodo3(@RequestBody JSONObject filter) {
		servizio.getStats(filter);
	}
}
