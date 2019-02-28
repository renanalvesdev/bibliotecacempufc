package br.com.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Aluno;
import br.com.proline.model.Emprestimo;
import br.com.proline.model.Livro;
import br.com.proline.model.Reserva;
import br.com.util.Data;

public class ReservaDao extends DaoGeneric<Reserva> {

	public List<Reserva> findByExample(Reserva filtro) {

		List<Reserva> lista = new ArrayList<>();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		// 3 -> todos , 1 -> corrente , 2 -> encerrado
		if (filtro.getSituacaoFiltro() != 3) {
			c.add(Restrictions.eq("status", filtro.getSituacaoFiltro() == 1 ? true : false));
		}

		c.addOrder(Order.desc("dataReserva"));
		lista = c.list();

		session.getTransaction().commit();
		session.close();

		return lista;
	}

	public List<Reserva> findByAluno(Aluno aluno) {

		List<Reserva> lista = new ArrayList<>();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (aluno != null) {

			c.add(Restrictions.eq("aluno", aluno));
			c.add(Restrictions.eq("status", true));
		}

		lista = c.list();

		session.getTransaction().commit();
		session.close();

		return lista;
	}

	public List<Reserva> findByEmprestimo(Emprestimo emprestimo) {

		List<Reserva> lista = new ArrayList<>();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (emprestimo != null) {

			c.add(Restrictions.eq("emprestimo", emprestimo));
			c.add(Restrictions.eq("status", true));
		}

		lista = c.list();

		session.getTransaction().commit();
		session.close();

		return lista;
	}

	public Reserva findByLivro(Livro livro) {

		Reserva reserva = new Reserva();
		Session session = criaSession();
		session.beginTransaction();

		Criteria c = session.createCriteria(getClasse());
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (livro != null) {
			c.createAlias("emprestimo", "emprestimo").add(Restrictions.eq("emprestimo.livro", livro));
			c.add(Restrictions.eq("status", true));
		}

		reserva = (Reserva) c.setMaxResults(1).uniqueResult();
		session.getTransaction().commit();
		session.close();

		return reserva;
	}

	public List<Reserva> findByReservasExpiradas(Integer limite) {

		List<Reserva> lista = new ArrayList<>();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		c.add(Restrictions.eq("status", true));
		c.createAlias("emprestimo", "emprestimo").add(Restrictions.eq("emprestimo.status", false));
		c.add(Restrictions.lt("emprestimo.dataDevolucao", Data.somaDatas(new Date(), -limite)));
		lista = c.list();

		session.getTransaction().commit();
		session.close();

		return lista;
	}
}
