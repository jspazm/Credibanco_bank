package com.credibanco.bank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.credibanco.bank.app.entity.Transaccion;

public interface ITransCompl extends JpaRepository<Transaccion, Long>{
	@Query("SELECT t FROM Transaccion t where t.transaction_id = :tr")
    Transaccion getTransaccionById(String tr);
}
