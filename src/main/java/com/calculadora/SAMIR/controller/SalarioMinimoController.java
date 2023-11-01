package com.calculadora.SAMIR.controller;

import java.util.ArrayList;
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

import com.calculadora.SAMIR.entities.SalarioMinimo;
import com.calculadora.SAMIR.repositories.SalarioMinimoRepository;

@RestController
@CrossOrigin
@RequestMapping("/salarioMinimo")
public class SalarioMinimoController {
	@Autowired
	private SalarioMinimoRepository repository;
	
	@GetMapping("/listar")
	public @ResponseBody List<SalarioMinimo> listarSalarioMinimo() {
		return repository.findAll();
	};	
	@GetMapping("/procuraPorAno/{data}")
	public @ResponseBody List<SalarioMinimo> listarSalrioAno(@PathVariable Integer data){
		List<SalarioMinimo>listaTudo = repository.findAll();
		List<SalarioMinimo> lista = new ArrayList<SalarioMinimo>();
		for(int i = 0; i < listaTudo.size(); i++) {
			int ano = listaTudo.get(i).getData().getYear();
			System.out.println("ano: " + ano);
			if(ano == data) {
				lista.add(listaTudo.get(i));
			}
		}
		return lista;
	}
	@PostMapping("/salvarall")
	public @ResponseBody List<SalarioMinimo> salvarLista(@RequestBody List<SalarioMinimo> lista){
		return repository.saveAll(lista);
		
	}
	@PostMapping("/salvar")
	public @ResponseBody SalarioMinimo salvar ( @RequestBody SalarioMinimo salario) {
		return repository.save(salario);
	}

	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<Object> removerTaxaDeCorrecao(@PathVariable Integer codigo) {
		try {
			repository.deleteById(codigo);
			return ResponseEntity.ok().build();
		} catch (Exception erro) {
			System.err.println(erro.getMessage());
			return ResponseEntity.status(500).body(erro.getMessage());
		}
	}
}
