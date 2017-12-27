package com.adslib;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by TrungTV on 12/26/2017.
 */

public class AdConfig {
    public String adMobIDBanner;
    public String fbIDBanner;
    public String saIDBanner;
    public String adMobIDInterstitial;
    public String fbIDInterstitial;
    public String saIDInterstitial;
    public AdOrder orderAd;
    public String adMobTestDeviceHash;
    public String fbTestDeviceHash;

}
