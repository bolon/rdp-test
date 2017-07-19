import io.restassured.RestAssured;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.RequestModel;
import utils.ResponseModel;
import utils.TestUtils;

import static io.restassured.RestAssured.given;

/**
 * Complete the docs.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UITest {
    private static final String BASE_URL_KEY_PROPERTY = "api_endpoint";
    private DesiredCapabilities capabilities;
    private RemoteWebDriver driver;
    private RequestModel rm;
    private WebDriverWait wait;

    @Before
    public void start() {
        capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        rm = TestUtils.generateValidRequest();
        RestAssured.baseURI = TestUtils.getProperty(BASE_URL_KEY_PROPERTY);
        RestAssured.useRelaxedHTTPSValidation();
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void destroy() {
        driver.close();
    }

    @Test
    public void test1SuccessOpenPaymentPage() {
        ResponseModel responseModel = given().contentType("application/json").
                body(rm).
                when().
                post("/payment-api").
                as(ResponseModel.class);

        //Navigate to the page
        driver.get(responseModel.getPaymentUrl());
        wait.until(ExpectedConditions.titleContains("RedDotPayment"));

        Assert.assertTrue(driver.getPageSource().contains("Transaction Details"));
    }

    @Test
    public void test2SuccessToBackUrl() {
        String backURL = rm.getBackUrl() + "/?transaction_id=" + rm.getOrderId();
        ResponseModel responseModel = given().contentType("application/json").
                body(rm).
                when().
                post("/payment-api").
                as(ResponseModel.class);

        //Navigate to the page
        driver.get(responseModel.getPaymentUrl());

        String backButtonMatcher = "a[id='payment-back-url']";
        WebElement btnBack = driver.findElement(By.cssSelector(backButtonMatcher));

        //Make sure btn exist
        wait.until(ExpectedConditions.elementToBeClickable(btnBack));

        btnBack.click();

        wait.until(ExpectedConditions.urlToBe(backURL));
        Assert.assertEquals(backURL, driver.getCurrentUrl());
    }
}
