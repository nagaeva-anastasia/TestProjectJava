package screens;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Page {
    protected static WebDriver driver;
    protected static WebDriverWait awaiter;

    public WebDriver GetDriver()
    {
        return driver;
    }

    public static WebDriverWait Wait()
    {
        return awaiter;
    }
}
