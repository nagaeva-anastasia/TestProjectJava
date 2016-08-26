package screens;

import java.util.concurrent.TimeUnit;
import com.google.common.base.Predicate;

import services.helpers.OpenPageEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage extends Page {
	private final String SENT_FOLDER_LINK = "a[href='/messages/sent/']";
	private final String DRAFTS_FOLDER_LINK = "a[href='/messages/drafts/']";
	private final String INBOX_FOLDER_LINK = "a[href='/messages/inbox/']";
	private final String LOGOUT_ID = "#PH_logoutLink";
	private final String GOTO_SCRIPT = "document.querySelector(\"%s\").click()";
	private final int TIME_TO_WAIT = 2000;

	protected BasePage(WebDriver driver) {

		Page.driver = driver;
		Page.awaiter = new WebDriverWait(Page.driver, 2);
		Page.driver.manage().timeouts().implicitlyWait(TIME_TO_WAIT, TimeUnit.MILLISECONDS);
		PageFactory.initElements(GetDriver(), this);
	}

	public boolean IsElementPresent(By locator) {
		try {
			Predicate<WebDriver> p1 = d -> d.findElements(locator).stream().anyMatch(el -> el.isDisplayed());
			Wait().until(p1);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public boolean IsElementPresent(WebElement element) {
		try {
			Wait().until((Predicate<WebDriver>) d -> element.isDisplayed());
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public void SwitchTo(WebElement frame) {
		if (frame == null) {
			GetDriver().switchTo().defaultContent();
		} else {
			GetDriver().switchTo().frame(frame);
		}
	}

	public void EditElement(WebElement element, String text) {
		if (element.getTagName() == "body") {
			element.clear();
			Actions builder = new Actions(GetDriver());
			builder.sendKeys(element, text).perform();
		}
		element.clear();
		element.sendKeys(text);
	}

	public MailListPage Open(OpenPageEnum page) {
		switch (page) {
		case Inbox:
			ExecuteScript(GOTO_SCRIPT, INBOX_FOLDER_LINK);
			break;

		case Drafts:
			ExecuteScript(GOTO_SCRIPT, DRAFTS_FOLDER_LINK);
			break;

		case Sent:
			ExecuteScript(GOTO_SCRIPT, SENT_FOLDER_LINK);
			break;
		default:
			break;
		}
		GetDriver().navigate().refresh();
		return new MailListPage(GetDriver());
	}

	public AuthPage Logout() throws InterruptedException {
		Thread.sleep(1000);
		ExecuteScript(GOTO_SCRIPT, LOGOUT_ID);
		return new AuthPage(GetDriver());
	}

	public void ExecuteScript(String script, String selector) {
		JavascriptExecutor js = (JavascriptExecutor) GetDriver();
		String executeString = String.format(script, selector);
		js.executeScript(executeString);
	}
}
