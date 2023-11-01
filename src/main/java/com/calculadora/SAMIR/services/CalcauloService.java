package com.calculadora.SAMIR.services;

// import java.text.DateFormat;
// import java.text.SimpleDateFormat;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.calculadora.SAMIR.DTO.CalculoDTO;
// import com.calculadora.SAMIR.DTO.InfoCalculoDTO;
// import com.calculadora.SAMIR.entities.Juros;
// import com.calculadora.SAMIR.entities.SalarioMinimo;
// import com.calculadora.SAMIR.entities.Correcao;
// import com.calculadora.SAMIR.entities.TaxaReajuste;
// import com.calculadora.SAMIR.repositories.CorrecaoRepository;
// import com.calculadora.SAMIR.repositories.JurosRepositorio;
// import com.calculadora.SAMIR.repositories.ReajusteRepositorio;
// import com.calculadora.SAMIR.repositories.SalarioMinimoRepository;

// @Service
public class CalcauloService {

	// @Autowired
	// private ReajusteRepositorio reajusteRepositorio;
	// @Autowired
	// private JurosRepositorio jurosRepositorio;
	// @Autowired
	// private CorrecaoRepository correcaoRepository;
	// @Autowired
	// private SalarioMinimoRepository salarioMinimoRepository;

	// public Object calcular(InfoCalculoDTO informacoes) {
	// 	try {
	// 		// System.out.println("AQUI");
	// 		// System.out.println(informacoes);
	// 		// System.out.println("salario: " + informacoes.getAtulizacao());
	// 		String[] arrayDib = informacoes.getDib().split("/");
	// 		int mesDib = Integer.parseInt(arrayDib[1]);
	// 		int anoDib = Integer.parseInt(arrayDib[2]);
	// 		String[] arrayDip = informacoes.getDip().split("/");
	// 		int mesDip = Integer.parseInt(arrayDip[1]);
	// 		int anoDip = Integer.parseInt(arrayDip[2]);
	// 		String[] arrayAtualizacao = informacoes.getAtulizacao().split("/");
	// 		int mesAtualizacao = Integer.parseInt(arrayAtualizacao[0]);
	// 		int anoAtualizacao = Integer.parseInt(arrayAtualizacao[1]);
	// 		int mesIncioJuros = 0;
	// 		int anoIncioJuros = 0;
	// 		if (informacoes.getIncioJuros() != null) {
	// 			String[] arrayInicioJuros = informacoes.getIncioJuros().split("/");
	// 			if (arrayInicioJuros.length > 1) {
	// 				mesIncioJuros = Integer.parseInt(arrayInicioJuros[1]) - 1;
	// 				anoIncioJuros = Integer.parseInt(arrayInicioJuros[2]);
	// 				if (mesIncioJuros == 0) {
	// 					mesIncioJuros = 12;
	// 					anoIncioJuros--;
	// 				}
	// 			}

	// 		}

	// 		List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();
	// 		List<SalarioMinimo> listaSalarioMinimo = new ArrayList<SalarioMinimo>();
	// 		if (informacoes.isSalarioMinimo() || informacoes.isLimiteMinimoMaximo()) {
	// 			listaSalarioMinimo = salarioMinimoRepository.findAll();
	// 		}
	// 		List<TaxaReajuste> listReajuste = reajusteRepositorio.findAll();
	// 		List<Correcao> listCorrecao = correcaoRepository
	// 				.findByTipoOrderByDataAsc(informacoes.getTipoCorrecao());
	// 		List<Juros> listJuros = new ArrayList<Juros>();
	// 		if (informacoes.isJuros()) {
	// 			listJuros = jurosRepositorio.findByTipoOrderByDataAsc(informacoes.getTipoJuros());
	// 		}

	// 		float correcaoAcumulada = 1;
	// 		float jurosAcumulado = 0;
	// 		float reajusteAcumulado = 1;
	// 		int mesCalculo = mesDib;
	// 		int anoCalculo = anoDib;
	// 		try {
	// 			if (informacoes.getDibAnterior() != null) {
	// 				String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
	// 				mesCalculo = Integer.parseInt(arrayDibAnterior[1]);
	// 				anoCalculo = Integer.parseInt(arrayDibAnterior[2]);
	// 			}
	// 		} catch (Exception e) {
	// 			System.err.println(e);
	// 		}

