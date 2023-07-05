package com.credibanco.bank.app.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tarjeta")
public class Tarjeta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_tbx_tr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pr_product_id")
	@JsonBackReference
	private Producto producto;
	private String card_num_comp;
	@Transient
	private String cardId;
	@Transient
	private String exp;
	private String tipo_card;
	private String customer;
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date expires;
	private Double saldo;
	private String estado;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date fecha_crea;
	@OneToMany(mappedBy = "tarjeta")
	@JsonManagedReference
	@JsonIgnore
	private List<Transaccion> transacciones;

	public Tarjeta() {
	}

	public Tarjeta(String card_num_comp, String tipo_card, String customer, Date expires, Double saldo, String estado,
			Date fecha_crea) {
		super();
		this.card_num_comp = card_num_comp;
		this.tipo_card = tipo_card;
		this.customer = customer;
		this.expires = expires;
		this.saldo = saldo;
		this.estado = estado;
		this.fecha_crea = fecha_crea;
	}

	public Long getId_tbx_tr() {
		return id_tbx_tr;
	}

	public void setId_tbx_tr(Long id_tbx_tr) {
		this.id_tbx_tr = id_tbx_tr;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getCard_num_comp() {
		return card_num_comp;
	}

	public void setCard_num_comp(String card_num_comp) {
		this.card_num_comp = card_num_comp;
	}

	public String getTipo_card() {
		return tipo_card;
	}

	public void setTipo_card(String tipo_card) {
		this.tipo_card = tipo_card;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha_crea() {
		return fecha_crea;
	}

	public void setFecha_crea(Date fecha_crea) {
		this.fecha_crea = fecha_crea;
	}

	public String getExp() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");
		String formattedDate = formatter.format(getExpires());
		exp = formattedDate;
		return exp;
	}

	public void setExp(String expira) {
		this.exp = expira;
	}

	public String getCardId() {
		cardId = producto.getProduct_id() + getCard_num_comp();
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public List<Transaccion> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(List<Transaccion> transacciones) {
		this.transacciones = transacciones;
	}

}
