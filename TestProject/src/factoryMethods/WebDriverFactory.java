package factoryMethods;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import configurations.AppConfiguration;

/**
 * Class stores driver object and capabilities
 * 
 * @author Vyacheslav Milashov
 */

public abstract class WebDriverFactory {
	protected WebDriver driver;
	protected DesiredCapabilities capabilities;

	public abstract WebDriver createDriver(AppConfiguration configuration) throws Exception;
}
