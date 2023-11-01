package com.calculadora.SAMIR.DTO;

import java.util.List;



import com.calculadora.SAMIR.entities.DescribeJuros;
import com.calculadora.SAMIR.util.Conversor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class DescribeJurosDTO {
    private int id;

    @NotBlank 
    private String describe;

    @NotBlank
    private int type;

    private List<JurosDTO> juros;

    public DescribeJuros toEntity(){
        return Conversor.converter(this, DescribeJuros.class);
    }
}
