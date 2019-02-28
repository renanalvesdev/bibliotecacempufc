package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Usuario;

public class UsuarioDao extends DaoGeneric<Usuario> {

	public List<Usuario> findByExample(Usuario filtro) {

		List<Usuario> lista = new ArrayList<Usuario>();
		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (filtro != null && filtro.getSituacaoFiltro() != 3) {
			c.add(Restrictions.eq("status", filtro.getSituacaoFiltro() == 1 ? true : false));
		}

		lista = c.list();
		
		session.getTransaction().commit();
		session.close();

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

	public Usuario login(Usuario usuarioFiltro) {
		
		Usuario usuario = new Usuario();
		
		Session session = criaSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(getClasse());

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		c.add(Restrictions.eq("login", usuarioFiltro.getLogin()));
		c.add(Restrictions.eq("senha", usuarioFiltro.getSenha()));
		c.add(Restrictions.eq("status", true));

		
		usuario = (Usuario) c.uniqueResult(); 
		
		session.getTransaction().commit();
		session.close();
		
		
		return usuario;
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