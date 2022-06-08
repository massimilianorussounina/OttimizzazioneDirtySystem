package com.example.dirtsystemec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class StartActivity extends Activity {

    private int currentApiVersion;
    public static String TAG;
    private Context context;
    protected boolean flagStart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TAG = getString(R.string.app_name);
        TAG = getString(R.string.app_name);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
        setContentView(R.layout.activity_start);
        context = this;
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            String flagString = bundle.getString("FLAG");
            flagStart = Boolean.parseBoolean(flagString);

        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/8bitfont.ttf");
        TextView textViewPowered = findViewById(R.id.textViewPowerBy);
        TextView textViewMusic = findViewById(R.id.textViewMusic);
        TextView textViewSoundEffect = findViewById(R.id.textViewSoundEffect);
        textViewPowered.setTypeface(typeface);
        textViewMusic.setTypeface(typeface);
        textViewSoundEffect.setTypeface(typeface);
        SeekBar seekBarMusic = findViewById(R.id.seekBarMusic);
        SeekBar seekBarSoundEffect = findViewById(R.id.seekBarSoundEffect);
        seekBarMusic.setMax(100);
        seekBarSoundEffect.setMax(100);
        seekBarMusic.setMin(0);
        seekBarSoundEffect.setMin(0);
        seekBarSoundEffect.setProgress((int)(MainActivity.volumeSoundEffect*100f));
        seekBarMusic.setProgress((int)(MainActivity.volumeMusic*100f));
        Button buttonResume = findViewById(R.id.buttonResume);
        Button buttonStart = findViewById(R.id.buttonStart);
        buttonResume.setTypeface(typeface);
        buttonStart.setTypeface(typeface);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    flagStart = true;
                    GameWorld.gameOverFlag=false;
                    Intent i= new Intent(context,MainActivity.class);
                    startActivity(i);
            }
        });
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("START"," ok");
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("FLAG_START", String.valueOf(true));
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);

            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("volume Music"," "+(float)progress/100f);
                MainActivity.volumeMusic=(float)progress/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarSoundEffect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("volume Effect"," "+(float)progress/100f);
                MainActivity.volumeSoundEffect=(float)progress/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        flagStart=true;

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");
    }

}