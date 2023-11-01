package com.calculadora.SAMIR.entities;

import java.io.Serializable;
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

import com.calculadora.SAMIR.DTO.JurosDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "juros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Juros implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column
    @NotNull 
	private LocalDate data;
	
	@Column
    @NotNull 
	private double juros;
	
	@ManyToOne
	@JoinColumn(name = "describe_juros_id")
	private DescribeJuros type;

	public JurosDTO toDto() {
		return new JurosDTO(id, data, juros, type.getType());
	}
}
