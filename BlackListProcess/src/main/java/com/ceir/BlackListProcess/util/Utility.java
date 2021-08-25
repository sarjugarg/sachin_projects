package com.ceir.BlackListProcess.util;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;


@Component
public class Utility {

	Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}


	public	String getYesterdayDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(yesterday());
	}

	public String getYesterdayId() {

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		String transactionId = dateFormat.format(yesterday());	
		return transactionId;

	}

	public	String getYesterDayMonth() {
		DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
		return dateFormat.format(yesterday());
	}


	public	void writeInFile(String fileName, String header, String record) {
		try {

			File tmpDir = new File(fileName);
			boolean exists = tmpDir.exists();

			FileWriter fw = new FileWriter(fileName, true);

			if (!exists)
				fw.append(header);

			fw.append("\n");
			fw.append(record);
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTxnId() {

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		String transactionId = dateFormat.format(date);	
		return transactionId;

	}

	public String currentDate() {
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String currentDate=dateFormat.format(date);
		return currentDate;
	}

	public String convertToDateformat(Date date) {
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String currentDate=dateFormat.format(date);
		return currentDate;
	}
	public Date convertDateToFormat(Date date) {
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date formattedDate = null;
		try {
			formattedDate=new SimpleDateFormat("yyyy-MM-dd").parse(dateFormat.format(date));
			return formattedDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return formattedDate;
		}
	}

	public String convertToDateIdformat(Date date) {
		DateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmm");
		String currentDate=dateFormat.format(date);
		return currentDate;
	}

	public  long getDifferenceDays(String input1, String input2) {

		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date d1=sdf.parse(input1);
			Date d2;
			d2 = sdf.parse(input2);
			long diff = d2.getTime() - d1.getTime();
			return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		} catch (ParseException e) {
			e.printStackTrace();
			long unit=0;
			return unit;
		}
	}

	public String addDaysInDate(Integer days,Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date); 
		c.add(Calendar.DATE, days); // Adding days
		String output = sdf.format(c.getTime());
		return output;
	}

	public String addDaysInCurrentDate(Integer days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date); 
		c.add(Calendar.DATE, days); // Adding days
		String output = sdf.format(c.getTime());
		return output;
	}

	public Date stringToDate(String date) {

		Date date1;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			return date1;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}  

	}
	public Date currentOnlyDate() {

		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			Date todayWithZeroTime = formatter.parse(formatter.format(today));
			return todayWithZeroTime;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}  
	}
	
	
}
