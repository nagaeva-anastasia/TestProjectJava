package screens;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import businessObjects.User;

public class AuthPage extends BasePage {

	@FindBy(xpath = "//form[@id='Auth']")
	private WebElement form;

	@FindBy(id = "mailbox__login")
	private WebElement login;

	@FindBy(id = "mailbox__login__domain")
	private WebElement domain;

	@FindBy(name = "Password")
	private WebElement password;

	public AuthPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(GetDriver(), this);
	}

	public MailListPage Login() throws IOException, InterruptedException {
		User user = new User();

		Thread.sleep(1500);
		// Wait().until((Predicate<WebDriver>) d -> domain.isEnabled());

		new Select(domain).selectByValue(user.Domain);
		EditElement(login, user.Login);
		EditElement(password, user.Password);

		form.submit();

		return new MailListPage(GetDriver());
	}

	public boolean IsAlreadyLoggedIn() {
		return !IsElementPresent(login);
	}

	public boolean IsLoginFormPresent() {
		return IsElementPresent(login) & IsElementPresent(password);
	}
}
