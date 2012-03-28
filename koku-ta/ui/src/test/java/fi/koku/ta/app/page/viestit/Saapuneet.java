package fi.koku.ta.app.page.viestit;

import fi.koku.ta.helper.BasePage;
import fi.koku.ta.helper.BaseTest;

import org.openqa.selenium.*;


public class Saapuneet extends BasePage {

	public Saapuneet(BaseTest test) {
		
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
