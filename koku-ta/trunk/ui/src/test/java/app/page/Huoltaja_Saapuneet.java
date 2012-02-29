package app.page;

import helper.BaseTest;
import helper.BasePage;

import org.openqa.selenium.*;


public class Huoltaja_Saapuneet extends BasePage {

	public Huoltaja_Saapuneet(BaseTest test) {
		
		super(test);
		
		driver.findElement(By.xpath("//ul/li[@id='msg_inbox']/a")).click();
		
	}
	
	public boolean subjectFound(String expectedSubject) {
		
		WebElement we;
		try {
			we = waitTofindElementWithCssSelector("td.messageItem.msgSubject");
		}
		catch (Exception e) {
			return false;
		}
		
		if(we.getText().equals(expectedSubject)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	
}
