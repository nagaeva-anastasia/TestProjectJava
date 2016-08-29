package screens;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Page {
	protected static WebDriver driver;
	protected static WebDriverWait awaiter;

	public WebDriver getDriver() {
		return driver;
	}

	public static WebDriverWait waitFor() {
		return awaiter;
	}
}
