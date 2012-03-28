package fi.koku.ta.app.page.viestit;

import fi.koku.ta.helper.BasePage;
import fi.koku.ta.helper.BaseTest;

import org.openqa.selenium.*;


public class Uusi_viesti extends BasePage {
	
	public Uusi_viesti(BaseTest test) {
		
		super(test);
		
		waitTofindElementWithXPath("//ul/li[@id='msg_new']/a[text() = 'Uusi viesti']").click();
		
		WebElement iframe = waitTofindElementWithXPath("//iframe[@class='xforms_container_iframe']");
		driver.switchTo().frame(iframe);
		
		//Ensure that expected element is loaded 
		waitTofindElementWithXPath("//input[@label='searchString']");

	}
	
	
	public void set_HETU(String hetu) {
		
		driver.findElement(By.xpath("//input[@label='searchString']")).sendKeys(hetu);
	}
	
	public void hae() {
		driver.findElement(By.xpath("//span[text() = 'Hae']")).click();
	}
	
	public void set_Lisaa_vanhemmat_vastaanottajiksi() {
		driver.findElement(By.xpath("//span[text()='Lisää haetun lapsen vanhemmat vastaanottajiksi']")).click();
	}

	public void set_Aihe(String aihe) {
		driver.findElement(By.xpath("//input[@title='Viestin aihe']")).sendKeys(aihe);
	}
	
	public void set_Viesti(String viesti) {
		driver.findElement(By.xpath("//textarea[@title='Viesti']")).sendKeys(viesti);
	}
	
	public void laheta() {
		driver.findElement(By.xpath("//span[text()='Lähetä käyttäjäviesti']")).click();
		
	}

}
