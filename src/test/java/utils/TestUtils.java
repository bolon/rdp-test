package utils;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Properties;
import java.util.SortedMap;

/**
 * Complete the docs.
 */
public class TestUtils {
    public static RequestWrapper generateValidRequest() {
        String redirectUrl = "https://secure-hollows-99342.herokuapp.com";
        String notifyUrl = "https://secure-hollows-99342.herokuapp.com/notif-handler-v2";
        String backUrl = "https://json.org";
        String mid = getProperty("mid");
        String orderId = "OR123";
        double amount = 1000.00;
        String ccy = "SGD";
        String apiMode = "redirection_hosted";
        String paymentType = "S";
        String mercRef = "the things to reference";

        String sig = getSignature(mid, orderId, paymentType, String.format("%.2f", amount), ccy);

        RequestWrapper requestWrapper = new RequestWrapper(redirectUrl, notifyUrl, backUrl, mid, orderId, amount, ccy, apiMode, paymentType, mercRef);
        requestWrapper.setSignature(sig);

        return requestWrapper;
    }

    /**
     * Get signature of response or request
     *
     * @param param String in order : mid, orderId/transactionId, paymentType, amount, ccy
     * @return After digest the aggregated String into Hash512 get the hex.
     */
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

    private static String getSHA512String(String stringToHash) {
        String generatedPassword;

        generatedPassword = DigestUtils.sha512Hex(stringToHash);

        return generatedPassword;
    }

    public static SortedMap putRespInSortedMap(String resp) {
        SortedMap sortedMap;
        Gson gson = new Gson();

        sortedMap = gson.fromJson(resp, SortedMap.class);

        for (Object s : sortedMap.keySet()) {
            if (sortedMap.get(s) instanceof Number) {
                sortedMap.put(s, ((Number) sortedMap.get(s)).longValue());
            }
        }

        return sortedMap;
    }

    /**
     * Get Aggregated string from sorted map by key for generic sign purpose
     * @param s Sorted map by key
     * @return aggregated string without 'signature' field. See the api docs.
     */
    public static String getAggregatedField(SortedMap s) {
        s.remove("signature");
        String aggregatedStr = "";

        for (Object key : s.keySet()) {
            aggregatedStr += TestUtils.getStringObj(s.get(key));
        }

        return aggregatedStr;
    }

    private static String getStringObj(Object obj) {
        if (obj instanceof Array) {
            String arrString = "";
            for (Object o : (Object[]) obj) {
                arrString += getStringObj(o);
            }

            return arrString;
        }

        return String.valueOf(obj);
    }
}