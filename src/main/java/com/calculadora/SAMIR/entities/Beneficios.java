package com.calculadora.SAMIR.entities;



import com.calculadora.SAMIR.DTO.BeneficiosDTO;
import com.calculadora.SAMIR.util.Conversor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beneficios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beneficios {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int codigo;
	private String name;
	private boolean salario13;
	private String[] inacumulavel;
	private boolean dif;

	public BeneficiosDTO toDto() {
		return Conversor.converter(this, BeneficiosDTO.class);
	}
}
