package br.com.bills.util;

import java.text.DecimalFormat;

public class Utils {

	public static String capitalize(String s) {
		if (s.length() == 0) {
			return s;
		}
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

	public static Double precision(Double d) {
		DecimalFormat twoDForm = new DecimalFormat("0.00");
		return Double.valueOf(twoDForm.format(d));
	}
}
