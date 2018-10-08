package com.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.adslib.AdConfig;
import com.adslib.AdInterstitial;

public class TestActivity extends AppCompatActivity {
    AdConfig adConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adConfig = getIntent().getParcelableExtra("KEY");
        AdInterstitial interstitial = new AdInterstitial(TestActivity.this, adConfig, true);
        interstitial.showFacebookAd();
    }
}
