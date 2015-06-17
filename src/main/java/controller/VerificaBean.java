package controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.FacesUtil;
import model.Entrada;
import model.Funcionario;
import model.StatusEntrada;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import service.EntradaService;
import service.FuncionarioService;
import service.NegocioException;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;

@Named
@SessionScoped
public class VerificaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4145284746237932468L;
	private Funcionario funcionario;
	private List<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();
	private List<Funcionario> listaPesquisa = new ArrayList<Funcionario>();
	private List<Entrada> entradas;
	@Inject
	private FuncionarioService funcService;

	@Inject
	private EntradaService entradaService;

	private Entrada entrada;
	private String strDigital;
	private String observacao;
	private String alerta;
	private String som;
	private String dedo;
	private String motivoInativo;
	private String nomePesquisar;
	private int erro;
	private Boolean exibirPainel = false;
	private DPFPVerification verificator;
	private DPFPTemplate templateArmazenado;
	private DPFPFeatureSet templateEnviado;
	private DPFPVerificationResult result;

	public VerificaBean() {
		observacao = " ";
		entrada = new Entrada();
		erro = 0;
		dedo = "Direito";

	}

	@PostConstruct
	public void buscarFunc() {
		try {
			entradas = new ArrayList<Entrada>();
			funcionario = new Funcionario();
			listaFuncionarios = funcService.buscarTodos();
			verificator = DPFPGlobal.getVerificationFactory()
					.createVerification();
			templateArmazenado = DPFPGlobal.getTemplateFactory()
					.createTemplate();
		} catch (NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void preRender(Long id) throws NegocioException {
		entradas = new ArrayList<Entrada>();
		entradas = entradaService.listarPorFuncionario(id);
	}

	public void listarHoje() throws NegocioException {

		entradas = new ArrayList<Entrada>();
		entradas = entradaService.listarPorData(new java.util.Date());
	}

	public void salvarEntrada() {
		this.entrada.setData(new java.util.Date());
		this.entrada.setFuncionario(funcionario);
		this.entrada.setStatus(StatusEntrada.ATIVO);
		this.entrada.setObservacao(observacao);
		try {
			this.entradaService.salvar(entrada);
			entrada = new Entrada();
			FacesUtil.addInfoMessage("Entrada Realizada");

		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}

	public void salvarSaida() {
		try {
			this.entrada.setStatus(StatusEntrada.INATIVO);
			this.entrada.setHoraSaida(new java.util.Date());
			this.entradaService.salvar(entrada);
			entrada = new Entrada();
			FacesUtil.addInfoMessage("Saida Confirmada");

		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}

	public void verificar() {
		templateEnviado = DPFPGlobal.getFeatureSetFactory().createFeatureSet(
				hexStringToByteArray(strDigital));
		for (Funcionario func : listaFuncionarios) {
			if (dedo.equalsIgnoreCase("Direito")) {
				System.out.println("Direito");
				templateArmazenado.deserialize(func.getIndicadorDireito());
			} else {
				System.out.println("Esquerdo");
				templateArmazenado.deserialize(func.getIndicadorEsquerdo());
			}
			result = verificator.verify(templateEnviado, templateArmazenado);
			if (result.isVerified()) {
				dedo = "Direito";
				this.funcionario = func;
				alerta = "ekiga-vm.wav";
				som = "liberado.mpeg";
				exibirPainel = true;
				erro = 0;
				// SALVANDO ENTRADA
				salvarEntrada();
				System.out.println("VERIFICADO");
				FacesUtil.addInfoMessage("VERIFICADO");
				return;
			} else {
				exibirPainel = false;
				if (erro > 2) {
					erro = 0;
					dedo = "Direito";

				}
				System.out.println(erro);
				this.funcionario = new Funcionario();
				alerta = "toydoorbell.wav";
				if (erro == 0) {
					som = "restrito_tentenovamente.mpeg";
				}
				if (erro == 1) {
					som = "restrito_indicadoresquerdo.mpeg";
					dedo = "Esquerdo";
					System.out.println("Dedo: " + dedo);
				}
				if (erro == 2) {
					som = "bloqueado_chame_agente.mpeg";
					dedo = "Direito";
				}
				funcionario = new Funcionario();
				erro++;
				FacesUtil.addErrorMessage("ACESSO NEGADO");

			}
		}

	}
	
	public void verificarSaida() {
		templateEnviado = DPFPGlobal.getFeatureSetFactory().createFeatureSet(
				hexStringToByteArray(strDigital));
		for (Entrada entrada : entradas) {
			if (dedo.equalsIgnoreCase("Direito")) {
				System.out.println("Direito");
				templateArmazenado.deserialize(entrada.getFuncionario().getIndicadorDireito());
			} else {
				System.out.println("Esquerdo");
				templateArmazenado.deserialize(entrada.getFuncionario().getIndicadorEsquerdo());
			}
			result = verificator.verify(templateEnviado, templateArmazenado);
			if (result.isVerified()) {
				dedo = "Direito";
				this.entrada = entrada;
				this.funcionario = entrada.getFuncionario();
				alerta = "ekiga-vm.wav";
				som = "liberado.mpeg";
				exibirPainel = true;
				erro = 0;
				// SALVANDO SAIDA
				salvarSaida();				
				FacesUtil.addInfoMessage("SAIDA");
				return;
			} else {
				exibirPainel = false;
				if (erro > 2) {
					erro = 0;
					dedo = "Direito";

				}
				System.out.println(erro);
				this.funcionario = new Funcionario();
				alerta = "toydoorbell.wav";
				if (erro == 0) {
					som = "restrito_tentenovamente.mpeg";
				}
				if (erro == 1) {
					som = "restrito_indicadoresquerdo.mpeg";
					dedo = "Esquerdo";
					System.out.println("Dedo: " + dedo);
				}
				if (erro == 2) {
					som = "bloqueado_chame_agente.mpeg";
					dedo = "Direito";
				}
				funcionario = new Funcionario();
				erro++;
				FacesUtil.addErrorMessage("ACESSO NEGADO");

			}
		}

	}


	public void inativar() {
		funcionario.setStatus(false);
		funcionario.setMotivoInativo(motivoInativo);
		try {
			funcService.salvar(funcionario);
			FacesUtil.addInfoMessage("Inativado");
		} catch (NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ativar() {
		funcionario.setStatus(true);
		funcionario.setMotivoInativo("");
		motivoInativo = "";
		try {
			funcService.salvar(funcionario);
			FacesUtil.addInfoMessage("Ativado");
		} catch (NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listarAtendimentos() {

	}

	public void pesquisarFuncionario() {
		try {
			listaPesquisa = funcService.porNome(nomePesquisar);
		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}

	public StreamedContent getImagemDinamica() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the view. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			return new DefaultStreamedContent(new ByteArrayInputStream(
					funcionario.getFoto()));
		}

	}

	public StreamedContent getImagemDinamicaTable(byte[] foto) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the view. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			return new DefaultStreamedContent(new ByteArrayInputStream(foto));
		}

	}

	// Not needed on client side. But may be needed on server.
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public FuncionarioService getFuncService() {
		return funcService;
	}

	public void setFuncService(FuncionarioService funcService) {
		this.funcService = funcService;
	}

	public String getStrDigital() {
		return strDigital;
	}

	public void setStrDigital(String strDigital) {
		this.strDigital = strDigital;
	}

	public String getDedo() {
		return dedo;
	}

	public void setDedo(String dedo) {
		this.dedo = dedo;
	}

	public Boolean getExibirPainel() {
		return exibirPainel;
	}

	public void setExibirPainel(Boolean exibirPainel) {
		this.exibirPainel = exibirPainel;
	}

	public String getAlerta() {
		return alerta;
	}

	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}

	public String getSom() {
		return som;
	}

	public void setSom(String som) {
		this.som = som;
	}

	public String getNomePesquisar() {
		return nomePesquisar;
	}

	public void setNomePesquisar(String nomePesquisar) {
		this.nomePesquisar = nomePesquisar;
	}

	public List<Funcionario> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<Funcionario> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public String getMotivoInativo() {
		return motivoInativo;
	}

	public void setMotivoInativo(String motivoInativo) {
		this.motivoInativo = motivoInativo;
	}

	public List<Entrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

}