import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.util.concurrent.TimeUnit;

public class Chapter2 {
    private WebDriver driver;
    private static String purchaseEmail;
    private static String purchaseOrderNumber;

    @BeforeMethod
    public void testInit() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void testCleanup() throws InterruptedException {
        Thread.sleep(3000);
    }

}
