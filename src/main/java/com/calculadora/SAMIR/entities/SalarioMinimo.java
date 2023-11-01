package com.calculadora.SAMIR.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import lombok.Data;

@Data 
@Entity
@Table(name = "salarioMinimo")
public class SalarioMinimo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private LocalDate data;
	private float valor;
}
