package app.page;

import helper.BaseTest;
import helper.BasePage;

import org.openqa.selenium.*;


public class Tyontekija extends BasePage {
	
	public static final String USER_ADMIN = "admin";
	public String role;

	
	public Tyontekija(BaseTest test) {
		
		super(test);
		
		role="";
		driver.navigate().to(getURL_Employee());
		
	}
	
	public Tyontekija(BaseTest test,String role) {
		
		super(test);
		
		this.role=role;
		driver.navigate().to(getURL_Employee());
		
	}
	
	/*
	 * Login
	 */
	public void login(String username,String password) {
		
		waitTofindElementWithXPath("//a[text() = 'Login']").click();
		
		waitTofindElementWithXPath("//input[@name='j_username']").sendKeys(username);
		
		driver.findElement(By.xpath("//input[@name='j_password']")).sendKeys(password);
		
		/*
		 * Screen capture added for demo purpose
		 */
		captureScreenShot("Esimerkki_Tyontekija_Login_Vaihe_Tiedot_Syotetty");
		
		driver.findElement(By.xpath("//form[@name='loginform']")).submit();
		
		//Ensure that expected element is found on page
		waitTofindElementWithXPath("//div[@id='loginIframe']");
		
		/*
		 * Screen capture added for demo purpose
		 */
		captureScreenShot("Esimerkki_Tyontekija_Login_Vaihe_Suoritettu");
		
	}
	
	
	/*
	 * Logout
	 */
	public void logout() {
		
		driver.switchTo().defaultContent();
		waitTofindElementWithXPath("//a[text() = 'Logout']").click();
		
		waitTofindElementWithXPath("//div[contains(text(), 'Kirjaudu järjestelmään')]");
		
	}
	
	
	/*
	 * Lokihallinta
	 */
	public void lokihallinta() {
		waitTofindElementWithXPath("//a[text() = 'LOK']").click();
	}
	
	public void lokihallinta_hae_lokitietoja() {
		waitTofindElementWithXPath("//p/a[contains(text(), 'Hae lokitietoja')]").click();
		
		//TODO: check user role and add asserts based on this 
	}
	
	public void lokihallinta_lokitietojen_arkistointi() {
		waitTofindElementWithXPath("//p/a[contains(text(), 'Lokitietojen arkistointi')]").click();
	}
	
	public void lokihallinta_takaisin() {
		waitTofindElementWithXPath("//a[contains(text(), 'Takaisin')]").click();
	}
	
	/*
	 * Sopimukset ja Suunnitelmat
	 */
	public void sopimuksetJaSuunnitelmat() {
		
		waitTofindElementWithXPath("//ul/li[@id='kks']/a[text() = 'Sopimukset ja suunnitelmat']").click();
		
		//Ensure that expected elements are found on page
		waitTofindElementWithXPath("//div/h1[contains(text(), 'Sopimukset ja Suunnitelmat')]");
		waitTofindElementWithXPath("//input[@value='Hae tiedot']");
		
	}
	
	/*
	 * Viestit
	 */
	public void viestit() {
		
		driver.findElement(By.xpath("//ul[@id='tabsHeader']/li/a[contains(text(), 'Message')] ")).click();
		
		//Ensure that expected elements are found on page
		driver.findElement(By.xpath("//ul/li[@id='msg_new']/a[text() = 'Uusi viesti']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_inbox']/a[text() = 'Saapuneet']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_outbox']/a"));
		driver.findElement(By.xpath("//ul/li/a[text() = 'Arkistoidut']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_archive_inbox']/a[text() = 'Saapuneet']"));
		driver.findElement(By.xpath("//ul/li[@id='msg_archive_outbox']/a"));
		
	}

	


}
