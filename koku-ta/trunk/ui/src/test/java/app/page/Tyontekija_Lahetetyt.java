package app.page;

import helper.BaseTest;
import helper.BasePage;

import org.openqa.selenium.*;

import java.util.List;


public class Tyontekija_Lahetetyt extends BasePage {

	public Tyontekija_Lahetetyt(BaseTest test) {
		
		super(test);
		
		driver.findElement(By.xpath("//ul/li[@id='msg_outbox']/a")).click();
		
	}
	
	public boolean subjectFound(String expectedSubject) {
		
		//List<WebElement> weList = driver.findElements(By.cssSelector("td.messageItem.msgSubject"));
		
		for(int j=0;j<10;j++) {
			List<WebElement> weList = driver.findElements(By.cssSelector("td.messageItem.msgSubject"));
			if(weList.size()>0) {
				break;
			} else {
				pauseSeconds(1);
			}
		}
		
		List<WebElement> weList = driver.findElements(By.cssSelector("td.messageItem.msgSubject"));
		for(int i=0;i<weList.size();i++) {
			if(weList.get(i).getText().equals(expectedSubject)) {
				return true;
			}
		}
		return false;
		
	}
	
	
}
