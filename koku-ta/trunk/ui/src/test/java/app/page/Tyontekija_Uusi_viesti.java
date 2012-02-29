package app.page;

import helper.BaseTest;
import helper.BasePage;

import org.openqa.selenium.*;


public class Tyontekija_Uusi_viesti extends BasePage {
	
	public Tyontekija_Uusi_viesti(BaseTest test) {
		
		super(test);
		
		driver.findElement(By.xpath("//ul/li[@id='msg_new']/a[text() = 'Uusi viesti']")).click();
		
		WebElement iframe = driver.findElement(By.xpath("//iframe[@class='xforms_container_iframe']"));
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

	public void browserBack() {
		driver.navigate().back();
	}

}
