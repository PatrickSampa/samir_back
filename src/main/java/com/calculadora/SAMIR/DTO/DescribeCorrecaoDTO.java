package com.calculadora.SAMIR.DTO;

import java.util.List;


import com.calculadora.SAMIR.entities.DescribeCorrecao;
import com.calculadora.SAMIR.util.Conversor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DescribeCorrecaoDTO {
    private int id;


    @NotBlank 
    private String describe;

    @NotBlank
    private int type;

    private List<CorrecaoDTO> Correcaos;

    public DescribeCorrecao toEntity(){
        return Conversor.converter(this, DescribeCorrecao.class);
    }
}
