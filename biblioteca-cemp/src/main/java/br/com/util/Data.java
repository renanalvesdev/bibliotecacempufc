package br.com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class Data {

	public static Date somaDatas(Date data, int acrescimo) {
		DateTime dataEntrada = new DateTime(data);
		return dataEntrada.plusDays(acrescimo).toDate();
	}

	public static int subtraiDatas(Date dataInicio, Date dataFim) {
		DateTime dataEntrada1 = new DateTime(dataInicio);
		DateTime dataEntrada2 = new DateTime(dataFim);

		return Days.daysBetween(dataEntrada1, dataEntrada2).getDays();
	}

	public static Date dataSemMinutos(Date data) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date today = data;

		try {
			today = formatter.parse(formatter.format(today));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return today;
	}
	
	public static String formataData(Date data) {
		SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formata.format(data);
		
		return dataFormatada;
	}

	public static Date primeiroDiaDoMes() {
		LocalDate todaydate = LocalDate.now();
		return Data.dataSemMinutos(Data.localDateToJava(todaydate.withDayOfMonth(1)));
	}

	public static Date localDateToJava(LocalDate localDate) {
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}

}
