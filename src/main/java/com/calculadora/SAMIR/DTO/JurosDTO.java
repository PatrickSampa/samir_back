package com.calculadora.SAMIR.DTO;

import java.time.LocalDate;

import com.calculadora.SAMIR.entities.DescribeJuros;
import com.calculadora.SAMIR.entities.Juros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JurosDTO {

	private int id;

	private LocalDate data;

	private double juros;

	private int type;

	public Juros toEntity(DescribeJuros describe) {
		return new Juros(id, data, juros, describe);
	}
}
