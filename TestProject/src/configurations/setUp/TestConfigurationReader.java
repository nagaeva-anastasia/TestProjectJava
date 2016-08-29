package configurations.setUp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.Platform;

public class TestConfigurationReader {
	public static boolean Remote;
	public static String Browser;
	public static Platform PlatformType;
	public static String BrowserVersion;
	public static String SeleniumHubUrl;
	public static int SeleniumHubPort;
	public static String ApplicationUrl;
	private static Properties prop;

	public TestConfigurationReader() throws IOException {
		prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
		}

		Remote = Boolean.valueOf(prop.getProperty("Remote"));

		Browser = prop.getProperty("Browser");

		if (Remote) {
			BrowserVersion = prop.getProperty("BrowserVersion");
			PlatformType = getPlatformType();
			SeleniumHubUrl = prop.getProperty("SeleniumHubUrl");
			SeleniumHubPort = Integer.parseInt(prop.getProperty("SeleniumHubPort"));
		}

		ApplicationUrl = prop.getProperty("ApplicationUrl");
	}

	private static Platform getPlatformType() {
		String platformValue = prop.getProperty("Platform");
		return Platform.valueOf(platformValue);
	}
}
