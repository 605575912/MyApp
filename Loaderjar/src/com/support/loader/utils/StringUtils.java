package com.support.loader.utils;


import com.support.loader.proguard.IProguard;

import java.text.DecimalFormat;

public class StringUtils implements IProguard {
	static DecimalFormat df2 = new DecimalFormat("0.00");
	static DecimalFormat df1 = new DecimalFormat("0.0");

	public static String getString(double value, int type) {
		String string = "";
		switch (type) {
			case 1:
				string = df1.format(value);
				break;
			case 2:
				string = df2.format(value);
				break;

			default:
				break;
		}
		if (string.equals("")) {
			string = value + "";
		}
		return string;
	}
	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
