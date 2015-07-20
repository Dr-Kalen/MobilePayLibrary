package com.dr.pay.wechat;


import com.dr.pay.common.PayInit;

/**
 *
 *  public static final String APP_ID = "wx0b11738fa560ec81";
    public static final String MCH_ID = "1238726802";
    public static final String API_KEY = "g464GD4d65g4ds64gsd84g564dfh4ghf";
 */
public class WeChatPayInit extends PayInit {

    private String partner;

    private String seller;

    private String sellerKey;

    public WeChatPayInit(String partner, String seller, String sellerKey) {
        this.partner = partner;
        this.seller = seller;
        this.sellerKey = sellerKey;
    }

    /**
     * APP ID
     * @return
     */
    public String getPartner() {
        return partner;
    }

    /**
     * 商户号
     * @return
     */
    public String getSeller() {
        return seller;
    }

    /**
     * API KEY
     * @return
     */
    public String getSellerKey() {
        return sellerKey;
    }
}
