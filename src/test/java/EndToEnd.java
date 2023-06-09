import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;


public class EndToEnd {
    private WebDriver driver;
    private static String purchaseEmail;
    private static String purchaseOrderNumber;

    @BeforeMethod
    public void testInit() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

    }

    @AfterMethod
    public void testCleanup() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

    @Test(priority = 1)
    public void completePurchaseSuccessfully_whenNewClient() throws InterruptedException {
        driver.navigate().to("https://demos.bellatrix.solutions");

        //HOME PAGE
        var addToCartFalcon9 = driver.findElement(By.cssSelector("[data-product_id='28']"));
        addToCartFalcon9.click();
        Thread.sleep(2000);

        var viewCartBtn = driver.findElement(By.cssSelector(".added_to_cart"));
        viewCartBtn.click();
        Thread.sleep(3000);


        //CART DETAIL PAGE
        var couponCodeTxt = driver.findElement(By.cssSelector("#coupon_code"));
        couponCodeTxt.sendKeys("happybirthday");

        var applyCouponBtn = driver.findElement(By.cssSelector("[name='apply_coupon']"));
        applyCouponBtn.click();

        Thread.sleep(3000);

        var alertMessageWhenAfterAppliedCoupon = driver.findElement(By.cssSelector("[class*='message']"));
        Assert.assertEquals(alertMessageWhenAfterAppliedCoupon.getText(), "Coupon code applied successfully.");

        Thread.sleep(3000);
        var increaseQtyTxt = driver.findElement(By.cssSelector("[id*='quantity']"));
        increaseQtyTxt.clear();
        increaseQtyTxt.sendKeys("2");

        Thread.sleep(3000);

        var updateCartBtn = driver.findElement(By.cssSelector("[name*='update']"));
        updateCartBtn.click();

        Thread.sleep(5000);

        var totalPriceLbl = driver.findElement(By.xpath("//*[@class='order-total']//span"));
        Assert.assertEquals(totalPriceLbl.getText(), "114.00€");

        var checkoutBtn = driver.findElement(By.cssSelector(".checkout-button"));
        checkoutBtn.click();
        Thread.sleep(3000);

        //BILLING DETAIL PAGE
        var firstnameTxt = driver.findElement(By.cssSelector("#billing_first_name"));
        firstnameTxt.sendKeys("Anton");

        var lastnameTxt = driver.findElement(By.cssSelector("#billing_last_name"));
        lastnameTxt.sendKeys("Angelov");

        var companyTxt = driver.findElement(By.cssSelector("#billing_company"));
        companyTxt.sendKeys("Space Flowers");

        var countryWrapper = driver.findElement(By.cssSelector("[id*='select2-billing_country']"));
        countryWrapper.click();

        var searchCountryTxt = driver.findElement(By.className("select2-search__field"));
        searchCountryTxt.sendKeys("Germany");

        var searchCountryResult = driver.findElement(By.id("select2-billing_country-results"));
        searchCountryResult.click();

        var streetAddressTxt = driver.findElement(By.id("billing_address_1"));
        streetAddressTxt.sendKeys("29 national street 1K");

        var apartmentAddressTxt = driver.findElement(By.id("billing_address_2"));
        apartmentAddressTxt.sendKeys("room 1");

        var postcodeTxt = driver.findElement(By.id("billing_postcode"));
        postcodeTxt.sendKeys("10115");

        var cityAddressTxt = driver.findElement(By.id("billing_city"));
        cityAddressTxt.sendKeys("Berlin");

        var phoneTxt = driver.findElement(By.cssSelector("#billing_phone"));
        phoneTxt.sendKeys("0912321233");

        var emailTxt = driver.findElement(By.cssSelector("#billing_email"));
        var randomEmail = new Random();
        var randomMail = randomEmail.nextInt(2000);
        emailTxt.sendKeys("infotest@" + randomMail + ".com");

        var createAnAccountCkb = driver.findElement(By.cssSelector("#createaccount"));
        createAnAccountCkb.click();

        Thread.sleep(2000);
        var placeOrderBtn = driver.findElement(By.cssSelector("#place_order"));
        placeOrderBtn.click();
        Thread.sleep(10000);

        //ORDER RECEIVED
        var titlePageLbl = driver.findElement(By.cssSelector("header .entry-title"));
        Assert.assertEquals(titlePageLbl.getText(), "Order received");

    }

}
