package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import configurations.setUp.TestConfigurationReader;
import screens.ComposePage;
import screens.MailListPage;
import services.dataProvider.TestDataProvider;
import services.helpers.LetterEnum;
import services.helpers.OpenPageEnum;

/**
 * Class works with drafts
 * 
 * describe test steps here
 * 
 * @author Vyacheslav Milashov
 */

public class DraftsDataTest extends BaseTest {

	/*
	 * Don't need java doc inside the test
	 */
	private ComposePage writeNew;

	/*
	 * @DataProvider public Object[][] dateAndPaycodesData() { use
	 * TestDataProvider here or define dataProvider in BaseTest }
	 */

	/*
	 * set common actions in preliminary steps
	 * 
	 * @BeforeClass public void preliminarySteps() { AuthPage authPage = new
	 * AuthPage(_driver); MailListPage inboxPage = authPage.login(); writeNew =
	 * inboxPage.openComposePage(); }
	 */

	@Test
	// @Test(dataProvider = "dateAndPaycodesData")
	public void checkIsDraftsSavedTest(/* parameters from data provider */) throws IOException, InterruptedException {
		TestDataProvider provider = new TestDataProvider();
		List<Map<LetterEnum, String>> lettersData = provider.getDraftsData();

		for (Map<LetterEnum, String> draft : lettersData) {
			writeNew.saveDraft(draft);
			Assert.assertTrue(writeNew.isDraftSaved());
			writeNew.getDriver().navigate().refresh();
		}

		MailListPage draftsPage = writeNew.open(OpenPageEnum.Drafts);
		Map<LetterEnum, String> providerData = lettersData.get(lettersData.size() - 1);

		/*
		 * Add messages for asserts Assert.assertEquals(actualShift,
		 * expectedShift, "Shift is not pasted.");
		 */
		Assert.assertTrue(draftsPage.isEmailAddressInProviderData(providerData.get(LetterEnum.Email)));
		Assert.assertTrue(draftsPage.isEmailSubjectInProviderData(providerData.get(LetterEnum.Subject)));

		if (!TestConfigurationReader.Browser.equals("IE")) {
			Assert.assertTrue(draftsPage.isBodyInProviderData());
		}
	}
}
