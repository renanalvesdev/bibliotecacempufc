package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.proline.model.Usuario;


public class UsuarioDao extends DaoGeneric<Usuario> {
	
	
	public List<Usuario> findByExample(Usuario filtro) {
		
		List<Usuario> lista = new ArrayList<Usuario>();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(filtro != null && filtro.getSituacaoFiltro() != 3) {
			c.add(Restrictions.eq("status", filtro.getSituacaoFiltro() == 1 ? true : false));
		}
	
		lista = c.list();
		
		return lista;
	}
	
	public Usuario findByLogin(String cpf) {
		cpf = cpf.replaceAll("[.-]", "");

		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (!StringUtils.isBlank(cpf))
			c.add(Restrictions.eq("login", cpf));

		return (Usuario) c.uniqueResult();
	}

	
	public Usuario login(Usuario usuario) {
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		c.add(Restrictions.eq("login", usuario.getLogin()));
		c.add(Restrictions.eq("senha", usuario.getSenha()));
		c.add(Restrictions.eq("status", true));

		return (Usuario) c.uniqueResult();
	}
	
	/*
	 * public Usuario findByFuncionario (Funcionario funcionario) { Criteria c =
	 * criaCriteria(); c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	 * 
	 * if(funcionario != null) { c.add(Restrictions.eq("funcionario", funcionario));
	 * }
	 * 
	 * 
	 * return (Usuario) c.uniqueResult(); }
	 */

}