package fi.koku.ta.helper;

import fi.koku.ta.helper.config.*;
import fi.koku.ta.helper.utils.Utils;

import org.openqa.selenium.*;

/*
 * Page class is used as base class for Page Object Model based test automation implementation
 */
public class BasePage {
	
	public BaseTest test;
	public WebDriver driver;
	public Configuration configuration;
	public Utils utils;

	public BasePage(BaseTest test) {
		this.test= test;
		driver=test.driver;
		configuration=test.configuration;
		utils=test.utils;
	}

	
	public String getURL_Employee() {
		return configuration.getURL_Employee();
	}
	
	public String getURL_Guardian() {
		return configuration.getURL_Guardian();
	}
	
	
	/*
	 * Helper method to wait for expected WebElement using xpath
	 */
	public WebElement waitTofindElementWithXPath(String xpathExpression) {
		return utils.waitTofindElementWithXPath(driver,xpathExpression);
	}
	
	/*
	 * Helper method to wait for expected WebElement using CssSelector
	 */
	public WebElement waitTofindElementWithCssSelector(String cssSelectorExpression) {
		return utils.waitTofindElementWithCssSelector(driver, cssSelectorExpression);
	}
	
	
	/*
	 * Helper method to ensure that leaving page works
	 */
	public void waitUntilElementNotFoundWithXPath(String xpathExpression) {
		utils.waitUntilElementNotFoundWithXPath(driver, xpathExpression);
	}
	
	
	/*
	 * Helper method to ensure that leaving page works
	 */
	public void waitUntilElementNotFoundWithCssSelector(String cssSelectorExpression) {
		utils.waitUntilElementNotFoundWithCssSelector(driver, cssSelectorExpression);
	}

	
	/*
	 * Helper method provided as alternative solution to 'sendKeys' method 
	 * (i.e. speed up execution via clipboard copy/paste functionality) 
	 */
	public void sendKeysViaClipboard(WebElement element, String data) {
		utils.sendKeysViaClipboard(element, data);
	}

	
	/*
	 * Helper method to get name of current method
	 */
	public String getNameOfMethod(StackTraceElement e[]) {
		return Utils.getNameOfMethod(e);
	}
	
	/*
	 * Helper method to capture screen shot
	 */
	public void captureScreenShot(String fileName) {
		utils.captureScreenShot(driver, fileName, test.getTimeStamp_FileNameFormat());
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
