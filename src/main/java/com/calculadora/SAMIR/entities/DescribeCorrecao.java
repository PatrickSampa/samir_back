package com.calculadora.SAMIR.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.calculadora.SAMIR.DTO.DescribeCorrecaoDTO;
import com.calculadora.SAMIR.util.Conversor;

import lombok.Data;

@Table(name = "describe_correcao")
@Entity
@Data

public class DescribeCorrecao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    @NotBlank
    private String describe;

    @Column(unique = true)
    @NotNull
    private int type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Correcao> correcao;

    public DescribeCorrecaoDTO toDto() {
        return Conversor.converter(this, DescribeCorrecaoDTO.class);
    }
}
