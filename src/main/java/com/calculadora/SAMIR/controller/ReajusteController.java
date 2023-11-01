package com.calculadora.SAMIR.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.SAMIR.entities.TaxaReajuste;
import com.calculadora.SAMIR.repositories.ReajusteRepositorio;

@RestController
@CrossOrigin
@RequestMapping("/reajuste")
public class ReajusteController {

	@Autowired
	private ReajusteRepositorio repository;

	@GetMapping("/listar")
	public @ResponseBody List<TaxaReajuste> listarTaxaDeReajuste() {
		return repository.findAllByOrderByDataAsc();
	}

	@GetMapping("/procurarPorCodigo/{codigo}")
	public @ResponseBody TaxaReajuste filtrarReajusteCodigo(@PathVariable Integer codigo) {
		return repository.findByCodigo(codigo);
	}

	@PostMapping("/salvar")
	public @ResponseBody String savarTaxaDeResajuste(@RequestBody TaxaReajuste taxa) {

		try {
			int size = 0;
			size = listarTaxaDeReajuste().size();
			size ++;
			size ++;
			taxa.setCodigo(size);
			repository.save(taxa);
			String text = "calculo feito com sucesso, id do ultimo elemento é: " + taxa.getCodigo() + " size: " + (listarTaxaDeReajuste().size() + 1) ;
			return text;
		} catch (Exception e) {
			String erro = "Erro no calculo " + e;
			return erro;
		}
	}
	@PostMapping("/Listarsalvar")
	public @ResponseBody String savarLista(@RequestBody List<TaxaReajuste> taxas) {
		try {
			repository.saveAll(taxas);
			return "Deu certo";
		} catch (Exception e) {
			String erro = "Erro no calculo " + e;
			return erro;
		}
		
	}

	@DeleteMapping("/deletar/{codigo}")
	public @ResponseBody String removerTaxaDeReajuste(@PathVariable Integer codigo) {

		try {
			TaxaReajuste j = filtrarReajusteCodigo(codigo);
			this.repository.delete(j);
			return "TAXA removido";
		} catch (Exception erro) {
			return"Falha na remoçao da TAXA" + erro.getMessage();
		}
		

	}
	// @DeleteMapping("/armageddon/1234")
	// public @ResponseBody String deletarTudo(@RequestBody List<TaxaReajuste> taxa) {

	// 	try {
	// 		for(int i = 0; i< taxa.size(); i++) {
	// 			int codigo = taxa.get(i).getCodigo();
	// 			TaxaReajuste j = filtrarReajusteCodigo(codigo);
	// 			this.repository.delete(j);	
	// 		}
	// 		return "TAXA removido";
	// 	} catch (Exception erro) {
	// 		return"Falha na remoçao da TAXA" + erro.getMessage();
	// 	}
	// }

}