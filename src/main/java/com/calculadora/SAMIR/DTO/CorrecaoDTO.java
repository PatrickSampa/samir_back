package com.calculadora.SAMIR.DTO;

import java.time.LocalDate;

import com.calculadora.SAMIR.entities.Correcao;
import com.calculadora.SAMIR.entities.DescribeCorrecao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrecaoDTO {
	private int id;

	private LocalDate data;

	private double percentual;

	private int type;

	public Correcao toEntity(DescribeCorrecao describe) {
		return new Correcao(id, data, percentual, describe);
	}
}
