package com.adslib;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

/**
 * Created by TrungDK on 12/25/2017.
 */

public class AdBanner {
    private AdView adMobBanner;
    private com.facebook.ads.AdView fbBanner;
    private Banner saBanner;
    private StartAppAd startAppAd;
    private RelativeLayout container;
    private AdConfig adConfig;
    private Activity activity;

    public AdBanner(Activity activity, AdConfig adConfig, RelativeLayout container) {
        this.activity = activity;
        this.adConfig = adConfig;
        this.container = container;
    }

    /*Show AdMob Banner*/
    private void showAdMobAd() {
        adMobBanner = new AdView(activity);
        adMobBanner.setAdSize(AdSize.SMART_BANNER);
        adMobBanner.setAdUnitId(adConfig.adMobID);
        adMobBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Toast.makeText(activity,"showAdMobAd Fail",Toast.LENGTH_SHORT).show();
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
                    case AdOrder.THIRD:
                        container.setVisibility(View.GONE);
                        break;
                    default:
                        container.setVisibility(View.GONE);
                        break;
                }
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adMobBanner.setLayoutParams(params);
        container.removeAllViews();
        container.addView(adMobBanner);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(adConfig.adMobTestDeviceHash)
                .build();
        adMobBanner.loadAd(adRequest);
    }

    /*Show FacebookAd Banner*/
    private void showFacebookAd() {
        fbBanner = new com.facebook.ads.AdView(activity, adConfig.fbID, com.facebook.ads
                .AdSize.BANNER_HEIGHT_50);
        AdSettings.addTestDevice(adConfig.fbTestDeviceHash);
        fbBanner.setAdListener(new com.facebook.ads.AdListener() {

            @Override
            public void onAdLoaded(Ad ad) {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

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
                    case AdOrder.THIRD:
                        container.setVisibility(View.GONE);
                        break;
                    default:
                        container.setVisibility(View.GONE);
                        break;
                }
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fbBanner.setLayoutParams(params);
        fbBanner.loadAd();
        container.removeAllViews();
        container.addView(fbBanner);
    }

    /*Show StartAppAd Banner*/
    private void showStartAppAd() {
        StartAppSDK.init(activity, adConfig.saID, true);
        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(activity);
        saBanner = new Banner(activity);
        saBanner.setBannerListener(new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailedToReceiveAd(View view) {
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
                    case AdOrder.THIRD:
                        container.setVisibility(View.GONE);
                        break;
                    default:
                        container.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onClick(View view) {

            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        saBanner.setLayoutParams(params);
        saBanner.showBanner();
        container.removeAllViews();
        container.addView(saBanner);
    }

    /*Show Ad*/
    public void showAd() {
        if (adConfig.orderAd.getOrderAdMob() == AdOrder.FIRST) {
            showAdMobAd();
        } else if (adConfig.orderAd.getOrderFacebookAd() == AdOrder.FIRST) {
            showFacebookAd();
        } else if (adConfig.orderAd.getOrderStartAppAd() == AdOrder.FIRST) {
            showStartAppAd();
        }
    }

    public void onResume() {
        if (adMobBanner != null)
            adMobBanner.resume();
        if (startAppAd != null)
            startAppAd.onResume();
    }

    public void onDestroy() {
        if (adMobBanner != null)
            adMobBanner.destroy();
        if (fbBanner != null)
            fbBanner.destroy();
    }

    public void onPause() {
        if (adMobBanner != null)
            adMobBanner.pause();
        if (startAppAd != null)
            startAppAd.onPause();
    }

    public void onBackPressed() {
        if (startAppAd != null)
            startAppAd.onBackPressed();
    }


}
