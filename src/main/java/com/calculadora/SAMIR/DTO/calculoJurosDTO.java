package com.calculadora.SAMIR.DTO;

import org.apache.commons.math3.util.Precision;

import lombok.Data;

@Data

public class calculoJurosDTO {

	private String data;
	private float reajusteAcumulado;
	private float salario;	
	private float correcao;
	private float salarioCorrigido;
	private float juros;
	private float salarioJuros;
	private float salarioTotal;
	
	public calculoJurosDTO(String data, float reajusteAcumulado, float salario, float correcao, float juros, float porcentagemRMI) {
		super();
		 float porcentagem = porcentagemRMI / 100;
		this.data = data;
		this.reajusteAcumulado = reajusteAcumulado;
		this.salario = Precision.round((salario * porcentagem),2);;
		this.correcao = correcao;
		this.salarioCorrigido = Precision.round(((salario * correcao) * porcentagem),2);
		this.juros = juros;
		this.salarioJuros = Precision.round(((salario * correcao * juros / 100) * porcentagem),2);
		this.salarioTotal = Precision.round((this.salarioCorrigido + this.salarioJuros),2);
	}
	
}
