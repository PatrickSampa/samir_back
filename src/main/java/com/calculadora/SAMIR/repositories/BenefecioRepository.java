package com.calculadora.SAMIR.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.SAMIR.entities.Beneficios;

public interface BenefecioRepository extends JpaRepository<Beneficios, Integer>{
	Beneficios findByName(String name);


}

