package fi.koku.ta.app.page;

import fi.koku.ta.helper.BasePage;
import fi.koku.ta.helper.BaseTest;

import org.openqa.selenium.*;


public class Huoltaja extends BasePage {
	
	public Huoltaja(BaseTest test) {
		
		super(test);
		
		driver.navigate().to(getURL_Guardian());
		
	}

	
	
	/*
	 * Login
	 */
	public void login(String username,String password) {
		
		/*
		 * Handle difference in Demo and Development environments 
		 */
		if(driver.findElements(By.xpath("//a[text() = 'Login']")).size()==0) {
			String demoXPath="//div[@class='AccountBlock AdministratorUser']";
		    if(driver.findElements(By.xpath(demoXPath)).size()==1) {
		    	driver.findElement(By.xpath(demoXPath)).click();
		    }
		} else {
			driver.findElement(By.xpath("//a[text() = 'Login']")).click();
		}
		
		WebElement we = waitTofindElementWithXPath("//input[@name='username']");
		we.clear();
		we.sendKeys(username);

		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
		
		driver.findElement(By.xpath("//a[text()='Sign in']/parent::div/parent::div/parent::div")).click();
		
		driver.navigate().to(waitTofindElementWithXPath("//div/a[contains(text(), 'KKS')]").getAttribute("href"));
		
		//Ensure that expected elements are found on page
		driver.findElement(By.xpath("//ul/li[@id='msg_inbox']/a[text() = 'Saapuneet']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_outbox']/a"));
		driver.findElement(By.xpath("//ul/li/a[text() = 'Arkistoidut']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_archive_inbox']/a[text() = 'Saapuneet']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_archive_outbox']/a"));

		
	}
	
	
	/*
	 * Logout
	 */
	public void logout() {
		
		/* 
		 * Selenium issue with Firefox 10:
		 * http://code.google.com/p/selenium/issues/detail?id=3313
		 */
		
		/*
		WebElement menu = driver.findElement(By.xpath("//div[@id='StarToolBarPortlet']"));
		
		Actions a = new Actions(driver); 
		a.build(); 
		a.moveToElement(menu);
		a.perform(); 
		
		driver.findElement(By.xpath("//a[@class='SignOutIcon']")).click();
		
		*/
		
		/*
		 * Use following approach for now (i.e. call script directly)
		 */
		
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver; js.executeScript("eXo.portal.logout()");
		} catch (Exception e2) {
			driver.close();
		}
		
		waitUntilElementNotFoundWithXPath("//div[@id='UIUserInfoPortlet']");
		
	}
	

}
