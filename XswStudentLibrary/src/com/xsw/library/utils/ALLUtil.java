package com.xsw.library.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ALLUtil {
	static SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd号");
	static SimpleDateFormat ydate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat ddate = new SimpleDateFormat("HH:mm");
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean isSameDate(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		// subYear==0,说明是同一年
		if (subYear == 0) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	public static String getDateFom(Date date) {
		String string = ydate.format(date);
		return string;
	}

	public static String getDateDay(Date date) {
		String string = ddate.format(date);
		return string;
	}

	public static String getdate(Date date) {
		String string = dateFm.format(date);
		return string;
	}

	/**
	 * 年月日 2015-3-2 @author lzx
	 * 
	 */
	public static String getYMD(long milliseconds) {
		Date date = null;
		try {
			date = new Date(milliseconds);
		} catch (Exception e) {
			// TODO: handle exception
			date = new Date();
		}
		String string = simpleDateFormat1.format(date);
		return string;
	}

	/**
	 * 年月日 2015-3-2 @author lzx
	 * 
	 */
	public static String getYMDhm(long milliseconds) {
		Date date = null;
		try {
			date = new Date(milliseconds);
		} catch (Exception e) {
			// TODO: handle exception
			date = new Date();
		}
		String string = ydate.format(date);
		return string;
	}

	public static String stringFilter(String str) throws PatternSyntaxException {

		String regEx = "[/\\:*?<>|\"\n\t]|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";

		Pattern p = Pattern.compile(regEx);

		Matcher m = p.matcher(str);

		return m.replaceAll("");

	}
	public static String StringFilter(String str) throws PatternSyntaxException {
		
		String regEx = "[/\\:*?<>|\"\n\t\\s*]|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
		
		Pattern p = Pattern.compile(regEx);
		
		Matcher m = p.matcher(str);
		
		return m.replaceAll("");
		
	}

}
