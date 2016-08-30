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
 * Class works with Drafts
 * 
 * @author Vyacheslav Milashov
 */

public class DraftsSavingTest extends BaseTest {

	/**
	 * Method checks if drafts saved correctly
	 * 
	 * @author Vyacheslav Milashov
	 */

	@Test
	public void checkDraftsSavingTest() throws IOException, InterruptedException {
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
}
