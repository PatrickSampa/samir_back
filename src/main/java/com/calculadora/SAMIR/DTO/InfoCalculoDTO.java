package com.calculadora.SAMIR.DTO;

import lombok.Data;

@Data

public class InfoCalculoDTO {
	private String inicioCalculo;
	private String dip;
	private String atulizacao;
	private String incioJuros;
	private float rmi;
	private boolean juros;
	private boolean salario13;
	private int tipoJuros;
	private int tipoCorrecao;
	private boolean salarioMinimo;
	private boolean limiteMinimoMaximo;
	private String dib;
	private float porcentagemRMI;
	private boolean salario13Obrigatorio;
	private String dibAnterior;
	private boolean selic;

	public boolean isSalario13Obrigatorio() {
		return salario13Obrigatorio;
	}
	
	public boolean isJuros() {
		return juros;
	}
	
	public boolean isSalario13() {
		return salario13;
	}

	public boolean isSalarioMinimo() {
		return salarioMinimo;
	}

	public boolean isLimiteMinimoMaximo() {
		return limiteMinimoMaximo;
	}
	
}
