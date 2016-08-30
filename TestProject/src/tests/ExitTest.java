package tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import screens.AuthPage;
import screens.MailListPage;

/**
 * Class checks if exit from Mailbox is correct
 * 
 * @author Vyacheslav Milashov
 */

public class ExitTest extends BaseTest {

	/**
	 * Method checks user exit
	 * 
	 * @author Vyacheslav Milashov
	 */

	@Test
	public void checkExitTest() throws IOException, InterruptedException {
		MailListPage inboxPage = new AuthPage(_driver).login();
		AuthPage authPage = inboxPage.logout();

		Assert.assertTrue(authPage.isLoginFormPresent());
	}
}
