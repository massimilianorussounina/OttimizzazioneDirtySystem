package com.example.dirtsystemec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import java.util.List;


public abstract class DrawableComponent  extends Component{

    protected String name;
    protected final Rect src = new Rect();
    protected final RectF dest = new RectF();
    protected Bitmap bitmap;
    protected Canvas canvas;
    protected Paint paint = new Paint();
    protected String text;
    protected float width, height,density;
    protected float screenSemiWidth, screenSemiHeight;


    DrawableComponent(String name){
        this.name = name;
    }


    @Override
    public ComponentType type(){
        return ComponentType.Drawable;
    }


    public abstract void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle);


    public void draw(Bitmap buffer){

        float coordinate_x,coordinate_y,angle;
        GameWorld gameWorld;
        Box view;
        List<Component> physicsComponents = (List<Component>) owner.getComponent(ComponentType.Physics);
        PhysicsComponent physicsComponent = null;

        if(physicsComponents != null) {
            for (Object c : physicsComponents) {
                physicsComponent = (PhysicsComponent) c;
                if (physicsComponent.name.compareTo(this.name) == 0) break;
            }
        }

        if (physicsComponent != null) {
             coordinate_x = physicsComponent.getBodyPositionX();
             coordinate_y = physicsComponent.getBodyPositionY();
             angle = physicsComponent.getBodyAngle();
             gameWorld = ((GameObject) owner).gameWorld;
             view = gameWorld.currentView;
            if (coordinate_x > view.xmin && coordinate_x < view.xmax &&
                    coordinate_y > view.ymin && coordinate_y < view.ymax) {
                // Screen position
                float screen_x = gameWorld.toPixelsX(coordinate_x),
                        screen_y = gameWorld.toPixelsY(coordinate_y);
                this.draw(buffer, screen_x, screen_y, angle);
            }
        } else {
            Object component = owner.getComponent(ComponentType.Position).get(0);
            if (component instanceof PositionComponent) {
                PositionComponent positionComponent = (PositionComponent) component;
                coordinate_x = positionComponent.getCoordinateX();
                coordinate_y = positionComponent.getCoordinateY();
                gameWorld = ((GameObject) owner).gameWorld;
                view = gameWorld.currentView;
                if (coordinate_x > view.xmin && coordinate_x < view.xmax &&
                        coordinate_y > view.ymin && coordinate_y < view.ymax) {
                    // Screen position
                    float screen_x = gameWorld.toPixelsX(coordinate_x),
                            screen_y = gameWorld.toPixelsY(coordinate_y);
                    this.draw(buffer, screen_x, screen_y, 1.5708f);
                }
            }
        }
    }

}


class RectDrawableComponent extends DrawableComponent{


    public RectDrawableComponent (String name,GameObject gameObject, float width, float height, int color){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = width;
        this.height = height;
        this.screenSemiWidth = gameWorld.toPixelsXLength(this.width)/2;
        this.screenSemiHeight = gameWorld.toPixelsYLength(this.height)/2;
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }


    @Override
    public void draw(Bitmap buffer, float coordinateX, float coordinateY, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinateX, coordinateY);
        canvas.drawRect(coordinateX- this.screenSemiWidth ,coordinateY- this.screenSemiHeight, coordinateX + this.screenSemiWidth, coordinateY + this.screenSemiHeight, this.paint);
        canvas.restore();
    }
}


class BitmapDrawableComponent extends DrawableComponent {


    BitmapDrawableComponent(String name,GameObject gameObject, float width, float height,
                            int drawable, int left, int top, int right, int bottom){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = width;
        this.height = height;
        screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(),drawable, bitmapFactory);
        src.set(left, top, right, bottom);
    }


    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        dest.left = coordinate_x - screenSemiWidth;
        dest.bottom = coordinate_y + screenSemiHeight;
        dest.right = coordinate_x + screenSemiWidth;
        dest.top = coordinate_y - screenSemiHeight;
        canvas.drawBitmap(bitmap, src, dest, null);
        canvas.restore();
    }
}


class SpriteDrawableComponent extends DrawableComponent {

    private final Sprite sprite;


    public SpriteDrawableComponent(String name,GameObject gameObject,Sprite sprite){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.sprite = sprite;
    }


    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        if (sprite instanceof ScoreSprite) {
            sprite.draw(((GameObject) owner).gameWorld.level);
        } else {
            sprite.draw(System.currentTimeMillis());
        }
    }
}


class TextDrawableComponent extends DrawableComponent{

    public TextDrawableComponent (String name, GameObject gameObject, String text, int color, Context context, int size){
        super(name);
        this.owner = gameObject;
        this.text=text;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(size);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/8bitfont.ttf");
        paint.setTypeface(typeface);
    }


    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        canvas.drawText(text, coordinate_x, coordinate_y, paint);
        canvas.restore();
    }


    public void setText(String text){
        this.text=text;
    }

}



