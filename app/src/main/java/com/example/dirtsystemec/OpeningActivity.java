package com.example.dirtsystemec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.eftimoff.androipathview.PathView;

public class OpeningActivity extends Activity {


    private View layout;
    private PathView logo;
    private TextView textViewAppName;
    private TextView poweredBy;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_activity);
        context = this;
        layout = findViewById(R.id.activity_opening);
        logo = findViewById(R.id.activity_opening_app_logo);
        textViewAppName = findViewById(R.id.activity_opening_app_name);
        poweredBy = findViewById(R.id.activity_opening_app_powered_by);
        initializeUserInterface();
    }



    private void initializeUserInterface() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        textViewAppName.animate().alpha(1).setDuration(2000);
        textViewAppName.animate().translationY(-175).setDuration(1500);
        poweredBy.animate().alpha(1).setDuration(2000);
        poweredBy.animate().translationY(-175).setDuration(2000);
        logo.setFillAfter(true);
        PathView.AnimatorBuilder animatorBuilder = logo.getPathAnimator();
        animatorBuilder.delay(100);
        animatorBuilder.duration(2500);
        animatorBuilder.interpolator(new AccelerateDecelerateInterpolator());
        animatorBuilder.start();
        animatorBuilder.listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
            @Override
            public void onAnimationEnd() {
                onPresentationEnd();
            }
        });
    }

    public void onPresentationEnd() {
        boolean b = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, StartActivity.class);

                startActivity(intent);
            }
        }, 1500);
    }

}