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

import com.calculadora.SAMIR.DTO.JurosDTO;
import com.calculadora.SAMIR.services.JurosService;

@RestController
@CrossOrigin
@RequestMapping("/juros")
public class JurosController {
	@Autowired
	private JurosService correcaoService;

	@GetMapping
	public @ResponseBody ResponseEntity<Object> findAll() {
		try {
			return ResponseEntity.ok(correcaoService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public @ResponseBody ResponseEntity<Object> save(@RequestBody JurosDTO jurosDTO) {
		System.out.println("CHEGOU REQUISICAO "+ jurosDTO);
		try {
			return ResponseEntity.ok(correcaoService.save(jurosDTO));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// @GetMapping("/procurarPorCodigo/{codigo}")
	// public @ResponseBody Juros filtrarJurosPorCodigo(@PathVariable Integer
	// codigo) {
	// return repository.findByCodigo(codigo);
	// }

	// @GetMapping("/procurarPorTipo/{tipo}")
	// public @ResponseBody List<Juros> filtrarJurosPorTipo(@PathVariable Integer
	// tipo) {
	// return repository.findByTipoOrderByDataAsc(tipo);
	// }

	// @PostMapping("/salvar")
	// public @ResponseBody String savarJuros(@RequestBody Juros taxa) {
	// try {
	// int size = 0;
	// size = listarJuros().size();
	// size ++;
	// size ++;
	// taxa.setCodigo(size);
	// repository.save(taxa);
	// String text = "calculo feito com sucesso, id do ultimo elemento é: " +
	// taxa.getCodigo() + "size: " + listarJuros().get(size - 2).getCodigo() ;
	// return text;
	// } catch (Exception e) {
	// String erro = "Erro no calculo " + e;
	// return erro;
	// }

	// }
	@GetMapping("/lista/{type}")
	public @ResponseBody ResponseEntity<Object> findByType(@PathVariable Integer type) {
		try {
			return ResponseEntity.ok(correcaoService.findByType(type));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/lista")
	public @ResponseBody ResponseEntity<Object> saveAll(@RequestBody List<JurosDTO> taxas) {
		try {
			return ResponseEntity.ok(correcaoService.saveAll(taxas));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<Object> delete(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(correcaoService.delete(id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	// @DeleteMapping("/deletarALL/1234")
	// public @ResponseBody ResponseEntity<Object> deleteAll() {
	// 	try {
	// 		return ResponseEntity.ok(correcaoService.deleteAll());
	// 	} catch (Exception e) {
	// 		return ResponseEntity.badRequest().body(e.getMessage());
	// 	}

	// }
	// @PutMapping("calcular/{tipo}/{operacao}")
	// public String CalcularParada ( @PathVariable("tipo") int tipo,
	// @PathVariable("operacao") String operacao, @RequestBody Juros taxa) {
	// List<Juros> taxasNovas = repository.findByTipo(tipo);

	// if (operacao.equals("adicionar")) {
	// for (int i = 0; i < taxasNovas.size(); i++) {
	// taxasNovas.get(i).setJurosAcumulados(taxasNovas.get(i).getJurosAcumulados() +
	// taxa.getJuros());
	// repository.save(taxasNovas.get(i));
	// }

	// return "Soma executada " + savarJuros(taxa);
	// } else if (operacao.equals("excluir")) {
	// for (int i = 0; i < taxasNovas.size(); i++) {
	// taxasNovas.get(i).setJurosAcumulados(taxasNovas.get(i).getJurosAcumulados() -
	// taxa.getJuros());
	// repository.save(taxasNovas.get(i));

	// }

	// return "subtracao executada ";

	// }

	// else {
	// return "ERRO falha na execucao";

	// }

	// }

}
