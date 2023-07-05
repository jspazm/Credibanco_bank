package com.credibanco.bank.app.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.credibanco.bank.app.entity.Producto;
import com.credibanco.bank.app.entity.Tarjeta;
import com.credibanco.bank.app.entity.Transaccion;
import com.credibanco.bank.app.repository.IProductoComp;
import com.credibanco.bank.app.repository.ITarjetaComp;
import com.credibanco.bank.app.repository.ITransCompl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class AppBusinessService {

	private final IProductoComp productoRepo;
	private final ITarjetaComp tarjetaRepo;
	private final ITransCompl transaccionRepo;

	public AppBusinessService(IProductoComp productoRepo, ITarjetaComp tarjetaRepo, ITransCompl transaccionRepo) {
		super();
		this.productoRepo = productoRepo;
		this.tarjetaRepo = tarjetaRepo;
		this.transaccionRepo = transaccionRepo;
	}

	@Transactional
	public Transaccion getTransaccionById(String tsId) {
		return transaccionRepo.getTransaccionById(tsId);
	}

	@Transactional
	public Producto crearTarjeta(String productId) throws Exception {
		Producto producto = null;

		if (productId.length() != 6) {
			throw new Exception("productId inválido");
		} else {
			producto = new Producto(productId, new java.util.Date());

			Tarjeta tarjeta = new Tarjeta(generateRandNumCard(), generateRandomTipoCard(), generateRandomName(),
					AddYearsToDate(), 0.0, String.valueOf('I'), new java.util.Date());
			tarjeta.setId_tbx_tr(producto.getId_tbx_pr());

			List<Tarjeta> tarjetaList = new ArrayList<Tarjeta>();
			tarjetaList.add(tarjeta);
			tarjetaList.get(0).setProducto(producto);

			producto.setTarjetas(tarjetaList);
			productoRepo.save(producto);

		}

		return producto;
	}

	@Transactional
	public String activarTarjeta(String cardId) throws JsonProcessingException {
		String productId = cardId.substring(0, Math.min(cardId.length(), 6));
		String comp_tarjeta = cardId.substring(cardId.length() - 10);

		Tarjeta tarjeta = tarjetaRepo.findNumCard(productId, comp_tarjeta);
		tarjeta.setEstado(String.valueOf('A'));
		tarjetaRepo.save(tarjeta);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(tarjeta);

		return json;
	}

	@Transactional
	public Tarjeta consultarTarjeta(String cardId) throws JsonProcessingException {
		String productId = cardId.substring(0, Math.min(cardId.length(), 6));
		String comp_tarjeta = cardId.substring(cardId.length() - 10);

		Tarjeta tarjeta = tarjetaRepo.findNumCard(productId, comp_tarjeta);

		return tarjeta;
	}

	@Transactional
	public String bloquearTarjeta(String cardId) throws JsonProcessingException {
		String productId = cardId.substring(0, Math.min(cardId.length(), 6));
		String comp_tarjeta = cardId.substring(cardId.length() - 10);

		Tarjeta tarjeta = tarjetaRepo.findNumCard(productId, comp_tarjeta);
		tarjeta.setEstado(String.valueOf('B'));
		tarjetaRepo.save(tarjeta);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(tarjeta);

		return json;
	}

	@Transactional
	public String recargarSaldo(String cardId, String saldo) throws JsonProcessingException {
		String productId = cardId.substring(0, Math.min(cardId.length(), 6));
		String comp_tarjeta = cardId.substring(cardId.length() - 10);

		Tarjeta tarjeta = tarjetaRepo.findNumCard(productId, comp_tarjeta);
		tarjeta.setSaldo(Double.valueOf(saldo));
		tarjetaRepo.save(tarjeta);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(tarjeta);

		return json;
	}

	public Date AddYearsToDate() {
		LocalDate currentDate = LocalDate.now();

		LocalDate futureDate = currentDate.plusYears(3);
		return Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@Transactional
	public Transaccion crearTransaccion(String cardId, String price) throws Exception {
		String productId = cardId.substring(0, Math.min(cardId.length(), 6));
		String comp_tarjeta = cardId.substring(cardId.length() - 10);
		Tarjeta tarjeta = tarjetaRepo.findNumCard(productId, comp_tarjeta);

		if (tarjeta == null) {
			throw new Exception("No existe tarjeta asociada");
		} else if (!tarjeta.getEstado().equals("A")) {
			throw new Exception("Tarjeta inactiva");
		} else if (getDifferenceHoursDates(tarjeta.getExpires(), new java.util.Date()) > 0) {
			throw new Exception("Tarjeta expirada");
		} else if (tarjeta.getSaldo() <= 0 || (Double.parseDouble(price) > tarjeta.getSaldo())) {
			throw new Exception("No hay saldo en la tarjeta");
		} else {
			Transaccion transaccion = new Transaccion(generateRandNumCard(), Double.valueOf(price),
					new java.util.Date(), String.valueOf('R'));
			transaccion.setTarjeta(tarjeta);
			transaccionRepo.save(transaccion);
			tarjeta.setSaldo(tarjeta.getSaldo() - Double.parseDouble(price));
			tarjetaRepo.save(tarjeta);

			return transaccion;
		}

	}

	@Transactional
	public Transaccion anularTransaccion(String cardId, String transId) throws Exception {
		String productId = cardId.substring(0, Math.min(cardId.length(), 6));
		String comp_tarjeta = cardId.substring(cardId.length() - 10);
		Tarjeta tarjeta = tarjetaRepo.findNumCard(productId, comp_tarjeta);
		Transaccion tr = transaccionRepo.getTransaccionById(transId);
		System.out.println(getDifferenceHoursDates(tr.getFecha_transaccion(), new java.util.Date()));

		if (tarjeta == null) {
			throw new Exception("No existe tarjeta asociada");
		} else if (tr == null) {
			throw new Exception("No existe transaccion");
		} else if (!tr.getEstado_tr().equals(String.valueOf('R'))) {
			throw new Exception("Transaccion anulada");
		} else if (getDifferenceHoursDates(tr.getFecha_transaccion(), new java.util.Date()) >= 24) {
			throw new Exception("Transaccion realizada despues de 24 horas");
		} else {

			tr.setEstado_tr(String.valueOf("C"));
			tr.setFecha_upd(new java.util.Date());
			transaccionRepo.save(tr);

			tarjeta.setSaldo(tarjeta.getSaldo() + tr.getValor_ts());
			tarjetaRepo.save(tarjeta);

			return tr;
		}

	}

	public String generateRandNumCard() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			int randomNumber = random.nextInt(10);
			char randomDigit = (char) (randomNumber + '0');
			sb.append(randomDigit);
		}

		String randomString = sb.toString();

		return randomString;
	}

	public long getDifferenceHoursDates(Date End_date, Date Init_date) {

		long milliseconds = Init_date.getTime() - End_date.getTime();

		// Calculate the number of hours
		long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
		return hours;
	}

	public String generateRandomTipoCard() {
		Random random = new Random();

		char[] characters = { 'C', 'D' };

		int randomIndex = random.nextInt(2);

		char randomCharacter = characters[randomIndex];

		return String.valueOf(randomCharacter);
	}

	public String generateRandomName() {
		Random random = new Random();

		String[] firstNames = { "John", "Emma", "Michael", "Sophia", "James", "Olivia" };
		String[] lastNames = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Davis" };

		String randomFirstName = firstNames[random.nextInt(firstNames.length)];
		String randomLastName = lastNames[random.nextInt(lastNames.length)];

		String randomName = randomFirstName + " " + randomLastName;

		return randomName;
	}

}
