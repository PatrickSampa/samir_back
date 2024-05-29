package com.calculadora.SAMIR.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Any;
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
import com.calculadora.SAMIR.entities.taxaIpcae;
import com.calculadora.SAMIR.repositories.IpcaeJurosRepository;
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

	@Autowired
	private IpcaeJurosRepository ipcaeJurosRepository;

	@PostMapping("/calcular")
	public @ResponseBody Object calcular(@RequestBody InfoCalculoDTO informacoes) {

		try {
			 if(informacoes.getDibAnterior() == ""){
				return CalcauloController2(informacoes);
			 }
			 
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
			float reajusteAcumuladoParaPrimeiroMes = 1;
			
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
			float reajusteParaPrimeiroMes = 1;
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
			
			boolean dataMenorQueDoisMilEDez = false;
			boolean entrouNoIfParaDataReajuste = false;
			int mesReajusteSalarioMin = 0;
			int mesReajusteSalarioMinAnteriorAoInicioDoCalculo = 0;
			boolean passYear = false;
			int contadorArrayQuantSalarioMinimo = 0;
			if (anoCalculo <= anoDip) {
				while (anoCalculo != anoDip + 1) {
					//System.out.println("PARA VERIFICAR O REAJUSTE 1= " + reajusteAcumulado  + " MES= " + mesCalculo + " ANO= " + anoCalculo);
					List<LocalDate> numerpReajustesSalarioMinimoEmUmAno = (existeMaisDeUmaAtualizacaoSoSalarioMinimoNoAno(listaSalarioMinimoParaTaxaSelic, anoCalculo));
					
					if(!entrouNoIfParaDataReajuste){
						mesReajusteSalarioMin = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						mesReajusteSalarioMinAnteriorAoInicioDoCalculo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, (anoCalculo - 1));
						entrouNoIfParaDataReajuste = true;
					}
					
					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
					String dataCalculo;
					 
					 if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {

						// quando ele e baseado no salario minimo entao o valor do reajuste anual e
						// sempre igual ao salario minimo
						/* if (!informacoes.isSalarioMinimo()) { */
						   							
							rmi = rmi * reajusteAcumulado;
							
						/* } */
						//System.out.println("TEM RMIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII" + reajusteAcumulado + " - " + rmi);
						/* if(contadorArrayQuantSalarioMinimo == 0){

							reajuste = reajusteAcumulado;  
						}
						
						contadorArrayQuantSalarioMinimo++;
						if(numerpReajustesSalarioMinimoEmUmAno.size() > 1 && contadorArrayQuantSalarioMinimo < numerpReajustesSalarioMinimoEmUmAno.size()){
							mesReajusteSalarioMin = numerpReajustesSalarioMinimoEmUmAno.get(contadorArrayQuantSalarioMinimo).getMonthValue();
						} */
					}
 


					if ((informacoes.isJuros() && informacoes.getIncioJuros() != null) || informacoes.isSelic()) {
						jurosAcumulado =  calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao, anoAtualizacao,
								mesIncioJuros, anoIncioJuros, dateFormat);
						if (jurosAcumulado == 0) {
							jurosAcumulado = calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao,
									anoAtualizacao,
									mesIncioJuros, anoIncioJuros, dateFormat);
						}
					}   

					correcaoAcumulada =  calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
							anoAtualizacao, dateFormat);

					if (correcaoAcumulada == 1) {
						correcaoAcumulada =  calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
								anoAtualizacao, dateFormat);
					}

					if (informacoes.isSalarioMinimo()
							|| rmi < salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo)) {
						rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);
						if (informacoes.isSalarioMinimo() == false) {
							informacoes.setSalarioMinimo(true);
						}
					}
	
					CalculoDTO calculoAdd;


						
						String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
							 boolean dibAnteriorTeste = String.valueOf(arrayDibAnterior.length).equals("1"); 
							int  mesDibAnterio = 0;
							int anoDibAnterio=0; 
							
							 if(!dibAnteriorTeste){
								mesDibAnterio = Integer.parseInt(arrayDibAnterior[1]);
								anoDibAnterio = Integer.parseInt(arrayDibAnterior[2]);
							 }
							

					float mesResjusteSalarioMinimo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
					
						if(!informacoes.isSelic()){
							jurosAcumulado = 0;
						}	 
						if(mesCalculo == 8 && anoCalculo == 2006){
							rmi = rmi * (float) 1.000096;
							dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, (float) 1.000096, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
							reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);

 
						}else if(mesReajusteSalarioMin >= mesInicioCalculo && mesCalculo == mesReajusteSalarioMin && anoCalculo == anoInicioCalculo){
							
							if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								//System.out.println("entrou 1 " + anoDibAnterio +" - " + anoInicioCalculo);
								//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
								//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
								//System.out.println("MES CAUCLO " + mesCalculo);
								reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(1, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else if (anoDibAnterio == anoInicioCalculo && mesDibAnterio < mesIncioJuros) {
								//System.out.println("entrou 2");
								reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesDibAnterio, (anoCalculo - 1), listReajuste,
										dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else {
								//System.out.println("entrou 3");
								reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesCalculo, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else { 
							//System.out.println("entrou 4 ");
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabelaSemADibAnterior(mesCalculo, anoCalculo, listReajuste, dateFormat, 1, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}

						
						if (!informacoes.isSalarioMinimo()) {
							rmi = rmi * reajusteAcumuladoParaPrimeiroMes;
						}
						reajusteParaPrimeiroMes = reajusteAcumuladoParaPrimeiroMes;
						
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						if(!informacoes.isSelic()){
							jurosAcumulado = 0;
						}	
							calculoAdd = new CalculoDTO(dataCalculo, reajusteParaPrimeiroMes, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);


						}else{
							//System.out.println("CORRECAO ACUMULADA ANTES DE ENTYRAR NO OBJETO3 = " + reajuste);
							
							//System.out.println("ENTROU NO ELSE " + mesCalculo + " - " + reajuste + " - " + reajusteAcumulado);
							dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
							if(!informacoes.isSelic()){
							jurosAcumulado = 0;
						}	
							calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, (float) correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
						}
						//System.out.println(calculoAdd);

						///System.out.println("PARA VERIFICAR O REAJUSTE 2= " + reajusteAcumulado);
					 
	 	
 

					//System.out.println("Verificar Periodo = " + verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo));
					if (!verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
						reajuste = 1;
						listCalculo.add(calculoAdd);
					}else{
						reajuste = 1;
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


					//System.out.println("REAJUSTEEEEEEEEEEEE " + mesResjusteSalarioMinimo + " Ano: "+ anoCalculo + " Mes" + mesCalculo);
				 if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
					

						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
					} else if (confirmadoData == 0) { 
						//System.out.println("OU AQUI???????????????????????????????");
						if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								//System.out.println("entrou 1 " + anoDibAnterio +" - " + anoInicioCalculo);
								//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
								//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
								//System.out.println("MES CAUCLO " + mesCalculo);
								reajusteAcumulado = ParaVerificarQuandoADibAnteriorForMenorQuEOAnoInicioCalculo(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
								//System.out.println("RETORNO = " + reajusteAcumulado);
							} else if (anoDibAnterio == anoInicioCalculo && mesDibAnterio < mesIncioJuros) {
								//System.out.println("entrou 2");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesDibAnterio, anoCalculo, listReajuste,
										dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else {
								//System.out.println("entrou 3");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else { 
							//System.out.println("entrou 4");
							reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}
					}else if(anoInicioCalculo == anoCalculo && mesReajusteSalarioMin == mesCalculo){
						//System.out.println("PEGOU DE 20199999999999");
						if (!informacoes.isSalarioMinimo()) {
						    System.out.println("SALARIOOOOOOOOOOOOOOOOOOOO MINIMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + rmi + " reajuste = " + reajusteAcumulado);							
							rmi = rmi * reajusteAcumulado;
						}
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesCalculo, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						//System.out.println("acumulados    =   " + reajusteAcumulado);
						
					}

					dataMenorQueDoisMilEDez = false;
					



					mesCalculo++;
					if (mesCalculo == 13) {
						mesCalculo = 01;
						anoCalculo++;
						contadorArrayQuantSalarioMinimo = 0;
						entrouNoIfParaDataReajuste = false;
						passYear = true;
						
					}
					confirmadoData++;
					
				}
			}

			return listCalculo;
		} catch (Exception e) {
			System.err.println(e + "ERRO NO DIBANTERIOR");
			return e;
		}
	}








	

	public List<CalculoDTO> CalcauloController2(InfoCalculoDTO informacoes){

		try { 
			  System.out.println("pra caaaaaaa");
			  System.out.println("INFORMACOESSSSSSSSSSSSSSSS");
			  System.out.println(informacoes);


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
			
			List<taxaIpcae> listTaxaIpcae = new ArrayList<taxaIpcae>();
			
			if(informacoes.getTipoCorrecao() == 6){

				listTaxaIpcae = ipcaeJurosRepository.findAllByOrderByDataAsc();

				informacoes.setTipoCorrecao(4);
			}


			System.out.println("AQUI TESTANDO EM HOMENAGEM AO RAFA: "+ listTaxaIpcae);
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
			float reajusteAcumuladoParaPrimeiroMes = 1;
			
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
			float reajusteParaPrimeiroMes = 1;
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
			//System.out.println(listaSalarioMinimoParaTaxaSelic);
			boolean dataMenorQueDoisMilEDez = false;
			boolean entrouNoIfParaDataReajuste = false;
			int mesReajusteSalarioMin = 0;
			int mesReajusteSalarioMinAnteriorAoInicioDoCalculo = 0;
			boolean passYear = false;
			int contadorArrayQuantSalarioMinimo = 0;
			if (anoCalculo <= anoDip) {
				//System.out.println(listJuros);
				//System.out.println(listCorrecao);
				//System.out.println(listCorrecao); 
				while (anoCalculo != anoDip + 1) {
					List<LocalDate> numerpReajustesSalarioMinimoEmUmAno = (existeMaisDeUmaAtualizacaoSoSalarioMinimoNoAno(listaSalarioMinimoParaTaxaSelic, anoCalculo));

					
					
					if(!entrouNoIfParaDataReajuste){
						System.out.println("ENTROU NO IFfff"); 
						buscarSegundoMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						mesReajusteSalarioMin = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						mesReajusteSalarioMinAnteriorAoInicioDoCalculo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, (anoCalculo - 1));
						entrouNoIfParaDataReajuste = true;
					}
					 
					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy"); 
					String dataCalculo;
					 

					/* System.out.println("verificar aqui");
					System.out.println(mesCalculo);
					System.out.println(mesReajusteSalarioMin);
					System.out.println(anoCalculo);
					System.out.println(anoInicioCalculo); */
					 if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
						/* System.out.println("ENTROU NO IF");
						System.out.println(mesCalculo);
						System.out.println(anoCalculo);
						System.out.println(anoInicioCalculo); */
						
						// quando ele e baseado no salario minimo entao o valor do reajuste anual e
						// sempre igual ao salario minimo
						
						if (!informacoes.isSalarioMinimo()) {
							rmi = rmi * reajusteAcumulado;
							
						} 
						
						/* if(contadorArrayQuantSalarioMinimo == 0){ */

							reajuste = reajusteAcumulado;  
						/* } */
						/* System.out.println(reajusteAcumulado);
						System.out.println(rmi);
						contadorArrayQuantSalarioMinimo++;
						if(numerpReajustesSalarioMinimoEmUmAno.size() > 1 && contadorArrayQuantSalarioMinimo < numerpReajustesSalarioMinimoEmUmAno.size()){
							System.out.println(numerpReajustesSalarioMinimoEmUmAno);
							mesReajusteSalarioMin = numerpReajustesSalarioMinimoEmUmAno.get(contadorArrayQuantSalarioMinimo).getMonthValue();
						}
						System.out.println("mesReajusteSalarioMin");
						System.out.println(mesReajusteSalarioMin); */

					}
					/* System.out.println("MINIMOOOOOOOOOOOOOO");
					System.out.println(rmi); */


					if ((informacoes.isJuros() && informacoes.getIncioJuros() != null) || informacoes.isSelic()) {
						jurosAcumulado =  calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao, anoAtualizacao,
								mesIncioJuros, anoIncioJuros, dateFormat);
								
						if (jurosAcumulado == 0) {
							jurosAcumulado = calculoJuros(mesCalculo, anoCalculo, listJuros, mesAtualizacao,
									anoAtualizacao,
									mesIncioJuros, anoIncioJuros, dateFormat);
						}
					}   

					correcaoAcumulada =  calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
							anoAtualizacao, dateFormat);
					if (correcaoAcumulada == 1) {
						correcaoAcumulada =  calculoCorrecao(mesCalculo, anoCalculo, listCorrecao, mesAtualizacao,
								anoAtualizacao, dateFormat);
					}

					/* System.out.println("PASSOU POR AQUI");
					System.out.println(informacoes.isSalarioMinimo()); */ 
					if (informacoes.isSalarioMinimo()
							|| rmi < salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo)) {
								//System.out.println("ENTROU AQUIIIIIIIIIIIIIII");
								/* int numeroBeneficio = Integer.parseInt(informacoes.getBeneficio().split(" - ")[0]);
								if(numeroBeneficio != 36){

									rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);
								} */
								rmi = salarioMinimo(mesCalculo, anoCalculo, dateFormat, listaSalarioMinimo);

						/* System.out.println("minimo de novo");
						System.out.println(rmi); */
						if (informacoes.isSalarioMinimo() == false) {
							informacoes.setSalarioMinimo(true);
						}
					}  
					
					CalculoDTO calculoAdd;


						
						String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
							 boolean dibAnteriorTeste = String.valueOf(arrayDibAnterior.length).equals("1"); 
							int  mesDibAnterio = 0;
							int anoDibAnterio=0; 
							
							 if(!dibAnteriorTeste){
								mesDibAnterio = Integer.parseInt(arrayDibAnterior[1]);
								anoDibAnterio = Integer.parseInt(arrayDibAnterior[2]);
							 }
							

					float mesResjusteSalarioMinimo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
					/* if(mesCalculo == 2 && anoCalculo == 2020){
						rmi = rmi * (float) 1.000096;
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						if(!informacoes.isSelic()){
						jurosAcumulado = 0;
						}	
					}else */
						/* if(mesCalculo == 2 && anoCalculo == 2020){
							rmi = rmi * (float) 1.0411;
							dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
							if(!informacoes.isSelic()){
							jurosAcumulado = 0;
							}



							calculoAdd = new CalculoDTO(dataCalculo, (float) 1.0411, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
							reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}else  */if(mesCalculo == 8 && anoCalculo == 2006){
							rmi = rmi * (float) 1.000096;
							dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
							if(!informacoes.isSelic()){
							jurosAcumulado = 0;
						}
						calculoAdd = new CalculoDTO(dataCalculo, (float) 1.000096, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
							reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);

						//anntes estava mesReajusteSalarioMin >= mesInicioCalculo, tirei pois a kelen disse que o primeiro mes não pode ser, caso caia no 01/01/0000
						}else if(mesReajusteSalarioMin > mesInicioCalculo && mesCalculo == mesReajusteSalarioMin && anoCalculo == anoInicioCalculo){
							//System.out.println("ENTROU NA GAMBIARRAAAAAAAAAAAAAAAAAAAAA");
							if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								//System.out.println("entrou 1 " + anoDibAnterio +" - " + anoInicioCalculo);
								//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
								//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
								//System.out.println("MES CAUCLO " + mesCalculo);
								reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(1, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else if (anoDibAnterio == anoInicioCalculo && mesDibAnterio < mesIncioJuros) {
								//System.out.println("entrou 2");
								reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesDibAnterio, (anoCalculo - 1), listReajuste,
										dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else {
								//System.out.println("entrou 3");
								reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesCalculo, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else { 
							//System.out.println("entrou 4 ");
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabelaSemADibAnterior(mesCalculo, anoCalculo, listReajuste, dateFormat, 1, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}

						//System.out.println("SAIU NA GAMBIARRAAAAAAAAAAAAAAAAAAAAA");
						if (!informacoes.isSalarioMinimo()) {
							rmi = rmi * reajusteAcumuladoParaPrimeiroMes;
						}
						reajusteParaPrimeiroMes = reajusteAcumuladoParaPrimeiroMes;
						//System.out.println("CORRECAO ACUMULADA ANTES DE ENTYRAR NO OBJETO2 = " + correcaoAcumulada);
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						if(!informacoes.isSelic()){
							jurosAcumulado = 0;
						}	
							calculoAdd = new CalculoDTO(dataCalculo, reajusteParaPrimeiroMes, rmi, correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);

						
						}else{
							//System.out.println("CORRECAO ACUMULADA ANTES DE ENTYRAR NO OBJETO3 = " + reajuste);
							
							
							dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
							if(!informacoes.isSelic()){
							jurosAcumulado = 0;
						}	
						
							calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, (float) correcaoAcumulada,
							jurosAcumulado,
							porcentagemRmi);
						}
						

						///System.out.println("PARA VERIFICAR O REAJUSTE 2= " + reajusteAcumulado);
					 
	 	

						
					
					if (!verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
						reajuste = 1;
						listCalculo.add(calculoAdd);
					}else{
						reajuste = 1;
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
								System.out.println(calculoAdd);
						System.out.println(calculoAdd);		
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
						return listCalculo;
					}
					//System.out.println("ACABOU");

					// verifica a data para fazer o colocar o reajuste


					//System.out.println("REAJUSTEEEEEEEEEEEEeeeeeeeeeeeeeeeeeeee " + mesResjusteSalarioMinimo + " Ano: "+ anoCalculo + " Mes" + mesCalculo);
				 if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
						//System.out.println("entrou de primeira aqui????????????????????????????");
						//System.out.println("entrou 0" + mesCalculo);
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						//System.out.println(reajusteAcumulado);
					} else if (confirmadoData == 0) { 
						//System.out.println("OU AQUI???????????????????????????????");
						if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								//System.out.println("entrou 1 " + anoDibAnterio +" - " + anoInicioCalculo);
								//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
								//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
								//System.out.println("MES CAUCLO " + mesCalculo);
								reajusteAcumulado = ParaVerificarQuandoADibAnteriorForMenorQuEOAnoInicioCalculo(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
								//System.out.println("RETORNO = " + reajusteAcumulado);
							} else if (anoDibAnterio == anoInicioCalculo && mesDibAnterio < mesIncioJuros) {
								//System.out.println("entrou 2");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesDibAnterio, anoCalculo, listReajuste,
										dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else { 
								//System.out.println("entrou 3");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else { 
							//System.out.println("entrou 4");
							//System.out.println(mesCalculo + " " + anoCalculo+ " "+ " "+" " +dateFormat+" "+ " "+ mesDibAnterio+"  " +anoInicioCalculo);
							if(anoInicioCalculo == anoCalculo){
								reajusteAcumulado = reajusteCasoDibAnteriorNaoExista(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo, mesInicioCalculo);
							}else{
								reajusteAcumulado = reajusteCasoDibAnteriorNaoExista(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoCalculo, mesCalculo);
							}
							
							//reajusteAcumulado = reajusteCasoDibAnteriorNaoExista(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo, mesInicioCalculo);
							//System.out.println("REAJUSTEEEEEEEEEEEEEEEEEEEEEEEEE = " + reajusteAcumulado);
						}
					}else if(anoInicioCalculo == anoCalculo && mesReajusteSalarioMin == mesCalculo){
						//System.out.println("PEGOU DE 20199999999999");
						if (!informacoes.isSalarioMinimo()) {
						   // System.out.println("SALARIOOOOOOOOOOOOOOOOOOOO MINIMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + rmi + " reajuste = " + reajusteAcumulado);							
							rmi = rmi * reajusteAcumulado;
						}
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesCalculo, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						//System.out.println("acumulados    =   " + reajusteAcumulado);
						
					}

					dataMenorQueDoisMilEDez = false;
					



					mesCalculo++;
					if (mesCalculo == 13) {
						contadorArrayQuantSalarioMinimo = 0;
						mesCalculo = 01;
						anoCalculo++;
						entrouNoIfParaDataReajuste = false;
						passYear = true;
						
					}
					confirmadoData++;
					
				}
			}

			return listCalculo;
		} catch (Exception e) {
			System.err.println(e + "ERRO NO DIBANTERIOR");
			throw new RuntimeException("Ocorreu um erro na nova funcao!");
		}
	}
	

	@PostMapping("/alcada")
	public @ResponseBody Object alcada(@RequestBody InfoCalculoDTO informacoes) {
		try {
			
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
			if(informacoes.getDibAnterior() == null){
				informacoes.setDibAnterior("");
			}
			if(informacoes.getDibAnterior() == ""){
				return beneficioAcumulado2(informacoes);
			 }
			//System.out.println("NÃO PASSOUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
			String[] arrayInicioCalculo = informacoes.getInicioCalculo().split("/");
			int mesInicioCalculo = Integer.parseInt(arrayInicioCalculo[1]);
			int anoInicioCalculo = Integer.parseInt(arrayInicioCalculo[2]);
			
			String[] arrayDip = informacoes.getDip().split("/");
			int mesDip = Integer.parseInt(arrayDip[1]);
			int anoDip = Integer.parseInt(arrayDip[2]);

			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
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
			float reajusteParaPrimeiroMes = 1;
			float reajusteAcumuladoParaPrimeiroMes = 1;
			int confirmadoData = 0;
			int contadorMes13salrio = 0;
			List<TaxaReajuste> listReajuste = reajusteRepositorio.findAll();

			List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();

			List<SalarioMinimo> listaSalarioMinimo = new ArrayList<SalarioMinimo>();
			boolean salario13Obrigatorio = informacoes.isSalario13Obrigatorio();
			if (informacoes.isSalarioMinimo() || informacoes.isLimiteMinimoMaximo()) {
				listaSalarioMinimo = salarioMinimoRepository.findAll();
			}

			if(informacoes.getDibAnterior() == null){
				informacoes.setDibAnterior("");
			}

			List<SalarioMinimo> listaSalarioMinimoParaTaxaSelic = new ArrayList<SalarioMinimo>();
			listaSalarioMinimoParaTaxaSelic = salarioMinimoRepository.findAll();
			boolean dataMenorQueDoisMilEDez = false;
			boolean entrouNoIfParaDataReajuste = false;
			int mesReajusteSalarioMin = 0;
			int mesReajusteSalarioMinAnteriorAoInicioDoCalculo = 0;
			boolean passYear = false;
			
			
			if (anoCalculo <= anoDip) {
				
				while (anoCalculo != anoDip + 1) {

					if(!entrouNoIfParaDataReajuste){
						
						mesReajusteSalarioMin = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						mesReajusteSalarioMinAnteriorAoInicioDoCalculo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, (anoCalculo - 1));
						entrouNoIfParaDataReajuste = true;
					}

					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
					String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

					// coloca o reajuste
					if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
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

					CalculoDTO calculoAdd;

					
					String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
					//System.out.println("22222222");
							 boolean dibAnteriorTeste = String.valueOf(arrayDibAnterior.length).equals("1"); 
							int mesDibAnterio = 0;
							int anoDibAnterio=0; 
							
							 if(!dibAnteriorTeste){
								mesDibAnterio = Integer.parseInt(arrayDibAnterior[1]);
								anoDibAnterio = Integer.parseInt(arrayDibAnterior[2]);
							 }

							 float mesResjusteSalarioMinimo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);

	


					 //calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, 0, 0, porcentagemRmi);

					 if(mesCalculo == 8 && anoCalculo == 2006){
						rmi = rmi * (float) 1.000096;
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
					calculoAdd = new CalculoDTO(dataCalculo, (float) 1.000096, rmi, 0,
						0,
						porcentagemRmi);
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);


					}else if(mesReajusteSalarioMin >= mesInicioCalculo && mesCalculo == mesReajusteSalarioMin && anoCalculo == anoInicioCalculo){
						//System.out.println("ENTROU NA GAMBIARRAAAAAAAAAAAAAAAAAAAAA");
						if (informacoes.getDibAnterior() != null
							&& informacoes.getDibAnterior().toString().length() > 0) {
						if (anoDibAnterio < anoInicioCalculo) {
							//System.out.println("entrou 1 " + anoDibAnterio +" - " + anoInicioCalculo);
							//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
							//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
							//System.out.println("MES CAUCLO " + mesCalculo);
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(1, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						} else if (anoDibAnterio == anoInicioCalculo /* && mesDibAnterio < mesIncioJuros */) {
							//System.out.println("entrou 2");
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesDibAnterio, (anoCalculo - 1), listReajuste,
									dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						} else {
							//System.out.println("entrou 3");
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesCalculo, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}
					} else { 
						//System.out.println("entrou 4 ");
						reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabelaSemADibAnterior(mesCalculo, anoCalculo, listReajuste, dateFormat, 1, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
					}

					//System.out.println("SAIU NA GAMBIARRAAAAAAAAAAAAAAAAAAAAA");
					if (!informacoes.isSalarioMinimo()) {
						rmi = rmi * reajusteAcumuladoParaPrimeiroMes;
					}
					reajusteParaPrimeiroMes = reajusteAcumuladoParaPrimeiroMes;
	
					dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, reajusteParaPrimeiroMes, rmi, 0,
						0,
						porcentagemRmi);


					}else{
						
						//System.out.println("ENTROU NO ELSE " + mesCalculo + " - " + reajuste + " - " + reajusteAcumulado);
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, (float) 0,
						0,
						porcentagemRmi);
					}

					//System.out.println("ACUMULADOSSSSS = " + calculoAdd);

					if (!verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
						reajuste = 1;
						listCalculo.add(calculoAdd);
					}else{
						reajuste = 1;
					}


					//reajuste = 1;
					//listCalculo.add(calculoAdd);
					/* if (informacoes.isSalario13()) {
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
					} */
					

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

						calculoAdd = salario13(mesCalculo, anoCalculo, rmi, contadorMes13salrio, 0,
								0, porcentagemRmi, salario13Obrigatorio, mesDip, anoDip);
								System.out.println(calculoAdd);
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









					if (mesDip == mesCalculo && anoCalculo == anoDip) {
						return listCalculo;
					}
					// verifica a data para fazer o colocar o reajuste
					
					if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
						//reajusteAcumulado = calculoReajusteAcumulados(mesCalculo, anoCalculo, listReajuste, dateFormat);
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						//System.out.println("VERIFY INACUMULADOS  "+ reajusteAcumulado +" " + mesCalculo +" "+ anoCalculo);
					} else if (confirmadoData == 0) {
						if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								//reajusteAcumulado = calculoReajusteAcumulados(1, anoCalculo, listReajuste, dateFormat);
								reajusteAcumulado = ParaVerificarQuandoADibAnteriorForMenorQuEOAnoInicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else if (anoDibAnterio == anoInicioCalculo/*  && mesDibAnterio < mesInicioCalculo */) {
								//System.out.println("entrou 2 INACUMULADOS");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesDibAnterio, anoCalculo, listReajuste,
								dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else {
								//System.out.println("entrou 3 INACUMULADOS");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else {
							//System.out.println("entrou 4 INACUMULADOS");
							//System.out.println(mesCalculo+" "+ anoCalculo);
							reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}
					}else if(anoInicioCalculo == anoCalculo && mesReajusteSalarioMin == mesCalculo){
						//System.out.println("PEGOU DE 20199999999999");
						if (!informacoes.isSalarioMinimo()) {
						   // System.out.println("SALARIOOOOOOOOOOOOOOOOOOOO MINIMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + rmi + " reajuste = " + reajusteAcumulado);							
							rmi = rmi * reajusteAcumulado;
						}
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesCalculo, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						//System.out.println("acumulados    =   " + reajusteAcumulado);
						
					}

					dataMenorQueDoisMilEDez = false;
					// faz a progressao da data
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

			return listCalculo;

		} catch (Exception e) {
			System.err.println(e);
			return e;
		}
	}








	public List<CalculoDTO> beneficioAcumulado2(InfoCalculoDTO informacoes) {
		try {
			//("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " + informacoes);
			//System.out.println("ENTROU NO LUGAR CERTO DOS ACUMULADOS PARA VERIFICAR OS REAJUSTES");
			String[] arrayInicioCalculo = informacoes.getInicioCalculo().split("/");
			int mesInicioCalculo = Integer.parseInt(arrayInicioCalculo[1]);
			int anoInicioCalculo = Integer.parseInt(arrayInicioCalculo[2]);
			
			String[] arrayDip = informacoes.getDip().split("/");
			int mesDip = Integer.parseInt(arrayDip[1]);
			int anoDip = Integer.parseInt(arrayDip[2]);

			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
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
			float reajusteParaPrimeiroMes = 1;
			float reajusteAcumuladoParaPrimeiroMes = 1;
			int confirmadoData = 0;
			int contadorMes13salrio = 0;
			List<TaxaReajuste> listReajuste = reajusteRepositorio.findAll();

			List<CalculoDTO> listCalculo = new ArrayList<CalculoDTO>();

			List<SalarioMinimo> listaSalarioMinimo = new ArrayList<SalarioMinimo>();
			boolean salario13Obrigatorio = informacoes.isSalario13Obrigatorio();
			if (informacoes.isSalarioMinimo() || informacoes.isLimiteMinimoMaximo()) {
				listaSalarioMinimo = salarioMinimoRepository.findAll();
			}

			if(informacoes.getDibAnterior() == null){
				informacoes.setDibAnterior("");
			}

			List<SalarioMinimo> listaSalarioMinimoParaTaxaSelic = new ArrayList<SalarioMinimo>();
			listaSalarioMinimoParaTaxaSelic = salarioMinimoRepository.findAll();
			boolean dataMenorQueDoisMilEDez = false;
			boolean entrouNoIfParaDataReajuste = false;
			int mesReajusteSalarioMin = 0;
			int mesReajusteSalarioMinAnteriorAoInicioDoCalculo = 0;
			boolean passYear = false;

			if (anoCalculo <= anoDip) {
				while (anoCalculo != anoDip + 1) {

					if(!entrouNoIfParaDataReajuste){
						mesReajusteSalarioMin = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);
						mesReajusteSalarioMinAnteriorAoInicioDoCalculo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, (anoCalculo - 1));
						entrouNoIfParaDataReajuste = true;
					}

					SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
					String dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));

					// coloca o reajuste
					if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
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

					CalculoDTO calculoAdd;

					
					String[] arrayDibAnterior = informacoes.getDibAnterior().split("/");
					//System.out.println("22222222");
							 boolean dibAnteriorTeste = String.valueOf(arrayDibAnterior.length).equals("1"); 
							int mesDibAnterio = 0;
							int anoDibAnterio=0; 
							
							 if(!dibAnteriorTeste){
								mesDibAnterio = Integer.parseInt(arrayDibAnterior[1]);
								anoDibAnterio = Integer.parseInt(arrayDibAnterior[2]);
							 }

							 float mesResjusteSalarioMinimo = buscarMesReajuste(listaSalarioMinimoParaTaxaSelic, anoCalculo);

	


					 //calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, 0, 0, porcentagemRmi);

					 if(mesCalculo == 8 && anoCalculo == 2006){
						rmi = rmi * (float) 1.000096;
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
					calculoAdd = new CalculoDTO(dataCalculo, (float) 1.000096, rmi, 0,
						0,
						porcentagemRmi);
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);


					}else if(mesReajusteSalarioMin >= mesInicioCalculo && mesCalculo == mesReajusteSalarioMin && anoCalculo == anoInicioCalculo){
						//System.out.println("ENTROU NA GAMBIARRAAAAAAAAAAAAAAAAAAAAA");
						if (informacoes.getDibAnterior() != null
							&& informacoes.getDibAnterior().toString().length() > 0) {
						if (anoDibAnterio < anoInicioCalculo) {
							//System.out.println("entrou 1 " + anoDibAnterio +" - " + anoInicioCalculo);
							//buscarMesReajuste(listaSalarioMinimoParaTaxaSelic);
							//reajusteAcumulado = calculoReajuste(1, anoCalculo, listReajuste, dateFormat, mesDibAnterio);
							//System.out.println("MES CAUCLO " + mesCalculo);
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(1, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						} else if (anoDibAnterio == anoInicioCalculo /* && mesDibAnterio < mesIncioJuros */) {
							//System.out.println("entrou 2");
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesDibAnterio, (anoCalculo - 1), listReajuste,
									dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						} else {
							//System.out.println("entrou 3");
							reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabela(mesCalculo, (anoCalculo - 1), listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						}
					} else { 
						//System.out.println("entrou 4 ");
						reajusteAcumuladoParaPrimeiroMes = BuscarReajusteDoPrimeiroMesDaTabelaSemADibAnterior(mesCalculo, anoCalculo, listReajuste, dateFormat, 1, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
					}

					//System.out.println("SAIU NA GAMBIARRAAAAAAAAAAAAAAAAAAAAA");
					if (!informacoes.isSalarioMinimo()) {
						rmi = rmi * reajusteAcumuladoParaPrimeiroMes;
					}
					reajusteParaPrimeiroMes = reajusteAcumuladoParaPrimeiroMes;
	
					dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, reajusteParaPrimeiroMes, rmi, 0,
						0,
						porcentagemRmi);


					}else{
						//System.out.println("CORRECAO ACUMULADA ANTES DE ENTYRAR NO OBJETO3 = " + reajuste);
						
						//System.out.println("ENTROU NO ELSE " + mesCalculo + " - " + reajuste + " - " + reajusteAcumulado);
						dataCalculo = out.format(in.parse(anoCalculo + "-" + mesCalculo + "-01"));
						calculoAdd = new CalculoDTO(dataCalculo, reajuste, rmi, (float) 0,
						0,
						porcentagemRmi);
					}



					if (!verificarPeriodo(mesCalculo, anoCalculo, mesInicioCalculo, anoInicioCalculo)) {
						reajuste = 1;
						listCalculo.add(calculoAdd);
					}else{
						reajuste = 1;
					}


					//reajuste = 1;
					//listCalculo.add(calculoAdd);
					/* if (informacoes.isSalario13()) {
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
					} */
					

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

						calculoAdd = salario13(mesCalculo, anoCalculo, rmi, contadorMes13salrio, 0,
								0, porcentagemRmi, salario13Obrigatorio, mesDip, anoDip);
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









					if (mesDip == mesCalculo && anoCalculo == anoDip) {
						return listCalculo;
					}
					// verifica a data para fazer o colocar o reajuste
					//System.out.println(mesCalculo);
					if (mesCalculo == mesReajusteSalarioMin && anoCalculo != anoInicioCalculo) {
						//reajusteAcumulado = calculoReajusteAcumulados(mesCalculo, anoCalculo, listReajuste, dateFormat);
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						System.out.println("VERIFY INACUMULADOS  "+ reajusteAcumulado +" " + mesCalculo +" "+ anoCalculo);
					} else if (confirmadoData == 0) {
						if (informacoes.getDibAnterior() != null
								&& informacoes.getDibAnterior().toString().length() > 0) {
							if (anoDibAnterio < anoInicioCalculo) {
								//reajusteAcumulado = calculoReajusteAcumulados(1, anoCalculo, listReajuste, dateFormat);
								reajusteAcumulado = ParaVerificarQuandoADibAnteriorForMenorQuEOAnoInicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else if (anoDibAnterio == anoInicioCalculo/*  && mesDibAnterio < mesInicioCalculo */) {
								System.out.println("entrou 2 INACUMULADOS");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesDibAnterio, anoCalculo, listReajuste,
								dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							} else {
								System.out.println("entrou 3 INACUMULADOS");
								reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							}
						} else { 
							System.out.println("entrou 4 INACUMULADOS");
							System.out.println(mesCalculo + " " + anoCalculo+ " "+ "  "+" " +dateFormat+" "+ " "+ mesDibAnterio+"  " +anoInicioCalculo);
							//reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
							reajusteAcumulado = reajusteCasoDibAnteriorNaoExista(mesCalculo, anoCalculo, listReajuste, dateFormat, mesDibAnterio, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo, mesInicioCalculo); 
						}
					}else if(anoInicioCalculo == anoCalculo && mesReajusteSalarioMin == mesCalculo){
						System.out.println("PEGOU DE 20199999999999");
						if (!informacoes.isSalarioMinimo()) {
						    System.out.println("SALARIOOOOOOOOOOOOOOOOOOOO MINIMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + rmi + " reajuste = " + reajusteAcumulado);							
							rmi = rmi * reajusteAcumulado;
						}
						reajusteAcumulado = dibAnteriorMenorAnoinicioCalculo(mesCalculo, anoCalculo, listReajuste, dateFormat, mesCalculo, listaSalarioMinimoParaTaxaSelic, anoInicioCalculo);
						System.out.println("acumulados    =   " + reajusteAcumulado);
						
					}

					dataMenorQueDoisMilEDez = false;
					// faz a progressao da data
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

			return listCalculo;

		} catch (Exception e) {
			System.err.println(e + "ERRO NO DIBANTERIOR ACUMULADOS");
			throw new RuntimeException("Ocorreu um erro na nova funcao!");
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

			float correcaoAcumulada =  calculoCorrecao(mesDib, anoDib, listCorrecao, mesAtualizacao, anoAtualizacao,
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



	
	public float salarioMinimoAuxicilioAcidente(int mesCalculo, int anoCalculo, DateTimeFormatter dateFormat,
			List<SalarioMinimo> listaSalarioMinimo, int anoInicioCalculo) {
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
				//System.out.println(listJuros);
				
		float jurosAcumulado = 0;
		//System.out.println("ENTROU NO CALCULO JUROS " + mesCalculo + " anoCalculo: " + anoCalculo);
				
		try { 
			for (int indexJuros = listJuros.size() - 1; indexJuros >= 0; indexJuros--) {
				int mesJuros = listJuros.get(indexJuros).getData().getMonthValue();
				int anoJuros = listJuros.get(indexJuros).getData().getYear();  
				
				if (verificarPeriodo(mesIncioJuros, anoIncioJuros, mesJuros, anoJuros)
						&& verificarPeriodo(mesJuros, anoJuros, mesAtualizacao, anoAtualizacao)) {
							
					jurosAcumulado += listJuros.get(indexJuros).getJuros();
					
				}

				if (mesJuros == 2 && anoJuros == 2021 && mesCalculo == 2 && anoCalculo == 2021) {
					
				}
				
				if (mesJuros == mesCalculo && anoCalculo == anoJuros) {
					
					return jurosAcumulado;
				}
			}
			

			if (listJuros.get(0).getType() == 0) {
				//System.out.println("ENTROU NO QUARTO IF DOS JUROS PARA VERIFICAR SELIC  = "+ jurosAcumulado);
				
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
				//System.out.println("ENTROU NO CORRECAO " + mesCalculo + " = " + anoCalculo);
				float correcaoAcumulada = 1;
				//System.out.println("PARA SABER O MES = " + mesCalculo);
				
		try {
			for (int indexCorrecao = listCorrecao.size() - 1; indexCorrecao >= 0; indexCorrecao--) {
				int mesCorrecao = listCorrecao.get(indexCorrecao).getData().getMonthValue();
				int anoCorrecao = listCorrecao.get(indexCorrecao).getData().getYear(); 
				if (verificarPeriodo(mesCorrecao, anoCorrecao, mesAtualizacao, anoAtualizacao)) {
					if(anoCorrecao == 2021 && mesCorrecao == 12){
						//System.out.println("PRIMEIRO IF " + correcaoAcumulada + " - " + (listCorrecao.get(indexCorrecao).getPercentual() / 1));
						correcaoAcumulada += ((listCorrecao.get(indexCorrecao).getPercentual() / 100));
						
					}else if(anoCorrecao > 2021){
						BigDecimal resultado = new BigDecimal(listCorrecao.get(indexCorrecao).getPercentual()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
						/* System.out.println(resultado);
						System.out.println("SEGUNDO IF " + correcaoAcumulada  + " - " +(resultado).floatValue());
						System.out.println("SOMA = " + ((resultado).floatValue() + 1)); */

						//correcaoAcumulada += (resultado).floatValue();
						correcaoAcumulada += Float.parseFloat((String.format("%.4f", resultado)).replace(",", "."));
						/* System.out.println("ano: " + anoCorrecao + " . " + "mes: " + mesCorrecao + " ." + " valor: " + listCorrecao.get(indexCorrecao).getPercentual());
						System.out.println("correcaoAacumulada: " + correcaoAcumulada); */
					
						//System.out.println(correcaoAcumulada);
					}else{
						//System.out.println("TERCEIRO " + correcaoAcumulada  + " - " + (listCorrecao.get(indexCorrecao).getPercentual() / 100));
						correcaoAcumulada *= ((listCorrecao.get(indexCorrecao).getPercentual() / 100) + 1);
						/* System.out.println("ano: " + anoCorrecao + " . " + "mes: " + mesCorrecao + " ." + " valor: " + listCorrecao.get(indexCorrecao).getPercentual());
						System.out.println("correcaoAacumulada: " + correcaoAcumulada); */
					}
					
					
				}
				if (mesCorrecao == mesCalculo && anoCalculo == anoCorrecao) {
					if(correcaoAcumulada == 0){
						return (float) 1;
					}
					if(mesCalculo == 1 && anoCalculo == 2023){
						BigDecimal original = BigDecimal.valueOf(correcaoAcumulada);
						//System.out.println("T = " + correcaoAcumulada);
					// Arredondar para três casas decimais
						BigDecimal arredondado = original.setScale(3, RoundingMode.HALF_UP);
						//System.out.println("TESTE " + arredondado.floatValue());
						return arredondado.floatValue();
					}
					
					//System.out.println("RETORNOU = " + correcaoAcumulada);
					/* System.out.println("RETORNOU = " + String.valueOf(correcaoAcumulada).length());
					System.out.println("RETORNOU = " + Float.parseFloat((String.format("%.4f", 1.0097001)).replace(",", "."))); */

					// Criar um BigDecimal com o número
					
					//return  arredondado.floatValue();
					return correcaoAcumulada;
			
				}
			} // termino do laco for da correcao;
			//System.out.println("Erro no Correçao!! mesCalculo: " + mesCalculo + " anoCalculo: " + anoCalculo);
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
					
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					
					
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
					
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					
					
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
					
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//if(anoSalarioMinimo == )
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			System.out.println("RETORNOU 01");
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}


	public float reajusteCasoDibAnteriorNaoExista(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
			DateTimeFormatter dateFormat, int mesDibAnterior, List<SalarioMinimo> listaSalarioMinimo, int anoInicioCalculo,
			int mesInicioCalculo) {
				
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


				if (mesReajuste == mesInicioCalculo && anoCalculo == anoReajuste) {
					System.out.println("calculoReajuste. Mes:  " + mesReajuste + ". Ano: " + anoReajuste);
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//if(anoSalarioMinimo == )
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			System.out.println("RETORNOU 01");
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}



	public float ParaVerificarQuandoADibAnteriorForMenorQuEOAnoInicioCalculo(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
			DateTimeFormatter dateFormat, int mesDibAnterior, List<SalarioMinimo> listaSalarioMinimo, int anoInicioCalculo) {
		float reajusteAcumulado = 1;
		int mesSalarioMinimo = buscarMesReajuste(listaSalarioMinimo, anoCalculo);
		
		try {
			for (int i = listReajuste.size() - 1; i >= 0; i--) {
				int mesReajuste = listReajuste.get(i).getData().getMonthValue();
				int anoReajuste = listReajuste.get(i).getData().getYear();
				if(anoInicioCalculo == anoCalculo && mesSalarioMinimo == mesReajuste && anoInicioCalculo == anoReajuste){
					//System.out.println(anoInicioCalculo + " "+anoCalculo + " " + mesReajuste);
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//System.out.println("SADLKASÇDLADKALÇDDLDLAÇDKÇLADÇDKASÇL " + buscarMesReajuste(listaSalarioMinimo, anoCalculo));
					
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}


				if (mesReajuste == mesSalarioMinimo && anoCalculo == anoReajuste) {
					
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//if(anoSalarioMinimo == )
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			System.out.println("RETORNOU 01");
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}












	
public float BuscarReajusteDoPrimeiroMesDaTabela(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
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
					
					reajusteAcumulado = (float) listReajuste.get(i).getReajusteAcumulado();
					//if(anoSalarioMinimo == )
					return (float) (listReajuste.get(i).getReajusteAcumulado());
				}

			} // termino do laco for do reajuste;
			System.out.println("RETORNOU 01");
			return 1;

		} catch (Exception e) {
			System.out.println(e);
			return reajusteAcumulado;
		}
	}




	public float BuscarReajusteDoPrimeiroMesDaTabelaSemADibAnterior(int mesCalculo, int anoCalculo, List<TaxaReajuste> listReajuste,
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

			} // termino do laco for do reajuste;
			System.out.println("RETORNOU 01");
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



	public int buscarSegundoMesReajuste(List<SalarioMinimo> listaSalarioMinimo, int anoCalculo){

		try{
			for( int i = 0; i<listaSalarioMinimo.size(); i++){
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

	
	public List<LocalDate> existeMaisDeUmaAtualizacaoSoSalarioMinimoNoAno(List<SalarioMinimo> listaSalarioMinimo, int anoCalculo){
		
		
		List<LocalDate> datasEncontradas = new ArrayList<LocalDate>();
		try{
			for( int i = 0; i<listaSalarioMinimo.size(); i++){
				int anoSalarioMinimo = listaSalarioMinimo.get(i).getData().getYear();
				if(anoSalarioMinimo == anoCalculo){
					datasEncontradas.add(listaSalarioMinimo.get(i).getData());
				} 
			}
			return datasEncontradas;
		} catch (Exception e){
			System.out.println(e);
		}
		return null;
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