package com.adslib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TrungTV on 12/26/2017.
 */

public class AdConfig implements Parcelable{

    public static AdConfig.adsType adsType;
    public String adMobIDBanner;
    public String fbIDBanner;
    public String saIDBanner;
    public String adMobIDInterstitial;
    public String fbIDInterstitial;
    public String saIDInterstitial;
    public String adMobTestDeviceHash;
    public String fbTestDeviceHash;
    public int orderAdMob;
    public int orderFacebookAd;
    public int orderStartAppAd;

    protected AdConfig(Parcel in) {
        adMobIDBanner = in.readString();
        fbIDBanner = in.readString();
        saIDBanner = in.readString();
        adMobIDInterstitial = in.readString();
        fbIDInterstitial = in.readString();
        saIDInterstitial = in.readString();
        adMobTestDeviceHash = in.readString();
        fbTestDeviceHash = in.readString();
        orderAdMob = in.readInt();
        orderFacebookAd = in.readInt();
        orderStartAppAd = in.readInt();
    }

    public AdConfig() {
    }

    public static final Creator<AdConfig> CREATOR = new Creator<AdConfig>() {
        @Override
        public AdConfig createFromParcel(Parcel in) {
            return new AdConfig(in);
        }

        @Override
        public AdConfig[] newArray(int size) {
            return new AdConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adMobIDBanner);
        dest.writeString(fbIDBanner);
        dest.writeString(saIDBanner);
        dest.writeString(adMobIDInterstitial);
        dest.writeString(fbIDInterstitial);
        dest.writeString(saIDInterstitial);
        dest.writeString(adMobTestDeviceHash);
        dest.writeString(fbTestDeviceHash);
        dest.writeInt(orderAdMob);
        dest.writeInt(orderFacebookAd);
        dest.writeInt(orderStartAppAd);
    }

    public enum adsType {
        FACEBOOK,
        AD_MOB,
        START_APP
    }

}
