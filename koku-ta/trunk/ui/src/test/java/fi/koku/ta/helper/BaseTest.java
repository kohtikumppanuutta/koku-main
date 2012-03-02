package fi.koku.ta.helper;

import org.openqa.selenium.WebDriver;

import fi.koku.ta.helper.config.Configuration;
import fi.koku.ta.helper.timestamp.*;
import fi.koku.ta.helper.utils.*;


/*
 * Page class is used as base class for Test class
 */
public class BaseTest {
	
	public Configuration configuration;
	public WebDriver driver;
	public String testMethodName;
	
	public Utils utils;

	public BaseTest() {
		
		configuration = new Configuration();
		utils = new Utils();
		
		/*
		 * Following variables are supposed to be initialized using setupTest() method below 
		 * (i.e. when browser is instance is created)
		 */
		driver=null;
		testMethodName=null;
		
	}
	
	public void setupTest(WebDriver driver,StackTraceElement e[]) {
		this.driver=driver;
		this.testMethodName = getNameOfMethod(e);
		
		printInfoAboutTest(testMethodName);
	}
	
	
	/*
	 * Helper method to capture screen shot
	 */
	public void captureScreenShot(String fileName) {
		utils.captureScreenShot(driver,fileName, getTimeStamp());
	}
	
	public void captureScreenShot() {
		utils.captureScreenShot(driver,testMethodName, getTimeStamp());
	}

	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	
	/*
	 * Helper method to streamline INFO output about test under execution
	 */
	public void printInfoAboutTest(String methodName) {
		System.out.println(String.format("INFO: test under execution: %s",methodName));
	}

	
	/*
	 * Helper method to get time stamp in following format: yyyy_MM_dd_HH_mm_ss
	 */
	public String getTimeStamp_FileNameFormat() {
		return new TimeStamp().getTimeStamp_FileNameFormat();
	}
	
	/*
	 * Helper method to get time stamp in following format: yyyy-MM-dd HH-mm-ss
	 */
	public String getTimeStamp() {
		return new TimeStamp().getTimeStamp_DefaultFormat();
	}
	
	
	/*
	 * Helper method to get name of current method
	 * 
	 * HINT:
	 * How to get input parameter: Thread.currentThread().getStackTrace()
	 * 
	 */
	public String getNameOfMethod(StackTraceElement e[]) {
		return Utils.getNameOfMethod(e);
	}
	
	/*
	 * Helper method to pause execution
	 */
	public void pauseSeconds(long pauseSeconds) {
		Utils.pauseSeconds(pauseSeconds);
	}
	
	/*
	 * Helper method to pause execution
	 */
	public void pauseMilliSeconds(long pauseMilliSeconds) {
		Utils.pauseMilliSeconds(pauseMilliSeconds);
	}



}
