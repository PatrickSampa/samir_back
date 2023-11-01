package com.calculadora.SAMIR.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.calculadora.SAMIR.entities.Correcao;
import com.calculadora.SAMIR.entities.DescribeCorrecao;

public interface CorrecaoRepository extends JpaRepository<Correcao, Integer> {

	/* pesquisar por tipo de taxa De Correcao */

	/* pesquisa por taxa De Correcao */
	// TaxaDeCorrecao findByCodigo(int codigo);

	// List<TaxaDeCorrecao> findAllByOrderByCodigoAsc();

	// List<TaxaDeCorrecao> findByTipoOrderByCodigoAsc(int tipo);
	// @Query("SELECT c FROM correcao c WHERE c.type = :type AND c.data <= :dataLimite ORDER BY c.data DESC")
	// List<Correcao> findByTypeAndDataBeforeOrderByDataDesc(DescribeCorrecao type, LocalDate dataLimite);

	List<Correcao> findByTypeOrderByDataAsc(DescribeCorrecao type);
	/* cadastrar taxa De Correcao */
//	<taxaSalvar extends taxaDeCorrecao> taxaSalvar save (taxaSalvar salvar);

}