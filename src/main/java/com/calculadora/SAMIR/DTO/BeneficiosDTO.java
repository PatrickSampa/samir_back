package com.calculadora.SAMIR.DTO;



import com.calculadora.SAMIR.entities.Beneficios;
import com.calculadora.SAMIR.util.Conversor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiosDTO {
	private int codigo;
	private String name;
	private boolean salario13;
	private String[] inacumulavel;
	private boolean dif;
	public Beneficios toEntity() {
		return Conversor.converter(this, Beneficios.class);
	}
}
