package com.adslib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    AdView adView;
    RelativeLayout lnAds;
    AdBanner banner;
    AdInterstitial interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnAds = findViewById(R.id.lnAds);
        showBanner();
//        showInterstitial();
    }

    private void showBanner() {
        AdConfig bannerConfig = new AdConfig();
        AdOrder adOrder = new AdOrder();
        bannerConfig.adMobID = "ca-app-pub-3940256099942544/6300978111";
        bannerConfig.fbID = "YOUR_PLACEMENT_ID";
        bannerConfig.saID = "";
        bannerConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        adOrder.setOrderAdMob(AdOrder.FIRST);
        adOrder.setOrderFacebookAd(AdOrder.SECOND);
        adOrder.setOrderStartAppAd(AdOrder.THIRD);
        bannerConfig.orderAd = adOrder;
        banner = new AdBanner(this, bannerConfig, lnAds);
        banner.showAd();
    }

    private void showInterstitial(){
        AdConfig bannerConfig = new AdConfig();
        AdOrder adOrder = new AdOrder();
        bannerConfig.adMobID = "ca-app-pub-3940256099942544/1033173712";
        bannerConfig.fbID = "YOUR_PLACEMENT_ID";
        bannerConfig.saID = "Your App ID";
        bannerConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        adOrder.setOrderAdMob(AdOrder.FIRST);
        adOrder.setOrderFacebookAd(AdOrder.SECOND);
        adOrder.setOrderStartAppAd(AdOrder.THIRD);
        bannerConfig.orderAd = adOrder;
        interstitial = new AdInterstitial(this, bannerConfig);
        interstitial.showAd();
    }

    @Override
    public void onPause() {
        if (banner != null) {
            banner.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (banner != null) {
            banner.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(banner != null) {
            banner.onBackPressed();
        }
        super.onBackPressed();
    }
}
