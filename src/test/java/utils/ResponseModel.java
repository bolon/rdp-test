package utils;

import com.google.gson.annotations.SerializedName;

/**
 * Complete the docs.
 */

public class ResponseModel {
    @SerializedName("created_timestamp")
    private long timeCreated;

    @SerializedName("expired_timestamp")
    private long timeExpired;

    @SerializedName("mid")
    private String mid;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("payment_url")
    private String paymentUrl;

    @SerializedName("response_code")
    private int respCode;

    @SerializedName("response_msg")
    private String respMsg;

    @SerializedName("signature")
    private String signature;

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
