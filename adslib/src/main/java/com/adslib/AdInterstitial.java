package com.adslib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

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
    private AdCallBack adCallBack;
    private String placementId = "video"; // placementId có 4 loại : ddd,video,rewardedVideo,game.

    public AdInterstitial(Activity activity, final AdConfig adConfig) {
        this.activity = activity;
        this.adConfig = adConfig;
    }

    /*Load AdMob Interstitial*/
    private void loadAdModAd() {
        adMobInterstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
        adMobInterstitialAd.setAdUnitId(adConfig.adMobIDInterstitial);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(adConfig.adMobTestDeviceHash)
                .build();

        if (adMobInterstitialAd != null) {
            adMobInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e(TAG, "AdMob interstitial load fail");
                    switch (adConfig.orderAdMob) {
                        case AdOrder.FIRST:
                            if (adConfig.orderFacebookAd == AdOrder.SECOND) {
                                loadFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.SECOND) {
                                loadUnityAds();
                            } else {
                                loadStartAppAd();
                            }
                            break;
                        case AdOrder.SECOND:
                            if (adConfig.orderFacebookAd == AdOrder.THIRD) {
                                showFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.THIRD) {
                                loadUnityAds();
                            } else {
                                loadStartAppAd();
                            }
                            break;
                        case AdOrder.THIRD:
                            if (adConfig.orderFacebookAd == AdOrder.FOUR) {
                                loadFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.FOUR) {
                                loadUnityAds();
                            } else {
                                loadStartAppAd();
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        }
        adMobInterstitialAd.loadAd(adRequest);
    }

    /*Show AdMob Interstitial*/
    private void showAdMobAd() {
        if (adMobInterstitialAd != null && adMobInterstitialAd.isLoaded()) {
            adMobInterstitialAd.show();
            adMobInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    if (adCallBack != null)
                        adCallBack.onClose();
                }
            });

        } else {
            if (adCallBack != null)
                adCallBack.onClose();
        }
    }

    /*Load FacebookAd Interstitial*/
    private void loadFacebookAd() {
        fbInterstitialAd = new InterstitialAd(activity, adConfig.fbIDInterstitial);
        AdSettings.addTestDevice(adConfig.fbTestDeviceHash);
        if (fbInterstitialAd != null) {
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
                    Log.e(TAG, "FacebookAd interstitial load fail");
                    switch (adConfig.orderFacebookAd) {
                        case AdOrder.FIRST:
                            if (adConfig.orderStartAppAd == AdOrder.SECOND) {
                                loadStartAppAd();
                            } else if (adConfig.orderUnityAd == AdOrder.SECOND) {
                                loadUnityAds();
                            } else {
                                loadAdModAd();
                            }
                            break;
                        case AdOrder.SECOND:
                            if (adConfig.orderStartAppAd == AdOrder.THIRD) {
                                loadStartAppAd();
                            } else if (adConfig.orderUnityAd == AdOrder.THIRD) {
                                loadUnityAds();
                            } else {
                                loadAdModAd();
                            }
                            break;
                        case AdOrder.THIRD:
                            if (adConfig.orderStartAppAd == AdOrder.FOUR) {
                                loadFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.FOUR) {
                                loadUnityAds();
                            } else {
                                loadStartAppAd();
                            }
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Ad loaded callback
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

        } else {
            if (adCallBack != null)
                adCallBack.onClose();
        }
        fbInterstitialAd.loadAd();
    }

    /*Show FacebookAd Interstitial*/
    private void showFacebookAd() {
        if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
            fbInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    if (adCallBack != null)
                        adCallBack.onClose();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Ad loaded callback
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
            fbInterstitialAd.show();

        } else {
            if (adCallBack != null)
                adCallBack.onClose();
        }
    }


    /*Load StartAppAd Interstitial*/
    private void loadStartAppAd() {
        StartAppSDK.init(activity, adConfig.saIDInterstitial, true);
        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(activity);
        if (startAppAd != null) {
            startAppAd.loadAd(new AdEventListener() {
                @Override
                public void onReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                    // Interstitial receive callback
                }

                @Override
                public void onFailedToReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                    Log.e(TAG, "StartAppAd interstitial load fail");
                    switch (adConfig.orderStartAppAd) {
                        case AdOrder.FIRST:
                            if (adConfig.orderAdMob == AdOrder.SECOND) {
                                loadAdModAd();
                            } else if (adConfig.orderUnityAd == AdOrder.SECOND) {
                                loadUnityAds();
                            } else {
                                loadFacebookAd();
                            }
                            break;
                        case AdOrder.SECOND:
                            if (adConfig.orderAdMob == AdOrder.THIRD) {
                                loadAdModAd();
                            } else if (adConfig.orderUnityAd == AdOrder.THIRD) {
                                loadUnityAds();
                            } else {
                                loadFacebookAd();
                            }
                            break;
                        case AdOrder.THIRD:
                            if (adConfig.orderAdMob == AdOrder.FOUR) {
                                loadAdModAd();
                            } else if (adConfig.orderUnityAd == AdOrder.FOUR) {
                                loadUnityAds();
                            } else {
                                loadStartAppAd();
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        } else {
            if (adCallBack != null)
                adCallBack.onClose();
        }
    }

    /*Show StartAppAd Interstitial*/
    private void showStartAppAd() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd();
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                    adCallBack.onClose();
                }

                @Override
                public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {
                    Log.e(TAG, "adDisplayed");
                }

                @Override
                public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {
                    Log.e(TAG, "adClicked");
                }

                @Override
                public void adNotDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {
                    Log.e(TAG, "adNotDisplayed");
                }
            });
        } else {
            if (adCallBack != null)
                adCallBack.onClose();
        }

    }

    /*Load Unity Ads Interstitial*/
    private void loadUnityAds() {
        UnityAds.initialize(activity, adConfig.unityGameId, null, true);
        UnityAds.setListener(new UnityAdsListener());
    }

    /*Show Unity Ads Interstitial*/
    private void showUnityAds() {
        if (UnityAds.isReady(placementId)) {
            UnityAds.show(activity, placementId);
        }
    }

    /*Load Ad*/
    public void loadAd() {
        if (adConfig.orderAdMob == AdOrder.FIRST) {
            loadAdModAd();
        } else if (adConfig.orderFacebookAd == AdOrder.FIRST) {
            loadFacebookAd();
        } else if (adConfig.orderStartAppAd == AdOrder.FIRST) {
            loadStartAppAd();
        } else if (adConfig.orderUnityAd == AdOrder.FIRST) {
            loadUnityAds();
        }
    }

    /*Show Ad*/
    public void showAd(AdCallBack adCallBack) {
        this.adCallBack = adCallBack;
        if (adMobInterstitialAd != null && adMobInterstitialAd.isLoaded())
            showAdMobAd();
        else if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded())
            showFacebookAd();
        else if (startAppAd != null && startAppAd.isReady())
            showStartAppAd();
        else if (UnityAds.isReady(placementId))
            showUnityAds();
        else if (adCallBack != null)
            adCallBack.onClose();
    }

    public void onResume() {
        if (startAppAd != null)
            startAppAd.onResume();
    }

    public void onDestroy() {
        if (fbInterstitialAd != null)
            fbInterstitialAd.destroy();
    }

    public void onPause() {
        if (startAppAd != null)
            startAppAd.onPause();
    }

    private class UnityAdsListener implements IUnityAdsListener {
        @Override
        public void onUnityAdsReady(String s) {
            Log.e(TAG, "onUnityAdsReady " + s);
        }

        @Override
        public void onUnityAdsStart(String s) {
            Log.e(TAG, "onUnityAdsStart");
        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            Log.e(TAG, "onUnityAdsFinish");
            if (adCallBack != null)
                adCallBack.onClose();
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            Log.e(TAG, "UnityAds interstitial load fail " + s);
            switch (adConfig.orderUnityAd) {
                case AdOrder.FIRST:
                    if (adConfig.orderStartAppAd == AdOrder.SECOND) {
                        showStartAppAd();
                    } else if (adConfig.orderFacebookAd == AdOrder.SECOND) {
                        showFacebookAd();
                    } else {
                        showAdMobAd();
                    }
                    break;
                case AdOrder.SECOND:
                    if (adConfig.orderStartAppAd == AdOrder.THIRD) {
                        showStartAppAd();
                    } else if (adConfig.orderFacebookAd == AdOrder.THIRD) {
                        showFacebookAd();
                    } else {
                        showAdMobAd();
                    }
                    break;
                case AdOrder.THIRD:
                    if (adConfig.orderStartAppAd == AdOrder.FOUR) {
                        showStartAppAd();
                    } else if (adConfig.orderFacebookAd == AdOrder.FOUR) {
                        showFacebookAd();
                    } else {
                        showAdMobAd();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
