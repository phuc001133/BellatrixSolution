import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

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

    private WebElement waitAndFindElement(By by) {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private List<WebElement> waitAndFindElements(By by) {
        var webDriverWait = new WebDriverWait(driver,Duration.ofSeconds(20));
        return webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    private WebElement waitToBeClickable(By by) {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
    }

    @Test(priority = 1)
    public void completePurchaseSuccessfully_whenNewClient() {
        driver.navigate().to("https://demos.bellatrix.solutions");

        //HOME PAGE
        var addToCartFalcon9 = waitAndFindElement(By.cssSelector("[data-product_id='28']"));
        addToCartFalcon9.click();

        var viewCartBtn = waitAndFindElement(By.cssSelector(".added_to_cart"));
        viewCartBtn.click();

        //CART DETAIL PAGE
        var couponCodeTxt = waitAndFindElement(By.cssSelector("#coupon_code"));
        couponCodeTxt.sendKeys("happybirthday");

        var applyCouponBtn = waitAndFindElement(By.cssSelector("[name='apply_coupon']"));
        applyCouponBtn.click();

        var alertMessageWhenAfterAppliedCoupon = waitAndFindElement(By.cssSelector("[class*='message']"));
        Assert.assertEquals(alertMessageWhenAfterAppliedCoupon.getText(), "Coupon code applied successfully.");
        
        var increaseQtyTxt = waitAndFindElement(By.cssSelector("[id*='quantity']"));
        increaseQtyTxt.clear();
        increaseQtyTxt.sendKeys("2");

        var updateCartBtn = waitToBeClickable(By.cssSelector("[name*='update']"));
        updateCartBtn.click();

        var totalPriceLbl = waitAndFindElement(By.xpath("//*[@class='order-total']//span"));
        Assert.assertEquals(totalPriceLbl.getText(), "114.00€");

        var checkoutBtn = waitAndFindElement(By.cssSelector(".checkout-button"));
        checkoutBtn.click();
        

        //BILLING DETAIL PAGE
        var firstnameTxt = waitAndFindElement(By.cssSelector("#billing_first_name"));
        firstnameTxt.sendKeys("Anton");

        var lastnameTxt = waitAndFindElement(By.cssSelector("#billing_last_name"));
        lastnameTxt.sendKeys("Angelov");

        var companyTxt = waitAndFindElement(By.cssSelector("#billing_company"));
        companyTxt.sendKeys("Space Flowers");

        var countryWrapper = waitAndFindElement(By.cssSelector("[id*='select2-billing_country']"));
        countryWrapper.click();

        var searchCountryTxt = waitAndFindElement(By.className("select2-search__field"));
        searchCountryTxt.sendKeys("Germany");

        var searchCountryResult = waitAndFindElement(By.id("select2-billing_country-results"));
        searchCountryResult.click();

        var streetAddressTxt = waitAndFindElement(By.id("billing_address_1"));
        streetAddressTxt.sendKeys("29 national street 1K");

        var apartmentAddressTxt = waitAndFindElement(By.id("billing_address_2"));
        apartmentAddressTxt.sendKeys("room 1");

        var postcodeTxt = waitAndFindElement(By.id("billing_postcode"));
        postcodeTxt.sendKeys("10115");

        var cityAddressTxt = waitAndFindElement(By.id("billing_city"));
        cityAddressTxt.sendKeys("Berlin");

        var phoneTxt = waitAndFindElement(By.cssSelector("#billing_phone"));
        phoneTxt.sendKeys("0912321233");

        var emailTxt = waitAndFindElement(By.cssSelector("#billing_email"));
        emailTxt.sendKeys("info@berlinspaceflowers.com");
        purchaseEmail = "info@berlinspaceflowers.com";

        var createAnAccountCkb = waitAndFindElement(By.cssSelector("#createaccount"));
        createAnAccountCkb.click();

        var placeOrderButton = waitAndFindElement(By.id("place_order"));
        placeOrderButton.click();
        
        var receivedMessage = waitAndFindElement(By.cssSelector("h1"));
        Assert.assertEquals(receivedMessage.getText(), "Order received");

    }

    @Test(priority = 2)
    public void completePurchaseSuccessfully_whenExistingClient() {
        driver.navigate().to("https://demos.bellatrix.solutions");

        //HOME PAGE
        var addToCartFalcon9 = waitAndFindElement(By.cssSelector("[data-product_id='28']"));
        addToCartFalcon9.click();

        var viewCartBtn = waitAndFindElement(By.cssSelector(".added_to_cart"));
        viewCartBtn.click();

        //CART DETAIL PAGE
        var couponCodeTxt = waitAndFindElement(By.cssSelector("#coupon_code"));
        couponCodeTxt.sendKeys("happybirthday");

        var applyCouponBtn = waitAndFindElement(By.cssSelector("[name='apply_coupon']"));
        applyCouponBtn.click();

        var alertMessageWhenAfterAppliedCoupon = waitAndFindElement(By.cssSelector("[class*='message']"));
        Assert.assertEquals(alertMessageWhenAfterAppliedCoupon.getText(), "Coupon code applied successfully.");

        
        var increaseQtyTxt = waitAndFindElement(By.cssSelector("[id*='quantity']"));
        increaseQtyTxt.clear();
        increaseQtyTxt.sendKeys("2");

        var updateCartBtn = waitToBeClickable(By.cssSelector("[name*='update']"));
        updateCartBtn.click();

        var totalPriceLbl = waitAndFindElement(By.xpath("//*[@class='order-total']//span"));
        Assert.assertEquals(totalPriceLbl.getText(), "114.00€");

        var checkoutBtn = waitAndFindElement(By.cssSelector(".checkout-button"));
        checkoutBtn.click();
        

        var loginLnk = waitAndFindElement(By.linkText("Click here to login"));
        loginLnk.click();

        var usernameTxt = waitAndFindElement(By.id("username"));
        usernameTxt.sendKeys(purchaseEmail);

        var passwordTxt = waitAndFindElement(By.id("password"));
        passwordTxt.sendKeys(GetUserPasswordFromDb(purchaseEmail));

        var loginBtn = waitAndFindElement(By.cssSelector("button[class*=login]"));
        loginBtn.click();

        var placeOrderBtn = waitAndFindElement(By.cssSelector("#place_order"));
        placeOrderBtn.click();

        //ORDER RECEIVED
        var titlePageLbl = waitAndFindElement(By.cssSelector("header .entry-title"));
        
        Assert.assertEquals(titlePageLbl.getText(), "Order received");

        var orderNumberLbl = waitAndFindElement(By.cssSelector(".order strong"));
        purchaseOrderNumber = orderNumberLbl.getText();
    }

    @Test(priority = 3)
    public void correctOrderDataDisplayed_whenNavigateToMyAccountOrderSection() {
        driver.navigate().to("https://demos.bellatrix.solutions");

        var myAccountLink = waitAndFindElement(By.linkText("My account"));
        myAccountLink.click();

        var usernameTxt = waitAndFindElement(By.id("username"));
        usernameTxt.sendKeys(purchaseEmail);

        var passwordTxt = waitAndFindElement(By.id("password"));
        passwordTxt.sendKeys(GetUserPasswordFromDb(purchaseEmail));

        

        var loginBtn = waitAndFindElement(By.cssSelector("button[class*=login]"));
        loginBtn.click();

        

        var ordersLbl = waitAndFindElement(By.linkText("Orders"));
        ordersLbl.click();
        

        var viewDetailOrderBtn = waitAndFindElements(By.linkText("View"));
        viewDetailOrderBtn.get(0).click();
        

        var orderName = waitAndFindElement(By.cssSelector("h1"));
        var expectOrderName = String.format("Order #%s", purchaseOrderNumber);
        Assert.assertEquals(orderName.getText(), expectOrderName);

    }



    private String GetUserPasswordFromDb(String userName)
    {
        return "@purISQzt%%DYBnLCIhaoG6$";
    }

}
