package com.calculadora.SAMIR.DTO;

import java.time.LocalDate;


import lombok.Data;


@Data
public class SalarioMinimoDTO {
	private int id;
	private LocalDate data;
	private float valor;

}
