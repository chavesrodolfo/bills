package br.com.bills.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

	public static String capitalize(String s) {
		if (s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	public static Double precision(Double d) {
		Locale.setDefault(new Locale("pt", "BR"));
		DecimalFormat twoDForm = new DecimalFormat("0.00");
		return Double.valueOf(twoDForm.format(d).replace(",", "."));
	}

	public static String formatarReal(Double valor) {
		Locale ptBR = new Locale("pt", "BR");
		NumberFormat moedaFormat = NumberFormat.getCurrencyInstance(ptBR);
		return moedaFormat.format(valor);
	}
	
	public static String getMes(int i) {
		if (i == 0) return "Janeiro";
		if (i == 1) return "Fevereiro";
		if (i == 2) return "Março";
		if (i == 3) return "Abril";
		if (i == 4) return "Maio";
		if (i == 5) return "Junho";
		if (i == 6) return "Julho";
		if (i == 7) return "Agosto";
		if (i == 8) return "Setembro";
		if (i == 9) return "Outubro";
		if (i == 10) return "Novembro";
		if (i == 11) return "Dezembro";
		return "";
	}

	public static String formatarData(Date data) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(data);
	}

	public static int getMesAtual() {
		Calendar data = Calendar.getInstance();
		data.setTime(new Date());
		int mes = data.get(Calendar.MONTH);
		return mes;
	}
}
