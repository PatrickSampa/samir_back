package com.calculadora.SAMIR.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.SAMIR.entities.taxaIpcae;
import com.calculadora.SAMIR.repositories.IpcaeJurosRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/ipcae")
public class TaxaIpcaeController {
    

    @Autowired
	private IpcaeJurosRepository repository;


    @GetMapping("/listar")
	public @ResponseBody List<taxaIpcae> listarTaxaDeIpcae() {
		return repository.findAllByOrderByDataAsc();
	}

    @PostMapping("/salvar")
    public @ResponseBody taxaIpcae salvarTaxaIpcae(@RequestBody taxaIpcae taxa) {
        System.out.println("Salvando taxa de IPCAE");
        System.out.println(taxa);
        return repository.save(taxa);
    }
    
}
