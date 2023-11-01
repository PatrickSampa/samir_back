package com.calculadora.SAMIR.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.SAMIR.DTO.BeneficiosDTO;
import com.calculadora.SAMIR.entities.Beneficios;
import com.calculadora.SAMIR.repositories.BenefecioRepository;
import com.calculadora.SAMIR.services.BeneficioService;

@RestController
@CrossOrigin
@RequestMapping("/beneficio")
public class BeneficioController {

	@Autowired
	private BenefecioRepository repository;
	@Autowired
	private BeneficioService beneficioService;
	
	@GetMapping("/listar")
	public ResponseEntity<Object> listarBEneficios() {
		try {
			List<Beneficios> beneficiosList = beneficioService.listarBEneficios();
			return ResponseEntity.ok(beneficiosList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/ProcurarPorName")
	public @ResponseBody BeneficiosDTO procurarPorName(@RequestBody String name) {
		try {
			return beneficioService.procurarPorName(name);		
		} catch (Exception e) {
			return null;
		}
	}
	
	@PostMapping("/salvar")
	public @ResponseBody Beneficios salvarBeneficio(@RequestBody Beneficios beneficio) {
		return repository.save(beneficio);
	}
	@PostMapping("/salvarLista")
	public @ResponseBody List<Beneficios> salvarLista(@RequestBody List<Beneficios> list){
		return repository.saveAll(list);
	}
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<Object> delete(@PathVariable Integer id) {
		try {
			Beneficios beneficio = repository.findById(id).get();
			if(beneficio != null){
				repository.delete(beneficio);
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.badRequest().body("Beneficio nao encontrado");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	// @DeleteMapping("/armageddon/1234")
	// public @ResponseBody String deletarTudo(@RequestBody String taxa) {
	// 	if(taxa=="1234") {
	// 		try {
	// 			this.repository.deleteAll();	
	// 		return "TAXA removido";
	// 	} catch (Exception erro) {
	// 		return"Falha na remoçao da TAXA" + erro.getMessage();
	// 	}
	// 	}
	// 	else {
	// 		return "Armageddon concluido com sucesso";
	// 	}
	// }
}
