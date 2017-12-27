package com.adslib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout lnAds;
    AdBanner banner;
    AdInterstitial interstitial;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnAds = findViewById(R.id.lnAds);
        text = findViewById(R.id.text);

        showBanner();
//        showInterstitial();


    }

    private void showBanner() {
        AdConfig bannerConfig = new AdConfig();
        bannerConfig.adMobIDBanner = "ca-app-pub-3940256099942544/6300978111";
        bannerConfig.fbIDBanner = "YOUR_PLACEMENT_ID";
        bannerConfig.saIDBanner = "";
        bannerConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        bannerConfig.orderAdMob = AdOrder.FIRST;
        bannerConfig.orderFacebookAd = AdOrder.SECOND;
        bannerConfig.orderStartAppAd = AdOrder.THIRD;
        banner = new AdBanner(this, bannerConfig, lnAds);
        banner.showAd();
    }

    private void showInterstitial() {
        AdConfig bannerConfig = new AdConfig();
        bannerConfig.adMobIDInterstitial = "ca-app-pub-3940256099942544/1033173712";
        bannerConfig.fbIDInterstitial = "YOUR_PLACEMENT_ID";
        bannerConfig.saIDInterstitial = "Your App ID";
        bannerConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        bannerConfig.orderAdMob = AdOrder.FIRST;
        bannerConfig.orderFacebookAd = AdOrder.SECOND;
        bannerConfig.orderStartAppAd = AdOrder.THIRD;
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
        if (banner != null) {
            banner.onBackPressed();
        }
        super.onBackPressed();
    }
}
