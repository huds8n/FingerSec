package repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import jpa.Transactional;
import model.Entrada;
import service.NegocioException;

public class Entradas implements Serializable {

	private static final long serialVersionUID = 899639438450915705L;
	@Inject
	private EntityManager manager;

	public Entrada guardar(Entrada entrada) {
		return manager.merge(entrada);
	}

	@Transactional
	public void remover(Entrada entrada) throws NegocioException {
		try {
			entrada = porId(entrada.getId());
			manager.remove(entrada);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Empresa não pode ser excluído.");
		}
	}

	public Entrada porId(Long id) {
		return manager.find(Entrada.class, id);
	}

	public List<Entrada> listarTodas() {
		return this.manager.createQuery("from Entrada", Entrada.class)
				.getResultList();
	}

	public List<Entrada> porFuncionario(Long id) {
		return this.manager
				.createQuery("from Entrada e where e.funcionario.id = :id",
						Entrada.class).setParameter("id", id).getResultList();
	}

	public List<Entrada> porPeriodo(Date dtInicial, Date dtFinal) {
		return this.manager
				.createQuery(
						"SELECT e FROM Entrada e WHERE e.data BETWEEN :startDate AND :endDate",
						Entrada.class).setParameter("startDate", dtInicial)
				.setParameter("endDate", dtFinal).getResultList();
	}

	@SuppressWarnings("deprecation")
	public List<Entrada> porData(Date data) {
		data.setHours(0);
		data.setMinutes(0);
		data.setSeconds(0);
		return this.manager
				.createQuery("select e from Entrada e where e.data  >= :data",
						Entrada.class).setParameter("data", data)
				.getResultList();
	}
	
	@SuppressWarnings("deprecation")
	public List<Entrada> porDataAtivos(Date data) {
		data.setHours(0);
		data.setMinutes(0);
		data.setSeconds(0);
		return this.manager
				.createQuery("select e from Entrada e where e.data  >= :data and e.status='ATIVO'",
						Entrada.class).setParameter("data", data)
				.getResultList();
	}

}
