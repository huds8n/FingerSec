package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import jsf.FacesUtil;
import model.Empresa;
import model.Funcionario;

import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.CroppedImage;

import service.EmpresaService;
import service.FuncionarioService;
import service.NegocioException;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;

@Named
@ViewScoped
public class FuncionarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Funcionario funcionario;
	private CroppedImage imagemRecortada;
	private String foto;
	private String fotoRecortada;
	private String arquivoFoto;
	private String arquivoFotoRecortada;
	private ServletContext servletContext;
	private String strUm, strDois, strTres;
	private boolean exibeImagemCapturada, exibeSalvar;
	@Inject
	private EmpresaService empresaService;
	private List<Empresa> listaEmpresas;
	private Empresa empresa;
	private boolean skip;
	private Long idEmpresa;
	private String[] pertencesSelecionados;
	private List<String> pertences;

	@Inject
	private FuncionarioService funcService;

	public FuncionarioBean() {

	}

	@PostConstruct
	public void preencherPertences() {
		pertences = new ArrayList<String>();
		pertences.add("Celular");
		pertences.add("Carteira");
		pertences.add("Chave");
	}

	public void buscar() throws NegocioException {
		funcionario = new Funcionario();
		listaEmpresas = new ArrayList<Empresa>();
		listaEmpresas = empresaService.listarTodas();
		servletContext = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
	}

	public void guardar() {
		empresa = new Empresa();
		try {
			empresa = empresaService.porID(idEmpresa);
		} catch (NegocioException e1) {
			e1.printStackTrace();
		}
		DPFPFeatureSet templateDedoEsquerdo;
		DPFPFeatureSet templateDedoDireito;
		templateDedoEsquerdo = DPFPGlobal.getFeatureSetFactory()
				.createFeatureSet(hexStringToByteArray(strUm));
		templateDedoDireito = DPFPGlobal.getFeatureSetFactory()
				.createFeatureSet(hexStringToByteArray(strDois));
		funcionario.setIndicadorDireito(templateDedoDireito.serialize());
		funcionario.setIndicadorEsquerdo(templateDedoEsquerdo.serialize());
		funcionario.setEmpresa(empresa);
		try {
			this.funcionario = funcService.salvar(this.funcionario);
		} catch (NegocioException e) {
			e.printStackTrace();
		}
		funcionario = new Funcionario();
		FacesUtil.addInfoMessage("Funcionario salvo com sucesso!!!");

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

	public void salvar() {
		try {
			funcionario.setEmpresa(empresaService.porID(idEmpresa));
			this.funcionario = funcService.salvar(this.funcionario);
			funcionario = new Funcionario();
			FacesUtil.addInfoMessage("Funcionario salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}

	public void salvarFoto() {
		funcionario.setFoto(imagemRecortada.getBytes());
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Foto SALVA!",
						"Informação"));
	}

	// metodos da foto
	public void oncapture(CaptureEvent captureEvent) {
		verificaExistenciaArquivo(arquivoFoto);
		foto = "foto" + getNumeroRandomico() + ".png";
		arquivoFoto = servletContext.getRealPath("") + File.separator
				+ "resources" + File.separator + "imagens" + File.separator
				+ "tmp" + File.separator + foto;
		System.out.println(servletContext.getRealPath("") + File.separator
				+ "resources" + File.separator + "imagens" + File.separator
				+ "tmp" + File.separator + foto);
		criaArquivo(arquivoFoto, captureEvent.getData());
		exibeImagemCapturada = true;
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Foto CAPTURADA com sucesso!", "Informação"));
	}

	private String getNumeroRandomico() {
		int i = (int) (Math.random() * 10000);
		return String.valueOf(i);
	}

	public void removerArquivos() {
		String dir = servletContext.getRealPath("") + File.separator
				+ "resources" + File.separator + "imagens" + File.separator
				+ "tmp" + File.separator;
		int cont = 0;
		File f = new File(dir);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {

				file.delete();
				cont++;
			}
		}
		FacesUtil.addInfoMessage(cont + " Arquivos Foram APAGADOS!!!");
	}

	private void verificaExistenciaArquivo(String arquivo) {
		if (arquivo != null) {
			File file = new File(arquivo);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	private void criaArquivo(String arquivo, byte[] dados) {

		FileImageOutputStream imageOutput;
		try {
			imageOutput = new FileImageOutputStream(new File(arquivo));
			imageOutput.write(dados, 0, dados.length);
			imageOutput.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FuncionarioBean.class.getName()).log(Level.SEVERE,
					null, ex);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Caminho não encontrado!", "Erro"));
		} catch (IOException ex) {
			Logger.getLogger(FuncionarioBean.class.getName()).log(Level.SEVERE,
					null, ex);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao criar arquivo!", "Erro"));
		}
	}

	public void recortar() {
		verificaExistenciaArquivo(arquivoFotoRecortada);
		fotoRecortada = "fotoRecortada" + getNumeroRandomico() + ".png";
		arquivoFotoRecortada = servletContext.getRealPath("") + File.separator
				+ "resources" + File.separator + "imagens" + File.separator
				+ "tmp" + File.separator + fotoRecortada;
		criaArquivo(arquivoFotoRecortada, imagemRecortada.getBytes());
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Foto RECORTADA com sucesso!", "Informação"));
		exibeSalvar = true;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public FuncionarioService getFuncService() {
		return funcService;
	}

	public void setFuncService(FuncionarioService funcService) {
		this.funcService = funcService;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public CroppedImage getImagemRecortada() {
		return imagemRecortada;
	}

	public void setImagemRecortada(CroppedImage imagemRecortada) {
		this.imagemRecortada = imagemRecortada;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getFotoRecortada() {
		return fotoRecortada;
	}

	public void setFotoRecortada(String fotoRecortada) {
		this.fotoRecortada = fotoRecortada;
	}

	public String getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(String arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public String getArquivoFotoRecortada() {
		return arquivoFotoRecortada;
	}

	public void setArquivoFotoRecortada(String arquivoFotoRecortada) {
		this.arquivoFotoRecortada = arquivoFotoRecortada;
	}

	public boolean isExibeImagemCapturada() {
		return exibeImagemCapturada;
	}

	public void setExibeImagemCapturada(boolean exibeImagemCapturada) {
		this.exibeImagemCapturada = exibeImagemCapturada;
	}

	public boolean isExibeSalvar() {
		return exibeSalvar;
	}

	public void setExibeSalvar(boolean exibeSalvar) {
		this.exibeSalvar = exibeSalvar;
	}

	public String getStrUm() {
		return strUm;
	}

	public void setStrUm(String strUm) {
		this.strUm = strUm;
	}

	public String getStrDois() {
		return strDois;
	}

	public void setStrDois(String strDois) {
		this.strDois = strDois;
	}

	public String getStrTres() {
		return strTres;
	}

	public void setStrTres(String strTres) {
		this.strTres = strTres;
	}

	public List<String> getPertences() {
		return pertences;
	}

	public void setPertences(List<String> pertences) {
		this.pertences = pertences;
	}

	public String[] getPertencesSelecionados() {
		return pertencesSelecionados;
	}

	public void setPertencesSelecionados(String[] pertencesSelecionados) {
		this.pertencesSelecionados = pertencesSelecionados;
	}

}