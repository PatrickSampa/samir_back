package com.calculadora.SAMIR.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.calculadora.SAMIR.DTO.BeneficiosDTO;
import com.calculadora.SAMIR.entities.Beneficios;
import com.calculadora.SAMIR.repositories.BenefecioRepository;


@Service
public class BeneficioService {

	@Autowired
	private BenefecioRepository beneficioRepository;
	
	public List<Beneficios> listarBEneficios() throws Exception {
		return beneficioRepository.findAll();
	}
	
	public BeneficiosDTO procurarPorName(String name) throws Exception {
		return beneficioRepository.findByName(name).toDto();
	}

	public Beneficios salvarBeneficio(Beneficios beneficio) {
		return beneficioRepository.save(beneficio);
	}
	public List<Beneficios> salvarLista(List<Beneficios> list){
		return beneficioRepository.saveAll(list);
	}
	public String deletarTudo(String taxa) {
		if(taxa=="1234") {
			try {
				this.beneficioRepository.deleteAll();	
			return "TAXA removido";
		} catch (Exception erro) {
			return"Falha na remoçao da TAXA" + erro.getMessage();
		}
		}
		else {
			return "Armageddon concluido com sucesso";
		}
		
		

	}
}