	// 		float rmi = informacoes.getRmi();
	// 		float porcentagemRmi = (informacoes.getPorcentagemRMI() != 0) ? informacoes.getPorcentagemRMI() : 100;
	// 		float reajuste = 1;
	// 		int confirmadoData = 0;

	// 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	// 		int contadorMes13salrio = 0;
	// 		boolean salario13Obrigatorio = informacoes.isSalario13Obrigatorio();
	// 		if (anoCalculo <= anoDip) {
	// 			while (anoCalculo != anoDip + 1) {

	// 				SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
	// 				SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
	// 				String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

	// 				// coloca o reajuste
	// 				if (mesCalculo == 1) {
	// 					// quando ele e baseado no salario minimo entao o valor do reajuste anual e
	// 					// sempre igual ao salario minimo
	// 					if (!informacoes.isSalarioMinimo()) {
	// 						rmi = rmi * reajusteAcumulado;
	// 					}
	// 					reajuste = reajusteAcumulado;
	// 				}
	// 				// verifica se no calculo tem o juros
	// 				if (informacoes.isJuros() && informacoes.getIncioJuros() != null) {
	// 					jurosAcumulado = calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao, anoAtualizacao,
	// 							mesIncioJuros, anoIncioJuros, dateFormat);
	// 				}
	// 				correcaoAcumulada = calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
	// 						anoAtualizacao, dateFormat);

	// 				if (informacoes.isSalarioMinimo()
	// 						|| rmi < salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo)) {
	// 					rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);
	// 					if (informacoes.isSalarioMinimo() == false) {
	// 						informacoes.setSalarioMinimo(true);
	// 					}
	// 				}
	// 				// estancia o objeto e adiciona na lista
	// 				CalculoDTO calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, correcaoAcumulada, jurosAcumulado,
	// 						porcentagemRmi);
	// 				// realizar a verificacao se as datas estao dentro do periodo de calculo;
	// 				if (!verificarPeriodo(mesCalculo, anoCalculo, mesDib, anoDib)) {
	// 					reajuste = 1;
	// 					listCalculo.add(calculoAdd);
	// 				}
	// 				// Instant instant = dt.toInstant();
	// 				// Instant nextDay = instant.plus(1, ChronoUnit.DAYS);
	// 				// System.out.println("Tomorrow: " + nextDay);

	// 				// System.out.println("Daqui há dez dias: " + dataFormatada.format(a));
	// 				if (informacoes.isSalario13() && !verificarPeriodo(mesCalculo, anoCalculo, mesDib, anoDib)) {
	// 					if (anoCalculo == anoDib && mesCalculo == mesDib) {
	// 						DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	// 						LocalDate dt = LocalDate.parse(informacoes.getDib(), parser);
	// 						LocalDate diaSeguinte = dt.plusDays(15);
	// 						int diaDib = Integer.parseInt(arrayDib[0]);
	// 						if (Integer.parseInt(parser.format(diaSeguinte).split("/")[0]) == 31) {
	// 							contadorMes13salrio++;
	// 						} else if (diaDib <= 15) {
	// 							contadorMes13salrio++;
	// 						}
	// 					} else {
	// 						contadorMes13salrio++;
	// 					}

	// 					calculoAdd = salario13(mesCalculo, anoCalculo, rmi, contadorMes13salrio, correcaoAcumulada,
	// 							jurosAcumulado, porcentagemRmi, salario13Obrigatorio, mesDip, anoDip);
	// 					if (!verificarPeriodo(mesCalculo, anoCalculo, mesDib, anoDib)) {
	// 						if (calculoAdd != null) {
	// 							if (anoCalculo == anoDip && mesCalculo == mesDip) {
	// 								int diaDip = Integer.parseInt(arrayDip[0]);
	// 								if (diaDip == 31) {
	// 									listCalculo.add(calculoAdd);
	// 								} else if (salario13Obrigatorio) {
	// 									listCalculo.add(calculoAdd);
	// 								}
	// 							} else {
	// 								listCalculo.add(calculoAdd);
	// 							}
	// 							contadorMes13salrio = 0;
	// 						}
	// 					}

