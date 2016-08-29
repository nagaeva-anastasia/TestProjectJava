package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import configurations.setUp.TestConfigurationReader;
import configurations.setUp.TestDriverFactory;
import screens.AuthPage;
import screens.ComposePage;
import screens.MailListPage;
import services.dataProvider.TestDataProvider;
import services.helpers.LetterEnum;
import services.helpers.OpenPageEnum;
import services.testService.TestService;

public class TestNGMailClientTest {
	private static WebDriver _driver;

	@BeforeTest
	public static void start() throws Exception {
		_driver = TestDriverFactory.CreateDriver();
		_driver.navigate().to("http://mail.ru");

		AuthPage authPage = new AuthPage(_driver);

		if (authPage.isAlreadyLoggedIn()) {
			TestService.tryToLogout(authPage);
		}

		TestService.clearMailbox(authPage);
		_driver.quit();
	}

	@BeforeMethod
	public void startBrowser() throws Exception {
		_driver = TestDriverFactory.CreateDriver();
		_driver.navigate().to("http://mail.ru");
	}

	@AfterMethod
	public void closeBrowser() throws Exception {
		AuthPage authPage = new AuthPage(_driver);

		if (authPage.isAlreadyLoggedIn()) {
			TestService.tryToLogout(authPage);
		}

		TestService.clearMailbox(authPage);
		_driver.quit();
	}

	@Test(priority = 1)
	public void testNGAuthorizationTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		MailListPage inboxPage = authPage.login();
		Assert.assertTrue(inboxPage.isAuthSucceed());
	}

	@Test(priority = 2)
	public void testNGDraftsSavingTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		TestDataProvider provider = new TestDataProvider();
		MailListPage inboxPage = authPage.login();
		ComposePage writeNew = inboxPage.openComposePage();

		for (Map<LetterEnum, String> draft : provider.getDraftsData()) {
			writeNew.saveDraft(draft);
			Assert.assertTrue(writeNew.isDraftSaved());
			writeNew.getDriver().navigate().refresh();
		}
	}

	@Test(priority = 3)
	public void testNGDraftsDataTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		TestDataProvider provider = new TestDataProvider();
		MailListPage inboxPage = authPage.login();
		ComposePage writeNew = inboxPage.openComposePage();
		List<Map<LetterEnum, String>> lettersData = provider.getDraftsData();

		for (Map<LetterEnum, String> draft : lettersData) {
			writeNew.saveDraft(draft);
			Assert.assertTrue(writeNew.isDraftSaved());
			writeNew.getDriver().navigate().refresh();
		}

		MailListPage draftsPage = writeNew.open(OpenPageEnum.Drafts);
		Map<LetterEnum, String> providerData = lettersData.get(lettersData.size() - 1);

		Assert.assertTrue(draftsPage.isEmailAddressInProviderData(providerData.get(LetterEnum.Email)));
		Assert.assertTrue(draftsPage.isEmailSubjectInProviderData(providerData.get(LetterEnum.Subject)));

		if (!TestConfigurationReader.Browser.equals("IE")) {
			Assert.assertTrue(draftsPage.isBodyInProviderData());
		}
	}

	@Test(priority = 4)
	public void testNGSendTest() throws IOException, InterruptedException {
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

	@Test(priority = 5)
	public void testNGExitTest() throws IOException, InterruptedException {
		MailListPage inboxPage = new AuthPage(_driver).login();
		AuthPage authPage = inboxPage.logout();

		Assert.assertTrue(authPage.IsLoginFormPresent());
	}
}
