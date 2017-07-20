import io.restassured.RestAssured;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.RequestWrapper;
import utils.ResponseWrapper;
import utils.TestUtils;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Complete the docs.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UITest {
    private static final String BASE_URL_KEY_PROPERTY = "api_endpoint";
    private DesiredCapabilities capabilities;
    private RemoteWebDriver driver;
    private RequestWrapper requestWrapper;
    private WebDriverWait wait;

    @Before
    public void start() {
        requestWrapper = TestUtils.generateValidRequest();

        capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        //small window to make sure element still exist
        driver.manage().window().setSize(new Dimension(416, 733));

        RestAssured.baseURI = TestUtils.getProperty(BASE_URL_KEY_PROPERTY);
        RestAssured.useRelaxedHTTPSValidation();
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void destroy() {
        driver.close();
    }

    /**
     * Test call to payment page.
     */
    @Test
    public void test1SuccessOpenPaymentPage() {
        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        //Navigate to the page
        driver.get(responseWrapper.getPaymentUrl());
        wait.until(ExpectedConditions.titleContains("RedDotPayment"));

        Assert.assertTrue(driver.getPageSource().contains("Transaction Details"));
    }

    /**
     * Test back url is working
     */
    @Test
    public void test2SuccessToBackUrl() {
        String backURL = requestWrapper.getBackUrl() + "/?transaction_id=" + requestWrapper.getOrderId();
        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        //Navigate to the page
        driver.get(responseWrapper.getPaymentUrl());

        String backButtonMatcher = "a[id='payment-back-url']";
        WebElement btnBack = driver.findElement(By.cssSelector(backButtonMatcher));

        //Make sure btn exist
        wait.until(ExpectedConditions.elementToBeClickable(btnBack));

        btnBack.click();

        wait.until(ExpectedConditions.urlToBe(backURL));
        Assert.assertEquals(backURL, driver.getCurrentUrl());
    }

    /**
     * Test form must be filled
     */
    @Test
    public void test3FailedToProceedWithRequiredFormEmpty() {
        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        //Navigate to the page
        driver.get(responseWrapper.getPaymentUrl());

        String payBtnMatcher = "button[class='btn btn-success']";
        WebElement btnPay = driver.findElement(By.cssSelector(payBtnMatcher));

        btnPay.click();
        Assert.assertEquals(responseWrapper.getPaymentUrl(), driver.getCurrentUrl());
    }

    @Test
    public void test4CheckInfoSendCorrect() {
        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        driver.get(responseWrapper.getPaymentUrl());

        String detailsMatcher = "div[class='desc-pad']";
        String amountMatcher = "div[class='amount-total'] span";

        List<WebElement> detailsElements = driver.findElements(By.cssSelector(detailsMatcher));
        List<WebElement> amountElement = driver.findElements(By.cssSelector(amountMatcher));

        String orderId = detailsElements.get(1).getAttribute("textContent").trim();
        String merchant = detailsElements.get(2).getAttribute("textContent").trim();
        String amount = amountElement.get(0).getAttribute("textContent").trim().replace(",", "");
        String ccy = amountElement.get(1).getAttribute("textContent").trim();

        Assert.assertEquals(requestWrapper.getAmount(), amount);    //todo : check
        Assert.assertEquals(requestWrapper.getCcy(), ccy);
        Assert.assertEquals(requestWrapper.getOrderId(), orderId);
        Assert.assertEquals(requestWrapper.getMerchantReference(), merchant);
    }

    @Test
    public void test5CheckAtLeast3CharCCV() {
        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        driver.get(responseWrapper.getPaymentUrl());
        wait.until(ExpectedConditions.titleContains("RedDotPayment"));

        String detailsMatcher = "input[id='cc-ccv']";
        String errorMsgMatcher = "div[class='err-msg']";

        WebElement formCCV = driver.findElementByCssSelector(detailsMatcher);
        formCCV.sendKeys("12");
        formCCV.sendKeys(Keys.ENTER);

        String errorMsg = "Please enter at least 3 characters.";
        Assert.assertEquals(errorMsg, driver.findElementByCssSelector(errorMsgMatcher).getAttribute("textContent").trim());
    }
}
