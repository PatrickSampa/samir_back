package com.calculadora.SAMIR.DTO;


import java.time.LocalDate;


import lombok.Data;


@Data
public class TaxaReajusteDTO {
	private int codigo;
	private LocalDate data;
	private double reajuste;
	private double aumentoReal;
	private double reajusteAcumulado;

}
