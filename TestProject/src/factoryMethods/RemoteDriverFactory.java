package factoryMethods;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import configurations.Configuration;

public class RemoteDriverFactory extends WebDriverFactory {
	@Override
    public WebDriver CreateDriver(Configuration configuration) throws MalformedURLException
    {
		if (driver != null) {
			return driver;
		}
        String remoteServer = BuildRemoteServer(configuration.SeleniumHubUrl, configuration.SeleniumHubPort);

        switch (configuration.Browser)
        {
            case "chrome":
                capabilities = DesiredCapabilities.chrome();
                break;

            case "IE":
                capabilities = DesiredCapabilities.internetExplorer();
                break;

            case "firefox":
                capabilities = DesiredCapabilities.firefox();
                break;
        }

        SetCapabilities(configuration.PlatformType, configuration.BrowserVersion);

        driver = new RemoteWebDriver(new URL(remoteServer), capabilities);

        return driver;
    }

    private void SetCapabilities(Platform platform, String browserVersion)
    {
        capabilities.setCapability(CapabilityType.PLATFORM, platform);
        capabilities.setCapability(CapabilityType.VERSION, browserVersion);
    }

    private static String BuildRemoteServer(String remoteServer, int remoteServerPort)
    {
    	String s =  String.format("%1s:%2s/wd/hub", remoteServer, remoteServerPort);
        return s;
    }
}
