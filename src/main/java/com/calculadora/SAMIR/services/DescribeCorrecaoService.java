package com.calculadora.SAMIR.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.SAMIR.DTO.DescribeCorrecaoDTO;
import com.calculadora.SAMIR.entities.DescribeCorrecao;
import com.calculadora.SAMIR.repositories.DescribeCorrecaoRepository;


@Service
public class DescribeCorrecaoService {
    @Autowired
    private DescribeCorrecaoRepository repository;

    public List<DescribeCorrecaoDTO> findAll()  {
        return repository.findAll()
                .stream()
                .map(DescribeCorrecao::toDto)
                .collect(Collectors.toList());
    }

    public DescribeCorrecaoDTO save(DescribeCorrecaoDTO describeCorrecaoDTO)  {
        return repository.save(describeCorrecaoDTO.toEntity()).toDto();
    }

    public DescribeCorrecaoDTO update(DescribeCorrecao describeCorrecao)  {
        return repository.save(describeCorrecao).toDto();
    }

    public void delete(Integer id)  {
        repository.deleteById(id);
    }

    public DescribeCorrecaoDTO findById(Integer id) {
        return repository.findById(id).get().toDto();
    }

    public DescribeCorrecaoDTO findByType(Integer type) {
        DescribeCorrecaoDTO describeCorrecaoDTO = repository.findByType(type).toDto();
        DescribeCorrecaoDTO voids = new DescribeCorrecaoDTO();
        if(describeCorrecaoDTO == voids){
            throw new IllegalArgumentException("Type is not exist in the database");
        }
        return describeCorrecaoDTO;
    }

}
