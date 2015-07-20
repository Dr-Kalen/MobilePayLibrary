package com.dr.pay.common;

/**
 * Created by kalen on 15/4/25.
 */
public interface IMobilePayService<T extends PayInit,K extends Product> {


    public void initPayService(T init);

    public String getPayOrderDetail(K product);

    public void pay(K product);

    public void setOnPayListener(OnPayListener listener);

    public boolean checkPayChannel();

    public String getPayVersion();

}