	// 				}
	// 				// para o calculo
	// 				if (mesDip == mesCalculo && anoCalculo == anoDip) {
	// 					return listCalculo;
	// 				}
	// 				// verifica a data para fazer o colocar o reajuste
	// 				if (mesCalculo == 1) {
	// 					reajusteAcumulado = calculoReajuste(mesCalculo, anoCalculo, listReajuste, dateFormat);
	// 				} else if (confirmadoData == 0) {

	// 					reajusteAcumulado = calculoReajuste(mesCalculo, anoCalculo, listReajuste, dateFormat);
	// 				}
	// 				// faz a progressao da data
	// 				mesCalculo++;
	// 				if (mesCalculo == 13) {
	// 					mesCalculo = 01;
	// 					anoCalculo++;
	// 				}
	// 				confirmadoData++;
	// 			}
	// 		}
	// 		return listCalculo;
	// 	} catch (Exception e) {
	// 		System.err.println(e);
	// 		return e;
	// 	}
	// }

	// public Object alcada(InfoCalculoDTO informacoes) {
	// 	try {
	// 		// System.out.println("alcada");
	// 		// System.out.println("alcada: " + informacoes.getDib());
	// 		// System.out.println("alcada: " + informacoes.getDip());
	// 		// System.out.println("alcada: " + informacoes.getAtulizacao());
	// 		String[] arrayDib = informacoes.getDib().split("/");
	// 		int mesDib = Integer.parseInt(arrayDib[1]);
	// 		int anoDib = Integer.parseInt(arrayDib[2]);
	// 		String[] arrayDip = informacoes.getDip().split("/");
	// 		int mesDip = Integer.parseInt(arrayDip[1]);
	// 		int anoDip = Integer.parseInt(arrayDip[2]);
	// 		String[] arrayAtualizacao = informacoes.getAtulizacao().split("/");
	// 		int mesAtualizacao = Integer.parseInt(arrayAtualizacao[0]);
	// 		int anoAtualizacao = Integer.parseInt(arrayAtualizacao[1]);

	// 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	// 		List<Correcao> listCorrecao = correcaoRepository
	// 				.findByTipoOrderByDataAsc(informacoes.getTipoCorrecao());
	// 		int mesCalculo = mesDib;
	// 		int anoCalculo = anoDib;

	// 		List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();

	// 		float correcaoAcumulada = 1;

	// 		if (anoCalculo <= anoDip) {
	// 			while (anoCalculo != anoDip + 1) {

	// 				SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
	// 				SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
	// 				String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

	// 				correcaoAcumulada = calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
	// 						anoAtualizacao, dateFormat);

	// 				// estancia o objeto e adiciona na lista
	// 				CalculoDTO calculoAdd = new CalculoDTO(dataCalculo, 0, 0, correcaoAcumulada, 0, 100);
	// 				listCalculo.add(calculoAdd);
	// 				// para o calculo
	// 				if (mesDip == mesCalculo && anoCalculo == anoDip) {
	// 					return listCalculo;
	// 				}
	// 				// faz a progressao da data
	// 				mesCalculo++;
	// 				if (mesCalculo == 13) {
	// 					mesCalculo = 01;
	// 					anoCalculo++;
	// 				}

	// 			}
	// 		}
	// 		return listCalculo;
	// 	} catch (Exception e) {
	// 		System.err.println(e);
	// 		return e;
	// 	}
	// }

	// public Object beneficioAcumulado(InfoCalculoDTO informacoes) {
	// 	try {
	// 		// System.out.println("AQUI");
	// 		// System.out.println(informacoes);
	// 		// System.out.println("salario: " + informacoes.getDib());
	// 		String[] arrayDib = informacoes.getDib().split("/");
	// 		int mesDib = Integer.parseInt(arrayDib[1]);
	// 		int anoDib = Integer.parseInt(arrayDib[2]);
	// 		String[] arrayDip = informacoes.getDip().split("/");
	// 		int mesDip = Integer.parseInt(arrayDip[1]);
	// 		int anoDip = Integer.parseInt(arrayDip[2]);

	// 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	// 		float reajusteAcumulado = 1;

