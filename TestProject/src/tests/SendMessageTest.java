package tests;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import screens.AuthPage;
import screens.ComposePage;
import screens.MailListPage;
import services.dataProvider.TestDataProvider;
import services.helpers.LetterEnum;

/**
 * Class checks messages sending
 * 
 * @author Vyacheslav Milashov
 */

public class SendMessageTest extends BaseTest {

	/**
	 * Methods checks if sending succeeded
	 * 
	 * @author Vyacheslav Milashov
	 */

	@Test
	public void checkSendingTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		TestDataProvider provider = new TestDataProvider();
		MailListPage inboxPage = authPage.login();
		ComposePage writeNewPage = inboxPage.openComposePage();
		Map<LetterEnum, String> providerData = provider.getLetterToSendData();
		MailListPage mailSentAlertPage = writeNewPage.sendMail(providerData);
		MailListPage sentPage = mailSentAlertPage.openSentAfterAlert();

		Assert.assertTrue(sentPage.isEmailAddressInProviderData(providerData.get(LetterEnum.Email)));
		Assert.assertTrue(sentPage.isEmailSubjectInProviderData(providerData.get(LetterEnum.Subject)));
	}
}
