package com.calculadora.SAMIR.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.SAMIR.DTO.JurosDTO;
import com.calculadora.SAMIR.entities.DescribeJuros;
import com.calculadora.SAMIR.entities.Juros;
import com.calculadora.SAMIR.repositories.JurosRepositorio;

@Service
public class JurosService {

	@Autowired
	private JurosRepositorio repository;
	@Autowired
	private DescribeJurosService describeJurosService;

	public List<JurosDTO> findAll() {
		return repository.findAll().stream().map(Juros::toDto).collect(Collectors.toList());
	}

	// public Juros filtrarJurosPorCodigo( Integer codigo) {
	// return repository.findByCodigo(codigo);
	// }

	public List<JurosDTO> findByType(Integer tipo) {
		
		DescribeJuros describeJuros = describeJurosService.findByType(tipo);
		return repository.findByTypeOrderByDataAsc(describeJuros).stream().map(Juros::toDto)
				.collect(Collectors.toList());
	}

	public JurosDTO save(JurosDTO taxa) throws Exception {
		try {
			DescribeJuros describeJuros = describeJurosService.findByType(taxa.getType());
			
			return repository.save(taxa.toEntity(describeJuros)).toDto();
		} catch (Exception e) {
			String erro = "Erro no calculo " + e;
			throw new IllegalArgumentException(erro);
		}
	}

	
	// }
	public List<Juros> saveAll(List<JurosDTO> taxas) throws Exception {
		try {
			DescribeJuros describeJuros = describeJurosService.findByType(taxas.get(0).getType());
			return repository.saveAll(
				taxas.stream().map(JurosDTO -> JurosDTO.toEntity(describeJuros)).collect(Collectors.toList()));
			} catch (Exception e) {
				String erro = "Erro no calculo " + e;
				throw new IllegalArgumentException(erro);
			}
			
		}
		
		// public static LocalDate verificarUltimoDiaMes(LocalDate data) {
		//     int ultimoDiaMes = data.lengthOfMonth();
			
		//     if (data.getDayOfMonth() == ultimoDiaMes) {
		//         return data.plusDays(1);
		//     } else {
		//         return data;
		//     }
		// }

	public String delete(Integer codigo) throws Exception {
		try {
			Juros j = repository.findById(codigo).get();
			this.repository.delete(j);
			return "Juros removido";
		} catch (Exception erro) {
			throw new IllegalArgumentException("Falha na remoçao do Juros" + erro.getMessage());
		}

	}

	public String deleteAll() throws Exception{
		try {
			repository.deleteAll();
			return "Juros removidos";
		} catch (Exception erro) {
			throw new IllegalArgumentException("Falha na remoçao do Juros" + erro.getMessage());
		}

	}
	// @PutMapping("calcular/{tipo}/{operacao}")
	// public String CalcularParada ( ("tipo") int tipo, ("operacao") String
	// operacao, Juros taxa) {
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
