package services.testService;

import java.io.IOException;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import configurations.setUp.TestConfigurationReader;
import configurations.setUp.TestDriverFactory;
import screens.AuthPage;
import screens.MailListPage;
import services.helpers.OpenPageEnum;

/**
 * Class used for additional test's functionality
 * 
 * @author Vyacheslav Milashov
 */

public class TestService {
	private static WebDriver _driver;
	private static int _failsCounter;

	/**
	 * Method tries logout
	 * 
	 * @author Vyacheslav Milashov
	 */

	public static void tryToLogout(AuthPage page) throws Exception {
		_driver = page.getDriver();
		try {
			page.logout();
			Assert.assertTrue(page.IsLoginFormPresent());
			_failsCounter = 0;
		}

		catch (Throwable t) {
			_failsCounter++;
			System.out.println(t.toString());
			if (_failsCounter > 5) {
				_driver.quit();
				throw new Exception("More than 5 fails in a row");
			}
			// Restart browser to try again
			_driver.quit();
			_driver = TestDriverFactory.ñreateDriver();
			_driver.navigate().to(TestConfigurationReader.ApplicationUrl);

			tryToLogout(page);
		}
	}

	/**
	 * Method clears mailbox and logs out
	 * 
	 * @author Vyacheslav Milashov
	 */

	public static void clearMailbox(AuthPage page) throws IOException, InterruptedException {
		MailListPage mail = page.login();
		mail.clearMailList();
		page.open(OpenPageEnum.Sent).clearMailList();
		page.open(OpenPageEnum.Drafts).clearMailList();
		page.logout();
	}
}
