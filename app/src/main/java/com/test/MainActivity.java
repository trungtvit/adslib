package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adslib.AdBanner;
import com.adslib.AdCallBack;
import com.adslib.AdConfig;
import com.adslib.AdInterstitial;
import com.adslib.AdOrder;

public class MainActivity extends AppCompatActivity {

    RelativeLayout lnAds;
    AdBanner banner;
    AdInterstitial interstitial;
    Button btnShowAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnAds = findViewById(R.id.lnAds);
        btnShowAd = findViewById(R.id.btnShowAd);

        showBanner();
        initInterstitialAd();

        btnShowAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAd();
            }
        });

    }

    private void showBanner() {
        final AdConfig bannerConfig = new AdConfig();
        bannerConfig.adMobIDBanner = "ca-app-pub-3940256099942544/6300978111";
        bannerConfig.fbIDBanner = "YOUR_PLACEMENT_ID";
        bannerConfig.saIDBanner = "123";
        bannerConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        bannerConfig.orderAdMob = AdOrder.FIRST;
        bannerConfig.orderFacebookAd = AdOrder.SECOND;
        bannerConfig.orderStartAppAd = AdOrder.THIRD;
        banner = new AdBanner(this, bannerConfig, lnAds);
        banner.showAd();
    }

    private void initInterstitialAd() {
        final AdConfig interstitialConfig = new AdConfig();
        interstitialConfig.adMobIDInterstitial = "ca-app-pub-3940256099942544/1033173712";
        interstitialConfig.fbIDInterstitial = "YOUR_PLACEMENT_ID";
        interstitialConfig.saIDInterstitial = "Your App ID";
        interstitialConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        interstitialConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        interstitialConfig.unityGameId = "3409799";
        interstitialConfig.orderAdMob = AdOrder.SECOND;
        interstitialConfig.orderFacebookAd = AdOrder.FOUR;
        interstitialConfig.orderStartAppAd = AdOrder.THIRD;
        interstitialConfig.orderUnityAd = AdOrder.FIRST;
        interstitial = new AdInterstitial(this, interstitialConfig);
        interstitial.loadAd();

    }


    private void showInterstitialAd() {
        interstitial.showAd(new AdCallBack() {
            @Override
            public void onClose() {
                Toast.makeText(MainActivity.this, "CLOSED", Toast.LENGTH_SHORT).show();
                initInterstitialAd();
            }
        });
    }

    @Override
    public void onPause() {
        if (banner != null)
            banner.onPause();

        if (interstitial != null)
            interstitial.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null)
            banner.onResume();
        if (interstitial != null)
            interstitial.onResume();

    }

    @Override
    public void onDestroy() {
        if (banner != null)
            banner.onDestroy();
        if (interstitial != null)
            interstitial.onDestroy();
        super.onDestroy();
    }

}
