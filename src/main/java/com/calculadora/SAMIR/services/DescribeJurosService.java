package com.calculadora.SAMIR.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.SAMIR.DTO.DescribeJurosDTO;
import com.calculadora.SAMIR.entities.DescribeJuros;
import com.calculadora.SAMIR.repositories.DescribeJurosRepository;

@Service
public class DescribeJurosService {
    @Autowired
    private DescribeJurosRepository repository;

    public List<DescribeJurosDTO> findAll() {
        return repository.findAll().stream().map(DescribeJuros::toDto).collect(Collectors.toList());
    }

    public DescribeJuros save(DescribeJurosDTO describeJurosDTO)  {
        return repository.save(describeJurosDTO.toEntity());
    }

    public DescribeJurosDTO update(DescribeJurosDTO describeJurosDTO) {
        return repository.save(describeJurosDTO.toEntity()).toDto();
    }

    public DescribeJuros findByType(int type) {
        return repository.findByType(type);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
