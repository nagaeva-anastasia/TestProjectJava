package configurations.setUp;

import org.openqa.selenium.WebDriver;

import configurations.LocalDriverConfiguration;
import configurations.RemoteDriverConfiguration;
import factoryMethods.LocalDriverFactory;
import factoryMethods.RemoteDriverFactory;
import factoryMethods.WebDriverFactory;

public class TestDriverFactory {
    public static WebDriver ñreateDriver() throws Exception
    {
        WebDriverFactory factory;
        if (TestConfigurationReader.ApplicationUrl == null) {
        	new TestConfigurationReader();
        }

        if (TestConfigurationReader.Remote)
        {
            factory = new RemoteDriverFactory();
                
            return factory.createDriver(
                    new RemoteDriverConfiguration(
                            TestConfigurationReader.Browser,
                            TestConfigurationReader.PlatformType,
                            TestConfigurationReader.BrowserVersion,
                            TestConfigurationReader.SeleniumHubUrl,
                            TestConfigurationReader.SeleniumHubPort));
        }

        factory = new LocalDriverFactory();

        return factory.createDriver(new LocalDriverConfiguration(TestConfigurationReader.Browser));
    }
}
