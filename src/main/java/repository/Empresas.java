package repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import jpa.Transactional;
import service.NegocioException;
import model.Empresa;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 899639438450915705L;
	@Inject
	private EntityManager manager;

	public Empresa guardar(Empresa produto) {
		return manager.merge(produto);
	}

	@Transactional
	public void remover(Empresa empresa) throws NegocioException {
		try {
			empresa = porId(empresa.getId());
			manager.remove(empresa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Empresa não pode ser excluído.");
		}
	}

	// @SuppressWarnings("unchecked")
	// public List<Produto> filtrados(ProdutoFilter filtro) {
	// Session session = manager.unwrap(Session.class);
	// Criteria criteria = session.createCriteria(Produto.class);
	//
	// if (StringUtils.isNotBlank(filtro.getSku())) {
	// criteria.add(Restrictions.eq("sku", filtro.getSku()));
	// }
	//
	// if (StringUtils.isNotBlank(filtro.getNome())) {
	// criteria.add(Restrictions.ilike("nome", filtro.getNome(),
	// MatchMode.ANYWHERE));
	// }
	//
	// return criteria.addOrder(Order.asc("nome")).list();
	// }

	public Empresa porId(Long id) {
		return manager.find(Empresa.class, id);
	}

	public List<Empresa> listarTodas() {
		return this.manager.createQuery("from Empresa", Empresa.class)
				.getResultList();
	}

	public List<Empresa> porNome(String nome) {
		return this.manager
				.createQuery("from Empresa where upper(nome) like :nome",
						Empresa.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

}
