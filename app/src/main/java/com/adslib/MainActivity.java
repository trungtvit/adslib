package com.adslib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    AdView adView;
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

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ad_key");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdKey adKey = dataSnapshot.getValue(AdKey.class);
//                if (adKey == null) {
//                    Log.e("MainActivity", "Key data is null!");
//                    return;
//                }
                Log.e("OKMEN",adKey.fb_key_banner);
                Log.e("OKMEN1",adKey.ad_mob_key_interstitial);
                Log.e("OKMEN2",adKey.ad_mob_key_banner);
                text.setText(adKey.fb_key_banner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        showBanner();
        showInterstitial();


    }

    private void showBanner() {
        AdConfig bannerConfig = new AdConfig();
        AdOrder adOrder = new AdOrder();
        bannerConfig.adMobIDBanner = "ca-app-pub-3940256099942544/6300978111";
        bannerConfig.fbIDBanner = "YOUR_PLACEMENT_ID";
        bannerConfig.saIDBanner = "";
        bannerConfig.adMobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fbTestDeviceHash = "dabda7d8ff5085fba05298aeb0155229";
        adOrder.setOrderAdMob(AdOrder.FIRST);
        adOrder.setOrderFacebookAd(AdOrder.SECOND);
        adOrder.setOrderStartAppAd(AdOrder.THIRD);
        bannerConfig.orderAd = adOrder;
        banner = new AdBanner(this, bannerConfig, lnAds);
        banner.showAd();
    }

    private void showInterstitial() {
        AdConfig bannerConfig = new AdConfig();
        AdOrder adOrder = new AdOrder();
        bannerConfig.adMobIDInterstitial = "ca-app-pub-3940256099942544/1033173712";
        bannerConfig.fbIDInterstitial = "YOUR_PLACEMENT_ID";
        bannerConfig.saIDInterstitial = "Your App ID";
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
        if (banner != null) {
            banner.onBackPressed();
        }
        super.onBackPressed();
    }
}
