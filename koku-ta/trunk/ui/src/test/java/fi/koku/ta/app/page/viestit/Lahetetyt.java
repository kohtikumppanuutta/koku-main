package fi.koku.ta.app.page.viestit;

import fi.koku.ta.helper.BasePage;
import fi.koku.ta.helper.BaseTest;

import org.openqa.selenium.*;

import java.util.List;


public class Lahetetyt extends BasePage {

	public Lahetetyt(BaseTest test) {
		
		super(test);
		
		driver.findElement(By.xpath("//ul/li[@id='msg_outbox']/a")).click();
		
	}
	
	public boolean subjectFound(String expectedSubject) {
		
		//List<WebElement> weList = driver.findElements(By.cssSelector("td.messageItem.msgSubject"));
		
		//System.out.format("DEBUG:expectedSubject=%s\n",expectedSubject);
		
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
