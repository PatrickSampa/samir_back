package com.calculadora.SAMIR.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.SAMIR.entities.SalarioMinimo;

public interface SalarioMinimoRepository extends JpaRepository<SalarioMinimo, Integer> {
	
	List<SalarioMinimo> findBydata(Date data);
	
}