	// 		int mesCalculo = mesDib;
	// 		int anoCalculo = anoDib;
	// 		float rmi = informacoes.getRmi();
	// 		float porcentagemRmi = (informacoes.getPorcentagemRMI() != 0) ? informacoes.getPorcentagemRMI() : 100;
	// 		float reajuste = 1;
	// 		int confirmadoData = 0;

	// 		List<TaxaReajuste> listReajuste = reajusteRepositorio.findAll();

	// 		List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();

	// 		List<SalarioMinimo> listaSalarioMinimo = new ArrayList<SalarioMinimo>();
	// 		boolean salario13Obrigatorio = informacoes.isSalario13Obrigatorio();
	// 		if (informacoes.isSalarioMinimo() || informacoes.isLimiteMinimoMaximo()) {
	// 			listaSalarioMinimo = salarioMinimoRepository.findAll();
	// 		}

	// 		if (anoCalculo <= anoDip) {
	// 			while (anoCalculo != anoDip + 1) {

	// 				SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
	// 				SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
	// 				String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

	// 				// coloca o reajuste
	// 				if (mesCalculo == 1) {
	// 					if (!informacoes.isSalarioMinimo()) {
	// 						rmi = rmi * reajusteAcumulado;
	// 					}
	// 					reajuste = reajusteAcumulado;
	// 				}
	// 				if (informacoes.isSalarioMinimo()
	// 						|| rmi < salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo)) {
	// 					rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);
	// 					if (informacoes.isSalarioMinimo() == false) {
	// 						informacoes.setSalarioMinimo(true);
	// 					}
	// 				}

	// 				// estancia o objeto e adiciona na lista
	// 				CalculoDTO calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, 0, 0, porcentagemRmi);
	// 				reajuste = 1;
	// 				listCalculo.add(calculoAdd);
	// 				if (informacoes.isSalario13()) {
	// 					calculoAdd = salario13(mesCalculo, anoCalculo, rmi, 12, 0, 0, porcentagemRmi,
	// 							salario13Obrigatorio, mesDip, anoDip);
	// 					if (calculoAdd != null) {
	// 						if (anoCalculo == anoDip && mesCalculo == mesDip) {
	// 							int diaDip = Integer.parseInt(arrayDip[0]);
	// 							if (diaDip == 31) {
	// 								listCalculo.add(calculoAdd);
	// 							} else if (salario13Obrigatorio) {
	// 								listCalculo.add(calculoAdd);
	// 							}
	// 						} else {
	// 							listCalculo.add(calculoAdd);
	// 						}
	// 					}
	// 				}
	// 				// para o calculo
	// 				if (mesDip == mesCalculo && anoCalculo == anoDip) {
	// 					return listCalculo;
	// 				}
	// 				// verifica a data para fazer o colocar o reajuste
	// 				if (mesCalculo == 1 || confirmadoData == 0) {
	// 					reajusteAcumulado = calculoReajuste(mesCalculo, anoCalculo, listReajuste, dateFormat);
	// 				}
	// 				// faz a progressao da data
	// 				mesCalculo++;
	// 				if (mesCalculo == 13) {
	// 					mesCalculo = 01;
	// 					anoCalculo++;
	// 				}
	// 				confirmadoData++;
	// 			}
	// 		}

	// 		return listCalculo;

	// 	} catch (Exception e) {
	// 		System.err.println(e);
	// 		return e;
	// 	}
	// }

	// public Object taxaUnicoa(InfoCalculoDTO informacoes) {
	// 	try {
	// 		String[] arrayDib = informacoes.getDib().split("/");
	// 		int mesDib = Integer.parseInt(arrayDib[1]);
	// 		int anoDib = Integer.parseInt(arrayDib[2]);
	// 		String[] arrayAtualizacao = informacoes.getAtulizacao().split("/");
	// 		int mesAtualizacao = Integer.parseInt(arrayAtualizacao[0]);
	// 		int anoAtualizacao = Integer.parseInt(arrayAtualizacao[1]);
	// 		List<Correcao> listCorrecao = correcaoRepository
	// 				.findByTipoOrderByDataAsc(informacoes.getTipoCorrecao());
	// 		int mesIncioJuros = mesDib - 1;
	// 		int anoIncioJuros = anoDib;
	// 		if (mesIncioJuros == 0) {
	// 			mesIncioJuros = 12;
	// 			anoIncioJuros--;
	// 		}
	// 		List<Juros> listJuros = jurosRepositorio.findByTipoOrderByDataAsc(informacoes.getTipoJuros());
	// 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	// 		float correcaoAcumulada = calculoCorrecao(mesDib, anoDib, listCorrecao, mesAtualizacao, anoAtualizacao,
	// 				dateFormat);
	// 		float jurosAcumulado = calculoJuros(mesDib, anoDib, listJuros, mesAtualizacao, anoAtualizacao,
	// 				mesIncioJuros, anoIncioJuros, dateFormat);
	// 		CalculoDTO calculoAdd = new CalculoDTO(informacoes.getDib(), 0, 0, correcaoAcumulada, jurosAcumulado, 0);

