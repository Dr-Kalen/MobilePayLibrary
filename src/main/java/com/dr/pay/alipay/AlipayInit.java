package com.dr.pay.alipay;


import com.dr.pay.common.PayInit;

/**
 * public static final String PARTNER = "2088711458041988";
 * public static final String SELLER = "shawn@magic-beans.cn";
 * public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANeXcIlkCiQEBOAVejlXw5c2z1i5Smp6TqDVn87uP1golK3qGk1kVGWwb1VCtLwCwPTQdklIzgvUhd9wejcCTb9tDvenVsc97GxtDsgQsab80xhxt0v8fMrvUW6GAo1v82wDNAnaJOgMyBiLz1qdFi6v3d4To4UIP1lYKA5IHRw/AgMBAAECgYAp9YRmiPzcQ5f7UNLH5Efh3z39o5pTHNi+vrTqnj3f144QRxa1Z0hxviP88Q2h5dtOCmBrJOtNLCCd+xMeL6mXs4pRDyJhMIXr9ATI9RHRGzhYBwpwntu17TkWEDZhPwCMZd/mfKuUnA6efUrUooOgBxC0w0hEKJ7Fx1WsRzGFIQJBAPRykkyMhearStHl5pvtAk47m1E7lRJdIKS26+cRJgtxzgQsfXXaaY5ofhzUZLlmembfmLQlEDvZUYSnKkQDlVECQQDhx8GhSgSW1fHmSf9iXjMaGcgpufXKJQdCdGP3lfQE9Fo/5DyuG/Hi9mcI2tlcugear47Fc6tkg8RjDyN2NHSPAkBLQTpo9poCeZl/JRfpyP18uD0ItR8fmCwEiYv2hwD/ZPnXrxqwvZzhiqnIllqr+TCVmyCX1RE6W50NCampZrEBAkEA1aLqOO+MwBzYs4s5N57iw8O5fqITTJ8U63CuVstxBm8gJmyUbjrK+nMks2BBZFgycc/ETFlSx91WqlTcvTCNSQJAeceuTlXiawsNLkWKKnmEq8HoNhrKAeLalvb4RjP3UXmfkbEhgWuwVa0gRM/mxRwGa4CRx/tm8X0gdqpSncduQA==";
 * public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
 */
public class AlipayInit extends PayInit {

    private String partner;

    private String seller;

    private String rsaPrivate;

    private String rsaPublic;

    public AlipayInit(String partner, String seller, String rsaPrivate) {
        this(partner, seller, rsaPrivate, null);
    }

    public AlipayInit(String partner, String seller, String rsaPrivate, String rsaPublic) {
        this.partner = partner;
        this.seller = seller;
        this.rsaPrivate = rsaPrivate;
        this.rsaPublic = rsaPublic;
    }

    public String getPartner() {
        return partner;
    }

    public String getSeller() {
        return seller;
    }

    public String getRsaPrivate() {
        return rsaPrivate;
    }

    public String getRsaPublic() {
        return rsaPublic;
    }

}
