package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Configuracoes;

public class ConfiguracoesDao extends DaoGeneric<Configuracoes> {
	
	public Configuracoes configuracao() {
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return (Configuracoes) c.uniqueResult();
	}
}