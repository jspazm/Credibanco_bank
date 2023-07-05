package com.credibanco.bank.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.bank.app.controller.jsondata.CardJsonData;
import com.credibanco.bank.app.entity.Producto;
import com.credibanco.bank.app.entity.Tarjeta;
import com.credibanco.bank.app.service.AppBusinessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class CardController {

	@Autowired
	private AppBusinessService appService;

	/*
	 *  1. Generar Numero de Tarjeta
	 * 
	 */
	@GetMapping("/card/{productId}/number")
	public @ResponseBody String generateNumCard(@PathVariable String productId) {
		try {
			Producto prodResp = appService.crearTarjeta(productId);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(prodResp.getTarjetas().get(0));
			return json;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return "{\n\terror: " + ex.getMessage() + "\n}";
		}
	}
	
	/*
	 *  2. Activar tarjeta
	 * 
	 */
	@PostMapping("/card/enroll")
	public ResponseEntity<String> activarTarjeta(@RequestBody CardJsonData card) throws JsonProcessingException{
		String tarjeta = appService.activarTarjeta(card.getCardId());
		System.out.println(tarjeta);
		return new ResponseEntity<>("Tarjeta activada exitosamente", HttpStatus.OK);
	}
	
	/*
	 *  3. Bloquear tarjeta
	 * 
	 */
	@DeleteMapping("/card/{cardId}")
    public ResponseEntity<String> bloquearTarjeta(@PathVariable String cardId) throws JsonProcessingException {
        String tarjeta = appService.bloquearTarjeta(cardId);
        System.out.println(tarjeta);
        return ResponseEntity.ok("Tarjeta bloqueada Exitosamente");
    }
	
	/*
	 *  4. Recargar saldo
	 * 
	 */
	@PostMapping("/card/balance")
	public ResponseEntity<String> recargarSaldoTarjeta(@RequestBody CardJsonData card) throws JsonProcessingException{
		String tarjeta = appService.recargarSaldo(card.getCardId(), card.getBalance());
		System.out.println(tarjeta);
		
		return new ResponseEntity<>(tarjeta, HttpStatus.OK);
	}
	
	/*
	 *  5. Consulta de Saldo
	 * 
	 */
	@GetMapping("/card/balance/{cardId}")
	public @ResponseBody String consultarSaldoTarjeta(@PathVariable String cardId) {
		try {
			Tarjeta tarjeta = appService.consultarTarjeta(cardId);
			return "{\n\tcardId: "+tarjeta.getCardId()+",\n\tbalance :"+tarjeta.getSaldo()+"\n}";

		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\n\tmessage: ERROR: " + ex.getMessage() + "\n}";
		}
	}
	
}
