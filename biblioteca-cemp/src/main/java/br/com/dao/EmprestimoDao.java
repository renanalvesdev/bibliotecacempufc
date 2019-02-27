package br.com.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.proline.model.Aluno;
import br.com.proline.model.Emprestimo;
import br.com.proline.model.Livro;
import br.com.util.Data;

public class EmprestimoDao extends DaoGeneric<Emprestimo> {

	public List<Emprestimo> findByExample(Emprestimo filtro) {

		List<Emprestimo> lista = new ArrayList<>();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		// 3 -> todos , 1 -> corrente , 2 -> encerrado
		if (filtro.getSituacaoFiltro() != 3) {
			c.add(Restrictions.eq("status", filtro.getSituacaoFiltro() == 1 ? true : false));
		}

		if (filtro.getDataInicialFiltro() != null) {
			c.add(Restrictions.ge("dataEmprestimo", filtro.getDataInicialFiltro()));
		}

		if (filtro.getDataFinalFiltro() != null) {
			c.add(Restrictions.le("dataEmprestimo", filtro.getDataFinalFiltro()));
		}

		// hoje
		if (filtro.getTipoDataEmprestimo() == 1) {
			c.add(Restrictions.between("dataEmprestimo", Data.dataSemMinutos(new Date()), new Date()));
		}

		// este mes
		if (filtro.getTipoDataEmprestimo() == 2) {
			c.add(Restrictions.between("dataEmprestimo", Data.primeiroDiaDoMes(), new Date()));
		}
		c.addOrder(Order.desc("dataEmprestimo"));

		lista = c.list();

		return lista;
	}

	public List<Emprestimo> findByAluno(Aluno aluno) {

		List<Emprestimo> lista = new ArrayList<>();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (aluno != null) {

			c.add(Restrictions.eq("aluno", aluno));
			c.add(Restrictions.eq("status", true));
		}

		lista = c.list();

		return lista;
	}

	public Emprestimo findByLivro(Livro livro) {

		Emprestimo emprestimo = new Emprestimo();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (livro != null) {
			c.add(Restrictions.eq("livro", livro));
			c.add(Restrictions.eq("status", true));
		}

		emprestimo = (Emprestimo) c.setMaxResults(1).uniqueResult();

		return emprestimo;
	}

	public List<Emprestimo> findByEmDiaOuAtrasados() {

		List<Emprestimo> lista = new ArrayList<>();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		c.add(Restrictions.ne("situacao", 3));
		lista = c.list();

		return lista;
	}

	public List<Emprestimo> findByTodos() {

		List<Emprestimo> lista = new ArrayList<>();
		Criteria c = criaCriteria();
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		lista = c.list();
		
		return lista;
	}
	
	
}
