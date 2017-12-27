package com.adslib;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

/**
 * Created by TrungDK on 12/25/2017.
 */

public class AdBanner {
    private static final String TAG = AdBanner.class.getSimpleName();

    private AdView adMobBanner;
    private com.facebook.ads.AdView fbBanner;
    private Banner saBanner;
    private StartAppAd startAppAd;
    private RelativeLayout container;
    private AdConfig adConfig;
    private Activity activity;
    private DatabaseReference mDatabase ;

    public AdBanner(Activity activity, final AdConfig adConfig, RelativeLayout container) {
        this.activity = activity;
        this.adConfig = adConfig;
        this.container = container;

        mDatabase= FirebaseDatabase.getInstance().getReference("ad_key");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdKey adKey = dataSnapshot.getValue(AdKey.class);
                if (adKey == null) {
                    return;
                }

                if(adKey.ad_mob_key_banner != adConfig.adMobIDBanner){
                    adConfig.adMobIDBanner = adKey.ad_mob_key_banner;
                    showAdMobAd();
                    return;
                }
                if(adKey.fb_key_banner != adConfig.fbIDBanner){
                    adConfig.fbIDBanner = adKey.fb_key_banner;
                    showFacebookAd();
                    return;
                }
                if(adKey.sa_key_banner != adConfig.saIDBanner){
                    adConfig.saIDBanner = adKey.sa_key_banner;
                    showStartAppAd();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*Show AdMob Banner*/
    private void showAdMobAd() {
        adMobBanner = new AdView(activity);
        adMobBanner.setAdSize(AdSize.SMART_BANNER);
        adMobBanner.setAdUnitId(adConfig.adMobIDBanner);
        adMobBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e(TAG,"Load AdMob banner fail");
                switch (adConfig.orderAdMob) {
                    case AdOrder.FIRST:
                        if (adConfig.orderFacebookAd == AdOrder.SECOND) {
                            showFacebookAd();
                        } else {
                            showStartAppAd();
                        }
                        break;
                    case AdOrder.SECOND:
                        if (adConfig.orderFacebookAd == AdOrder.THIRD) {
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
        fbBanner = new com.facebook.ads.AdView(activity, adConfig.fbIDBanner, com.facebook.ads
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
                Log.e(TAG,"Load FacebookAd banner fail");
                switch (adConfig.orderFacebookAd) {
                    case AdOrder.FIRST:
                        if (adConfig.orderStartAppAd == AdOrder.SECOND) {
                            showStartAppAd();
                        } else {
                            showAdMobAd();
                        }
                        break;
                    case AdOrder.SECOND:
                        if (adConfig.orderStartAppAd == AdOrder.THIRD) {
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
        StartAppSDK.init(activity, adConfig.saIDBanner, true);
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
                Log.e(TAG,"Load StartAppAd banner fail");
                switch (adConfig.orderStartAppAd) {
                    case AdOrder.FIRST:
                        if (adConfig.orderAdMob == AdOrder.SECOND) {
                            showAdMobAd();
                        } else {
                            showFacebookAd();
                        }
                        break;
                    case AdOrder.SECOND:
                        if (adConfig.orderAdMob == AdOrder.THIRD) {
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

        if (adConfig.orderAdMob == AdOrder.FIRST) {
            showAdMobAd();
        } else if (adConfig.orderFacebookAd == AdOrder.FIRST) {
            showFacebookAd();
        } else if (adConfig.orderStartAppAd == AdOrder.FIRST) {
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
