package utils;

import com.google.gson.annotations.SerializedName;

/**
 * Complete the docs.
 */
public class RequestWrapper {
    public RequestWrapper(String redirectUrl, String notifyUrl, String backUrl, String mid, String orderId, double amount, String ccy, String apiMode, String paymentType, String merchantReference) {
        this.redirectUrl = redirectUrl;
        this.notifyUrl = notifyUrl;
        this.backUrl = backUrl;
        this.mid = mid;
        this.orderId = orderId;
        this.amount = String.format("%.2f", amount);
        this.ccy = ccy;
        this.apiMode = apiMode;
        this.paymentType = paymentType;
        this.merchantReference = merchantReference;
    }

    @SerializedName("redirect_url")
    private String redirectUrl;

    @SerializedName("notify_url")
    private String notifyUrl;

    @SerializedName("back_url")
    private String backUrl;

    @SerializedName("mid")
    private String mid;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("amount")
    private String amount;

    @SerializedName("ccy")
    private String ccy;

    @SerializedName("api_mode")
    private String apiMode;

    @SerializedName("payment_type")
    private String paymentType;

    @SerializedName("merchant_reference")
    private String merchantReference;

    @SerializedName("signature")
    private String signature;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = String.format("%.2f", amount);
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getApiMode() {
        return apiMode;
    }

    public void setApiMode(String apiMode) {
        this.apiMode = apiMode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
