package com.credibanco.bank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.credibanco.bank.app.entity.Producto;

@Repository
public interface IProductoComp extends JpaRepository<Producto, Long> {
}
