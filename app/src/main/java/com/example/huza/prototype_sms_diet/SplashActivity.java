package com.example.huza.prototype_sms_diet;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /// set LOGO's top margin due to STATUS_BAR
        int resourceId = -1;
        int statusbar_height = 0;
        resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusbar_height = getResources().getDimensionPixelSize(resourceId);
        }

        ImageView iv = findViewById(R.id.splash_logo);
        final TextView tv = findViewById(R.id.splash_HuZA);
        tv.setVisibility(View.GONE);

        int iv_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int iv_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

//        ConstraintLayout.LayoutParams iv_lp = new ConstraintLayout.LayoutParams(iv_width, iv_height);
//        iv_lp.topToTop = R.id.splash_container;
//        iv_lp.bottomToBottom = R.id.splash_container;
//        iv_lp.leftToLeft = R.id.splash_container;
//        iv_lp.rightToRight = R.id.splash_container;
//
//        //iv_lp.setMargins(0, 0, 0, statusbar_height*5);
//        /iv_lp.setMargins(0, 0, 0, statusbar_height);
//
//        iv.setLayoutParams(iv_lp);


        // ANIMATION START
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_transition_from_bottom);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv.startAnimation(animation);


    }

    public void goNext(){
        PreferenceManager preferenceManager = new PreferenceManager(this);

        if (preferenceManager.isFirstTimeLaunch()) {
            startActivity(new Intent(this, WelcomeActivity.class));
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_slide_out_to_left);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
