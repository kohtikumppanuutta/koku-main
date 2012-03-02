package fi.koku.ta.app.page;

import fi.koku.ta.helper.BasePage;
import fi.koku.ta.helper.BaseTest;

import org.openqa.selenium.*;


public class Huoltaja_Saapuneet extends BasePage {

	public Huoltaja_Saapuneet(BaseTest test) {
		
		super(test);
		
		waitTofindElementWithXPath("//ul/li[@id='msg_inbox']/a").click();
		
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
