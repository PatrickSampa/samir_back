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

import com.calculadora.SAMIR.DTO.CorrecaoDTO;
import com.calculadora.SAMIR.services.CorrecaoService;

@RestController
@CrossOrigin
@RequestMapping("/correcao")
public class CorrecaoController {

	/* objetos de açao */
	@Autowired
	private CorrecaoService correcaoService;
	// @Autowired
	// private CorrecaoRepository repository;
	

	@GetMapping
	public @ResponseBody ResponseEntity<Object> findAll() {
		try {

			return ResponseEntity.ok(correcaoService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// @GetMapping("/procurarPorCodigo/{codigo}")
	// public @ResponseBody Correcao filtrarCorrecaoCodigo(@PathVariable Integer codigo) {
	// 	return repository.findById(codigo).get();
	// }

	@GetMapping("/lista/{tipo}")
	public @ResponseBody ResponseEntity<Object> filtrarCorrecao(@PathVariable Integer tipo) {
		try {

			return ResponseEntity.ok(correcaoService.findByType(tipo));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public @ResponseBody ResponseEntity<Object> save (@RequestBody CorrecaoDTO correcaoDTO){
		try {
			return ResponseEntity.ok(correcaoService.save(correcaoDTO));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	@PostMapping("/lista")
	public @ResponseBody ResponseEntity<Object> saveAll (@RequestBody List<CorrecaoDTO> correcaoDTOs){
		try {
			return ResponseEntity.ok(correcaoService.saveAll(correcaoDTOs));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	// // @PostMapping("/salvar")
	// // public @ResponseBody String savarTaxaDeCorrecao(@RequestBody TaxaDeCorrecao
	// // taxa) {
	// // try {
	// // int size = 0;
	// // size = listarTaxaDeCorrecao().size();
	// // size ++;
	// // taxa.setCodigo(size);
	// // repository.save(taxa);
	// // String text = "calculo feito com sucesso, id do ultimo elemento é: " +
	// // taxa.getCodigo() + " size: " + (listarTaxaDeCorrecao().size() + 1) ;
	// // return text;
	// // } catch (Exception e) {
	// // int size = 0;
	// // size = listarTaxaDeCorrecao().size();
	// // size ++;
	// // size ++;
	// // size ++;
	// // String erro = "Erro no calculo " + size + " " + e;
	// // return erro;
	// // }
	// // }
	@PostMapping("/Listarsalvar")
	public @ResponseBody ResponseEntity<Object> savarLista(@RequestBody List<CorrecaoDTO> taxas) {
		try {
			
			return ResponseEntity.ok(correcaoService.saveAll(taxas));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<Object> removerTaxaDeCorrecao(@PathVariable Integer codigo) {
		try {
			return ResponseEntity.ok(correcaoService.delete(codigo));
		} catch (Exception erro) {
			System.err.println(erro.getMessage());
			return ResponseEntity.status(500).body(erro.getMessage());
		}
	}


	// @DeleteMapping("/lista/{codigo}")
	// public @ResponseBody ResponseEntity<Object> removerTaxasDeCorrecao(@PathVariable Integer codigo) {
	// 	try {
	// 		return ResponseEntity.ok(correcaoService.deleteAll(codigo));
	// 	} catch (Exception erro) {
	// 		System.err.println(erro.getMessage());
	// 		return ResponseEntity.status(500).body(erro.getMessage());
	// 	}
	// }

	// // @PutMapping("calcular/{tipo}/{operacao}")
	// // public String CalcularParada (@RequestBody TaxaDeCorrecao taxa,
	// // @PathVariable("tipo") int tipo, @PathVariable("operacao") String operacao) {
	// // List<TaxaDeCorrecao> taxasNovas = repository.findByTipo(tipo);

	// // if (operacao.equals("adicionar")) {
	// // for (int i = 0; i < taxasNovas.size(); i++) {
	// // taxasNovas.get(i).setTaxaAcumulada(taxasNovas.get(i).getTaxaAcumulada() *
	// // taxa.getTaxaCorrecao());
	// // repository.save(taxasNovas.get(i));
	// // }
	// // return "Multiplicaçao executada " + listarTaxaDeCorrecao().size() + " " +
	// // savarTaxaDeCorrecao(taxa);
	// // } else if (operacao.equals("excluir")) {
	// // for (int i = 0; i < taxasNovas.size(); i++) {
	// // taxasNovas.get(i).setTaxaAcumulada(taxasNovas.get(i).getTaxaAcumulada() /
	// // taxa.getTaxaCorrecao());
	// // repository.save(taxasNovas.get(i));

	// // }
	// // repository.delete(taxa);
	// // List<TaxaDeCorrecao> lista_size = repository.findAll();
	// // return"Divisao executada " + lista_size.size();

	// // }

	// // else {
	// // return "ERRO falha na execucao";

	// // }

	// // }

}
