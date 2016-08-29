package screens;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

import services.helpers.OpenPageEnum;

/**
 * Class describes base structure of Mail.ru pages
 * 
 * @author Vyacheslav Milashov
 */

public class BasePage extends AbstractPage {
	private final String SENT_FOLDER_LINK = "a[href='/messages/sent/']";
	private final String DRAFTS_FOLDER_LINK = "a[href='/messages/drafts/']";
	private final String INBOX_FOLDER_LINK = "a[href='/messages/inbox/']";
	private final String LOGOUT_ID = "#PH_logoutLink";
	private final String GOTO_SCRIPT = "document.querySelector(\"%s\").click()";
	private final int TIME_TO_WAIT = 2000;

	protected BasePage(WebDriver driver) {

		AbstractPage.driver = driver;
		AbstractPage.awaiter = new WebDriverWait(AbstractPage.driver, 2);
		AbstractPage.driver.manage().timeouts().implicitlyWait(TIME_TO_WAIT, TimeUnit.MILLISECONDS);
		PageFactory.initElements(getDriver(), this);
	}

	/**
	 * Method checks element presence
	 * 
	 * @author Vyacheslav Milashov
	 */

	public boolean isElementPresent(By locator) {
		try {
			Predicate<WebDriver> p1 = d -> d.findElements(locator).stream().anyMatch(el -> el.isDisplayed());
			waitFor().until(p1);
			return true;
		}
		catch (Throwable t) {
			return false;
		}
	}

	/**
	 * Method checks element presence
	 * 
	 * @author Vyacheslav Milashov
	 */

	public boolean isElementPresent(WebElement element) {
		try {
			waitFor().until((Predicate<WebDriver>) d -> element.isDisplayed());
			return true;
		}
		catch (Throwable t) {
			return false;
		}
	}

	/**
	 * Method checks element presence
	 * 
	 * @author Vyacheslav Milashov
	 */

	public void switchTo(WebElement frame) {
		if (frame == null) {
			getDriver().switchTo().defaultContent();
		} else {
			getDriver().switchTo().frame(frame);
		}
	}

	/**
	 * Method edits element's content
	 * 
	 * @author Vyacheslav Milashov
	 */

	public void editElement(WebElement element, String text) {
		if (element.getTagName() == "body") {
			element.clear();
			Actions builder = new Actions(getDriver());
			builder.sendKeys(element, text).perform();
		}
		element.clear();
		element.sendKeys(text);
	}

	/**
	 * Method opens pages
	 * 
	 * @author Vyacheslav Milashov
	 */

	public MailListPage open(OpenPageEnum page) {
		switch (page) {
			case Inbox:
				executeScript(GOTO_SCRIPT, INBOX_FOLDER_LINK);
				break;

			case Drafts:
				executeScript(GOTO_SCRIPT, DRAFTS_FOLDER_LINK);
				break;

			case Sent:
				executeScript(GOTO_SCRIPT, SENT_FOLDER_LINK);
				break;
			default:
				break;
		}
		getDriver().navigate().refresh();
		return new MailListPage(getDriver());
	}

	/**
	 * Method logs out
	 * 
	 * @author Vyacheslav Milashov
	 */

	public AuthPage logout() throws InterruptedException {
		Thread.sleep(1000);
		executeScript(GOTO_SCRIPT, LOGOUT_ID);
		return new AuthPage(getDriver());
	}

	/**
	 * Method executes scripts
	 * 
	 * @author Vyacheslav Milashov
	 */

	public void executeScript(String script, String selector) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		String executeString = String.format(script, selector);
		js.executeScript(executeString);
	}
}
