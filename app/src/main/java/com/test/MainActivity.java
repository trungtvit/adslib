package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslib.AdBanner;
import com.adslib.AdCallBack;
import com.adslib.AdConfig;
import com.adslib.AdInterstitial;
import com.adslib.AdKey;
import com.adslib.AdOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    RelativeLayout lnAds;
    AdBanner banner;
    AdInterstitial interstitial;
    Button btnShowAd;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnAds = findViewById(R.id.lnAds);
        btnShowAd = findViewById(R.id.btnShowAd);
        mDatabase = FirebaseDatabase.getInstance().getReference("ad_key");

//        showBanner();
//        showInterstitial();

        btnShowAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
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
        bannerConfig.orderAdMob = AdOrder.THIRD;
        bannerConfig.orderFacebookAd = AdOrder.SECOND;
        bannerConfig.orderStartAppAd = AdOrder.FIRST;
        banner = new AdBanner(this, bannerConfig, lnAds);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdKey adKey = dataSnapshot.getValue(AdKey.class);
                if (adKey == null) {
                    return;
                }

                if (adKey.ad_mob_key_banner != bannerConfig.adMobIDBanner) {
                    bannerConfig.adMobIDBanner = adKey.ad_mob_key_banner;
                    banner.showAd();
                    return;
                }
                if (adKey.fb_key_banner != bannerConfig.fbIDBanner) {
                    bannerConfig.fbIDBanner = adKey.fb_key_banner;
                    banner.showAd();
                    return;
                }
                if (adKey.sa_key_banner != bannerConfig.saIDBanner) {
                    bannerConfig.saIDBanner = adKey.sa_key_banner;
                    banner.showAd();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                banner.showAd();
            }
        });
    }

    private void showInterstitial() {
        final AdConfig interstitialConfig = new AdConfig();
        interstitialConfig.adMobIDInterstitial = "ca-app-pub-3940256099942544/1033173712";
        interstitialConfig.fbIDInterstitial = "YOUR_PLACEMENT_ID";
        interstitialConfig.saIDInterstitial = "Your App ID";
        interstitialConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        interstitialConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        interstitialConfig.orderAdMob = AdOrder.FIRST;
        interstitialConfig.orderFacebookAd = AdOrder.THIRD;
        interstitialConfig.orderStartAppAd = AdOrder.SECOND;
        interstitial = new AdInterstitial(this, interstitialConfig);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdKey adKey = dataSnapshot.getValue(AdKey.class);
                if (adKey == null) {
                    return;
                }

                if (adKey.ad_mob_key_interstitial != interstitialConfig.adMobIDInterstitial) {
                    interstitialConfig.adMobIDInterstitial = adKey.ad_mob_key_interstitial;
                    showAdInterstitial();
                    return;
                }
                if (adKey.fb_key_interstitial != interstitialConfig.fbIDInterstitial) {
                    interstitialConfig.fbIDInterstitial = adKey.fb_key_interstitial;
                    showAdInterstitial();
                    return;
                }
                if (adKey.sa_key_interstitial != interstitialConfig.saIDInterstitial) {
                    interstitialConfig.saIDInterstitial = adKey.sa_key_interstitial;
                    showAdInterstitial();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showAdInterstitial();
            }
        });
    }

    private void showAdInterstitial() {
        interstitial.showAd(new AdCallBack() {
            @Override
            public void onClose() {
                Toast.makeText(MainActivity.this, "CLOSED", Toast.LENGTH_SHORT).show();
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
