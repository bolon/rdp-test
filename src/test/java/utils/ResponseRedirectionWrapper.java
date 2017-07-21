package utils;

import com.google.gson.annotations.SerializedName;

/**
 * Complete the docs.
 */
public class ResponseRedirectionWrapper extends ResponseWrapper {
    @SerializedName("request_amount")
    private String requestAmount;

    @SerializedName("request_ccy")
    private String requestCCY;

    @SerializedName("authorized_amount")
    private String authorizedAmount;

    @SerializedName("authorized_ccy")
    private String authorizedCCY;

    @SerializedName("transaction_type")
    private String transactionType;

    @SerializedName("acquirer_response_code")
    private String acquirerRespCode;

    @SerializedName("acquirer_response_msg")
    private String acquirerRespMsg;

    @SerializedName("acquirer_authorized_amount")
    private String acquirerAuthorizedAmount;

    @SerializedName("acquirer_authorized_ccy")
    private String acquirerAuthorizedCCY;

    @SerializedName("merchant_reference")
    private String merchantReference;

    @SerializedName("acquirer_created_timestamp")
    private String acquirerCreatedTimestamp;

    @SerializedName("acquirer_transaction_id")
    private String acquirerTransactionId;

    @SerializedName("acquirer_authorization_code")
    private String acquirerAuthorizationCode;

    @SerializedName("first_6")
    private String first6;

    @SerializedName("last_4")
    private String last4;

    @SerializedName("payer_name")
    private String payerName;

    @SerializedName("exp_date")
    private String expDate;

    public String getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getRequestCCY() {
        return requestCCY;
    }

    public void setRequestCCY(String requestCCY) {
        this.requestCCY = requestCCY;
    }

    public String getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(String authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public String getAuthorizedCCY() {
        return authorizedCCY;
    }

    public void setAuthorizedCCY(String authorizedCCY) {
        this.authorizedCCY = authorizedCCY;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAcquirerRespCode() {
        return acquirerRespCode;
    }

    public void setAcquirerRespCode(String acquirerRespCode) {
        this.acquirerRespCode = acquirerRespCode;
    }

    public String getAcquirerRespMsg() {
        return acquirerRespMsg;
    }

    public void setAcquirerRespMsg(String acquirerRespMsg) {
        this.acquirerRespMsg = acquirerRespMsg;
    }

    public String getAcquirerAuthorizedAmount() {
        return acquirerAuthorizedAmount;
    }

    public void setAcquirerAuthorizedAmount(String acquirerAuthorizedAmount) {
        this.acquirerAuthorizedAmount = acquirerAuthorizedAmount;
    }

    public String getAcquirerAuthorizedCCY() {
        return acquirerAuthorizedCCY;
    }

    public void setAcquirerAuthorizedCCY(String acquirerAuthorizedCCY) {
        this.acquirerAuthorizedCCY = acquirerAuthorizedCCY;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getAcquirerCreatedTimestamp() {
        return acquirerCreatedTimestamp;
    }

    public void setAcquirerCreatedTimestamp(String acquirerCreatedTimestamp) {
        this.acquirerCreatedTimestamp = acquirerCreatedTimestamp;
    }

    public String getAcquirerTransactionId() {
        return acquirerTransactionId;
    }

    public void setAcquirerTransactionId(String acquirerTransactionId) {
        this.acquirerTransactionId = acquirerTransactionId;
    }

    public String getAcquirerAuthorizationCode() {
        return acquirerAuthorizationCode;
    }

    public void setAcquirerAuthorizationCode(String acquirerAuthorizationCode) {
        this.acquirerAuthorizationCode = acquirerAuthorizationCode;
    }

    public String getFirst6() {
        return first6;
    }

    public void setFirst6(String first6) {
        this.first6 = first6;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    @Override
    public String getTimeCreated() {
        return super.getTimeCreated();
    }
}
