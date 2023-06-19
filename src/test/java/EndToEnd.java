import decorators.Browser;
import decorators.Driver;
import decorators.LoggingDriver;
import decorators.WebCoreDriver;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.UUID;

public class EndToEnd {
    private Driver driver;
    private static String purchaseEmail;
    private static String purchaseOrderNumber;

    @BeforeMethod
    public void testInit() {
        driver = new LoggingDriver(new WebCoreDriver());
        driver.start(Browser.CHROME);
    }

    @AfterMethod
    public void testCleanup() {
        driver.quit();
    }

//    private WebElement driver.findElement(By by) {
//        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
//        return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
//    }
//
//    private List<WebElement> driver.findElements(By by) {
//        var webDriverWait = new WebDriverWait(driver,Duration.ofSeconds(20));
//        return webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
//    }
//
//    private WebElement waitToBeClickable(By by) {
//        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
//        return webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
//    }

    private void addRocketToCart() {
        var addToCartFalcon9 = driver.findElement(By.cssSelector("[data-product_id='28']"));
        addToCartFalcon9.click();

        var viewCartBtn = driver.findElement(By.cssSelector(".added_to_cart"));
        viewCartBtn.click();
    }
    private void applyCoupon() {
        var couponCodeTxt = driver.findElement(By.cssSelector("#coupon_code"));
        couponCodeTxt.typeText("happybirthday");

        var applyCouponBtn = driver.findElement(By.cssSelector("[name='apply_coupon']"));
        applyCouponBtn.click();

        var alertMessageWhenAfterAppliedCoupon = driver.findElement(By.cssSelector("[class*='message']"));
        Assert.assertEquals(alertMessageWhenAfterAppliedCoupon.getText(), "Coupon code applied successfully.");
    }
    private void increaseProductQuantity() {
        var increaseQtyTxt = driver.findElement(By.cssSelector("[id*='quantity']"));
        increaseQtyTxt.typeText("2");

        var updateCartBtn = driver.findElement(By.cssSelector("[name*='update']"));
        updateCartBtn.click();

        var totalPriceLbl = driver.findElement(By.xpath("//*[@class='order-total']//span"));
        Assert.assertEquals(totalPriceLbl.getText(), "114.00€");
    }
    private void login(String username) {
        var usernameTxt = driver.findElement(By.id("username"));
        usernameTxt.typeText(username);

        var passwordTxt = driver.findElement(By.id("password"));
        passwordTxt.typeText(GetUserPasswordFromDb(username));

        var loginBtn = driver.findElement(By.cssSelector("button[class*=login]"));
        loginBtn.click();
    }

    @Test(priority = 1)
    public void completePurchaseSuccessfully_whenNewClient() {
        driver.goToUrl("https://demos.bellatrix.solutions");

        //HOME PAGE
        addRocketToCart();

        //CART DETAIL PAGE
        applyCoupon();
        increaseProductQuantity();

        var checkoutBtn = driver.findElement(By.cssSelector(".checkout-button"));
        checkoutBtn.click();


        //BILLING DETAIL PAGE
        var firstnameTxt = driver.findElement(By.cssSelector("#billing_first_name"));
        firstnameTxt.typeText("Anton");

        var lastnameTxt = driver.findElement(By.cssSelector("#billing_last_name"));
        lastnameTxt.typeText("Angelov");

        var companyTxt = driver.findElement(By.cssSelector("#billing_company"));
        companyTxt.typeText("Space Flowers");

        var countryWrapper = driver.findElement(By.cssSelector("[id*='select2-billing_country']"));
        countryWrapper.click();

        var searchCountryTxt = driver.findElement(By.className("select2-search__field"));
        searchCountryTxt.typeText("Germany");

        var searchCountryResult = driver.findElement(By.id("select2-billing_country-results"));
        searchCountryResult.click();

        var streetAddressTxt = driver.findElement(By.id("billing_address_1"));
        streetAddressTxt.typeText("29 national street 1K");

        var apartmentAddressTxt = driver.findElement(By.id("billing_address_2"));
        apartmentAddressTxt.typeText("room 1");

        var postcodeTxt = driver.findElement(By.id("billing_postcode"));
        postcodeTxt.typeText("10115");

        var cityAddressTxt = driver.findElement(By.id("billing_city"));
        cityAddressTxt.typeText("Berlin");

        var phoneTxt = driver.findElement(By.cssSelector("#billing_phone"));
        phoneTxt.typeText("0912321233");

        var emailTxt = driver.findElement(By.cssSelector("#billing_email"));
        emailTxt.typeText("info@berlinspaceflowers.com");
        purchaseEmail = "info@berlinspaceflowers.com";

        var createAnAccountCkb = driver.findElement(By.cssSelector("#createaccount"));
        createAnAccountCkb.click();

        var placeOrderButton = driver.findElement(By.id("place_order"));
        placeOrderButton.click();

        var receivedMessage = driver.findElement(By.cssSelector("h1"));
        Assert.assertEquals(receivedMessage.getText(), "Order received");

    }

    @Test(priority = 2)
    public void completePurchaseSuccessfully_whenExistingClient() {
        driver.goToUrl("https://demos.bellatrix.solutions");

        //HOME PAGE
        addRocketToCart();

        //CART DETAIL PAGE
        applyCoupon();
        increaseProductQuantity();

        var checkoutBtn = driver.findElement(By.cssSelector(".checkout-button"));
        checkoutBtn.click();

        var loginLnk = driver.findElement(By.linkText("Click here to login"));
        loginLnk.click();

        login(purchaseEmail);

        var placeOrderBtn = driver.findElement(By.cssSelector("#place_order"));
        placeOrderBtn.click();

        //ORDER RECEIVED
        var titlePageLbl = driver.findElement(By.cssSelector("header .entry-title"));

        Assert.assertEquals(titlePageLbl.getText(), "Order received");

        var orderNumberLbl = driver.findElement(By.cssSelector(".order strong"));
        purchaseOrderNumber = orderNumberLbl.getText();
    }

    @Test(priority = 3)
    public void correctOrderDataDisplayed_whenNavigateToMyAccountOrderSection() {
        driver.goToUrl("https://demos.bellatrix.solutions");

        var myAccountLink = driver.findElement(By.linkText("My account"));
        myAccountLink.click();

        login(purchaseEmail);

        var ordersLbl = driver.findElement(By.linkText("Orders"));
        ordersLbl.click();

        var viewDetailOrderBtn = driver.findElements(By.linkText("View"));
        viewDetailOrderBtn.get(0).click();

        var orderName = driver.findElement(By.cssSelector("h1"));
        var expectOrderName = String.format("Order #%s", purchaseOrderNumber);
        Assert.assertEquals(orderName.getText(), expectOrderName);
    }

    private String GetUserPasswordFromDb(String userName)
    {
        return "@purISQzt%%DYBnLCIhaoG6$";
    }

    //UUID: universally unique identifier
    private String generateUniqueEmail() {
        return String.format("%s@berlinspaceflowers.com", UUID.randomUUID());
    }
}
