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
import utils.*;

import java.util.List;
import java.util.SortedMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

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
        Assert.assertEquals(responseWrapper.getPaymentUrl(), driver.getCurrentUrl());
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

        Assert.assertEquals(requestWrapper.getAmount(), amount);
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

    @Test
    public void test6PaymentFailed() {
        String nameOnCard = "Fernando";
        String testCardNumber = "4111 1111 1111 1110";
        String emailAddrs = "simbolon012@gmail.com";
        int month = 12;
        int year = 2018;
        int cvv = 123;

        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        driver.get(responseWrapper.getPaymentUrl());
        wait.until(ExpectedConditions.titleContains("RedDotPayment"));

        String idFormCardName = "card-name";
        String idFormCardMonth = "exp-month";
        String idFormCardYear = "exp-year";
        String idFormEmail = "cc-mail";
        String idFormCardNumber = "cc-no";
        String idFormCCV = "cc-ccv";
        String payBtnMatcher = "button[class='btn btn-success']";

        WebElement formCardName = driver.findElementById(idFormCardName);
        WebElement formCardExpMonth = driver.findElementById(idFormCardMonth);
        WebElement formCardExpYear = driver.findElementById(idFormCardYear);
        WebElement formEmail = driver.findElementById(idFormEmail);
        WebElement formCardNumber = driver.findElementById(idFormCardNumber);
        WebElement formCardCCV = driver.findElementById(idFormCCV);
        WebElement btnPay = driver.findElement(By.cssSelector(payBtnMatcher));

        formCardName.click();
        formCardName.sendKeys(nameOnCard);

        formCardExpMonth.click();
        formCardExpMonth.sendKeys(String.valueOf(month));

        formCardExpYear.click();
        formCardExpYear.sendKeys(String.valueOf(year));

        formCardNumber.click();
        formCardNumber.sendKeys(String.valueOf(testCardNumber));

        formCardCCV.click();
        formCardCCV.sendKeys(String.valueOf(cvv));

        formEmail.click();
        formEmail.sendKeys(emailAddrs);

        btnPay.click();

        wait.until(ExpectedConditions.urlContains(requestWrapper.getRedirectUrl()));

        //check the notification by accessing notification-handler
        String expectedMsg = "failed";
        int respCodeFailed = -1;

        given().
                baseUri(TestUtils.getProperty("get_notif_endpoint")).
                when().get().
                then().assertThat().
                statusCode(200).
                body("response_value.response_msg", equalTo(expectedMsg)).
                and().
                body("response_value.response_code", equalTo(String.valueOf(respCodeFailed)));
    }

    @Test
    public void test7PaymentSuccess() {
        String nameOnCard = "Fernando";
        String testCardNumber = "4111 1111 1111 1111";
        String emailAddrs = "simbolon012@gmail.com";
        int month = 12;
        int year = 2018;
        int cvv = 123;

        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        driver.get(responseWrapper.getPaymentUrl());
        wait.until(ExpectedConditions.titleContains("RedDotPayment"));

        String idFormCardName = "card-name";
        String idFormCardMonth = "exp-month";
        String idFormCardYear = "exp-year";
        String idFormEmail = "cc-mail";
        String idFormCardNumber = "cc-no";
        String idFormCCV = "cc-ccv";
        String payBtnMatcher = "button[class='btn btn-success']";

        WebElement formCardName = driver.findElementById(idFormCardName);
        WebElement formCardExpMonth = driver.findElementById(idFormCardMonth);
        WebElement formCardExpYear = driver.findElementById(idFormCardYear);
        WebElement formEmail = driver.findElementById(idFormEmail);
        WebElement formCardNumber = driver.findElementById(idFormCardNumber);
        WebElement formCardCCV = driver.findElementById(idFormCCV);
        WebElement btnPay = driver.findElement(By.cssSelector(payBtnMatcher));

        formCardName.click();
        formCardName.sendKeys(nameOnCard);

        formCardExpMonth.click();
        formCardExpMonth.sendKeys(String.valueOf(month));

        formCardExpYear.click();
        formCardExpYear.sendKeys(String.valueOf(year));

        formCardNumber.click();
        formCardNumber.sendKeys(String.valueOf(testCardNumber));

        formCardCCV.click();
        formCardCCV.sendKeys(String.valueOf(cvv));

        formEmail.click();
        formEmail.sendKeys(emailAddrs);

        btnPay.click();

        wait.until(ExpectedConditions.urlContains(requestWrapper.getRedirectUrl()));

        //check the notification by accessing dummy web-handler
        String expectedMsg = "successful";
        int respCodeFailed = 0;

        given().
                baseUri(TestUtils.getProperty("get_notif_endpoint")).
                when().get().
                then().assertThat().
                statusCode(200).
                body("response_value.response_msg", equalTo(expectedMsg)).
                and().
                body("response_value.response_code", equalTo(String.valueOf(respCodeFailed)));
    }

    /**
     * Check if rdp-server is the sender of response payment
     */
    @Test
    public void test8PaymentResultVerify() {
        String nameOnCard = "Fernando";
        String testCardNumber = "4111 1111 1111 1111";
        String emailAddrs = "simbolon012@gmail.com";
        int month = 12;
        int year = 2018;
        int cvv = 123;

        ResponseWrapper respPaymentWrapper = given().contentType("application/json").
                body(requestWrapper).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        driver.get(respPaymentWrapper.getPaymentUrl());
        wait.until(ExpectedConditions.titleContains("RedDotPayment"));

        String idFormCardName = "card-name";
        String idFormCardMonth = "exp-month";
        String idFormCardYear = "exp-year";
        String idFormEmail = "cc-mail";
        String idFormCardNumber = "cc-no";
        String idFormCCV = "cc-ccv";
        String payBtnMatcher = "button[class='btn btn-success']";

        WebElement formCardName = driver.findElementById(idFormCardName);
        WebElement formCardExpMonth = driver.findElementById(idFormCardMonth);
        WebElement formCardExpYear = driver.findElementById(idFormCardYear);
        WebElement formEmail = driver.findElementById(idFormEmail);
        WebElement formCardNumber = driver.findElementById(idFormCardNumber);
        WebElement formCardCCV = driver.findElementById(idFormCCV);
        WebElement btnPay = driver.findElement(By.cssSelector(payBtnMatcher));

        formCardName.click();
        formCardName.sendKeys(nameOnCard);

        formCardExpMonth.click();
        formCardExpMonth.sendKeys(String.valueOf(month));

        formCardExpYear.click();
        formCardExpYear.sendKeys(String.valueOf(year));

        formCardNumber.click();
        formCardNumber.sendKeys(String.valueOf(testCardNumber));

        formCardCCV.click();
        formCardCCV.sendKeys(String.valueOf(cvv));

        formEmail.click();
        formEmail.sendKeys(emailAddrs);

        btnPay.click();

        //wait until redirect
        wait.until(ExpectedConditions.urlContains(requestWrapper.getRedirectUrl()));

        //after redirect
        String currentUrl = driver.getCurrentUrl();
        String transId = currentUrl.substring(currentUrl.indexOf('=') + 1);

        Assert.assertNotNull(transId);
        Assert.assertTrue(transId.contains(respPaymentWrapper.getOrderId()));

        RedirectRequestWrapper rqWrapperRedirect = new RedirectRequestWrapper();
        rqWrapperRedirect.setRequestMid(respPaymentWrapper.getMid());
        rqWrapperRedirect.setTransactionId(transId);

        String aggregatedStr = rqWrapperRedirect.getRequestMid() + rqWrapperRedirect.getTransactionId();

        rqWrapperRedirect.setSignature(TestUtils.getSignature(aggregatedStr));

        //check the resp if its from RDP server
        String redirectRespStr = given().contentType("application/json").
                body(rqWrapperRedirect).
                baseUri(TestUtils.getProperty("api_redirect_endpoint")).
                when().
                post().
                asString();

        ResponseRedirectionWrapper redirectResp = given().contentType("application/json").
                body(rqWrapperRedirect).
                baseUri(TestUtils.getProperty("api_redirect_endpoint")).
                when().
                post().
                as(ResponseRedirectionWrapper.class);

        SortedMap s = TestUtils.putRespInSortedMap(redirectRespStr);

        aggregatedStr = TestUtils.getAggregatedField(s);
        String sigGenerated = TestUtils.getSignature(aggregatedStr);
        String sigToCheck = redirectResp.getSignature();

        Assert.assertEquals(sigGenerated, sigToCheck);
    }
}
