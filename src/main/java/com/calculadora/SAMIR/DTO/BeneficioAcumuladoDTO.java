package com.calculadora.SAMIR.DTO;

import lombok.Data;

//@Entity
//@Table
@Data
public class BeneficioAcumuladoDTO {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;

    private String nomeBeneficio;
    private String dataDeInicio;
    private String dataFinal;
    private String rmi;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

}
