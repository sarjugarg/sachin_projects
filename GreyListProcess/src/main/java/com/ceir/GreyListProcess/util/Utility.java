package com.ceir.GreyListProcess.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.ceir.GreyListProcess.model.GreylistDb;
import com.ceir.GreyListProcess.model.GreylistDbHistory;
import com.ceir.GreyListProcess.model.constants.GreyListOperation;


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

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String transactionId = dateFormat.format(yesterday());	
		return transactionId;

	}
	
	public  Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);
		return cal.getTime();
	}
	
	public  Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, +days);
		return cal.getTime();
	}
	

	public	String getYesterDayMonth() {
		DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
		return dateFormat.format(yesterday());
	}


	public	void writeInFile(String fileName, String header, String record) {
		try {

			File tmpDir = new File(fileName);
			boolean exists = tmpDir.exists();
			if (exists) {
				tmpDir.delete();
				tmpDir.createNewFile();
			}
			FileWriter fw = new FileWriter(fileName,true);
	
			//if (!exists)
				fw.append(header);

			fw.append("\n");
			fw.append(record);
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public	void writeGreyListInFile(String fileName, String header, List<GreylistDb> record) {
		try {
			File tmpDir = new File(fileName);
			boolean exists = tmpDir.exists();
			if (exists) {
				tmpDir.delete();
				tmpDir.createNewFile();
			}

			FileWriter fw = new FileWriter(fileName,true);
			
			//if (!exists)
				fw.append(header);
				
			fw.append("\n");
			for(GreylistDb greyListData:record) {
				fw.append(String.valueOf(greyListData.getActualImei()));
				fw.append("\n");
			}
			
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public	void writeGreyListHistoryInFile(String fileName, String header, List<GreylistDbHistory> record) {
		try {

			File tmpDir = new File(fileName);
			boolean exists = tmpDir.exists();
			if (exists) {
				tmpDir.delete();
				tmpDir.createNewFile();
			}

			
			FileWriter fw = new FileWriter(fileName,true);
			//if (!exists)
				fw.append(header);

			fw.append("\n");
			for(GreylistDbHistory greyListData:record) {
				fw.append(String.valueOf(greyListData.getActualImei()) +"," +GreyListOperation.getUserStatusByCode(greyListData.getOperation()).getDescription());
				fw.append("\n");
			}
			
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
	
	public String convertToDateIdformat(Date date) {
		DateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmm");
		String currentDate=dateFormat.format(date);
		return currentDate;
	}
	public String convertToDate(Date date) {
		DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
		String currentDate=dateFormat.format(date);
		return currentDate;
	}
	
	public String convertToDateOnlyformat(Date date) {
		DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
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
	
}



