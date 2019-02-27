package br.com.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.enums.TipoEmprestimo;
import br.com.proline.model.Aluno;
import br.com.proline.model.Configuracoes;
import br.com.proline.model.Emprestimo;
import br.com.proline.model.Reserva;

public class Email {

	public String descricao;
	// Empréstimo, Renovação ou Devolução
	public static void emailEmprestimo(Aluno aluno, List<Emprestimo> listaEmprestimos, Configuracoes conf,
			TipoEmprestimo tipo) {

		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(conf.getLoginEmailBiblioteca(), conf.getSenhaEmailBiblioteca());
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(conf.getLoginEmailBiblioteca())); // Remetente

			Address[] toUser = InternetAddress.parse(aluno.getEmail());

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("[BIBLIOTECA CEMP-UFC]Comprovante de " + tipo.getNome());// Assunto
			message.setContent(Email.mensagemEmailEmprestimo(aluno, listaEmprestimos, tipo),
					"text/html; charset=utf-8");
			Transport.send(message);

			System.out.println("Feito!!!");

		} catch (MessagingException e) {
			FacesUtil.addErroMessage(e.toString());
			throw new RuntimeException(e);
		}
	}

	// RESERVA
	public static void emailReserva(Aluno aluno, List<Reserva> listaReserva, Configuracoes conf) {

		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(conf.getLoginEmailBiblioteca(), conf.getSenhaEmailBiblioteca());
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(conf.getLoginEmailBiblioteca())); // Remetente

			Address[] toUser = InternetAddress.parse(aluno.getEmail());

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("[BIBLIOTECA CEMP-UFC] Comprovante de Reserva");// Assunto
			message.setContent(Email.mensagemEmailReserva(aluno, listaReserva), "text/html; charset=utf-8");
			Transport.send(message);

			System.out.println("Feito!!!");

		} catch (MessagingException e) {
			FacesUtil.addErroMessage(e.toString());
			throw new RuntimeException(e);
		}
	}

	public static String mensagemEmailEmprestimo(Aluno aluno, List<Emprestimo> listaEmprestimo, TipoEmprestimo tipo) {
		// precisa ser melhorado
		String dataOperacao = Data.formataData(listaEmprestimo.get(0).getDataEmprestimo());
		String dataPrevisaoDevolucao = Data.formataData(listaEmprestimo.get(0).getDataDevolucaoPrevista());
		String dataDevolucao = new String();

		if (tipo.equals(TipoEmprestimo.DEVOLUCAO))
			dataDevolucao = Data.formataData(listaEmprestimo.get(0).getDataDevolucao());

		StringBuilder mensagem = new StringBuilder();
		StringBuilder listaLivros = new StringBuilder();

		for (Emprestimo emp : listaEmprestimo) {
			listaLivros.append("- " + emp.getLivro().getTitulo() + "<br/>");
		}
		mensagem.append("<b>Data do Empréstimo: </b>" + dataOperacao + "<br/>");
		if (tipo.equals(TipoEmprestimo.DEVOLUCAO))
			mensagem.append("<b>Data da Devolução: </b>" + dataDevolucao + "<br/>");
		else
			mensagem.append("<b>Data Prevista da Devolução: </b>" + dataPrevisaoDevolucao + "<br/>");

		mensagem.append("<b>Exemplares: </b><br/>");
		mensagem.append(listaLivros);

		return mensagem.toString();
	}

	public static String mensagemEmailReserva(Aluno aluno, List<Reserva> listaReserva) {
		// precisa ser melhorado
		String dataOperacao = Data.formataData(listaReserva.get(0).getDataReserva());

		StringBuilder mensagem = new StringBuilder();
		StringBuilder listaLivros = new StringBuilder();

		for (Reserva emp : listaReserva) {
			listaLivros.append("- " + emp.getEmprestimo().getLivro().getTitulo() + "<br/>");
		}
		mensagem.append("<b>Data da Reserva: </b>" + dataOperacao + "<br/>");

		mensagem.append("<b>Exemplares: </b><br/>");
		mensagem.append(listaLivros);

		return mensagem.toString();
	}

}
