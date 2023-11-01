package com.calculadora.SAMIR.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.SAMIR.entities.TaxaReajuste;

public interface ReajusteRepositorio extends JpaRepository<TaxaReajuste, Integer> {

	/* pesquisar por tipo de taxa De Correcao */

	/* pesquisa por taxa De Correcao */
	TaxaReajuste findByCodigo(int codigo);
	
	

	List<TaxaReajuste> findAllByOrderByDataAsc();

	
	/* cadastrar taxa De Correcao */
	//<taxaSalvar extends TaxaReajuste> taxaSalvar save(taxaSalvar salvar);

}