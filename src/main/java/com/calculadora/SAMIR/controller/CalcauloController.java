package com.calculadora.SAMIR.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.SAMIR.DTO.CalculoDTO;
import com.calculadora.SAMIR.DTO.CorrecaoDTO;
import com.calculadora.SAMIR.DTO.InfoCalculoDTO;
import com.calculadora.SAMIR.DTO.JurosDTO;
import com.calculadora.SAMIR.entities.SalarioMinimo;
import com.calculadora.SAMIR.entities.TaxaReajuste;
import com.calculadora.SAMIR.repositories.ReajusteRepositorio;
import com.calculadora.SAMIR.repositories.SalarioMinimoRepository;
import com.calculadora.SAMIR.services.CorrecaoService;
import com.calculadora.SAMIR.services.JurosService;

@RestController
@CrossOrigin
@RequestMapping("/calculo")
public class CalcauloController {

	@Autowired
	private ReajusteRepositorio reajusteRepositorio;
	@Autowired
	private JurosService jurosRepositorio;
	@Autowired
	private CorrecaoService correcaoRepository;
	@Autowired
	private SalarioMinimoRepository salarioMinimoRepository;

	@PostMapping("/calcular")
	public @ResponseBody Object calcular(@RequestBody InfoCalculoDTO informacoes) {
		try {
			 //System.out.println("AQUIIIIIIIIIIIIIIIIII "+ informacoes);
			// System.out.println("salario: " + informacoes.getAtulizacao());
			String[] arrayInicioCalculo = informacoes.getInicioCalculo().split("/");
			int mesInicioCalculo = Integer.parseInt(arrayInicioCalculo[1]);
			int anoInicioCalculo = Integer.parseInt(arrayInicioCalculo[2]);
			String[] arrayDip = informacoes.getDip().split("/");
			int mesDip = Integer.parseInt(arrayDip[1]);
			int anoDip = Integer.parseInt(arrayDip[2]);
			String[] arrayAtualizacao = informacoes.getAtulizacao().split("/");
			int mesAtualizacao = Integer.parseInt(arrayAtualizacao[0]);
			int anoAtualizacao = Integer.parseInt(arrayAtualizacao[1]);
			int mesIncioJuros = 0;
			int anoIncioJuros = 0;
			if (informacoes.getIncioJuros() != null) {
				String[] arrayInicioJuros = informacoes.getIncioJuros().split("/");
				if (arrayInicioJuros.length > 1) {
					mesIncioJuros = Integer.parseInt(arrayInicioJuros[1]) - 1;
					anoIncioJuros = Integer.parseInt(arrayInicioJuros[2]);
					if (mesIncioJuros == 0) {
						mesIncioJuros = 12;
						anoIncioJuros--;
					}
				}

			}
			
			//System.out.println("AQUI TESTANDO EM HOMENAGEM AO RAFA: "+ mesIncioJuros +" "+ anoIncioJuros);
			List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();
			List<SalarioMinimo> listaSalarioMinimo = new ArrayList<SalarioMinimo>();
			if (informacoes.isSalarioMinimo() || informacoes.isLimiteMinimoMaximo()) {
				listaSalarioMinimo = salarioMinimoRepository.findAll();
			}

			List<TaxaReajuste> listReajuste = reajusteRepositorio.findAll();
			List<CorrecaoDTO> listCorrecao = correcaoRepository
					.findByType(informacoes.getTipoCorrecao());
			List<JurosDTO> listJuros = new ArrayList<JurosDTO>();

			float correcaoAcumulada = 1;
			float jurosAcumulado = 0;
			float reajusteAcumulado = 1;
			
			int mesCalculo = mesInicioCalculo;
			int anoCalculo = anoInicioCalculo;

			try {
				if (informacoes.getDib() != null) {
					String[] arrayDib = informacoes.getDib().split("/");
					mesCalculo = Integer.parseInt(arrayDib[1]);
					anoCalculo = Integer.parseInt(arrayDib[2]);
				}
			} catch (Exception e) { 
				System.err.println(e);
			}
		

			float rmi = informacoes.getRmi();

			float porcentagemRmi = (informacoes.getPorcentagemRMI() != 0) ? informacoes.getPorcentagemRMI() : 100;
			float reajuste = 1;
			int confirmadoData = 0;

			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			int contadorMes13salrio = 0;
			boolean salario13Obrigatorio = informacoes.isSalario13Obrigatorio();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate selicDate = LocalDate.parse("01/11/2021", formatter);
			if(informacoes.isSelic()){
				listJuros = jurosRepositorio.findByType(0);
				for (int indexCorrecao = listCorrecao.size() - 1; indexCorrecao >= 0; indexCorrecao--) {
					if (selicDate.isBefore(listCorrecao.get(indexCorrecao).getData())) {
						listCorrecao.get(indexCorrecao).setPercentual(0);
					}
				}
			}else if(informacoes.isJuros()){
				listJuros = jurosRepositorio.findByType(informacoes.getTipoJuros());
			}
			List<SalarioMinimo> listaSalarioMinimoParaTaxaSelic = new ArrayList<SalarioMinimo>();
			listaSalarioMinimoParaTaxaSelic = salarioMinimoRepository.findAll();
			System.out.println(listaSalarioMinimoParaTaxaSelic);
			boolean dataMenorQueDoisMilEDez = false;
			boolean entrouNoIfParaDataReajuste = false;
			int mesReajusteSalarioMin = 0;
			boolean passYear = false;
			if (anoCalculo <= anoDip) {
				System.out.println("DEBTRI DO IIIIIIIIIFFFFFFFFFFFFFFFFFFFFFFFF " + mesCalculo);
				while (anoCalculo != anoDip + 1) {
					
 
					if(!entrouNoIfParaDataReajuste){
						mesReajusteSalarioMin = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						entrouNoIfParaDataReajuste = true;
					}
					// System.out.println("Chegou a data Selic: " +
					// selicDate.before(formatter.parse("01/" + mesCalculo + "/" + anoCalculo)) + "
					// Data calculo: " + formatter.parse("01/" + mesCalculo + "/" + anoCalculo));
					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
					String dataCalculo;
					 
					 if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {

						// quando ele e baseado no salario minimo entao o valor do reajuste anual e
						// sempre igual ao salario minimo
						if (!informacoes.isSalarioMinimo()) {
							rmi = rmi * reajusteAcumulado;
						}
						reajuste = reajusteAcumulado;
					}

					//System.out.println("TESTE123 "+ reajuste);
					// verifica se no calculo tem o juros
					if ((informacoes.isJuros() && informacoes.getIncioJuros() != null) || informacoes.isSelic()) {
						jurosAcumulado = calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao, anoAtualizacao,
								mesIncioJuros, anoIncioJuros, dateFormat);
						if (jurosAcumulado == 0) {
							jurosAcumulado = calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao,
									anoAtualizacao,
									mesIncioJuros, anoIncioJuros, dateFormat);
						}
					}
					correcaoAcumulada = calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
							anoAtualizacao, dateFormat);
					if (correcaoAcumulada == 1) {
						correcaoAcumulada = calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
								anoAtualizacao, dateFormat);
					}