	// 		return calculoAdd;
	// 	} catch (Exception e) {
	// 		System.err.println(e);
	// 		return e;
	// 	}
	// }

	// // ele compara as datas, se data fornecida for menor que a data informada ele
	// // retorna true
	// public boolean verificarPeriodo(int mesFornecido, int anoFornecido, int mesInformado, int anoInformado) {
	// 	if (anoFornecido <= anoInformado) {
	// 		if (anoFornecido == anoInformado) {
	// 			if (mesFornecido < mesInformado) {

	// 				return true;
	// 			}
	// 		} else {
	// 			return true;
	// 		}
	// 	}
	// 	return false;
	// }

	// public float salarioMinimo(int mesCalculo, int anoCalculo, DateFormat dateFormat,
	// 		List<SalarioMinimo> listaSalarioMinimo) {
	// 	float salariominimo = 0;
	// 	for (int i = 0; i < listaSalarioMinimo.size(); i++) {
	// 		String strDate = dateFormat.format(listaSalarioMinimo.get(i).getData());
	// 		int ano = Integer.parseInt(strDate.split(" ")[0].split("-")[0]);
	// 		int mes = Integer.parseInt(strDate.split(" ")[0].split("-")[1]);
	// 		if (ano == anoCalculo) {
	// 			if (mes <= mesCalculo) {
	// 				salariominimo = listaSalarioMinimo.get(i).getValor();
	// 			}
	// 		} else if (ano >= anoCalculo) {
	// 			return salariominimo;
	// 		}
	// 	}
	// 	return salariominimo;
	// }

	// /*
	//  * pega o valor do juros correspondente ao mes do calculo, faz o calculo do
	//  * juros somando os valos mes a mes a patir da data de atualizaca ate chegar a
	//  * data do calculo ou data de inicio do juros, sempre a data de atulizacao o
	//  * valor do juros é 0
	//  */
	// public float calculoJuros(int mesCalculo, int anoCalculo, List<Juros> listJuros, int mesAtualizacao,
	// 		int anoAtualizacao, int mesIncioJuros, int anoIncioJuros, DateFormat dateFormat) {
	// 	float jurosAcumulado = 0;

	// 	try {
	// 		for (int indexJuros = listJuros.size() - 1; indexJuros >= 0; indexJuros--) {
	// 			String[] dataJuros = dateFormat.format(listJuros.get(indexJuros).getData()).split(" ")[0].split("-");
	// 			int mesJuros = Integer.parseInt(dataJuros[1]);
	// 			int anoJuros = Integer.parseInt(dataJuros[0]);
	// 			if (verificarPeriodo(mesIncioJuros, anoIncioJuros, mesJuros, anoJuros)
	// 					&& verificarPeriodo(mesJuros, anoJuros, mesAtualizacao, anoAtualizacao)) {
	// 				jurosAcumulado += listJuros.get(indexJuros).getJuros();

	// 			}
	// 			if (mesJuros == mesCalculo && anoCalculo == anoJuros) {
	// 				return jurosAcumulado;
	// 			}
	// 		}
	// 		return 0;
	// 	} catch (Exception e) {
	// 		System.out.println(e);
	// 		return jurosAcumulado;
	// 	}
	// }

	// /*
	//  * pega o valor de correcao correspondente ao mes do calculo, faz o calculo da
	//  * correcao multiplicando os valos mes a mes a patir da data de atualizaca ate
	//  * chegar a data do calculo, sempre a data de atulizacao o valor da correcao é 1
	//  */
	// public float calculoCorrecao(int mesCalculo, int anoCalculo, List<Correcao> listCorrecao, int mesAtualizacao,
	// 		int anoAtualizacao, DateFormat dateFormat) {
	// 	float correcaoAcumulada = 1;
	// 	try {
	// 		for (int indexCorrecao = listCorrecao.size() - 1; indexCorrecao >= 0; indexCorrecao--) {

