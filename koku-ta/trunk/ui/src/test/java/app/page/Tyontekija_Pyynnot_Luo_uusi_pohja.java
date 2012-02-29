package app.page;

import helper.BaseTest;
import helper.BasePage;

import org.openqa.selenium.*;


public class Tyontekija_Pyynnot_Luo_uusi_pohja extends BasePage {

	public Tyontekija_Pyynnot_Luo_uusi_pohja(BaseTest test) {
		
		super(test);
		
		driver.findElement(By.xpath("//ul/li[@id='luopohja']/a")).click();
		
		WebElement iframe = waitTofindElementWithXPath("//iframe[@class='xforms_container_iframe']");
		driver.switchTo().frame(iframe);
		
		//Ensure that expected element is loaded 
		waitTofindElementWithXPath("//span[text()='Lisää kysymys']");
		
	}
	
	public void set_Otsikko(String otsikko) {
		driver.findElement(By.xpath("//span[text()='Lomakkeen otsikko']/following-sibling::span//input")).sendKeys(otsikko);
	}

	private void set_Kysymys(String kysymys) {
		
		WebElement we = driver.findElement(By.xpath("//span[text()='Kysymys']/following-sibling::span//input"));
        we.clear();
		we.sendKeys(kysymys);
		
	}

	private void lisaa_Kysymys() {
		driver.findElement(By.xpath("//span[text()='Lisää kysymys']")).click();
	}


	public void lisaa_Kysymys_Vastaustyyppi_Vapaa_tekstikentta(String kysymys) {
		
		set_Kysymys(kysymys);
		driver.findElement(By.xpath("//span[@label='vastausVapaa']")).click();
		lisaa_Kysymys();
		
	}
	public void lisaa_Kysymys_Vastaustyyppi_Kylla_Ei(String kysymys) {
		
		set_Kysymys(kysymys);
		driver.findElement(By.xpath("//span[@label='vastausKyllaEi']")).click();
		lisaa_Kysymys();
		
	}
	public void lisaa_Kysymys_Vastaustyyppi_Vapaavalintaiset_vaihtoehdot(String kysymys) {
		
		set_Kysymys(kysymys);
		driver.findElement(By.xpath("//span[@label='vastausVapaatVaihtoehdot']")).click();
		lisaa_Kysymys();
		
	}
	public void lisaa_Kysymys_Vastaustyyppi_Kalenteri(String kysymys) {
	   //TODO	
	}
	
	public void lisaa_Kysymys_Vastaustyyppi_Numero(String kysymys) {
		
		set_Kysymys(kysymys);
		driver.findElement(By.xpath("//span[@label='vastausNumeroVaihtoehdot']")).click();
		lisaa_Kysymys();
		
	}

	public void set_Pohjan_nakyvyys_Nakyy_vain_minulle() {
		driver.findElement(By.xpath("//span[@label='PohjaNakyvyys_Mina']")).click();
	}
	public void set_Pohjan_nakyvyys_Nakyy_kaikille() {
		driver.findElement(By.xpath("//span[@label='PohjaNakyvyys_Kaikki']")).click();
	}
	
	
	public void tallennaKyselypohja() {
		driver.findElement(By.xpath("//span[text()='Tallenna kyselypohja']")).click();
	}

	
	public void browserBack() {
		driver.navigate().back();
	}

}
