package com.credibanco.bank.app.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Transaccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_tbx_ts;
	private String transaction_id;
	private Double valor_ts;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cr_card_id")
	@JsonBackReference
	private Tarjeta tarjeta;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date fecha_transaccion;
	private String estado_tr;
	@JsonIgnore
	private Date fecha_upd;

	public Transaccion() {
	}

	public Transaccion(String transaction_id, Double valor_ts, Date fecha_transaccion, String estado_tr) {
		super();
		this.transaction_id = transaction_id;
		this.fecha_transaccion = fecha_transaccion;
		this.estado_tr = estado_tr;
		this.valor_ts = valor_ts;
	}

	public Long getId_tbx_ts() {
		return id_tbx_ts;
	}

	public void setId_tbx_ts(Long id_tbx_ts) {
		this.id_tbx_ts = id_tbx_ts;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Tarjeta getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Date getFecha_transaccion() {
		return fecha_transaccion;
	}

	public void setFecha_transaccion(Date fecha_transaccion) {
		this.fecha_transaccion = fecha_transaccion;
	}

	public String getEstado_tr() {
		return estado_tr;
	}

	public void setEstado_tr(String estado_tr) {
		this.estado_tr = estado_tr;
	}

	public Date getFecha_upd() {
		return fecha_upd;
	}

	public void setFecha_upd(Date fecha_upd) {
		this.fecha_upd = fecha_upd;
	}

	public Double getValor_ts() {
		return valor_ts;
	}

	public void setValor_ts(Double valor_ts) {
		this.valor_ts = valor_ts;
	}
	

}
