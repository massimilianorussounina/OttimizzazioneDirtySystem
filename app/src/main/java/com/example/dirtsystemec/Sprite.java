package com.example.dirtsystemec;

import android.graphics.Canvas;

public abstract class Sprite {

    protected int currentAnimation;
    protected long lastValue;
    protected final float coordinate_x;
    protected final float coordinate_y;
    protected final SpriteSheet spriteSheet;
    protected final Canvas canvas;
    protected final GameWorld gameWorld;

    public Sprite(GameWorld gw, SpriteSheet spriteSheet, float coordinateX, float coordinateY, float width, float height, int widthFrame,
                  int heightFrame, int delay, int numberOfAnimations, long lastValue){
        this.canvas = new Canvas(gw.buffer);
        this.gameWorld = gw;
        this.spriteSheet = spriteSheet;
        this.lastValue = lastValue;
        this.currentAnimation = 0;
        this.coordinate_x = gw.toPixelsX(coordinateX);
        this.coordinate_y = gw.toPixelsY(coordinateY);
        float screen_semi_width = gw.toPixelsXLength(width) / 2;
        float screen_semi_height = gw.toPixelsYLength(height) / 2;
        this.spriteSheet.setFrameSize(widthFrame,heightFrame, screen_semi_width, screen_semi_height);
        for(int i = 0;i<numberOfAnimations;i++){
            this.spriteSheet.setAnimation(i,delay);
        }
    }


    public abstract void draw(long currentTimeStamp);

}


class FireSprite extends Sprite {

    public FireSprite(GameWorld gameWorld, SpriteSheet spritesheet, float coordinateX, float coordinateY, float width, float height, int widthFrame,
                      int heightFrame, int delay, int numberOfAnimations) {
        super(gameWorld, spritesheet, coordinateX, coordinateY, width, height, widthFrame, heightFrame, delay, numberOfAnimations,System.currentTimeMillis());
    }


    @Override
    public void draw(long currentValue) {
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        if (currentValue - lastValue > spriteSheet.getDelay()[currentAnimation]) {
            if (currentAnimation >= spriteSheet.getDelay().length - 1) {
                currentAnimation = 0;
            } else {
                currentAnimation = currentAnimation + 1;
            }
            lastValue = currentValue;
        }
        spriteSheet.drawAnimation(canvas, currentAnimation, 0, coordinate_x, coordinate_y);
        canvas.restore();
    }

}

class SeaSprite extends Sprite{

    public SeaSprite(GameWorld gameWorld, SpriteSheet spritesheet, float coordinateX, float coordinateY, float width, float height, int widthFrame,
                     int heightFrame, int delay, int numberOfAnimations){
        super(gameWorld,spritesheet,coordinateX,coordinateY,width,height,widthFrame,heightFrame,delay,numberOfAnimations,System.currentTimeMillis());
    }


    @Override
    public void draw(long currentValue){
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);

        if(currentValue-lastValue > spriteSheet.getDelay()[currentAnimation]){
            if(currentAnimation >= spriteSheet.getDelay().length-1){
                currentAnimation = 0;
            }else{
                currentAnimation = currentAnimation + 1;
            }
            lastValue = currentValue;
        }

        spriteSheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
        canvas.restore();
    }

}

class ScoreSprite extends Sprite{

    public ScoreSprite(GameWorld gameWorld, SpriteSheet spritesheet, float coordinateX, float coordinateY, float width, float height, int widthFrame,
                       int heightFrame, int delay, int numberOfAnimations, int lastValue){
        super(gameWorld,spritesheet,coordinateX,coordinateY,width,height,widthFrame,heightFrame,delay,numberOfAnimations,lastValue);
        currentAnimation=0;
    }


    @Override
    public void draw(long currentValue){
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);

        if(currentAnimation < (gameWorld.level-1)){
            spriteSheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
            currentAnimation = currentAnimation + 1;
        }
        spriteSheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
        canvas.restore();
    }

}


