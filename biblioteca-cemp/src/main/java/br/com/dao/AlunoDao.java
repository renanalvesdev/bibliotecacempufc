package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Aluno;

public class AlunoDao extends DaoGeneric<Aluno> {

	public Aluno findByMatricula(String matricula) {
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (matricula != null) {
			c.add(Restrictions.eq("matricula", matricula));
		}

		return (Aluno) c.uniqueResult();
	}

	public List<Aluno> todos() {

		List<Aluno> lista = new ArrayList<>();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		lista = c.list();
		return lista;

	}

}
