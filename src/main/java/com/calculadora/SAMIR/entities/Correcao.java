package com.calculadora.SAMIR.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.calculadora.SAMIR.DTO.CorrecaoDTO;
import com.calculadora.SAMIR.util.Conversor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "correcao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Correcao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column
	@NotNull
	private LocalDate data;

	@Column
	@NotNull
	private double percentual;

	@ManyToOne
	@JoinColumn(name = "describe_correcao_type")
	private DescribeCorrecao type;

	public CorrecaoDTO toDto() {
		return Conversor.converter(this, CorrecaoDTO.class);
	}

}
