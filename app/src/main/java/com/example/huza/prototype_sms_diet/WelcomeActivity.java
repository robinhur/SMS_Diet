package com.example.huza.prototype_sms_diet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void btn_clicked(View v) {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.setFirstTimeLaunch(false);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
