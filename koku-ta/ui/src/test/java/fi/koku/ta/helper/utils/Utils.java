
package fi.koku.ta.helper.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class Utils {
	
	public static final String USER_ADMIN = "admin";

	public int maxWaitTimeInSeconds=30;


	public Utils() {
		
	}
	
	/*
	 * Helper method to resolve name of current method
	 */
	public static String getNameOfMethod(StackTraceElement e[]) {
		
		boolean doNext = false;
		for (StackTraceElement s : e) {
			if (doNext) {
				//System.out.println(s.getMethodName());
				return s.getMethodName();
		     }
		     doNext = s.getMethodName().equals("getStackTrace");
		}
		/*
		 * This code should never be reached
		 */
		return "Utils_getNameOfMethod";
	}
	
	/*
	 * Use this method to wait for expected WebElement
	 */
	public WebElement waitTofindElementWithXPath(WebDriver driver, String xpathExpression) {
		Wait<WebDriver> wait = new WebDriverWait(driver, maxWaitTimeInSeconds);
		return wait.until(locateElementWithLocator(By.xpath(xpathExpression)));
		
	}
	
	
	public WebElement waitTofindElementWithCssSelector(WebDriver driver,String cssSelectorExpression) {
		Wait<WebDriver> wait = new WebDriverWait(driver, maxWaitTimeInSeconds);
		return wait.until(locateElementWithLocator(By.cssSelector(cssSelectorExpression)));
		
	}


	private ExpectedCondition<WebElement> locateElementWithLocator(final By locator) {
		
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				List<WebElement> weList = driver.findElements(locator);
				if (weList.size()>0) {
					return weList.get(0);
				}
				return null;
		    }
		};
		
	}
	
	/*
	 * Helper method - Ensure that navigation works (i.e. leave page) 
	 */
	public void waitUntilElementNotFoundWithXPath(WebDriver driver, String xpathExpression) {
		
		for(int i=0;i<maxWaitTimeInSeconds;i++) {
			try {
				driver.findElement(By.xpath(xpathExpression));
				pauseSeconds(1);
			} 
			catch (NoSuchElementException e) {
				return;
			}
			catch (WebDriverException e) {
				return;
			}
		}
		
	}

	/*
	 * Helper method - Ensure that navigation works (i.e. leave page) 
	 */
	public void waitUntilElementNotFoundWithCssSelector(WebDriver driver, String cssSelectorExpression) {
		
		for(int i=0;i<maxWaitTimeInSeconds;i++) {
			try {
				driver.findElement(By.cssSelector(cssSelectorExpression));
				pauseSeconds(1);
			} 
			catch (NoSuchElementException e) {
				return;
			}
			catch (WebDriverException e) {
				return;
			}
		}
		
	}


	
	/*
	 * Alternative solution for slow sendKeys() method provided by WebDriver
	 * i)  Copy string to clipboard
	 * ii) Paste data from clipboard
	 * 
	 * Source: http://www.exampledepot.com/egs/java.awt.datatransfer/ToClip.html
	 */
	public void sendKeysViaClipboard(WebElement element, String data) {
		
		setClipboard(data);
		element.sendKeys(Keys.CONTROL + "v");
		
	}
	
	/*
	 *  This method writes a string to the system clipboard.
	 *  otherwise it returns null.
	 */
	private void setClipboard(String str) {
		
		StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		
	}

	/*
	 *  If a string is on the system clipboard, this method returns it;
	 *  otherwise it returns null.
	 */
	public static String getClipboard() {
		
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		
		try {
			if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String text = (String)t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		} catch (UnsupportedFlavorException e) {
		} catch (IOException e) {
		}
		
		return null;
	}

	
	public void captureScreenShot(WebDriver driver, String fileName,String timeStamp) {
		
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		
		String directoryPath="Ixonos_ScreenShots";
		String filePath = directoryPath + File.separator + fileName + "_" + timeStamp + ".png";
		
		try {
			createDirectory(directoryPath);
		}
		catch (Exception e) {
			System.out.format(e.getMessage());
			return;
		}
		
		try {
			FileUtils.copyFile(scrFile, new File(filePath));  
		} catch (Exception e) {
			System.out.format("FAILURE: failed to capture screen\n\n");
		}

	}
	
	/*
	* Create a directory; all non-existent ancestor directories are automatically created
	*/
	public static void createDirectory(String directoryName) throws Exception {
		
		File file=new File(directoryName);
		if(!file.exists()) {
			
		    boolean success = file.mkdirs();
		    if (!success) {
		    	String message=String.format("FAILURE: in Utils::createDirectory() - Directory:%s\n",directoryName);
		    	throw new Exception(message);
		    }
			
		}

	}

	

	
	public static void pauseSeconds(long pauseSeconds) {
		
		System.out.format("INFO:Utils::pauseSeconds: %d ...",pauseSeconds);
		try {
			Thread.sleep(pauseSeconds*1000);
		} catch (InterruptedException e) {
		}
		System.out.format("consumed\n");
		
	}
	
	public static void pauseMilliSeconds(long pauseMilliSeconds) {
		
		System.out.format("INFO:Utils::pauseMilliSeconds: %d ...",pauseMilliSeconds);
		try {
			Thread.sleep(pauseMilliSeconds);
		} catch (InterruptedException e) {
		}
		System.out.format("consumed\n");
		
	}



}
