package utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Complete the docs.
 */
public class TestUtils {
    public static RequestModel generateValidRequest() {
        String redirectUrl = "http://www.google.com";
        String notifyUrl = "https://en.wikipedia.org/wiki";
        String backUrl = "https://json.org";
        String mid = getProperty("mid");
        String orderId = "OR123";
        double amount = 1000.00;
        String ccy = "SGD";
        String apiMode = "redirection_hosted";
        String paymentType = "S";
        String mercRef = "the things to reference";

        String sig = getSignature(mid, orderId, paymentType, String.format("%.2f", amount), ccy);

        RequestModel requestModel = new RequestModel(redirectUrl, notifyUrl, backUrl, mid, orderId, amount, ccy, apiMode, paymentType, mercRef);
        requestModel.setSignature(sig);

        return requestModel;
    }

    public static String getSignature(String... param) {
        String aggregatedField = "";

        for (String s : param) {
            aggregatedField += s.trim();
        }

        aggregatedField += getProperty("secret_key");

        return getSHA512String(aggregatedField);
    }

    public static String getProperty(String propertyName) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("key.properties"));

            return prop.getProperty(propertyName);
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }

    static String getSHA512String(String stringToHash) {
        String generatedPassword;

        generatedPassword = DigestUtils.sha512Hex(stringToHash);

        return generatedPassword;
    }
}