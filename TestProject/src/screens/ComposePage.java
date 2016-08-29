package screens;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

import businessObjects.Letter;
import configurations.setUp.TestConfigurationReader;
import services.dataProvider.TestDataProvider;
import services.helpers.LetterEnum;

public class ComposePage extends BasePage {
	@FindBy(css = "textarea.js-input.compose__labels__input")
	private WebElement emailField;

	@FindBy(xpath = "//*[contains(@id, 'compose_subj')]")
	private WebElement subjectField;

	@FindBy(id = "tinymce")
	private WebElement mailBody;

	@FindBy(xpath = "//*[contains(@id, 'composeEditor_ifr')]")
	private WebElement frameElement;

	@FindBy(xpath = "//span[contains(text(), 'Сохранить')]")
	private WebElement saveButton;

	@FindBy(css = "div[data-name='send']")
	private List<WebElement> sendButtons;

	@FindBy(xpath = "//div[@class='b-toolbar__message']")
	private WebElement checkElement;

	private final String COMPOSE_EMPTY = "div.is-compose-empty_in";
	private final String SUBMIT_BUTTON = "button[type='submit']";

	public ComposePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(getDriver(), this);
	}

	public void saveDraft(Map<LetterEnum, String> letterData) {
		editMailFields(letterData);
		saveButton.click();
	}

	public MailListPage sendMail(Map<LetterEnum, String> letterData) {
		editMailFields(letterData);

		Actions builder = new Actions(getDriver());
		WebElement sendButton = sendButtons.stream().filter(s -> s.isDisplayed()).findFirst().get();
		builder.click(sendButton).perform();

		if (TestConfigurationReader.Browser.equals("IE")) {
			WebElement el = getDriver().findElement(By.cssSelector(COMPOSE_EMPTY));
			waitFor().until((Predicate<WebDriver>) d -> el.isDisplayed());
			el.findElement(By.cssSelector(SUBMIT_BUTTON)).click();
		}

		return new MailListPage(getDriver());
	}

	public boolean isDraftSaved() {
		waitFor().until((Predicate<WebDriver>) d -> checkElement.getText().contains("Сохранено"));
		return true;
	}

	public boolean isTextPresentInBody() throws IOException {
		TestDataProvider provider = new TestDataProvider();

		List<Map<LetterEnum, String>> list = provider.getDraftsData();
		Map<LetterEnum, String> lastProviderData = list.get(list.size() - 1);

		switchTo(frameElement);
		waitFor().until((Predicate<WebDriver>) d -> mailBody.getText().contains(lastProviderData.get(LetterEnum.Body)));
		getDriver().switchTo().defaultContent();
		return true;
	}

	private void editMailFields(Map<LetterEnum, String> letterData) {
		Letter letter = new Letter(letterData);

		editElement(emailField, letter.EmailAddress);
		editElement(subjectField, letter.Subject);
		if (!TestConfigurationReader.Browser.equals("IE")) {
			bodyElementEdit(letter.Body);
		}
	}

	private void bodyElementEdit(String text) {
		switchTo(frameElement);
		WebElement mailBody = waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("tinymce")));
		editElement(mailBody, text);
		getDriver().switchTo().defaultContent();
	}
}
