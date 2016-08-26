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
		PageFactory.initElements(GetDriver(), this);
	}

	public ComposePage OpenComposePage() throws InterruptedException {
		ExecuteScript(CLICK_SCRIPT_NO_JQUERY, WRITE_NEW);
		Wait().until((Predicate<WebDriver>) d -> d.getTitle().contains("Новое письмо"));
		return new ComposePage(GetDriver());
	}

	public MailListPage OpenSentAfterAlert() {
		IsElementPresent(sentAlert);
		return Open(OpenPageEnum.Sent);
	}

	public boolean IsAuthSucceed() {
		return writeButton.getText().contains("Написать");
	}

	public boolean IsEmailAddressInProviderData(String providerData) {
		Wait().until((Predicate<WebDriver>) d -> !d.getTitle().contains("Входящие"));
		Wait().until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(SENT_ALERT_SELECTOR)));

		if (GetDriver().getTitle().contains("Отправленные")) {
			GetDriver().navigate().refresh();
		}

		Wait().until((Predicate<WebDriver>) d -> d.findElement(By.cssSelector(MAIL_SELECTOR)).isDisplayed());

		String text = GetLastMailText(ADDRESS_SELECTOR);
		return text.equals(providerData);
	}

	public boolean IsEmailSubjectInProviderData(String providerData) {
		String text = GetLastMailText(SUBJECTS_SELECTOR);
		return text.equals(providerData);
	}

	public boolean IsBodyInProviderData() throws IOException {
		ComposePage composePage = OpenLastDraft();
		return composePage.IsTextPresentInBody();
	}

	public ComposePage OpenLastDraft() {
		Wait().until((Predicate<WebDriver>) d -> d.getTitle().contains("Черновики"));
		ExecuteScript(CLICK_SCRIPT, MAIL_SELECTOR);
		Wait().until((Predicate<WebDriver>) d -> d.getTitle().contains("Новое письмо"));
		return new ComposePage(GetDriver());
	}

	public void ClearMailList() throws InterruptedException {
		if (IsElementPresent(emptyMessage)) {
			return;
		}

		Actions builder = new Actions(GetDriver());

		Wait().until((Predicate<WebDriver>) d -> dropdowns.stream().filter(el -> el.isDisplayed()).count() > 0);

		while (TestConfigurationReader.Browser.equals("IE")) {
			GetDriver().navigate().refresh();
			//Thread.sleep(500);
			WebElement dropdown = dropdowns.stream().filter(el -> el.isDisplayed()).findAny().get();
			builder.sendKeys(dropdown, Keys.ENTER).perform();
			//ExecuteScript(CLICK_SCRIPT_NO_JQUERY, LETTERS_SELECTOR);
			if (IsElementPresent(selectOnPage)){
				builder.click(selectOnPage).perform();
			}
			else{
				continue;
			}
		}
		if (!TestConfigurationReader.Browser.equals("IE")) {
			WebElement dropdown = dropdowns.stream().filter(el -> el.isDisplayed()).findAny().get();
			builder.click(dropdown).perform();
		}

		IsElementPresent(selectOnPage);
		builder.click(selectOnPage).perform();

		if (IsElementPresent(selectFromAllPages)) {
			builder.click(selectFromAllPages).perform();
		}

		WebElement del = deleteElements.stream().filter(el -> el.isDisplayed()).findAny().get();
		builder.sendKeys(del, Keys.DELETE).perform();
	}

	private String GetLastMailText(String selector) {
		JavascriptExecutor js = (JavascriptExecutor) GetDriver();
		String script = String.format(LAST_MAIL_TEXT_SCRIPT, selector);
		String text = (String) js.executeScript(script);
		return text;
	}
}
