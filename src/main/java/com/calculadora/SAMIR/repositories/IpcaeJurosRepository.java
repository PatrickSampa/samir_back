package com.calculadora.SAMIR.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.calculadora.SAMIR.entities.taxaIpcae;


public interface IpcaeJurosRepository extends JpaRepository<taxaIpcae, Integer> {
 
    


    List<taxaIpcae> findAllByOrderByDataAsc();


    <taxaSalvar extends taxaIpcae> taxaSalvar save(taxaSalvar salvar);
}
