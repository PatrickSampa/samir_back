package com.calculadora.SAMIR.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.SAMIR.entities.TaxaReajuste;
import com.calculadora.SAMIR.repositories.ReajusteRepositorio;

@Service
public class ReajusteService {

	@Autowired
	private ReajusteRepositorio repository;

	public List<TaxaReajuste> listarTaxaDeReajuste() {
		return repository.findAllByOrderByDataAsc();
	}

	
	public TaxaReajuste filtrarReajusteCodigo(Integer codigo) {
		return repository.findByCodigo(codigo);
	}

	
	public String savarTaxaDeResajuste(TaxaReajuste taxa) {

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
	public String savarLista(List<TaxaReajuste> taxas) {
		try {
			repository.saveAll(taxas);
			return "Deu certo";
		} catch (Exception e) {
			String erro = "Erro no calculo " + e;
			return erro;
		}
		
	}

	public String removerTaxaDeReajuste(Integer codigo) {

		try {
			TaxaReajuste j = filtrarReajusteCodigo(codigo);
			this.repository.delete(j);
			return "TAXA removido";
		} catch (Exception erro) {
			return"Falha na remoçao da TAXA" + erro.getMessage();
		}
		

	}
	
	public String deletarTudo(List<TaxaReajuste> taxa) {

		try {
			for(int i = 0; i< taxa.size(); i++) {
				int codigo = taxa.get(i).getCodigo();
				TaxaReajuste j = filtrarReajusteCodigo(codigo);
				this.repository.delete(j);	
			}
			return "TAXA removido";
		} catch (Exception erro) {
			return"Falha na remoçao da TAXA" + erro.getMessage();
		}
		

	}

}