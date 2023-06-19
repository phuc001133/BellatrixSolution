import com.google.common.base.Stopwatch;
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
import java.util.concurrent.TimeUnit;

public class EndToEnd {
    private static Stopwatch stopwatch;
    private Driver driver;
    private static String purchaseEmail;
    private static String purchaseOrderNumber;

    @BeforeMethod
    public void testInit() {
        stopwatch = Stopwatch.createStarted();
        driver = new LoggingDriver(new WebCoreDriver());
        driver.start(Browser.CHROME);
        System.out.printf(" end browser init: %d", stopwatch.elapsed(TimeUnit.SECONDS));
    }

    @AfterMethod
    public void testCleanup() {
        driver.quit();
        System.out.printf(" after test: %d", stopwatch.elapsed(TimeUnit.SECONDS));
        stopwatch.stop();
    }

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
    private void increaseProductQuantity() throws InterruptedException {
        var increaseQtyTxt = driver.findElement(By.cssSelector("[id*='quantity']"));
        increaseQtyTxt.typeText("2");
        Thread.sleep(2000);
        var updateCartBtn = driver.findElement(By.cssSelector("[name*='update']"));
        updateCartBtn.click();
        Thread.sleep(4000);

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
    public void completePurchaseSuccessfully_whenNewClient() throws InterruptedException {
        System.out.printf("start completePurchaseSuccessfully_whenNewClient: %d", stopwatch.elapsed(TimeUnit.SECONDS));
        driver.goToUrl("https://demos.bellatrix.solutions");

        //HOME PAGE
        addRocketToCart();

        //CART DETAIL PAGE
        applyCoupon();
        Thread.sleep(2000);
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
        emailTxt.typeText(generateUniqueEmail());
        purchaseEmail = generateUniqueEmail();

        var createAnAccountCkb = driver.findElement(By.cssSelector("#createaccount"));
        createAnAccountCkb.click();

        var placeOrderButton = driver.findElement(By.id("place_order"));
        placeOrderButton.click();

        var receivedMessage = driver.findElement(By.cssSelector("h1"));
        Assert.assertEquals(receivedMessage.getText(), "Order received");
        System.out.printf("End completePurchaseSuccessfully_whenNewClient: %d", stopwatch.elapsed(TimeUnit.SECONDS));

    }

    @Test(priority = 2)
    public void completePurchaseSuccessfully_whenExistingClient() throws InterruptedException {
        System.out.printf("start completePurchaseSuccessfully_whenExistingClient: %d", stopwatch.elapsed(TimeUnit.SECONDS));
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
        System.out.printf("End completePurchaseSuccessfully_whenExistingClient: %d", stopwatch.elapsed(TimeUnit.SECONDS));
    }

    @Test(priority = 3)
    public void correctOrderDataDisplayed_whenNavigateToMyAccountOrderSection() {
        System.out.printf("start correctOrderDataDisplayed_whenNavigateToMyAccountOrderSection: %d", stopwatch.elapsed(TimeUnit.SECONDS));
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
        System.out.printf("End correctOrderDataDisplayed_whenNavigateToMyAccountOrderSection: %d", stopwatch.elapsed(TimeUnit.SECONDS));
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
