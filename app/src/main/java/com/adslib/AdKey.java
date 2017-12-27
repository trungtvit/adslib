package com.adslib;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by TrungDK on 12/26/2017.
 */
@IgnoreExtraProperties
public class AdKey {

    public String ad_mob_key_banner;
    public String fb_key_banner;
    public String sa_key_banner;
    public String ad_mob_key_interstitial;
    public String fb_key_interstitial;
    public String sa_key_interstitial;

    public AdKey() {
    }

    public AdKey(String ad_mob_key_banner, String fb_key_banner, String sa_key_banner, String ad_mob_key_interstitial, String fb_key_interstitial, String sa_key_interstitial) {
        this.ad_mob_key_banner = ad_mob_key_banner;
        this.fb_key_banner = fb_key_banner;
        this.sa_key_banner = sa_key_banner;
        this.ad_mob_key_interstitial = ad_mob_key_interstitial;
        this.fb_key_interstitial = fb_key_interstitial;
        this.sa_key_interstitial = sa_key_interstitial;
    }
}
