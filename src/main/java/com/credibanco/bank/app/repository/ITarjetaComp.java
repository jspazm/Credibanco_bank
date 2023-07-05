package com.credibanco.bank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.credibanco.bank.app.entity.Tarjeta;

@Repository
public interface ITarjetaComp extends JpaRepository<Tarjeta, Long>{ 
	@Query("SELECT t FROM Tarjeta t JOIN t.producto p WHERE p.product_id = :prodId AND t.card_num_comp = :car_comp")
    Tarjeta findNumCard(String prodId, String car_comp);
}