	// 			String[] data = dateFormat.format(listCorrecao.get(indexCorrecao).getData()).split(" ")[0].split("-");
	// 			int mesCorrecao = Integer.parseInt(data[1]);
	// 			int anoCorrecao = Integer.parseInt(data[0]);
	// 			if (mesCalculo == 2 && anoCalculo == 2022 && anoCorrecao > 2021) {
	// 				// System.out.println("correcao acumuulada: " + correcaoAcumulada);
	// 				// System.out.println("correcao acumuulada: " + mesAtualizacao);
	// 				// System.out.println("correcao acumuulada: " + anoAtualizacao);
	// 				// System.out.println("correcao acumuulada: " + mesAtualizacao);
	// 				// System.out.println("correcao acumuulada: " + anoAtualizacao);
	// 				// System.out.println("teste mes: " + mesCorrecao);
	// 				// System.out.println("teste ano: " + anoCorrecao);
	// 				// System.out.println("teste boolean: " + (mesCorrecao == mesCalculo &&
	// 				// anoCalculo == anoCorrecao));
	// 			}
	// 			if (verificarPeriodo(mesCorrecao, anoCorrecao, mesAtualizacao, anoAtualizacao)) {
	// 				correcaoAcumulada *= listCorrecao.get(indexCorrecao).getTaxaCorrecao();
	// 			}
	// 			if (mesCorrecao == mesCalculo && anoCalculo == anoCorrecao) {
	// 				return correcaoAcumulada;
	// 			}
	// 		} // termino do laco for da correcao;
	// 		return 1;
	// 	} catch (Exception e) {
	// 		System.out.println(e);
	// 		return correcaoAcumulada;
	// 	}

	// }

	// public CalculoDTO salario13(int mesCalculo, int anoCalculo, float rmi, int contadorMes13salrio,
	// 		float correcaoAcumulada, float jurosAcumulado, float porcentagemRmi, boolean salario13Obrigatorio,
	// 		int mesDip, int anoDip) {
	// 	CalculoDTO salario13;
	// 	rmi = rmi * contadorMes13salrio / 12;
	// 	if (mesCalculo == 12) {
	// 		salario13 = new CalculoDTO(("13Salario/12/" + anoCalculo), 1, rmi, correcaoAcumulada, jurosAcumulado,
	// 				porcentagemRmi);
	// 		return salario13;
	// 	} else if (mesCalculo == mesDip && anoCalculo == anoDip && salario13Obrigatorio) {
	// 		salario13 = new CalculoDTO(("13Salario/12/" + anoCalculo), 1, rmi, correcaoAcumulada, jurosAcumulado,
	// 				porcentagemRmi);
	// 		return salario13;
	// 	}
	// 	return null;
	// }

	// /*
	//  * ele pega o reajuste correspondente ao mes do calculo (obs: a data do reajuste
	//  * sempre inicia com data da dib ou dib-Anterior no primeiro ano, nos demais ano
	//  * é o valor do reajuste do primeiro mes do ano
	//  */
	// public float calculoReajuste(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
	// 		DateFormat dateFormat) {
	// 	float reajusteAcumulado = 1;
	// 	try {
	// 		for (int i = listReajuste.size() - 1; i >= 0; i--) {
	// 			String[] dataReajuste = dateFormat.format(listReajuste.get(i).getData()).split(" ")[0].split("-");
	// 			int mesReajuste = Integer.parseInt(dataReajuste[1]);
	// 			int anoReajuste = Integer.parseInt(dataReajuste[0]);

	// 			if (mesReajuste == mesCalculo && anoCalculo == anoReajuste) {
	// 				reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
	// 				return (float) (listReajuste.get(i).getReajusteAcumulado()) + 1;
	// 			}

	// 		} // termino do laco for do reajuste;
	// 		return 1;

	// 	} catch (Exception e) {
	// 		System.out.println(e);
	// 		return reajusteAcumulado;
	// 	}
	// }

}
