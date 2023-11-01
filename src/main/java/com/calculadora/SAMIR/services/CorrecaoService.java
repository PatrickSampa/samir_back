package com.calculadora.SAMIR.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.SAMIR.DTO.CorrecaoDTO;
import com.calculadora.SAMIR.entities.Correcao;
import com.calculadora.SAMIR.entities.DescribeCorrecao;
import com.calculadora.SAMIR.repositories.CorrecaoRepository;

@Service
public class CorrecaoService {

	@Autowired
	private CorrecaoRepository repository;
	@Autowired
	private DescribeCorrecaoService describeCorrecaoService;

	public List<CorrecaoDTO> findAll() {
		return repository.findAll().stream().map(Correcao::toDto).collect(Collectors.toList());
	}

	public CorrecaoDTO findById(Integer codigo) throws Exception {
		return repository.findById(codigo).get().toDto();
	}

	public List<CorrecaoDTO> findByType(int type) throws Exception {
		DescribeCorrecao describeCorrecao = describeCorrecaoService.findByType(type).toEntity();
		return repository.findByTypeOrderByDataAsc(describeCorrecao).stream().map(Correcao::toDto).collect(Collectors.toList());
	}

	public CorrecaoDTO save (CorrecaoDTO correcaoDTO){
		DescribeCorrecao describeCorrecao = describeCorrecaoService.findByType(correcaoDTO.getType()).toEntity();
		return repository.save(correcaoDTO.toEntity(describeCorrecao)).toDto();
	} 

	public List<Correcao> saveAll(List<CorrecaoDTO> taxas) throws Exception {
		try {
			DescribeCorrecao describeCorrecao = describeCorrecaoService.findByType(taxas.get(0).getType()).toEntity();
			// for(CorrecaoDTO taxa: taxas){
			// 	taxa.setData(verificarUltimoDiaMes(taxa.getData()));
			// }
			return repository.saveAll(taxas.stream().map(CorrecaoDTO -> CorrecaoDTO.toEntity(describeCorrecao)).collect(Collectors.toList()));
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro no calculo " + e.getMessage());
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
			Correcao j = repository.findById(codigo).get();
			this.repository.delete(j);
			return "TAXA removido";
		} catch (Exception erro) {
			throw new IllegalArgumentException("Falha na remoçao da TAXA" + erro.getMessage());
		}

	}

	public String deleteAll(Integer type) throws Exception{
		try {
			DescribeCorrecao describeCorrecao = describeCorrecaoService.findByType(type).toEntity();
			repository.deleteAll(repository.findByTypeOrderByDataAsc(describeCorrecao));
			return "taxas removidas";
		} catch (Exception e) {
			throw new IllegalArgumentException("Falha na remoçao da TAXA" + e.getMessage());
		}
	}

}
