package br.com.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<E> {

	private Class<E> classe;

	@SuppressWarnings("unchecked")
	public DaoGeneric() {
		this.classe = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected final Criteria criaCriteria() {
		return criaSession().createCriteria(getClasse());
	}

	public final Session criaSession() {
		return (Session) JPAUtil.getEntityManager().getDelegate();
	}

	public Class<E> getClasse() {
		return classe;
	}

	public void salvar(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		entityManager.persist(entidade);
		entityTransaction.commit();
		entityManager.close();
	}

	public E salvarOuAtualizar(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		E retorno = entityManager.merge(entidade);
		entityTransaction.commit();

		entityManager.close();

		return retorno;
	}

	public void excluirPorId(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id)
				.executeUpdate();

		entityTransaction.commit();
		entityManager.close();

	}

	public void desativaPorId(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery(
				"update " + entidade.getClass().getCanonicalName() + " set status = false " + "where id = " + id)
				.executeUpdate();

		entityTransaction.commit();
		entityManager.close();

	}

	public List<E> getListEntity(Class<E> entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		List<E> retorno = entityManager.createQuery("from " + entidade.getName() + " where status = true ").getResultList();

		entityTransaction.commit();
		entityManager.close();

		return retorno;
	}
	
	

}
