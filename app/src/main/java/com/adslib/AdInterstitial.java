package com.adslib;

import android.app.Activity;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

/**
 * Created by TrungTV on 12/26/2017.
 */

public class AdInterstitial {
    private static final String TAG = AdInterstitial.class.getSimpleName();

    private InterstitialAd fbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd adMobInterstitialAd;
    private StartAppAd startAppAd;
    private AdConfig adConfig;
    private Activity activity;
    private DatabaseReference mDatabase;

    public AdInterstitial(Activity activity, final AdConfig adConfig) {
        this.activity = activity;
        this.adConfig = adConfig;

        mDatabase = FirebaseDatabase.getInstance().getReference("ad_key");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdKey adKey = dataSnapshot.getValue(AdKey.class);
                if (adKey == null) {
                    return;
                }

                if (adKey.ad_mob_key_interstitial != adConfig.adMobIDInterstitial) {
                    adConfig.adMobIDInterstitial = adKey.ad_mob_key_interstitial;
                    showAdMobAd();
                    return;
                }
                if (adKey.fb_key_interstitial != adConfig.fbIDInterstitial) {
                    adConfig.fbIDInterstitial = adKey.fb_key_interstitial;
                    showFacebookAd();
                    return;
                }
                if (adKey.sa_key_interstitial != adConfig.saIDInterstitial) {
                    adConfig.saIDInterstitial = adKey.sa_key_interstitial;
                    showStartAppAd();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*Show AdMob Interstitial*/
    private void showAdMobAd() {
        adMobInterstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
        adMobInterstitialAd.setAdUnitId(adConfig.adMobIDInterstitial);
        adMobInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adMobInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e(TAG, "Load AdMob interstitial fail");
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

    /*Show FacebookAd Interstitial*/
    private void showFacebookAd() {
        fbInterstitialAd = new InterstitialAd(activity, adConfig.fbIDInterstitial);
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
                Log.e(TAG, "Load FacebookAd interstitial fail");
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

    /*Show StartAppAd Interstitial*/
    private void showStartAppAd() {
        StartAppSDK.init(activity, adConfig.saIDInterstitial, true);
        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(activity);
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                startAppAd.showAd();
            }

            @Override
            public void onFailedToReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                Log.e(TAG, "Load StartAppAd interstitial fail");
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
                    default:
                        break;
                }
            }
        });

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
}
