package screens;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class stores driver for Web page
 * 
 * @author Vyacheslav Milashov
 */

public class AbstractPage {
	protected static WebDriver driver;
	protected static WebDriverWait awaiter;

	/**
	 * Method returns driver for Web page
	 * 
	 * @author Vyacheslav Milashov
	 */

	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Method returns WebDriverWait for current project configuration
	 * 
	 * @author Vyacheslav Milashov
	 */

	public static WebDriverWait waitFor() {
		return awaiter;
	}
}
