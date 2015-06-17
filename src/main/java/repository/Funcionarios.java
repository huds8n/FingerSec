package repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import jpa.Transactional;
import service.NegocioException;
import model.Funcionario;

public class Funcionarios implements Serializable {

	private static final long serialVersionUID = 899639438450915705L;
	@Inject
	private EntityManager manager;

	public Funcionario guardar(Funcionario funcionario) {
		return manager.merge(funcionario);
	}

	@Transactional
	public void remover(Funcionario funcionario) throws NegocioException {
		try {
			funcionario = porId(funcionario.getId());
			manager.remove(funcionario);
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

	public Funcionario porId(Long id) {
		return manager.find(Funcionario.class, id);
	}

	public List<Funcionario> todos() {
		return this.manager.createQuery("from Funcionario", Funcionario.class)
				.getResultList();
	}

	public List<Funcionario> porNome(String nome) {
		return this.manager
				.createQuery("from Funcionario where upper(nome) like :nome",
						Funcionario.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

}
