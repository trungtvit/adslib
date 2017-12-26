package com.adslib;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by TrungDK on 12/26/2017.
 */
@IgnoreExtraProperties
public class AdKey {

    public String ad_mob_key;
    public String fb_key;
    public String sa_key;

    public AdKey() {
    }

    public AdKey(String ad_mob_key, String fb_key, String sa_key) {
        this.ad_mob_key = ad_mob_key;
        this.fb_key = fb_key;
        this.sa_key = sa_key;
    }
}
