package com.example.dirtsystemec;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Sound;
import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GameWorld {

    protected final int[] maxScore = {0,900,1580,2060,3110,3370,4270,4950,5430,6480};
    protected final int[] numBarrelLevel = {0,0,4,3,7,2,5,4,3,7,2};
    protected volatile int numberBarrel = 5;
    protected volatile long score = 0;
    protected volatile int level = 1;
    protected final static float bufferWidth = 1080, bufferHeight = 1920;
    protected Bitmap buffer;
    private final Canvas canvas;
    protected static boolean gameOverFlag = false;
    protected List<GameObject> listGameObject;
    private volatile boolean verifyAction = false;
    private long timeOfLastSound = 0;
    protected volatile List<GameObject> listBarrel ;
    protected PhysicsComponent bulldozer;
    protected  World world;
    protected GameObject gameObjectBulldozer;
    protected final Box physicalSize, screenSize, currentView;
    private final MyContactListener contactListener;
    private final TouchConsumer touchConsumer;
    private TouchHandler touchHandler;
    private int numFps;
    private final HandlerUI handlerUI;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    protected final Activity activity;
    protected boolean flagCollisionBarrel = false;
    protected volatile float speed = 7f,torque = 7f ;
    private TextDrawableComponent timerTex,numberBarrelText,textScore;
    protected volatile long startTime,currentTime,maxTime = 240000,timerPause = 0,timeResume = 0;
    protected float positionYBulldozer;
    protected boolean zeroBarrel = false;
    protected GameObject gameOver;


    public GameWorld(Box physicalSize, Box screenSize, Activity theActivity,HandlerUI handlerUI) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.activity = theActivity;
        this.handlerUI = handlerUI;
        this.buffer = Bitmap.createBitmap((int)bufferWidth,(int) bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World( 0.0f, 0.0f);  // gravity vector
        this.currentView = physicalSize;
        this.numFps = 0;
        this.startTime= System.currentTimeMillis();

        this.contactListener = new MyContactListener();
        world.setContactListener(contactListener);
        this.touchConsumer = new TouchConsumer(this);

        this.listGameObject = new ArrayList<>();
        this.listBarrel = new ArrayList<>();
        this.canvas = new Canvas(buffer);
        Rect src = new Rect();
        src.set(0,0,(int)bufferHeight,1080);
    }


    public synchronized void update(float elapsedTime) {
        GameObject gameObjectBulldozer = null;
        numFps++;

        if (!gameOverFlag) {
            positionYBulldozer = bulldozer.body.getPositionY();
            int direction = ((DynamicPositionComponent) bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
            if (positionYBulldozer > 19.9f && direction == 1) {
                rotationBulldozer(-8f, positionYBulldozer, this, -1, activity);
                verifyAction = false;
            } else if (positionYBulldozer < -19.9f && direction == -1) {
                rotationBulldozer(-8f, positionYBulldozer, this, 1, activity);
                verifyAction = false;
            }

            /* update Timer */
            if (numFps == 10) {
                if (timeResume != 0 && timerPause != 0) {
                    startTime = startTime + (timeResume - timerPause);
                    timeResume = 0;
                    timerPause = 0;
                }
                if (currentTime >= 0) {
                    currentTime = maxTime - (System.currentTimeMillis() - startTime);
                    timerTex.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(currentTime),
                            TimeUnit.MILLISECONDS.toSeconds(currentTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTime))
                    ));
                }


                /* Update Score */
                if (!gameOverFlag)
                    score = (long) (score + (listBarrel.size() * 1.5f));
                if (score >= maxScore[maxScore.length - 1]-1) {
                    deleteBulldozer();
                    gameOverFlag = true;
                    gameOver = GameObject.createTextGameOver(0, -7.5f, this, "   WIN   ");

                }
                if (level < 10 && score >= maxScore[level]) {
                    level = level + 1;
                    numberBarrel=numBarrelLevel[level];
                    if(level==5)
                        speed=speed+3f;
                    saveGame();
                }

                numberBarrelText.setText(String.format("%02d", numberBarrel));
                textScore.setText("Score: " + score);
            }

            if (listBarrel.size() == 0) {
                verifyAction = false;
            }

            /*    Inizio Fase AI   */
            if (numFps == 10) {
                if (!verifyAction) {
                    for (GameObject gameObject : listGameObject) {
                        if (gameObject.name.equals("bulldozer")) {
                            gameObjectBulldozer = gameObject;
                            break;
                        }
                    }
                    if (gameObjectBulldozer != null) {
                        List<Component> components = gameObjectBulldozer.getComponent(ComponentType.AI);
                        if (components != null) {
                            for (Component component : components) {
                                Action action = ((FsmAIComponent) component).fsm.stepAndGetAction(this);
                                if (action != null) {
                                    if (action.equals(Action.burned)) {
                                        int result = searchBarrel();
                                        burnedBarrel(result, this, activity);
                                        verifyAction = true;
                                    } else if (action.equals(Action.waited)) {
                                        moveToCenter(this, activity);
                                    }
                                }
                            }
                        }
                    }
                }
                numFps = 0;
            }

            /*                     */

            if (currentTime<0 ||  zeroBarrel) {
                gameOverFlag = true;
                gameOver = GameObject.createTextGameOver(0, -7.5f, this, "GAME OVER");
            }


            /* Simulazione Fisica */
            world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

            /*                     */

            /* Fase Collisioni    */
            handleCollisions(contactListener.getCollisions());

            /*                  */
            /* update Timer */
            for (Input.TouchEvent event : touchHandler.getTouchEvents())
                touchConsumer.consumeTouchEvent(event);
        }
        else {
            deleteBulldozer();
            for (Input.TouchEvent event : touchHandler.getTouchEvents())
                touchConsumer.consumeTouchEvent(event);
        }

    }


    public synchronized void render() {
        canvas.drawARGB(100,126,193,243);

        for(GameObject gameObject: listGameObject){
            List<Component> components = gameObject.getComponent(ComponentType.Drawable);
            if(components != null){
                for (Component component: components) {
                    ((DrawableComponent) component).draw(buffer);
                }
            }
        }


    }


    public synchronized void addGameObject(GameObject obj) {

        if(obj.name!=null && obj.name.equals("bulldozer")) {
            gameObjectBulldozer = obj;
            listGameObject.add(obj);
            for (Component psh : obj.getComponent(ComponentType.Physics)) {
                if (((PhysicsComponent)psh).name.equals("chassis")){
                    bulldozer=(PhysicsComponent)psh;
                }
            }

        } else if(obj.name!=null && obj.name.equals("barrel")){

            listGameObject.add(0,obj);
        } else if (obj.name!= null && obj.name.equals("timer")){

            timerTex=(TextDrawableComponent)obj.getComponent(ComponentType.Drawable).get(0);
            listGameObject.add(obj);

        } else if (obj.name!= null && obj.name.equals("numberBarrel")){

            numberBarrelText=(TextDrawableComponent)obj.getComponent(ComponentType.Drawable).get(0);
            listGameObject.add(obj);

        } else if(obj.name!= null && obj.name.equals("textScore")){

            textScore = (TextDrawableComponent)obj.getComponent(ComponentType.Drawable).get(0);
            listGameObject.add(obj);

        } else {
            listGameObject.add(obj);
        }
    }


    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {

            if (event.a.name.equals("barrel")) {
                if (!listBarrel.contains((GameObject) event.a.owner)) {
                    flagCollisionBarrel = false;
                    listBarrel.add((GameObject) event.a.owner);
                    handleSoundCollisions(event);
                    if (event.b.name.equals("incinerator")) handleDeleteBarrel(event);
                } else if (event.b.name.equals("incinerator")) {
                    handleSoundCollisions(event);
                    handleDeleteBarrel(event);
                } else {

                    handleSoundCollisions(event);
                }

            } else if (event.b.name.equals("barrel")) {

                if (!listBarrel.contains((GameObject) event.b.owner)) {
                    flagCollisionBarrel = false;
                    listBarrel.add((GameObject) event.b.owner);
                   handleSoundCollisions(event);
                    if (event.a.name.equals("incinerator")) handleDeleteBarrel(event);
                } else if (event.a.name.equals("incinerator")) {
                    handleSoundCollisions(event);
                    handleDeleteBarrel(event);
                } else {
                    handleSoundCollisions(event);
                }
            }
        }
    }


    private void handleSoundCollisions(Collision event){
        Sound sound = CollisionSounds.getSound(((GameObject)event.a.owner).name, ((GameObject)event.b.owner).name);

        if (sound!=null) {
            long currentTime = System.nanoTime();
            if (currentTime - timeOfLastSound > 500_000_000) {
                timeOfLastSound = currentTime;
                sound.play(MainActivity.volumeSoundEffect);
            }
        }
    }


    public void rotationBulldozer(float coordinateX, float coordinateY, GameWorld gameWorld, int direction, Activity context){
        GameObject gameObjectBulldozer = null;

        for(GameObject gameObject: listGameObject){
            if(gameObject.name.equals("bulldozer")){
                gameObjectBulldozer = gameObject;
                break;
            }
        }

        if(gameObjectBulldozer != null){
            List<Component> componentsAi = gameObjectBulldozer.getComponent(ComponentType.AI);
            List<Component> componentsPhysics = gameObjectBulldozer.getComponent(ComponentType.Physics);
            listGameObject.remove(gameObjectBulldozer);

            if(componentsPhysics != null){
                for (Component component : componentsPhysics){
                    world.destroyBody(((PhysicsComponent)component).body);
                    ((PhysicsComponent)component).body.setUserData(null);
                    ((PhysicsComponent)component).body.delete();
                    ((PhysicsComponent)component).body = null;
                }
            }
            GameObject.createBulldozer(coordinateX,coordinateY,gameWorld,direction,context,componentsAi,speed);
            verifyAction = false;
        }

    }


    private void deleteBulldozer() {
        GameObject gameObjectBulldozer = null;

        for (GameObject gameObject : listGameObject) {
            if (gameObject.name.equals("bulldozer")) {
                gameObjectBulldozer = gameObject;
                break;
            }
        }

        if (gameObjectBulldozer != null) {
            List<Component> componentsAi = gameObjectBulldozer.getComponent(ComponentType.AI);
            List<Component> componentsPhysics = gameObjectBulldozer.getComponent(ComponentType.Physics);
            listGameObject.remove(gameObjectBulldozer);

            if (componentsPhysics != null) {
                for (Component component : componentsPhysics) {
                    world.destroyBody(((PhysicsComponent) component).body);
                    ((PhysicsComponent) component).body.setUserData(null);
                    ((PhysicsComponent) component).body.delete();
                    ((PhysicsComponent) component).body = null;
                }
            }
        }
    }


    public float toMetersX(float x) {
        return currentView.xmin + x * (currentView.width/screenSize.width); }


    public float toMetersY(float y) {
        return (currentView.ymin + (y * (currentView.height/ screenSize.height))); }


    public float toPixelsX(float x) {
        return (x-currentView.xmin)/currentView.width*bufferWidth; }


    public float toPixelsY(float y) {
        return ((y-currentView.ymin)/currentView.height)*bufferHeight;}


    public float toPixelsXLength(float x) {
        return x/currentView.width*bufferWidth;
    }


    public float toPixelsYLength(float y) {
        return y/currentView.height*bufferHeight;
    }


    public synchronized void setGravity(float x, float y) {
        world.setGravity(x, y);
    }


    @Override
    public void finalize() {
        world.delete();
    }


    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }


    public void eventTouch(float coordinateX, float coordinateY){
        if(!gameOverFlag) {
            if ((coordinateX <= 13.5f && coordinateX >= 9f) && (coordinateY >= 23f && coordinateY <= 26)) {
                handlerUI.sendEmptyMessage(0);
            }else if(!flagCollisionBarrel && ((coordinateY>=-22 && coordinateY <=21.6))){
                if (numberBarrel > 0) {
                    flagCollisionBarrel = true;
                    GameObject.createBarrel(13f, coordinateY, this);
                    numberBarrel = numberBarrel - 1;
                    numberBarrelText.setText(String.format("%02d", numberBarrel));
                }
            }
        }
        else{
            handlerUI.sendEmptyMessage(1);
            score=0;
            startTime= System.currentTimeMillis();
            listBarrel= new ArrayList<>();
            currentTime=0;
            numberBarrel=5;
            level=1;
            speed=7;
            saveGame();
        }

    }


    public void handleDeleteBarrel(Collision event){
        if(event.a.name.equals("incinerator")  && event.b.name.equals("barrel")){
            listGameObject.remove((GameObject) event.b.owner);
            listBarrel.remove((GameObject) event.b.owner);
            world.destroyBody(event.b.body);
            event.b.body.setUserData(null);
            event.b.body.delete();
            event.b.body = null;
            if(listBarrel.size() == 0){
                verifyAction = false;
            }
        }else if(event.a.name.equals("barrel")  && event.b.name.equals("incinerator")){
            listGameObject.remove((GameObject) event.a.owner);
            listBarrel.remove((GameObject) event.a.owner);
            world.destroyBody(event.a.body);
            event.a.body.setUserData(null);
            event.a.body.delete();
            event.a.body = null;
            if(listBarrel.size() == 0){
                verifyAction = false;
            }
        }
        if(numberBarrel ==0 && listBarrel.isEmpty())
            zeroBarrel=true;
    }


    protected int searchBarrel(){
        int contRight = 0;
        int contLeft = 0;
        float positionYBulldozer = bulldozer.body.getPositionY();
        if(listBarrel.size() == 0){
            return 0;
        }else{
            for (GameObject g: listBarrel) {
                List<Component> positionComponents = g.getComponent(ComponentType.Position);
                PositionComponent positionComponent = (PositionComponent) positionComponents.get(0);
                    if(positionYBulldozer > positionComponent.getCoordinateY() ){
                        contLeft = contLeft+1;

                    }else{
                        contRight= contRight+1;
                    }
            }
            if(contLeft > contRight){
                return -1;
            }else{
                return 1;
            }
        }
    }


    protected void burnedBarrel(int direction,GameWorld gameWorld,Activity context){
        int invert = ((DynamicPositionComponent) bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
        float positionYBulldozer = bulldozer.body.getPositionY();
        if(invert != direction){
            float positionXBulldozer = bulldozer.body.getPositionX();
            rotationBulldozer(positionXBulldozer,positionYBulldozer,gameWorld,direction,context);
        }

        for(Component c:bulldozer.owner.getComponent(ComponentType.Joint)){
            if(c instanceof RevoluteJointComponent) {
                if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                    ((RevoluteJointComponent) c).joint.setMotorSpeed(direction * speed);
                ((RevoluteJointComponent) c).joint.setMaxMotorTorque(torque);
            }
        }
    }


    protected void moveToCenter(GameWorld gameWorld, Activity context){
        float positionYBulldozer = bulldozer.body.getPositionY();
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) (gameObjectBulldozer).getComponent(ComponentType.Position).get(0);
        int direction = (dynamicPositionComponent.direction);

        if(direction == -1){
            if(positionYBulldozer < 0.1f){
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(30f);
                    }
                }
            }else{
                if (positionYBulldozer < -0.5f) {
                    rotationBulldozer(-8f, positionYBulldozer, this, 1, activity);
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(torque);
                        }
                    }
                }else{
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(-speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(torque);
                        }
                    }
                }
            }
        }else{
            if(positionYBulldozer >= -0.1f ){
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(30f);
                    }
                }
            }else{
                if (positionYBulldozer > 3) {
                    rotationBulldozer(-8f, positionYBulldozer, this, -1, activity);
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(-speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(torque);
                        }
                    }
                }else{
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(torque);
                        }
                    }
                }
            }
        }
    }


    protected void saveGame(){
        handlerUI.sendEmptyMessage(2);
    }


    public void setTimerPause(long time){
        this.timerPause=time;
    }


    public void setTimeResume(long time){
        this.timeResume=time;
    }


    public long getScore() {
        return score;
    }

}
