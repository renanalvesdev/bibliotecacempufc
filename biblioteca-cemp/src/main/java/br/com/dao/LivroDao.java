package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Livro;

public class LivroDao extends DaoGeneric<Livro> {

	public List<Livro> findByExample(Livro filtro) {

		List<Livro> livros = new ArrayList<Livro>();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (filtro.getTipo() != 3) {
			c.add(Restrictions.eq("status", filtro.getTipo() == 1 ? true : false));
		}

		c.addOrder(Order.desc("titulo"));
		livros = c.list();

		session.getTransaction().commit();
		session.close();

		return livros;
	}

	public Livro findByCodigo(String codigo) {

		Livro livro = new Livro();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (codigo != null) {
			c.add(Restrictions.eq("codigo", codigo));
			c.add(Restrictions.eq("status", true));
		}

		livro = (Livro) c.uniqueResult();

		session.getTransaction().commit();
		session.close();

		return livro;
	}
}