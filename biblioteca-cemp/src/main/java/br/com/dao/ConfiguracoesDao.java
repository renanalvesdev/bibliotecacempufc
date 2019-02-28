package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Configuracoes;

public class ConfiguracoesDao extends DaoGeneric<Configuracoes> {

	public Configuracoes configuracao() {

		Configuracoes conf = new Configuracoes();

		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		conf = (Configuracoes) c.uniqueResult();

		session.getTransaction().commit();
		session.close();

		return conf;
	}
}