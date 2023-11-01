package com.calculadora.SAMIR.entities;


//@Entity
//@Table
public class BeneficioAcumulado {
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


    public String getNomeBeneficio() {
        return nomeBeneficio;
    }

    public void setNomeBeneficio(String nomeBeneficio) {
        this.nomeBeneficio = nomeBeneficio;
    }

    public String getDataDeInicio() {
        return dataDeInicio;
    }

    public void setDataDeInicio(String dataDeInicio) {
        this.dataDeInicio = dataDeInicio;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getRmi() {
        return rmi;
    }

    public void setRmi(String rmi) {
        this.rmi = rmi;
    }
}
