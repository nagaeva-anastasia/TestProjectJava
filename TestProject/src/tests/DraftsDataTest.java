package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import configurations.setUp.TestConfigurationReader;
import screens.AuthPage;
import screens.ComposePage;
import screens.MailListPage;
import services.dataProvider.TestDataProvider;
import services.helpers.LetterEnum;
import services.helpers.OpenPageEnum;

/**
 * Class works with drafts
 * 
 * @author Vyacheslav Milashov
 */

public class DraftsDataTest extends BaseTest {

	/**
	 * Method checks drafts
	 * 
	 * @author Vyacheslav Milashov
	 */

	@Test
	public void checkDraftsDataTest() throws IOException, InterruptedException {
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
}
