package com.adslib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

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
    public AdConfig adConfig;
    private Activity activity;
    private AdCallBack adCallBack;
    private ProgressDialog progressDialog;
    private boolean isShowFacebook;

    public AdInterstitial(final Activity activity, final AdConfig adConfig, boolean isShowFacebook) {
        this.activity = activity;
        this.adConfig = adConfig;
        this.isShowFacebook = isShowFacebook;

        UnityAds.initialize(activity, adConfig.unityGameId, null, true);
        UnityAds.setListener(new UnityAdsListener());

        progressDialog = new ProgressDialog(activity, R.style.MyTheme);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setCancelable(true);

    }

    /*Show AdMob Interstitial*/
    private void showAdMobAd() {
        adConfig.adsType = AdConfig.adsType.AD_MOB;
        adMobInterstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
        adMobInterstitialAd.setAdUnitId(adConfig.adMobIDInterstitial);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(adConfig.adMobTestDeviceHash)
                .build();
        adMobInterstitialAd.loadAd(adRequest);

        if (adMobInterstitialAd != null) {
            adMobInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    adMobInterstitialAd.show();
                    dismissProgress();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    dismissProgress();
                    Log.e(TAG, "AdMob interstitial load fail");
                    switch (adConfig.orderAdMob) {
                        case AdOrder.FIRST:
                            if (adConfig.orderFacebookAd == AdOrder.SECOND) {
                                showFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.SECOND) {
                                showUnityAds();
                            } else {
                                showStartAppAd();
                            }
                            break;
                        case AdOrder.SECOND:
                            if (adConfig.orderFacebookAd == AdOrder.THIRD) {
                                showFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.THIRD) {
                                showUnityAds();
                            } else {
                                showStartAppAd();
                            }
                            break;
                        case AdOrder.THIRD:
                            if (adConfig.orderFacebookAd == AdOrder.FOUR) {
                                showFacebookAd();
                            } else if (adConfig.orderUnityAd == AdOrder.FOUR) {
                                showUnityAds();
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
                    dismissProgress();
                    if (adCallBack != null)
                        adCallBack.onClose();
                }
            });

        } else {
            dismissProgress();
            if (adCallBack != null)
                adCallBack.onClose();
        }
    }

    /*Show FacebookAd Interstitial*/
    public void showFacebookAd() {
        dismissProgress();
        adConfig.adsType = AdConfig.adsType.FACEBOOK;
        if (isShowFacebook) {
            showProgress();
            fbInterstitialAd = new InterstitialAd(activity, adConfig.fbIDInterstitial);
            AdSettings.addTestDevice(adConfig.fbTestDeviceHash);
            fbInterstitialAd.loadAd();
            if (fbInterstitialAd != null) {
                fbInterstitialAd.setAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        // Interstitial displayed callback
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        dismissProgress();
                        if (adCallBack != null)
                            adCallBack.onClose();
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        dismissProgress();
                        Log.e(TAG, "FacebookAd interstitial load fail");
                        switch (adConfig.orderFacebookAd) {
                            case AdOrder.FIRST:
                                if (adConfig.orderStartAppAd == AdOrder.SECOND) {
                                    showStartAppAd();
                                } else if (adConfig.orderUnityAd == AdOrder.SECOND) {
                                    showUnityAds();
                                } else {
                                    showAdMobAd();
                                }
                                break;
                            case AdOrder.SECOND:
                                if (adConfig.orderStartAppAd == AdOrder.THIRD) {
                                    showStartAppAd();
                                } else if (adConfig.orderUnityAd == AdOrder.THIRD) {
                                    showUnityAds();
                                } else {
                                    showAdMobAd();
                                }
                                break;
                            case AdOrder.THIRD:
                                if (adConfig.orderStartAppAd == AdOrder.FOUR) {
                                    showStartAppAd();
                                } else if (adConfig.orderUnityAd == AdOrder.FOUR) {
                                    showUnityAds();
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
                        dismissProgress();
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
                dismissProgress();
                if (adCallBack != null)
                    adCallBack.onClose();
            }
        } else {
            if (adCallBack != null)
                adCallBack.onClose();
        }
    }

    /*Show StartAppAd Interstitial*/
    private void showStartAppAd() {
        adConfig.adsType = AdConfig.adsType.START_APP;
        StartAppSDK.init(activity, adConfig.saIDInterstitial, true);
        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(activity);
        if (startAppAd != null) {
            startAppAd.loadAd(new AdEventListener() {
                @Override
                public void onReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                    startAppAd.showAd();
                    dismissProgress();
                }

                @Override
                public void onFailedToReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {
                    dismissProgress();
                    Log.e(TAG, "StartAppAd interstitial load fail");
                    switch (adConfig.orderStartAppAd) {
                        case AdOrder.FIRST:
                            if (adConfig.orderAdMob == AdOrder.SECOND) {
                                showAdMobAd();
                            } else if (adConfig.orderUnityAd == AdOrder.SECOND) {
                                showUnityAds();
                            } else {
                                showFacebookAd();
                            }
                            break;
                        case AdOrder.SECOND:
                            if (adConfig.orderAdMob == AdOrder.THIRD) {
                                showAdMobAd();
                            } else if (adConfig.orderUnityAd == AdOrder.THIRD) {
                                showUnityAds();
                            } else {
                                showFacebookAd();
                            }
                            break;
                        case AdOrder.THIRD:
                            if (adConfig.orderAdMob == AdOrder.FOUR) {
                                showAdMobAd();
                            } else if (adConfig.orderUnityAd == AdOrder.FOUR) {
                                showUnityAds();
                            } else {
                                showFacebookAd();
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                    adCallBack.onClose();
                    dismissProgress();
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
            dismissProgress();
            if (adCallBack != null)
                adCallBack.onClose();
        }

    }

    /*Show Unity ads Interstitial*/
    private void showUnityAds() {
        adConfig.adsType = AdConfig.adsType.UNITY_ADS;
        final String placementId = "video"; // placementId có 4 loại : ddd,video,rewardedVideo,game.
        if (UnityAds.isReady(placementId)) {
            UnityAds.show(activity, placementId);
        }


    }

    /*Show Ad*/
    public void showAd(AdCallBack adCallBack) {
        this.adCallBack = adCallBack;
        showProgress();
        if (adConfig.orderAdMob == AdOrder.FIRST) {
            showAdMobAd();
        } else if (adConfig.orderFacebookAd == AdOrder.FIRST) {
            showFacebookAd();
        } else if (adConfig.orderStartAppAd == AdOrder.FIRST) {
            showStartAppAd();
        } else if (adConfig.orderUnityAd == AdOrder.FIRST) {
            showUnityAds();
        }
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

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    private void dismissProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private class UnityAdsListener implements IUnityAdsListener {
        @Override
        public void onUnityAdsReady(String s) {
            Log.e(TAG, "onUnityAdsReady " + s);
            dismissProgress();
        }

        @Override
        public void onUnityAdsStart(String s) {
            Log.e(TAG, "onUnityAdsStart");
            dismissProgress();
        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            Log.e(TAG, "onUnityAdsFinish");
            dismissProgress();
            if (adCallBack != null)
                adCallBack.onClose();
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            dismissProgress();
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
