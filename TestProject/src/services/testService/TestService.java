package services.testService;

import java.io.IOException;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import configurations.setUp.TestConfigurationReader;
import configurations.setUp.TestDriverFactory;
import screens.AuthPage;
import screens.MailListPage;
import services.helpers.OpenPageEnum;

public class TestService {
	private static WebDriver _driver;
	private static int _failsCounter;

	public static void TryToLogout(AuthPage page) throws Exception {
		_driver = page.GetDriver();
		try {
			page.Logout();
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
			_driver = TestDriverFactory.CreateDriver();
			_driver.navigate().to(TestConfigurationReader.ApplicationUrl);

			TryToLogout(page);
		}
	}

	public static void ClearMailbox(AuthPage page) throws IOException, InterruptedException {
		MailListPage mail = page.Login();
		mail.ClearMailList();
		page.Open(OpenPageEnum.Sent).ClearMailList();
		page.Open(OpenPageEnum.Drafts).ClearMailList();
		page.Logout();
	}
}
