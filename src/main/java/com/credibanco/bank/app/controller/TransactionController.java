package com.credibanco.bank.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.bank.app.controller.jsondata.CardJsonData;
import com.credibanco.bank.app.entity.Transaccion;
import com.credibanco.bank.app.service.AppBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class TransactionController {

	@Autowired
	private AppBusinessService appService;
	
	/*
	 *  6. Transaccion de Compra
	 * 
	 */
	@PostMapping("/transaction/purchase")
	public ResponseEntity<String> realizarTransaccion(@RequestBody CardJsonData card){
		try {
		Transaccion transResp = appService.crearTransaccion(card.getCardId(),card.getPrice());
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(transResp);
		System.out.println(json);
		return new ResponseEntity<>(json, HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
			String message =  "{\n\terror: " + ex.getMessage() + "\n}";
			return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 *  7. Consultar transacción
	 * 
	 */
	@GetMapping("/transaction/{transactionId}")
	public @ResponseBody String getTransaction(@PathVariable String transactionId) {
		try {
			Transaccion ts = appService.getTransaccionById(transactionId);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(ts);
			return json;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\n\terror: " + ex.getMessage() + "\n}";
		}
	}
	
	
	/*	NIVEL 2
	 *  1. Anular Transaccion
	 * 
	 */
	@PostMapping("/transaction/anulation")
	public ResponseEntity<String> anularTransaccion(@RequestBody CardJsonData card){
		try {
		Transaccion tr = appService.anularTransaccion(card.getCardId(), card.getTransactionId());
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(tr);
		System.out.println(json);
		return new ResponseEntity<>(json, HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			String message =  "{\n\terror: ERROR: " + ex.getMessage() + "\n}";
			return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
