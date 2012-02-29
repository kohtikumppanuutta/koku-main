package helper.timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeStamp {
	
	private long epocTime;   //example format: 1323419303593
	private String fileNameFormat; //example format: 2012_02_24_13_32_55
	private String defaultFormat; //example format: 2012-02-24 13-32-55
	
	public TimeStamp() {
		setTimeStamp_CurrentTime();
	}
	
	public TimeStamp(long epocTime) {
		this.epocTime=epocTime;
		fileNameFormat=getFileNameFormat();
		defaultFormat=getDefaultFormat();
	}
	
	public TimeStamp(String epocTimeAsString) {
		epocTime=Long.parseLong(epocTimeAsString.trim());
		fileNameFormat=getFileNameFormat();
		defaultFormat=getDefaultFormat();
	}
	
	private long getCurrentTimeInMillis() {
		
		Calendar myCalendar   = Calendar.getInstance();
		return myCalendar.getTimeInMillis();
	}
	private String getFileNameFormat() {
		
		SimpleDateFormat formatter    =null;
		Calendar         myCalendar   =null;
		
		myCalendar  = Calendar.getInstance();
	    Date myDate = myCalendar.getTime();

	    formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	    
	    myDate.setTime(epocTime);
	    
	    return formatter.format(myDate);
		
	}
	private String getDefaultFormat() {
		SimpleDateFormat formatter    =null;
		Calendar         myCalendar   =null;
		
		myCalendar  = Calendar.getInstance();
	    Date myDate = myCalendar.getTime();

	    formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	    
	    myDate.setTime(epocTime);
	    
	    return formatter.format(myDate);
	}
	
	private void setTimeStamp_CurrentTime() {
		epocTime = getCurrentTimeInMillis();
		fileNameFormat=getFileNameFormat();
		defaultFormat=getDefaultFormat();
	}
	
	public long getTimeStamp_EpocTime() {
		return epocTime;
	}
	
	public String getTimeStamp_FileNameFormat() {
		return fileNameFormat;
	}
	
	public String getTimeStamp_DefaultFormat() {
		return defaultFormat;
	}
	


}
