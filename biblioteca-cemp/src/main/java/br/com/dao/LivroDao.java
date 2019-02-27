package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Livro;

public class LivroDao extends DaoGeneric<Livro> {

	public List<Livro> findByExample(Livro filtro) {
		
		List<Livro> livros = new ArrayList<Livro>();
		Criteria c = criaCriteria();
		
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (filtro.getTipo() != 3) {
			c.add(Restrictions.eq("status", filtro.getTipo() == 1 ? true : false));
		}
		
		c.addOrder(Order.desc("titulo"));
		livros = c.list();
		
		return livros;
	}
	
	public Livro findByCodigo(String codigo) {
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (codigo != null) {
			c.add(Restrictions.eq("codigo", codigo));
			c.add(Restrictions.eq("status", true));
			
		}

		return (Livro) c.uniqueResult();
	}
}