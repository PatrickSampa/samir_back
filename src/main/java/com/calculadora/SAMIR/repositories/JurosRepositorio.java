package com.calculadora.SAMIR.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.SAMIR.entities.DescribeJuros;
import com.calculadora.SAMIR.entities.Juros;

public interface JurosRepositorio extends JpaRepository<Juros, Integer> {

	List<Juros> findByTypeOrderByDataAsc(DescribeJuros type);

	// @Query("SELECT j FROM juros j WHERE j.type = :tipo AND j.data <= :dataLimite ORDER BY j.data DESC")
	// List<Juros> findRecentJurosByTypeAndDateBefore(DescribeJuros type, LocalDate dataLimite);

	
	<JurosSalvar extends Juros> JurosSalvar save(JurosSalvar salvar);

}
