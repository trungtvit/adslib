package com.adslib;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

/**
 * Created by TrungTV on 12/26/2017.
 */

public class AdInterstitial {
    private InterstitialAd fbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd adMobInterstitialAd;
    private StartAppAd startAppAd;
    private AdConfig adConfig;
    private Activity activity;

    public AdInterstitial(Activity activity, AdConfig adConfig) {
        this.activity = activity;
        this.adConfig = adConfig;
    }

    private void showAdMobAd(){
        adMobInterstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
        adMobInterstitialAd.setAdUnitId(adConfig.adMobID);
        adMobInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                adMobInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(activity,"showAdMob Fail",Toast.LENGTH_SHORT).show();
                switch (adConfig.orderAd.getOrderAdMob()) {
                    case AdOrder.FIRST:
                        if (adConfig.orderAd.getOrderFacebookAd() == AdOrder.SECOND) {
                            showFacebookAd();
                        } else {
                            showStartAppAd();
                        }
                        break;
                    case AdOrder.SECOND:
                        if (adConfig.orderAd.getOrderFacebookAd() == AdOrder.THIRD) {
                            showFacebookAd();
                        } else {
                            showStartAppAd();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(adConfig.adMobTestDeviceHash)
                .build();
        adMobInterstitialAd.loadAd(adRequest);
    }

    private void showFacebookAd(){
        fbInterstitialAd = new InterstitialAd(activity, adConfig.fbID);
        AdSettings.addTestDevice(adConfig.fbTestDeviceHash);
        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Toast.makeText(activity,"showFacebookAd Fail",Toast.LENGTH_SHORT).show();
                switch (adConfig.orderAd.getOrderFacebookAd()) {
                    case AdOrder.FIRST:
                        if (adConfig.orderAd.getOrderStartAppAd() == AdOrder.SECOND) {
                            showStartAppAd();
                        } else {
                            showAdMobAd();
                        }
                        break;
                    case AdOrder.SECOND:
                        if (adConfig.orderAd.getOrderStartAppAd() == AdOrder.THIRD) {
                            showStartAppAd();
                        } else {
                            showAdMobAd();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                fbInterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });
        fbInterstitialAd.loadAd();
    }

    private void showStartAppAd(){
        StartAppSDK.init(activity, adConfig.saID, true);
        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(activity);
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                startAppAd.showAd();
            }

            @Override
            public void onFailedToReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                Toast.makeText(activity,"showStartAppAd Fail",Toast.LENGTH_SHORT).show();
                switch (adConfig.orderAd.getOrderStartAppAd()) {
                    case AdOrder.FIRST:
                        if (adConfig.orderAd.getOrderAdMob() == AdOrder.SECOND) {
                            showAdMobAd();
                        } else {
                            showFacebookAd();
                        }
                        break;
                    case AdOrder.SECOND:
                        if (adConfig.orderAd.getOrderAdMob() == AdOrder.THIRD) {
                            showAdMobAd();
                        } else {
                            showFacebookAd();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void showAd(){
        if (adConfig.orderAd.getOrderAdMob() == AdOrder.FIRST) {
            showAdMobAd();
        } else if (adConfig.orderAd.getOrderFacebookAd() == AdOrder.FIRST) {
            showFacebookAd();
        } else if (adConfig.orderAd.getOrderStartAppAd() == AdOrder.FIRST) {
            showStartAppAd();
        }
    }
}
