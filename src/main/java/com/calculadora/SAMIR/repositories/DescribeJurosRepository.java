package com.calculadora.SAMIR.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.SAMIR.entities.DescribeJuros;
public interface DescribeJurosRepository extends JpaRepository<DescribeJuros, Integer>{
    DescribeJuros findByType(int type);
}
