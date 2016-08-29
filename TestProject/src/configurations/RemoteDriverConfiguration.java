package configurations;

import org.openqa.selenium.Platform;

public class RemoteDriverConfiguration extends Configuration {
	public RemoteDriverConfiguration(String browser, Platform platform, String browserVersion, String seleniumHubUrl,
			int seleniumHubPort) {
		Browser = browser;
		PlatformType = platform;
		BrowserVersion = browserVersion;
		SeleniumHubUrl = seleniumHubUrl;
		SeleniumHubPort = seleniumHubPort;
	}
}
