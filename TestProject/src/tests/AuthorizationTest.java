package tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import screens.AuthPage;
import screens.MailListPage;

/**
 * Class is used to check User authorization
 * 
 * @author Vyacheslav Milashov
 */

public class AuthorizationTest extends BaseTest {

	/**
	 * Method checks authorization
	 * 
	 * @author Vyacheslav Milashov
	 */

	@Test
	public void checkAuthorizationTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		MailListPage inboxPage = authPage.login();
		Assert.assertTrue(inboxPage.isAuthSucceed());
	}
}
