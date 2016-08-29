package configurations;

import org.openqa.selenium.Platform;

/**
 * Class stores remote driver configuration
 * 
 * @author Vyacheslav Milashov
 */

public class RemoteDriverConfiguration extends AppConfiguration {
	public RemoteDriverConfiguration(String browser, Platform platform, String browserVersion, String seleniumHubUrl, int seleniumHubPort) {
		Browser = browser;
		PlatformType = platform;
		BrowserVersion = browserVersion;
		SeleniumHubUrl = seleniumHubUrl;
		SeleniumHubPort = seleniumHubPort;
	}
}
