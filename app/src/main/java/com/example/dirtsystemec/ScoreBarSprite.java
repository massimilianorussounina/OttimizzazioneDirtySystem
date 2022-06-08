package com.example.dirtsystemec;

import android.graphics.Canvas;

public class ScoreBarSprite {

    private int currentAnimation;
    private long lastTimeStamp;
    private final float coordinate_x;
    private final float coordinate_y;
    private final SpriteSheet spritesheet;
    private final Canvas canvas;


    public ScoreBarSprite(GameWorld gw, SpriteSheet spritesheet, float x, float y){
        this.canvas = new Canvas(gw.buffer);
        this.spritesheet = spritesheet;
        this.lastTimeStamp = System.currentTimeMillis();
        this.currentAnimation = 0;
        this.coordinate_x = gw.toPixelsX(x);
        this.coordinate_y = gw.toPixelsY(y);
        float screen_semi_width = gw.toPixelsXLength(1.7f) / 2;
        float screen_semi_height = gw.toPixelsYLength(8.5f) / 2;
        spritesheet.setFrameSize(60,398, screen_semi_width, screen_semi_height);
        for(int i = 0;i<spritesheet.getNumberOfAnimations();i++){
            spritesheet.setAnimation(i,2000);
        }
    }


    public void draw(long currentTimeStamp){
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        if(currentTimeStamp-lastTimeStamp > spritesheet.getDelay()[currentAnimation]){
            if(currentAnimation >= spritesheet.getDelay().length-1){
                currentAnimation = 0;
            }else{
                currentAnimation = currentAnimation+1;
            }
            lastTimeStamp = currentTimeStamp;
        }
        spritesheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
        canvas.restore();
    }

}
