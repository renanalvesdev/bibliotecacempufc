package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Aluno;

public class AlunoDao extends DaoGeneric<Aluno> {

	public Aluno findByMatricula(String matricula) {

		Aluno aluno = new Aluno();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (matricula != null) {
			c.add(Restrictions.eq("matricula", matricula));
		}

		aluno = (Aluno) c.uniqueResult();

		session.getTransaction().commit();
		session.close();

		return aluno;
	}

	public List<Aluno> todos() {

		List<Aluno> lista = new ArrayList<>();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		lista = c.list();

		session.getTransaction().commit();
		session.close();

		return lista;

	}

}
