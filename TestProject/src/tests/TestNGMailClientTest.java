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
	public static void Start() throws Exception {
		_driver = TestDriverFactory.CreateDriver();
		_driver.navigate().to("http://mail.ru");

		AuthPage authPage = new AuthPage(_driver);

		if (authPage.IsAlreadyLoggedIn()) {
			TestService.TryToLogout(authPage);
		}

		TestService.ClearMailbox(authPage);
		_driver.quit();
	}

	@BeforeMethod
	public void StartBrowser() throws Exception {
		_driver = TestDriverFactory.CreateDriver();
		_driver.navigate().to("http://mail.ru");
	}

	@AfterMethod
	public void CloseBrowser() throws Exception {
		AuthPage authPage = new AuthPage(_driver);

		if (authPage.IsAlreadyLoggedIn()) {
			TestService.TryToLogout(authPage);
		}

		TestService.ClearMailbox(authPage);
		_driver.quit();
	}

	@Test(priority = 1)
	public void TestNGAuthorizationTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		MailListPage inboxPage = authPage.Login();
		Assert.assertTrue(inboxPage.IsAuthSucceed());
	}

	@Test(priority = 2)
	public void TestNGDraftsSavingTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		TestDataProvider provider = new TestDataProvider();
		MailListPage inboxPage = authPage.Login();
		ComposePage writeNew = inboxPage.OpenComposePage();

		for (Map<LetterEnum, String> draft : provider.GetDraftsData()) {
			writeNew.SaveDraft(draft);
			Assert.assertTrue(writeNew.IsDraftSaved());
			writeNew.GetDriver().navigate().refresh();
		}
	}

	@Test(priority = 3)
	public void TestNGDraftsDataTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		TestDataProvider provider = new TestDataProvider();
		MailListPage inboxPage = authPage.Login();
		ComposePage writeNew = inboxPage.OpenComposePage();
		List<Map<LetterEnum, String>> lettersData = provider.GetDraftsData();

		for (Map<LetterEnum, String> draft : lettersData) {
			writeNew.SaveDraft(draft);
			Assert.assertTrue(writeNew.IsDraftSaved());
			writeNew.GetDriver().navigate().refresh();
		}

		MailListPage draftsPage = writeNew.Open(OpenPageEnum.Drafts);
		Map<LetterEnum, String> providerData = lettersData.get(lettersData.size() - 1);

		Assert.assertTrue(draftsPage.IsEmailAddressInProviderData(providerData.get(LetterEnum.Email)));
		Assert.assertTrue(draftsPage.IsEmailSubjectInProviderData(providerData.get(LetterEnum.Subject)));

		if (!TestConfigurationReader.Browser.equals("IE")) {
			Assert.assertTrue(draftsPage.IsBodyInProviderData());
		}
	}

	@Test(priority = 4)
	public void TestNGSendTest() throws IOException, InterruptedException {
		AuthPage authPage = new AuthPage(_driver);
		TestDataProvider provider = new TestDataProvider();
		MailListPage inboxPage = authPage.Login();
		ComposePage writeNewPage = inboxPage.OpenComposePage();
		Map<LetterEnum, String> providerData = provider.GetLetterToSendData();
		MailListPage mailSentAlertPage = writeNewPage.SendMail(providerData);
		MailListPage sentPage = mailSentAlertPage.OpenSentAfterAlert();

		Assert.assertTrue(sentPage.IsEmailAddressInProviderData(providerData.get(LetterEnum.Email)));
		Assert.assertTrue(sentPage.IsEmailSubjectInProviderData(providerData.get(LetterEnum.Subject)));
	}

	@Test(priority = 5)
	public void TestNGExitTest() throws IOException, InterruptedException {
		MailListPage inboxPage = new AuthPage(_driver).Login();
		AuthPage authPage = inboxPage.Logout();

		Assert.assertTrue(authPage.IsLoginFormPresent());
	}
}
