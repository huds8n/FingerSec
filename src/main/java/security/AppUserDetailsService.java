package security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Grupo;
import model.Usuario;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import repository.Usuarios;
import cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
		Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
		Usuario usuario = usuarios.porLogin(login);
		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Grupo grupo : usuario.getGrupos()) {
			authorities.add(new SimpleGrantedAuthority(grupo.getNome()
					.toUpperCase()));
		}

		return authorities;
	}

}
