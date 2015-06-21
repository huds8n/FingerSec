package controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.FacesUtil;
import model.Grupo;
import model.Usuario;
import service.GrupoService;
import service.NegocioException;
import service.UsuarioService;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Usuario usuario;

	private String senha;
	private String senha2;

	@Inject
	private UsuarioService userService;

	@Inject
	private GrupoService gpService;
	private List<Grupo> grupos = new ArrayList<>();
	private List<String> listaGrupos = new ArrayList<>();
	private List<String> gruposSelecionados = new ArrayList<String>();

	public void salvar() {
		try {
			List<Grupo> listaGruposString = new ArrayList<Grupo>();
			for (String grupo : gruposSelecionados) {
				listaGruposString.add(gpService.peloNome(grupo));

			}
			this.usuario.setSenha(criptografar(senha).toLowerCase());
			this.usuario.setGrupos(listaGruposString);
			this.userService.salvar(usuario);
			limpar();
			FacesUtil.addInfoMessage("Cadastro Novo Salvo com Sucesso");
		} catch (NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}

	public void limpar() {
		usuario = new Usuario();
	}

	public String criptografar(String senha) {

		MessageDigest algoritmo;
		byte messageDigest[];
		StringBuilder hexString;
		try {
			algoritmo = MessageDigest.getInstance("MD5"); // 32 letras
			messageDigest = algoritmo.digest(senha.getBytes("UTF-8"));
			hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", 0xFF & b));
			}
			senha = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return senha;
	}

	@PostConstruct
	public void inicializar() {

		usuario = new Usuario();
		this.grupos = gpService.buscarTodos();

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public List<String> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<String> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public List<String> getGruposSelecionados() {
		return gruposSelecionados;
	}

	public void setGruposSelecionados(List<String> gruposSelecionados) {
		this.gruposSelecionados = gruposSelecionados;
	}

}
