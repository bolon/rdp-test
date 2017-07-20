import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.RequestWrapper;
import utils.ResponseWrapper;
import utils.TestUtils;

import java.util.SortedMap;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


/**
 * API Unit test
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class APITest {
    private static final String BASE_URL_KEY_PROPERTY = "api_endpoint";
    private RequestWrapper rm;

    @Before
    public void start() {
        rm = TestUtils.generateValidRequest();
        RestAssured.baseURI = TestUtils.getProperty(BASE_URL_KEY_PROPERTY);
        RestAssured.useRelaxedHTTPSValidation();
    }

    /**
     * Make sure the length of signature is 128.
     */
    @Test
    public void test1SigLengthValid() {
        int expectedLength = 128;

        assertEquals("Expected length of HASH512 = 128", expectedLength, rm.getSignature().length());
    }

    /**
     * Test call to payment-api with valid request is success by checking the response code.
     */
    @Test
    public void test2CallSuccess() {
        int respCodeSuccess = 0;

        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(rm).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        Assert.assertEquals(responseWrapper.getRespCode(), respCodeSuccess);
    }

    /**
     * Test call to payment-api with invalid request is failed by checking the response code.
     */
    @Test
    public void test3CallFailedSignature() {
        int respCodeInvalidSig = -1003;

        RequestWrapper newReq = rm;
        newReq.setAmount(200);

        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(newReq).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        Assert.assertEquals(responseWrapper.getRespCode(), respCodeInvalidSig);
    }

    /**
     * Test call to payment-api with invalid request is failed by checking the response code.
     */
    @Test
    public void test4CallFailedSignature() {
        int respCodeInvalidSig = -1014;

        RequestWrapper newReq = rm;
        newReq.setSignature(rm.getSignature() + "x");

        ResponseWrapper responseWrapper = given().contentType("application/json").
                body(newReq).
                when().
                post("/payment-api").
                as(ResponseWrapper.class);

        Assert.assertEquals(responseWrapper.getRespCode(), respCodeInvalidSig);
    }

    @Test
    public void test5AuthenticateResponseSig() {
        String resp = given().contentType("application/json").
                body(rm).
                when().
                post("/payment-api").
                asString();


        SortedMap s = TestUtils.putRespInSortedMap(resp);
        String sigActual = String.valueOf(s.get("signature"));

        String aggregatedStr = TestUtils.getAggregatedField(s);
        String sigToCheck = TestUtils.getSignature(aggregatedStr);

        Assert.assertEquals(sigActual, sigToCheck);
    }
}
