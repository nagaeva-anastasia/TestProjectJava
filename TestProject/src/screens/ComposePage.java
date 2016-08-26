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
		PageFactory.initElements(GetDriver(), this);
	}

	public void SaveDraft(Map<LetterEnum, String> letterData) {
		EditMailFields(letterData);
		saveButton.click();
	}

	public MailListPage SendMail(Map<LetterEnum, String> letterData) {
		EditMailFields(letterData);

		Actions builder = new Actions(GetDriver());
		WebElement sendButton = sendButtons.stream().filter(s -> s.isDisplayed()).findFirst().get();
		builder.click(sendButton).perform();

		if (TestConfigurationReader.Browser.equals("IE")) {
			WebElement el = GetDriver().findElement(By.cssSelector(COMPOSE_EMPTY));
			Wait().until((Predicate<WebDriver>) d -> el.isDisplayed());
			el.findElement(By.cssSelector(SUBMIT_BUTTON)).click();
		}

		return new MailListPage(GetDriver());
	}

	public boolean IsDraftSaved() {
		Wait().until((Predicate<WebDriver>) d -> checkElement.getText().contains("Сохранено"));
		return true;
	}

	public boolean IsTextPresentInBody() throws IOException {
		TestDataProvider provider = new TestDataProvider();

		List<Map<LetterEnum, String>> list = provider.GetDraftsData();
		Map<LetterEnum, String> lastProviderData = list.get(list.size() - 1);

		SwitchTo(frameElement);
		Wait().until((Predicate<WebDriver>) d -> mailBody.getText().contains(lastProviderData.get(LetterEnum.Body)));
		GetDriver().switchTo().defaultContent();
		return true;
	}

	private void EditMailFields(Map<LetterEnum, String> letterData) {
		Letter letter = new Letter(letterData);

		EditElement(emailField, letter.EmailAddress);
		EditElement(subjectField, letter.Subject);
		if (!TestConfigurationReader.Browser.equals("IE")) {
			BodyElementEdit(letter.Body);
		}
	}

	private void BodyElementEdit(String text) {
		SwitchTo(frameElement);
		WebElement mailBody = Wait().until(ExpectedConditions.visibilityOfElementLocated(By.id("tinymce")));
		EditElement(mailBody, text);
		GetDriver().switchTo().defaultContent();
	}
}
