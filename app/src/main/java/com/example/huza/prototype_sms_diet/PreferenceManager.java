package com.example.huza.prototype_sms_diet;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by 허씨네 on 2018-03-15.
 */

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SMS_DIET";

    public PreferenceManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean("IsFirstTimeLaunch", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        Toast.makeText(mContext, String.valueOf(pref.getBoolean("IsFirstTimeLaunch", true)), Toast.LENGTH_SHORT).show();
        return pref.getBoolean("IsFirstTimeLaunch", true);
    }

}
