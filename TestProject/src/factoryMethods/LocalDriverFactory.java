package factoryMethods;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import configurations.Configuration;

public class LocalDriverFactory extends WebDriverFactory {
	@Override
	public WebDriver CreateDriver(Configuration configuration) {
		if (driver != null) {
			return driver;
		}
		String projectDir = System.getProperty("user.dir");
		String driverPath = String.format("%s/src/services/drivers/", projectDir);

		switch (configuration.Browser) {
		case "chrome":
			File file = new File(String.format("%s/chromedriver.exe", driverPath));
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver = new ChromeDriver();
			break;

		case "IE":
			file = new File(String.format("%s/IEDriverServer.exe", driverPath));
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			driver = new InternetExplorerDriver();
			break;

		case "firefox":
			file = new File(String.format("%s/geckodriver.exe", driverPath));
			System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
			driver = new FirefoxDriver();
			break;

		default:
			file = new File(String.format("%s/chromedriver.exe", driverPath));
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver = new ChromeDriver();
			break;
		}

		return driver;
	}
}
