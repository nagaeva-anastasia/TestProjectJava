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
		PageFactory.initElements(getDriver(), this);
	}

	public MailListPage login() throws IOException, InterruptedException {
		User user = new User();

		Thread.sleep(1500);
		// Wait().until((Predicate<WebDriver>) d -> domain.isEnabled());

		new Select(domain).selectByValue(user.Domain);
		editElement(login, user.Login);
		editElement(password, user.Password);

		form.submit();

		return new MailListPage(getDriver());
	}

	public boolean isAlreadyLoggedIn() {
		return !isElementPresent(login);
	}

	public boolean IsLoginFormPresent() {
		return isElementPresent(login) & isElementPresent(password);
	}
}
