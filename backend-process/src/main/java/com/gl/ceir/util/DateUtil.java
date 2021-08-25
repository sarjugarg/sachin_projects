package com.gl.ceir.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DateUtil {
	
	public static String nextDate(int nextdateDay) {
		return nextDate("yyyy-MM-dd", nextdateDay);
	}

	public static String nextDate(String format, int nextdateDay) {
		if(Objects.isNull(format)) {
			format = "yyyy-MM-dd";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DAY_OF_MONTH, nextdateDay);  
		return sdf.format(cal.getTime());  
	}

}
