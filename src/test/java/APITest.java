import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.RequestModel;
import utils.ResponseModel;
import utils.TestUtils;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


/**
 * API Unit test
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class APITest {
    private static final String BASE_URL_KEY_PROPERTY = "api_endpoint";
    private RequestModel rm;

    @Before
    public void start() {
        rm = TestUtils.generateValidRequest();
        RestAssured.baseURI = TestUtils.getProperty(BASE_URL_KEY_PROPERTY);
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void test1SigLengthValid() {
        int expectedLength = 128;

        assertEquals("Expected length of HASH512 = 128", expectedLength, rm.getSignature().length());
    }

    @Test
    public void test2CallSuccess() {
        int respCodeSuccess = 0;

        ResponseModel responseModel = given().contentType("application/json").
                body(rm).
                when().
                post("/payment-api").
                as(ResponseModel.class);

        Assert.assertEquals(responseModel.getRespCode(), respCodeSuccess);
    }

    @Test
    public void test3CallFailedSignature() {
        int respCodeInvalidSig = -1003;

        RequestModel newReq = rm;
        newReq.setAmount(200);

        ResponseModel responseModel = given().contentType("application/json").
                body(newReq).
                when().
                post("/payment-api").
                as(ResponseModel.class);

        Assert.assertEquals(responseModel.getRespCode(), respCodeInvalidSig);
    }
}
