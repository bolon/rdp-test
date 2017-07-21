package utils;

import com.google.gson.annotations.SerializedName;

/**
 * Complete the docs.
 */
public class RedirectRequestWrapper {
    public RedirectRequestWrapper() {
    }

    @SerializedName("request_mid")
    private String requestMid;

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("signature")
    private String signature;

    public String getRequestMid() {
        return requestMid;
    }

    public void setRequestMid(String requestMid) {
        this.requestMid = requestMid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
