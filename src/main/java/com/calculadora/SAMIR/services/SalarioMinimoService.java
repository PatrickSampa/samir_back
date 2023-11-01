package com.calculadora.SAMIR.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.SAMIR.entities.SalarioMinimo;
import com.calculadora.SAMIR.repositories.SalarioMinimoRepository;

@Service
public class SalarioMinimoService {
	@Autowired
	private SalarioMinimoRepository repository;

	public List<SalarioMinimo> listarSalarioMinimo() {
		return repository.findAll();
	};

	public List<SalarioMinimo> listarSalrioAno(Integer data) {
		List<SalarioMinimo> listaTudo = repository.findAll();
		List<SalarioMinimo> lista = new ArrayList<SalarioMinimo>();
		for (int i = 0; i < listaTudo.size(); i++) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			String strDate = dateFormat.format(listaTudo.get(i).getData());
			int ano = Integer.parseInt(strDate.split(" ")[0].split("-")[0]);
			System.out.println("ano: " + ano);
			if (ano == data) {
				lista.add(listaTudo.get(i));
			}
		}
		return lista;
	}

	public List<SalarioMinimo> salvarLista(List<SalarioMinimo> lista) {
		return repository.saveAll(lista);

	}

	public SalarioMinimo salvar(SalarioMinimo salario) {
		return repository.save(salario);
	}
}
