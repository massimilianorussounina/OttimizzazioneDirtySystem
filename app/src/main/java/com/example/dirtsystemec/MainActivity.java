package com.example.dirtsystemec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.impl.AndroidAudio;
import com.badlogic.androidgames.framework.impl.MultiTouchHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final float coordinateXMin = -13.5f, coordinateXMax = 13.5f, coordinateYMin = -24f, coordinateYMax = 24f;
    public static String TAG;
    private AndroidFastRenderView renderView;
    private int currentApiVersion;
    private Music bulldozerMusic,backgroundMusic;
    private GameWorld gw;
    public static float  volumeMusic=0.8f, volumeSoundEffect=0.5f;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private boolean flagStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

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


        sharedPref = this.getPreferences(this.MODE_PRIVATE);
        editor = sharedPref.edit();
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            String flagString = bundle.getString("FLAG_START");
            flagStart = Boolean.parseBoolean(flagString);

        }


        HandlerUI handlerUI = new HandlerUI(this);

        /* Initialization Audio */

        Audio audio = new AndroidAudio(this);
        CollisionSounds.init(audio);
        bulldozerMusic = audio.newMusic("soundTractor.mp3");
        bulldozerMusic.play();
        bulldozerMusic.setLooping(true);
        bulldozerMusic.setVolume(volumeSoundEffect);
        backgroundMusic = audio.newMusic("soundtrack.mp3");
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(volumeMusic);



        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Box physicalSize = new Box(coordinateXMin, coordinateYMin, coordinateXMax, coordinateYMax);
        Box screenSize   = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);
        gw = new GameWorld(physicalSize, screenSize, this, handlerUI);
        Log.e("FLAG",String.valueOf(flagStart));
        if(flagStart) {
            loadData();
        }
        gw.setGravity(-10,0);

        GameObject.createTimer(11,-2.7f,gw);

        GameObject.createEnclosure(coordinateXMax,coordinateXMin,coordinateYMax,coordinateYMin,gw);

        GameObject.createBridge(-7.6f,0,-8.5f,4.5f,-2f,0f,-2f,1f,2f,1f
                ,-8.5f,-4.5f,2f,0f,2f,1f,-2f,1f,gw);


        GameObject.createSea(-13.4f,0f,-12.5f,0f,gw);
        GameObject.createGround(-11.5f,7.0f,gw);
        GameObject.createGround(-11.5f,-7.0f,gw);
        GameObject.createGround(-11.5f,-14.0f,gw);
        GameObject.createGround(-11.5f,14.0f,gw);
        GameObject.createGround(-11.5f,17.0f,gw);
        GameObject.createGround(-11.5f,-17.0f,gw);


        GameObject.createIncinerator(-12.5f,-22.7f,-10.1f,-22.7f,gw);
        GameObject.createIncinerator(-12.5f,22.7f,-10.1f,22.7f,gw);


        GameObject.createBulldozer(-6.6f,0,gw,-1,this,null,gw.speed);
        GameObject.createScoreBar(4.2f,22.5f,gw,0);
        GameObject.createTextNumberBarrel(9.2f,-23.45f,gw);
        GameObject.createTextScore(11.25f,-21f,gw);
        GameObject.createButtonPause(11f,22f,gw);
        GameObject.createBarrelIcon(11.7f,-22.8f,gw);

        renderView = new AndroidFastRenderView(this, gw);

        setContentView(renderView);
        MultiTouchHandler touchHandler = new MultiTouchHandler(renderView, 1, 1);
        gw.setTouchHandler(touchHandler);
        MyThread myThread = new MyThread(gw);
        myThread.start();

    }


    @Override
    public void onResume() {
        super.onResume();

        bulldozerMusic.setVolume(volumeSoundEffect);
        backgroundMusic.setVolume(volumeMusic);
        bulldozerMusic.play();
        backgroundMusic.play();
        gw.setTimeResume(System.currentTimeMillis());
        renderView.resume();
    }


    @Override
    public void onPause() {
        super.onPause();

        bulldozerMusic.pause();
        backgroundMusic.pause();
        gw.setTimerPause(System.currentTimeMillis());
        renderView.pause();
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


    public void showMenu(boolean flagResume){
        Intent i= new Intent(this,StartActivity.class);
        i.putExtra("FLAG", String.valueOf(flagResume));
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }


    public void SaveData(){
        Log.e("SAVE"," Save Coming.. ");
        editor.putLong("SCORE",gw.score);
        editor.putFloat("SPEED",gw.speed);
        editor.putInt("LEVEL",gw.level);
        editor.putLong("TIME",gw.currentTime);
        editor.putInt("NUM_BARREL",gw.numberBarrel);
        editor.putBoolean("GAME_OVER",GameWorld.gameOverFlag);
        Gson gson = new Gson();
        List<Float> barrelList = new ArrayList<Float>();
        for(GameObject obj: gw.listBarrel){
            try {

                barrelList.add(((PhysicsComponent)obj.getComponent(ComponentType.Physics).get(0)).body.getPositionY());
            } catch (Exception e) {
                Log.e("Barrel Delete",e.getMessage());
            }
        }
        String jsonText = gson.toJson(barrelList);
        editor.putString("BARREL", jsonText);
        editor.apply();
        editor.commit();

    }


    private void loadData(){
        float posX;
        if(!sharedPref.getBoolean("GAME_OVER",true)) {
            Log.e("SAVE SCORE :", "" + sharedPref.getLong("SCORE", 0));
            Log.e("SAVE LEVEL :", "" + sharedPref.getInt("LEVEL", 1));
            Log.e("SAVE TIME :", "" + sharedPref.getLong("TIME", 0));
            Log.e("SAVE N_BARREL :", "" + sharedPref.getInt("NUM_BARREL", 5));
            gw.score = sharedPref.getLong("SCORE", 0);
            gw.level = sharedPref.getInt("LEVEL", 1);
            gw.currentTime = sharedPref.getLong("TIME", 0);
            gw.speed = sharedPref.getFloat("SPEED", 7f);


            Gson gson = new Gson();
            String jsonText = sharedPref.getString("BARREL", null);
            Float[] posY = gson.fromJson(jsonText, Float[].class);

            for (int i = 0; i < posY.length; i++) {
                if (posY[i] >= -2.4f && posY[i] <= 2.4f) {
                    posX = -6.6f;
                } else {
                    posX = -5.6f;
                }
                GameObject.createBarrel(posX, posY[i].floatValue(), gw);

            }

            Log.e("GAME OVER", String.valueOf(GameWorld.gameOverFlag));
            gw.startTime = System.currentTimeMillis() - (gw.maxTime - gw.currentTime);
        }
    }

}