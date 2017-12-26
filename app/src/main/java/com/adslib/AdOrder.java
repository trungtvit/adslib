package com.adslib;

/**
 * Created by TrungTV on 12/26/2017.
 */

public class AdOrder {
    public static final int FIRST = 1 ;
    public static final int SECOND = 2;
    public static final int THIRD = 3;

    private int orderAdMob;
    private int orderFacebookAd;
    private int orderStartAppAd;

    public AdOrder() {
    }

    public int getOrderAdMob() {
        return orderAdMob;
    }

    public void setOrderAdMob(int orderAdMob) {
        this.orderAdMob = orderAdMob;
    }

    public int getOrderFacebookAd() {
        return orderFacebookAd;
    }

    public void setOrderFacebookAd(int orderFacebookAd) {
        this.orderFacebookAd = orderFacebookAd;
    }

    public int getOrderStartAppAd() {
        return orderStartAppAd;
    }

    public void setOrderStartAppAd(int orderStartAppAd) {
        this.orderStartAppAd = orderStartAppAd;
    }
}
