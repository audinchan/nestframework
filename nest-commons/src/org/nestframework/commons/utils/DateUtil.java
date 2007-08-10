package org.nestframework.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static Date longToDate(long longdata) {
		return new Date(longdata);
	}

	/**
	 * Format data to string with specified style.
	 * 
	 * @param dtmDate
	 *            Date
	 * @param num
	 *            style. 1:YYYY.MM.DD 2:hh:mm:ss 3:YYYY.MM.DD HH:MM:ss
	 *            4:YYYY-MM-DD 5:YYYY 6:MM 7:DD
	 * @return dateString String
	 */
	public static String dateToStr(java.util.Date dtmDate, int num) {
		if (dtmDate == null)
			return "";
		String f;
		switch (num) {
		case 1:
			f = "yyyy.MM.dd";
			break;
		case 2:
			f = "kk:mm:ss";
			break;
		case 3:
			f = "yyyy.MM.dd kk:mm:ss";
			break;
		case 4:
			f = "yyyy-MM-dd";
			break;
		case 5:
			f = "yyyy";
			break;
		case 6:
			f = "MM";
			break;
		case 7:
			f = "dd";
			break;
		default:
			f = "yyyy.MM.dd kk:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(f);
		String dateString = sdf.format(dtmDate);
		return dateString;
	}

	/**
	 * Format data to string.
	 * 
	 * @param dtmDate
	 *            Date
	 * @param formatstr
	 *            format
	 * @return dateString String
	 */
	public static String dateToStr(java.util.Date dtmDate, String formatstr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatstr);
		String dateString = sdf.format(dtmDate);
		return dateString;
	}

	/**
	 * Normal data format.
	 * 
	 * @return String[]
	 */
	private static String[] getdateformat() {
		String[] formatstring = { "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd" };
		return formatstring;
	}

	/**
	 * Parse String to Date.
	 * 
	 * @param dateString
	 *            String of Date, the format is yyyy-MM-dd or yyyy/MM/dd or
	 *            yyyy.MM.dd
	 * @return Date
	 * @throws ParseException
	 */
	public static Date stringToDate(String dateString) throws ParseException {
		String[] formatstring = getdateformat();
		int index = 0;
		Date parseDate = null;
		ParseException throwe = null;
		SimpleDateFormat sdf = new SimpleDateFormat();

		while (formatstring != null && index < formatstring.length) {
			try {
				sdf.applyPattern(formatstring[index]);
				index++;
				parseDate = sdf.parse(dateString);
				break;
			} catch (ParseException gete) {
				throwe = gete;
				continue;
			}
		}
		if (parseDate == null)
			throw throwe;
		return parseDate;
	}

}
