package factoryMethods;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import configurations.Configuration;

public abstract class WebDriverFactory {
	protected WebDriver driver;
	protected DesiredCapabilities capabilities;

	public abstract WebDriver createDriver(Configuration configuration) throws Exception;
}