					if (informacoes.isSalarioMinimo()
							|| rmi < salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo)) {
						rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);
						// System.out.println(rmi);
						if (informacoes.isSalarioMinimo() == false) {
							informacoes.setSalarioMinimo(true);
						}
					}
					CalculoDTO calculoAdd;
					// estancia o objeto e adiciona na lista
					//System.out.println("datacaulo "+ dataCalculo);
					/* if(reajusteAcumulado != 1){
						System.out.println("ENTROU IFFFFFFFFFFFFFFFFFFFFFF " + mesCalculo);
						int mesReajuste = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesReajuste + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
					}else{ */
						System.out.println("ENTROU IFFFFFFFFFFF 2222222222222 " + mesCalculo);
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
					/* }  */
 
					 
	 	
							
					// realizar a verificacao se as datas estao dentro do periodo de calculo;
					if (!verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
						reajuste = 1;
						listCalculo.add(calculoAdd);
					}
					

					// System.out.println("Daqui há dez dias: " + dataFormatada.format(a));
					if (informacoes.isSalario13()
							&& !verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
						if (anoCalculo == anoInicioCalculo && mesCalculo == mesInicioCalculo) {
							DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");
							LocalDate dt = LocalDate.parse(informacoes.getInicioCalculo(), parser);
							LocalDate diaSeguinte = dt.plusDays(15);
							int diaDib = Integer.parseInt(arrayInicioCalculo[0]);
							if (Integer.parseInt(parser.format(diaSeguinte).split("/")[0]) == 31) {
								contadorMes13salrio++;
							} else if (diaDib <= 15) {
								contadorMes13salrio++;
							}
						} else {
							contadorMes13salrio++;
						}

						calculoAdd = salario13(mesCalculo, anoCalculo, rmi, contadorMes13salrio, correcaoAcumulada,
								jurosAcumulado, porcentagemRmi, salario13Obrigatorio, mesDip, anoDip);
						if (!verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
							if (calculoAdd != null) {
								if (anoCalculo == anoDip && mesCalculo == mesDip) {
									int diaDip = Integer.parseInt(arrayDip[0]);
									if (diaDip == 31) {
										listCalculo.add(calculoAdd);
									} else if (salario13Obrigatorio) {
										listCalculo.add(calculoAdd);
									}
								} else {
									listCalculo.add(calculoAdd);
								}
								contadorMes13salrio = 0;
							}
						}

					}
					// para o calculo
					if (mesDip == mesCalculo && anoCalculo == anoDip) {
						//System.out.println("RETORNOOOOOOOOOOOOOOOO " + listCalculo);
						return listCalculo;
					}

					// verifica a data para fazer o colocar o reajuste
					System.out.println("TESTE " + mesCalculo);
					        String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
							int mesDibAnterio = Integer.parseInt(arrayDibAnterior[1]);
							int anoDibAnterio = Integer.parseInt(arrayDibAnterior[2]);
					float mesResjusteSalarioMinimo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
					System.out.println("REAJUSTEEEEEEEEEEEE " + mesResjusteSalarioMinimo + " Ano: "+ anoCalculo);
				 if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
						System.out.println("entrou de primeira aqui????????????????????????????");
						System.out.println("entrou 0" + mesCalculo);
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
					} else if (confirmadoData == 0) { 
						System.out.println("OU AQUI???????????????????????????????");
						if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								System.out.println("entrou 1");
								//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
								//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
								System.out.println("MES CAUCLO " + mesCalculo);
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else if (anoDibAnterio == anoInicioCalculo && mesDibAnterio < mesIncioJuros) {
								System.out.println("entrou 2");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesDibAnterio, anoCalculo, listReajuste,
										dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else {
								System.out.println("entrou 3");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else { 
							System.out.println("entrou 4");
							reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}
					}
					// faz a progressao da data
					dataMenorQueDoisMilEDez = false;




					mesCalculo++;
					if (mesCalculo == 13) {
						mesCalculo = 01;
						anoCalculo++;
						entrouNoIfParaDataReajuste = false;
						passYear = true;
						
					}
					confirmadoData++;
					
				}
			}
			//System.out.println("RETORNOOOOOOOOOOOOOOOOOOOO "+ listCalculo);
			return listCalculo;
		} catch (Exception e) {
			System.err.println(e);
			return e;
		}
	}

	@PostMapping("/alcada")
	public @ResponseBody Object alcada(@RequestBody InfoCalculoDTO informacoes) {
		try {
			// System.out.println("alcada");
			// System.out.println("alcada: " + informacoes.getDib());
			// System.out.println("alcada: " + informacoes.getDip());
			// System.out.println("alcada: " + informacoes.getAtulizacao());
			String[] arrayDib = informacoes.getInicioCalculo().split("/");
			int mesDib = Integer.parseInt(arrayDib[1]);
			int anoDib = Integer.parseInt(arrayDib[2]);
			String[] arrayDip = informacoes.getDip().split("/");
			int mesDip = Integer.parseInt(arrayDip[1]);
			int anoDip = Integer.parseInt(arrayDip[2]);
			String[] arrayAtualizacao = informacoes.getAtulizacao().split("/");
			int mesAtualizacao = Integer.parseInt(arrayAtualizacao[0]);
			int anoAtualizacao = Integer.parseInt(arrayAtualizacao[1]);

			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			List<CorrecaoDTO> listCorrecao = correcaoRepository
					.findByType(informacoes.getTipoCorrecao());
			int mesCalculo = mesDib;
			int anoCalculo = anoDib;

			List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();

			float correcaoAcumulada = 1;

			if (anoCalculo <= anoDip) {
				while (anoCalculo != anoDip + 1) {

					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
					String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

					correcaoAcumulada = calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
							anoAtualizacao, dateFormat);

					// estancia o objeto e adiciona na lista
					CalculoDTO calculoAdd = new CalculoDTO(dataCalculo, 0, 0, correcaoAcumulada, 0, 100);
					listCalculo.add(calculoAdd);
					// para o calculo
					if (mesDip == mesCalculo && anoCalculo == anoDip) {
						return listCalculo;
					}
					// faz a progressao da data
					mesCalculo++;
					if (mesCalculo == 13) {
						mesCalculo = 01;
						anoCalculo++;
					}

				}
			}
			return listCalculo;
		} catch (Exception e) {
			System.err.println(e);
			return e;
		}
	}

	@PostMapping("/beneficioAcumulado")
	public @ResponseBody Object beneficioAcumulado(@RequestBody InfoCalculoDTO informacoes) {
		try {

			 System.out.println("yeaaaaaaaah " + informacoes);
			// System.out.println(informacoes);
			// System.out.println("salario: " + informacoes.getDib());
			System.out.println("CHEGOU   " + informacoes);
			String[] arrayDib = informacoes.getInicioCalculo().split("/");
			int mesDib = Integer.parseInt(arrayDib[1]);
			int anoDib = Integer.parseInt(arrayDib[2]);
			System.out.println("ACUMULADOS "+ arrayDib +" "+ mesDib +" "+ anoDib);
			String[] arrayDip = informacoes.getDip().split("/");
			int mesDip = Integer.parseInt(arrayDip[1]);
			int anoDip = Integer.parseInt(arrayDip[2]);

			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			float reajusteAcumulado = 1;

			int mesCalculo = mesDib;
			int anoCalculo = anoDib;
			float rmi = informacoes.getRmi();
			System.out.println("informacoes.getPorcentagemRMI(): " + informacoes.getPorcentagemRMI());
			float porcentagemRmi = (informacoes.getPorcentagemRMI() != 0) ? informacoes.getPorcentagemRMI() : 100;
			float reajuste = 1;
			int confirmadoData = 0;

			List<TaxaReajuste> listReajuste = reajusteRepositorio.findAll();

			List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();

			List<SalarioMinimo> listaSalarioMinimo = new ArrayList<SalarioMinimo>();
			boolean salario13Obrigatorio = informacoes.isSalario13Obrigatorio();
			if (informacoes.isSalarioMinimo() || informacoes.isLimiteMinimoMaximo()) {
				listaSalarioMinimo = salarioMinimoRepository.findAll();
			}

			if (anoCalculo <= anoDip) {
				while (anoCalculo != anoDip + 1) {

					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
					String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

					// coloca o reajuste
					if (mesCalculo == 1) {
						if (!informacoes.isSalarioMinimo()) {
							rmi = rmi * reajusteAcumulado;
						}
						reajuste = reajusteAcumulado;
					}
					if (informacoes.isSalarioMinimo()
							|| rmi < salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo)) {
						rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);
						if (informacoes.isSalarioMinimo() == false) {
							informacoes.setSalarioMinimo(true);
						}
					}

					// estancia o objeto e adiciona na lista
					CalculoDTO calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, 0, 0, porcentagemRmi);
					reajuste = 1;
					listCalculo.add(calculoAdd);
					if (informacoes.isSalario13()) {
						calculoAdd = salario13(mesCalculo, anoCalculo, rmi, 12, 0, 0, porcentagemRmi,
								salario13Obrigatorio, mesDip, anoDip);
						if (calculoAdd != null) {
							if (anoCalculo == anoDip && mesCalculo == mesDip) {
								int diaDip = Integer.parseInt(arrayDip[0]);
								if (diaDip == 31) {
									listCalculo.add(calculoAdd);
								} else if (salario13Obrigatorio) {
									listCalculo.add(calculoAdd);
								}
							} else {
								listCalculo.add(calculoAdd);
							}
						}
					}
					// para o calculo
					if (mesDip == mesCalculo && anoCalculo == anoDip) {
						return listCalculo;
					}
					// verifica a data para fazer o colocar o reajuste
					System.out.println(mesCalculo);
					if (mesCalculo == 1) {
						reajusteAcumulado = calculoReajusteAcumulados(mesCalculo, anoCalculo, listReajuste, dateFormat);
						System.out.println("VERIFY INACUMULADOS  "+ reajusteAcumulado +" " + mesCalculo +" "+ anoCalculo);
					} else if (confirmadoData == 0) {
						if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
							int mesDibAnterio = Integer.parseInt(arrayDibAnterior[1]);
							int anoDibAnterio = Integer.parseInt(arrayDibAnterior[2]);
							System.out.println("DIB ANTERIOR ACUMULADO = "+ mesDibAnterio +" "+ anoDibAnterio);
							System.out.println("DIB ANTERIOR ACUMULADO = "+ anoDibAnterio +" "+ mesDib);
							if (anoDibAnterio < anoDib) {
								System.out.println("entrou INACUMULADOS 1");
								reajusteAcumulado = calculoReajusteAcumulados(1, anoCalculo, listReajuste, dateFormat);
							} else if (anoDibAnterio == anoDib && mesDibAnterio < mesDib) {
								System.out.println("entrou 2 INACUMULADOS");
								reajusteAcumulado = calculoReajusteAcumulados(mesDibAnterio, anoCalculo, listReajuste,
										dateFormat);
							} else {
								System.out.println("entrou 3 INACUMULADOS");
								reajusteAcumulado = calculoReajusteAcumulados(mesCalculo, anoCalculo, listReajuste, dateFormat);
							}
						} else {
							System.out.println("entrou 4 INACUMULADOS");
							System.out.println(mesCalculo+" "+ anoCalculo);
							reajusteAcumulado = calculoReajusteAcumulados(mesCalculo, anoCalculo, listReajuste, dateFormat);
						}
					}
					// faz a progressao da data
					mesCalculo++;
					if (mesCalculo == 13) {
						mesCalculo = 01;
						anoCalculo++;
					}
					confirmadoData++;
				}
			}

			return listCalculo;

		} catch (Exception e) {
			System.err.println(e);
			return e;
		}
	}

	@PostMapping("/taxaUnica")
	public Object taxaUnicoa(@RequestBody InfoCalculoDTO informacoes) {
		try {
			String[] arrayDib = informacoes.getInicioCalculo().split("/");
			int mesDib = Integer.parseInt(arrayDib[1]);
			int anoDib = Integer.parseInt(arrayDib[2]);
			String[] arrayAtualizacao = informacoes.getAtulizacao().split("/");
			int mesAtualizacao = Integer.parseInt(arrayAtualizacao[0]);
			int anoAtualizacao = Integer.parseInt(arrayAtualizacao[1]);
			List<CorrecaoDTO> listCorrecao = correcaoRepository
					.findByType(informacoes.getTipoCorrecao());
			int mesIncioJuros = mesDib - 1;
			int anoIncioJuros = anoDib;
			if (mesIncioJuros == 0) {
				mesIncioJuros = 12;
				anoIncioJuros--;
			}

			List<JurosDTO> listJuros = jurosRepositorio.findByType(informacoes.getTipoJuros());
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate selicDate = LocalDate.parse("01/11/2021", formatter);
			
			if(informacoes.isSelic()){
				listJuros = jurosRepositorio.findByType(0);
				for (int indexCorrecao = listCorrecao.size() - 1; indexCorrecao >= 0; indexCorrecao--) {
					if (selicDate.isBefore(listCorrecao.get(indexCorrecao).getData())) {
						listCorrecao.get(indexCorrecao).setPercentual(0);
					}
				}
			}else if(informacoes.isJuros()){
				listJuros = jurosRepositorio.findByType(informacoes.getTipoJuros());
			}

			// if (informacoes.isJuros()) {
			// 	if (informacoes.isSelic()
			// 			&& (selicDate.isBefore(LocalDate.of(anoDib, mesDib, 1)))
			// 			&& selicValidador) {
			// 		selicValidador = false;
			// 		informacoes.setSelic(false);
			// 		listCorrecao = new ArrayList<CorrecaoDTO>();
			// 		listJuros = jurosRepositorio.findByType(0);
			// 	} else {
			// 		listJuros = jurosRepositorio.findByType(informacoes.getTipoJuros());
			// 	}
			// }

			// if (informacoes.isSelic()) {
			// 	List<JurosDTO> listSelic = jurosRepositorio.findByType(0);
			// 	System.out.println(listCorrecao.get(0).getData());
			// 	System.out.println(listCorrecao.get(0).getData());

			// 	for (int indexJuros = listJuros.size() - 1; indexJuros >= 0; indexJuros--) {
			// 		if (selicDate.isBefore(listJuros.get(indexJuros).getData())) {
			// 			listJuros.remove(indexJuros);
			// 		}
			// 	}
			// 	listJuros.addAll(listSelic);
			// 	// for(JurosDTO jurosSelic : listJuros){
			// 	// System.out.println("Data do juros: " + formatter.format(jurosSelic.getData())
			// 	// + " valor juros " + jurosSelic.getJuros());
			// 	// }

			// 	for (int indexCorrecao = listCorrecao.size() - 1; indexCorrecao >= 0; indexCorrecao--) {
			// 		if (selicDate.isBefore(listCorrecao.get(indexCorrecao).getData())) {
			// 			listCorrecao.get(indexCorrecao).setPercentual(0);
			// 		}
			// 	}
			// 	// for(CorrecaoDTO correcaoSelic : listCorrecao){
			// 	// System.out.println(("Data do Correcao: " +
			// 	// formatter.format(correcaoSelic.getData()) + " valor correcao: " +
			// 	// correcaoSelic.getTaxaCorrecao()));
			// 	// }

			// }

			float correcaoAcumulada = calculoCorrecao(mesDib, anoDib, listCorrecao, mesAtualizacao, anoAtualizacao,
					dateFormat);
			float jurosAcumulado = calculoJuros(mesDib, anoDib, listJuros, mesAtualizacao, anoAtualizacao,
					mesIncioJuros, anoIncioJuros, dateFormat);
			CalculoDTO calculoAdd = new CalculoDTO(informacoes.getInicioCalculo(), 0, 0, correcaoAcumulada,
					jurosAcumulado, 0);

			return calculoAdd;
		} catch (Exception e) {
			System.err.println(e);
			return e;
		}
	}

	// ele compara as datas, se data fornecida for menor que a data informada ele
	// retorna true
	public boolean verificarPeriodo(int mesFornecido, int anoFornecido, int mesInformado, int anoInformado) {
		if (anoFornecido <= anoInformado) {
			if (anoFornecido == anoInformado) {
				if (mesFornecido < mesInformado) {

					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public float salarioMinimo(int mesCalculo, int anoCalculo, DateTimeFormatter dateFormat,
			List<SalarioMinimo> listaSalarioMinimo) {
		float salariominimo = 1;
		// System.out.println("mes: " + mesCalculo + " ano: " + anoCalculo);
		for (SalarioMinimo salarioMinimoList : listaSalarioMinimo) {

			int ano = salarioMinimoList.getData().getYear();
			int mes = salarioMinimoList.getData().getMonthValue();
			if (ano == anoCalculo) {
				if (mes <= mesCalculo) {
					salariominimo = salarioMinimoList.getValor();
					// System.out.println("mesSalario: " + mes + " anoSalrio: " + ano);
				}
			} else if (ano >= anoCalculo) {
				return salariominimo;
			}
		}

		return salariominimo;
	}

	/*
	 * pega o valor do juros correspondente ao mes do calculo, faz o calculo do
	 * juros somando os valos mes a mes a patir da data de atualizaca ate chegar a
	 * data do calculo ou data de inicio do juros, sempre a data de atulizacao o
	 * valor do juros é 0
	 */
	public float calculoJuros(int mesCalculo, int anoCalculo, List<JurosDTO> listJuros, int mesAtualizacao,
			int anoAtualizacao, int mesIncioJuros, int anoIncioJuros, DateTimeFormatter dateFormat) {
		float jurosAcumulado = 0;

		try {
			for (int indexJuros = listJuros.size() - 1; indexJuros >= 0; indexJuros--) {
				int mesJuros = listJuros.get(indexJuros).getData().getMonthValue();
				int anoJuros = listJuros.get(indexJuros).getData().getYear();

				if (verificarPeriodo(mesIncioJuros, anoIncioJuros, mesJuros, anoJuros)
						&& verificarPeriodo(mesJuros, anoJuros, mesAtualizacao, anoAtualizacao)) {
					jurosAcumulado += listJuros.get(indexJuros).getJuros();

				}
				if (mesJuros == 2 && anoJuros == 2021 && mesCalculo == 2 && anoCalculo == 2021) {
					System.out.println("juros!! mesCalculo: " + (mesCalculo == mesJuros) + " anoCalculo: "
							+ (anoCalculo == anoJuros) + " Certeza: " + mesJuros + "/" + anoJuros);
				}
				if (mesJuros == mesCalculo && anoCalculo == anoJuros) {
					return jurosAcumulado;
				}
			}
			System.out.println("Erro no juros!! mesCalculo: " + mesCalculo + " anoCalculo: " + anoCalculo);
			if (listJuros.get(0).getType() == 0) {
				return jurosAcumulado;
			}
			return 0;
		} catch (Exception e) {
			System.out.println(e);
			return jurosAcumulado;
		}
	}

	/*
	 * pega o valor de correcao correspondente ao mes do calculo, faz o calculo da
	 * correcao multiplicando os valos mes a mes a patir da data de atualizaca ate
	 * chegar a data do calculo, sempre a data de atulizacao o valor da correcao é 1
	 */
	public float calculoCorrecao(int mesCalculo, int anoCalculo, List<CorrecaoDTO> listCorrecao, int mesAtualizacao,
			int anoAtualizacao, DateTimeFormatter dateFormat) {
		float correcaoAcumulada = 1;
		try {
			for (int indexCorrecao = listCorrecao.size() - 1; indexCorrecao >= 0; indexCorrecao--) {
				int mesCorrecao = listCorrecao.get(indexCorrecao).getData().getMonthValue();
				int anoCorrecao = listCorrecao.get(indexCorrecao).getData().getYear();
				if (verificarPeriodo(mesCorrecao, anoCorrecao, mesAtualizacao, anoAtualizacao)) {
					correcaoAcumulada *= ((listCorrecao.get(indexCorrecao).getPercentual() / 100) + 1);
				}
				if (mesCorrecao == mesCalculo && anoCalculo == anoCorrecao) {
					return correcaoAcumulada;
				}
			} // termino do laco for da correcao;
			System.out.println("Erro no Correçao!! mesCalculo: " + mesCalculo + " anoCalculo: " + anoCalculo);
			return 1;
		} catch (Exception e) {
			System.out.println(e);
			return correcaoAcumulada;
		}

	}

	public CalculoDTO salario13(int mesCalculo, int anoCalculo, float rmi, int contadorMes13salrio,
			float correcaoAcumulada, float jurosAcumulado, float porcentagemRmi, boolean salario13Obrigatorio,
			int mesDip, int anoDip) {
		CalculoDTO salario13;
		rmi = rmi * contadorMes13salrio / 12;
		if (mesCalculo == 12) {
			salario13 = new CalculoDTO(("13Salario/12/" + anoCalculo), 1, rmi, correcaoAcumulada, jurosAcumulado,
					porcentagemRmi);
			return salario13;
		} else if (mesCalculo == mesDip && anoCalculo == anoDip && salario13Obrigatorio) {
			salario13 = new CalculoDTO(("13Salario/12/" + anoCalculo), 1, rmi, correcaoAcumulada, jurosAcumulado,
					porcentagemRmi);
			return salario13;
		}
		return null;
	}

	/*
	 * ele pega o reajuste correspondente ao mes do calculo (obs: a data do reajuste
	 * sempre inicia com data da dib ou dib-Anterior no primeiro ano, nos demais ano
	 * é o valor do reajuste do primeiro mes do ano
	 * 
	 * 
	 * 
	 * 
	 */

	public float calculoReajusteAcumulados(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
			DateTimeFormatter dateFormat) {
		float reajusteAcumulado = 1;

		try {
			for (int i = listReajuste.size() - 1; i >= 0; i--) {
				int mesReajuste = listReajuste.get(i).getData().getMonthValue();
				int anoReajuste = listReajuste.get(i).getData().getYear();
				//System.out.println("calculoReajuste. Mes:  " + mesReajuste + ". Ano: " + anoReajuste);
				if (mesReajuste == mesCalculo && anoCalculo == anoReajuste) {
					System.out.println("calculoReajuste. Mes:  " + mesReajuste + ". Ano: " + anoReajuste);
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					System.out.println(reajusteAcumulado);
					
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}





	public float calculoReajuste(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
			DateTimeFormatter dateFormat) {
		float reajusteAcumulado = 1;

		try {
			for (int i = listReajuste.size() - 1; i >= 0; i--) {
				int mesReajuste = listReajuste.get(i).getData().getMonthValue();
				int anoReajuste = listReajuste.get(i).getData().getYear();
				//System.out.println("calculoReajuste. Mes:  " + mesReajuste + ". Ano: " + anoReajuste);
				if (mesReajuste == mesCalculo && anoCalculo == anoReajuste) {
					System.out.println("calculoReajuste. Mes:  " + mesReajuste + ". Ano: " + anoReajuste);
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					System.out.println(reajusteAcumulado);
					
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}



	//DE TESTE
	public float dibAnteriorMenorAnoinicioCalculo(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
			DateTimeFormatter dateFormat, int mesDibAnterior, List<SalarioMinimo> listaSalarioMinimo, int anoInicioCalculo) {
		float reajusteAcumulado = 1;
		int mesSalarioMinimo = buscarMesReajuste(listaSalarioMinimo, anoCalculo);
		try {
			for (int i = listReajuste.size() - 1; i >= 0; i--) {
				int mesReajuste = listReajuste.get(i).getData().getMonthValue();
				int anoReajuste = listReajuste.get(i).getData().getYear();
				if(anoInicioCalculo == anoCalculo && mesDibAnterior == mesReajuste && anoInicioCalculo == anoReajuste){
					//System.out.println(anoInicioCalculo + " "+anoCalculo + " " + mesReajuste);
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//System.out.println("SADLKASÇDLADKALÇDDLDLAÇDKÇLADÇDKASÇL " + buscarMesReajuste(listaSalarioMinimo, anoCalculo));
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}


				if (mesReajuste == mesSalarioMinimo && anoCalculo == anoReajuste) {
					System.out.println("calculoReajuste. Mes:  " + mesReajuste + ". Ano: " + anoReajuste);
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//if(anoSalarioMinimo == )
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}


	public int buscarMesReajuste(List<SalarioMinimo> listaSalarioMinimo, int anoCalculo){
		try{
			for( int i = listaSalarioMinimo.size() - 1; i>=0; i--){
				int anoSalarioMinimo = listaSalarioMinimo.get(i).getData().getYear();
				if(anoSalarioMinimo == anoCalculo){
					return (int) listaSalarioMinimo.get(i).getData().getMonthValue();
				} 
			}
		} catch (Exception e){
			System.out.println(e);
		}
		return 12;
	}

	



	public List<TaxaReajuste> obterMaioresTaxasPorAnoDERajuste(List<TaxaReajuste> listReajuste) {
		List<TaxaReajuste> maioresTaxas = new ArrayList<TaxaReajuste>();
		int anoAtual = 0;
		double maiorTaxaAnoAtual = 0;

		for (TaxaReajuste objeto : listReajuste) {
			int anoObjeto = objeto.getData().getYear();
			if (anoObjeto != anoAtual) {
				objeto.setData(LocalDate.of(anoAtual, 1, 1));
				objeto.setReajusteAcumulado(maiorTaxaAnoAtual);
				maioresTaxas.add(objeto);
				anoAtual = anoObjeto;
				maiorTaxaAnoAtual = 0;
			}

			if (objeto.getReajusteAcumulado() > maiorTaxaAnoAtual) {
				maiorTaxaAnoAtual = objeto.getReajusteAcumulado();
			}
		}
		TaxaReajuste ultimoReajuste = listReajuste.get(0);
		ultimoReajuste.setData(LocalDate.of(anoAtual, 1, 1));
		ultimoReajuste.setReajusteAcumulado(maiorTaxaAnoAtual);
		maioresTaxas.add(ultimoReajuste);

		return maioresTaxas;
	}

}