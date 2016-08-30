package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import configurations.setUp.TestDriverFactory;
import screens.AuthPage;
import services.testService.TestService;

/**
 * Class contains before and after methods for test running
 * 
 * @author Vyacheslav Milashov
 */

public class BaseTest {
	public static WebDriver _driver;

	/**
	 * Method runs once before all tests in suite
	 * 
	 * @author Vyacheslav Milashov
	 */

	@BeforeSuite
	public static void start() throws Exception {
		_driver = TestDriverFactory.createDriver();
		_driver.navigate().to("http://mail.ru");

		AuthPage authPage = new AuthPage(_driver);

		if (authPage.isAlreadyLoggedIn()) {
			TestService.tryToLogout(authPage);
		}

		TestService.clearMailbox(authPage);
		_driver.quit();
	}

	/**
	 * Method runs before each test method
	 * 
	 * @author Vyacheslav Milashov
	 */

	@BeforeMethod
	public void startBrowser() throws Exception {
		_driver = TestDriverFactory.createDriver();
		_driver.navigate().to("http://mail.ru");
	}

	/**
	 * Method runs after each test method
	 * 
	 * @author Vyacheslav Milashov
	 */

	@AfterMethod
	public void closeBrowser() throws Exception {
		AuthPage authPage = new AuthPage(_driver);

		if (authPage.isAlreadyLoggedIn()) {
			TestService.tryToLogout(authPage);
		}

		TestService.clearMailbox(authPage);
		_driver.quit();
	}
}
