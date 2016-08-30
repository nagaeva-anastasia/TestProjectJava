package screens;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import businessObjects.User;

/**
 * Class works with Authorization page
 * 
 * @author Vyacheslav Milashov
 */

public class AuthPage extends BasePage {

	@FindBy(xpath = "//*[@id='Auth']")
	private WebElement form;

	@FindBy(xpath = "//*[@id='mailbox__login']")
	private WebElement login;

	@FindBy(xpath = "//*[@id='mailbox__login__domain']")
	private WebElement domain;

	@FindBy(xpath = "//*[@name='Password']")
	private WebElement password;

	public AuthPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(getDriver(), this);
	}

	/**
	 * Method makes login
	 * 
	 * @author Vyacheslav Milashov
	 */

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

	/**
	 * Method checks if user already logged in
	 * 
	 * @author Vyacheslav Milashov
	 */

	public boolean isAlreadyLoggedIn() {
		return !isElementPresent(login);
	}

	/**
	 * Method checks if there is login form on the page
	 * 
	 * @author Vyacheslav Milashov
	 */

	public boolean isLoginFormPresent() {
		return isElementPresent(login) & isElementPresent(password);
	}
}
