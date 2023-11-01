package com.calculadora.SAMIR.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.SAMIR.entities.DescribeCorrecao;

public interface DescribeCorrecaoRepository extends JpaRepository<DescribeCorrecao, Integer> {
    DescribeCorrecao findByType(Integer type);
}
