package screens;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

import configurations.setUp.TestConfigurationReader;
import services.helpers.OpenPageEnum;

public class MailListPage extends BasePage {
	@FindBy(css = "a[data-bem='b-toolbar__btn']")
	private WebElement writeButton;

	@FindBy(css = "div[title='Выделить']")
	private List<WebElement> dropdowns;

	@FindBy(css = "a[data-name='all']")
	private WebElement selectOnPage;

	@FindBy(css = "a.pseudo-link[data-id='selectAllLetters']")
	private WebElement selectFromAllPages;

	@FindBy(xpath = "//span[contains(text(), 'Удалить')]")
	private List<WebElement> deleteElements;

	@FindBy(css = "span.b-datalist__empty__text")
	private WebElement emptyMessage;

	@FindBy(css = "div.message-sent__getTitle()")
	private WebElement sentAlert;

	private final String SENT_ALERT_SELECTOR = "div.message-sent__getTitle()";
	private final String MAIL_SELECTOR = "div.b-datalist__item__panel";
	private final String ADDRESS_SELECTOR = "div.b-datalist__item__addr";
	private final String SUBJECTS_SELECTOR = "div.b-datalist__item__subj";
	private final String WRITE_NEW = "a[data-bem='b-toolbar__btn']";
	private final String LAST_MAIL_TEXT_SCRIPT = "return $(\"%s\").filter(':visible').first().contents().first().text()";
	private final String CLICK_SCRIPT = "$(\"%s\").filter(':visible').first().click()";
	private final String CLICK_SCRIPT_NO_JQUERY = "document.querySelector(\"%s\").click()";

	public MailListPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(getDriver(), this);
	}

	public ComposePage openComposePage() throws InterruptedException {
		executeScript(CLICK_SCRIPT_NO_JQUERY, WRITE_NEW);
		waitFor().until((Predicate<WebDriver>) d -> d.getTitle().contains("Новое письмо"));
		return new ComposePage(getDriver());
	}

	public MailListPage openSentAfterAlert() {
		isElementPresent(sentAlert);
		return open(OpenPageEnum.Sent);
	}

	public boolean isAuthSucceed() {
		return writeButton.getText().contains("Написать");
	}

	public boolean isEmailAddressInProviderData(String providerData) {
		waitFor().until((Predicate<WebDriver>) d -> !d.getTitle().contains("Входящие"));
		waitFor().until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(SENT_ALERT_SELECTOR)));

		if (getDriver().getTitle().contains("Отправленные")) {
			getDriver().navigate().refresh();
		}

		waitFor().until((Predicate<WebDriver>) d -> d.findElement(By.cssSelector(MAIL_SELECTOR)).isDisplayed());

		String text = getLastMailText(ADDRESS_SELECTOR);
		return text.equals(providerData);
	}

	public boolean isEmailSubjectInProviderData(String providerData) {
		String text = getLastMailText(SUBJECTS_SELECTOR);
		return text.equals(providerData);
	}

	public boolean isBodyInProviderData() throws IOException {
		ComposePage composePage = openLastDraft();
		return composePage.isTextPresentInBody();
	}

	public ComposePage openLastDraft() {
		waitFor().until((Predicate<WebDriver>) d -> d.getTitle().contains("Черновики"));
		executeScript(CLICK_SCRIPT, MAIL_SELECTOR);
		waitFor().until((Predicate<WebDriver>) d -> d.getTitle().contains("Новое письмо"));
		return new ComposePage(getDriver());
	}

	public void clearMailList() throws InterruptedException {
		if (isElementPresent(emptyMessage)) {
			return;
		}

		Actions builder = new Actions(getDriver());

		waitFor().until((Predicate<WebDriver>) d -> dropdowns.stream().filter(el -> el.isDisplayed()).count() > 0);

		while (TestConfigurationReader.Browser.equals("IE")) {
			getDriver().navigate().refresh();
			// Thread.sleep(500);
			WebElement dropdown = dropdowns.stream().filter(el -> el.isDisplayed()).findAny().get();
			builder.sendKeys(dropdown, Keys.ENTER).perform();
			// ExecuteScript(CLICK_SCRIPT_NO_JQUERY, LETTERS_SELECTOR);
			if (isElementPresent(selectOnPage)) {
				builder.click(selectOnPage).perform();
			} else {
				continue;
			}
		}
		if (!TestConfigurationReader.Browser.equals("IE")) {
			WebElement dropdown = dropdowns.stream().filter(el -> el.isDisplayed()).findAny().get();
			builder.click(dropdown).perform();
		}

		isElementPresent(selectOnPage);
		builder.click(selectOnPage).perform();

		if (isElementPresent(selectFromAllPages)) {
			builder.click(selectFromAllPages).perform();
		}

		WebElement del = deleteElements.stream().filter(el -> el.isDisplayed()).findAny().get();
		builder.sendKeys(del, Keys.DELETE).perform();
	}

	private String getLastMailText(String selector) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		String script = String.format(LAST_MAIL_TEXT_SCRIPT, selector);
		String text = (String) js.executeScript(script);
		return text;
	}
}
