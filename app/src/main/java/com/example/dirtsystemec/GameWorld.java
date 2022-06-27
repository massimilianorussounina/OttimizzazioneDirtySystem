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
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
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
    //protected List<GameObject> listGameObject;
    protected Queue<GameObject> listGameObject;
    private volatile boolean verifyAction = false;
    private long timeOfLastSound = 0;
    //protected volatile List<GameObject> listBarrel ;
    protected volatile Queue<GameObject> listBarrel ;
    protected PhysicsComponent bulldozer;
    protected  World world;
    protected GameObject gameObjectBulldozer;
    protected final Box physicalSize, screenSize, currentView;
    private final MyContactListener contactListener;
    private final TouchConsumer touchConsumer;
    protected TouchHandler touchHandler;
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
    protected long  scoreTime ,lastScoreTime=0;


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

        //this.listGameObject = new ArrayList<>();
        this.listGameObject = new LinkedBlockingDeque<>();
        this.listBarrel = new LinkedBlockingDeque<>();
        this.canvas = new Canvas(buffer);
        Rect src = new Rect();
        src.set(0,0,(int)bufferHeight,1080);
        this.lastScoreTime=0;
    }


    public static synchronized void update(float elapsedTime,GameWorld gameWorld) {
        GameObject gameObjectBulldozer = null;
        gameWorld.numFps++;

        if (!gameOverFlag) {
            gameWorld.positionYBulldozer = gameWorld.bulldozer.body.getPositionY();
            int direction = ((DynamicPositionComponent) gameWorld.bulldozer.owner.components.get(ComponentType.Position.hashCode()).get(0)).direction;
            if (gameWorld.positionYBulldozer > 19.9f && direction == 1) {
                rotationBulldozer(-8f, gameWorld.positionYBulldozer, gameWorld, -1, gameWorld.activity);
                gameWorld.verifyAction = false;
            } else if (gameWorld.positionYBulldozer < -19.9f && direction == -1) {
                rotationBulldozer(-8f, gameWorld.positionYBulldozer, gameWorld, 1, gameWorld.activity);
                gameWorld.verifyAction = false;
            }

            /* update Timer */
            if (gameWorld.numFps == 10) {
                if (gameWorld.timeResume != 0 && gameWorld.timerPause != 0) {
                    gameWorld.startTime =gameWorld.startTime + (gameWorld.timeResume - gameWorld.timerPause);
                    gameWorld.timeResume = 0;
                    gameWorld.timerPause = 0;
                }
                if (gameWorld.currentTime >= 0) {
                    gameWorld.currentTime = gameWorld.maxTime - (System.currentTimeMillis() - gameWorld.startTime);
                    gameWorld.timerTex.text=String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(gameWorld.currentTime),
                            TimeUnit.MILLISECONDS.toSeconds(gameWorld.currentTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(gameWorld.currentTime))
                    );
                }


                /* Update Score */
                if (!gameOverFlag) {
                    gameWorld.scoreTime = System.currentTimeMillis();
                    if (gameWorld.scoreTime >= gameWorld.lastScoreTime) {
                        gameWorld.lastScoreTime = gameWorld.scoreTime+1000;
                        gameWorld.score = (long) (gameWorld.score +( (gameWorld.listBarrel.size() * 1.5f))*6);

                    }
                }
                if (gameWorld.score >= gameWorld.maxScore[gameWorld.maxScore.length - 1]-1) {
                    deleteBulldozer(gameWorld);
                    gameOverFlag = true;
                    gameWorld.gameOver = GameObject.createTextGameOver(0, -7.5f, gameWorld, "   WIN   ");

                }
                if (gameWorld.level < 10 && gameWorld.score >= gameWorld.maxScore[gameWorld.level]) {
                    gameWorld.level = gameWorld.level + 1;
                    gameWorld.numberBarrel=gameWorld.numBarrelLevel[gameWorld.level];
                    if(gameWorld.level==5)
                        gameWorld.speed=gameWorld.speed+3f;
                    gameWorld.saveGame();
                }

                gameWorld.numberBarrelText.text=String.format("%02d", gameWorld.numberBarrel);
                gameWorld.textScore.text="Score: " + gameWorld.score;
            }

            if (gameWorld.listBarrel.size() == 0) {
                gameWorld.verifyAction = false;
            }

            /*    Inizio Fase AI   */
            if (gameWorld.numFps == 10) {
                if (!gameWorld.verifyAction) {
                    for (GameObject gameObject : gameWorld.listGameObject) {
                        if (gameObject.name.equals("bulldozer")) {
                            gameObjectBulldozer = gameObject;
                            break;
                        }
                    }
                    if (gameObjectBulldozer != null) {
                        List<Component> components = gameObjectBulldozer.components.get(ComponentType.AI.hashCode());
                        if (components != null) {
                            for (Component component : components) {
                                Action action = ((FsmAIComponent) component).fsm.stepAndGetAction(gameWorld,((FsmAIComponent) component).fsm);
                                if (action != null) {
                                    if (action.equals(Action.burned)) {
                                        int result = searchBarrel(gameWorld);
                                        burnedBarrel(result, gameWorld, gameWorld.activity);
                                        gameWorld.verifyAction = true;
                                    } else if (action.equals(Action.waited)) {
                                        moveToCenter(gameWorld, gameWorld.activity);
                                    }
                                }
                            }
                        }
                    }
                }
                gameWorld.numFps = 0;
            }

            /*                     */

            if (gameWorld.currentTime<0 || gameWorld.zeroBarrel) {
                gameOverFlag = true;
                gameWorld.gameOver = GameObject.createTextGameOver(0, -7.5f, gameWorld, "GAME OVER");
            }


            /* Simulazione Fisica */
            gameWorld.world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

            /*                     */

            /* Fase Collisioni    */
            handleCollisions(MyContactListener.getCollisions(gameWorld.contactListener),gameWorld);

            /*                  */
            /* update Timer */
            for (Input.TouchEvent event : gameWorld.touchHandler.getTouchEvents())
                gameWorld.touchConsumer.consumeTouchEvent(event);
        }
        else {
            deleteBulldozer(gameWorld);
            for (Input.TouchEvent event : gameWorld.touchHandler.getTouchEvents())
                gameWorld.touchConsumer.consumeTouchEvent(event);
        }

    }


    public static synchronized void render(GameWorld gameWorld) {
        gameWorld.canvas.drawARGB(100,126,193,243);

        for(GameObject gameObject: gameWorld.listGameObject){
            List<Component> components = gameObject.components.get(ComponentType.Drawable.hashCode());
            if(components != null){
                for (Component component: components) {
                    ((DrawableComponent) component).draw(gameWorld.buffer);
                }
            }
        }


    }


    public static synchronized void addGameObject(GameObject obj,GameWorld gameWorld) {

        if(obj.name!=null && obj.name.equals("bulldozer")) {
            gameWorld.gameObjectBulldozer = obj;
            gameWorld.listGameObject.add(obj);
            for (Component psh : obj.components.get(ComponentType.Physics.hashCode())) {
                if (((PhysicsComponent)psh).name.equals("chassis")){
                    gameWorld.bulldozer=(PhysicsComponent)psh;
                }
            }

        } else if(obj.name!=null && obj.name.equals("barrel")){

            //listGameObject.add(0,obj);
            gameWorld.listGameObject.add(obj);
        } else if (obj.name!= null && obj.name.equals("timer")){

            gameWorld.timerTex=(TextDrawableComponent)obj.components.get(ComponentType.Drawable.hashCode()).get(0);
            gameWorld.listGameObject.add(obj);

        } else if (obj.name!= null && obj.name.equals("numberBarrel")){

            gameWorld.numberBarrelText=(TextDrawableComponent)obj.components.get(ComponentType.Drawable.hashCode()).get(0);
            gameWorld.listGameObject.add(obj);

        } else if(obj.name!= null && obj.name.equals("textScore")){

            gameWorld.textScore = (TextDrawableComponent)obj.components.get(ComponentType.Drawable.hashCode()).get(0);
            gameWorld.listGameObject.add(obj);

        } else {
            gameWorld.listGameObject.add(obj);
        }
    }


    private static void handleCollisions(Collection<Collision> collisions,GameWorld gameWorld) {
        for (Collision event: collisions) {

            if (event.a.name.equals("barrel")) {
                if (!gameWorld.listBarrel.contains((GameObject) event.a.owner)) {
                    gameWorld.flagCollisionBarrel = false;
                    gameWorld.listBarrel.add((GameObject) event.a.owner);
                    handleSoundCollisions(event,gameWorld);
                    if (event.b.name.equals("incinerator")) handleDeleteBarrel(event,gameWorld);
                } else if (event.b.name.equals("incinerator")) {
                    handleSoundCollisions(event,gameWorld);
                    handleDeleteBarrel(event,gameWorld);
                } else {

                    handleSoundCollisions(event,gameWorld);
                }

            } else if (event.b.name.equals("barrel")) {

                if (!gameWorld.listBarrel.contains((GameObject) event.b.owner)) {
                    gameWorld.flagCollisionBarrel = false;
                    gameWorld.listBarrel.add((GameObject) event.b.owner);
                   handleSoundCollisions(event,gameWorld);
                    if (event.a.name.equals("incinerator")) handleDeleteBarrel(event,gameWorld);
                } else if (event.a.name.equals("incinerator")) {
                    handleSoundCollisions(event,gameWorld);
                    handleDeleteBarrel(event,gameWorld);
                } else {
                    handleSoundCollisions(event,gameWorld);
                }
            }
        }
    }


    private static void handleSoundCollisions(Collision event,GameWorld gameWorld){
        Sound sound = CollisionSounds.getSound(((GameObject)event.a.owner).name, ((GameObject)event.b.owner).name);

        if (sound!=null) {
            long currentTime = System.nanoTime();
            if (currentTime - gameWorld.timeOfLastSound > 500_000_000) {
                gameWorld.timeOfLastSound = currentTime;
                sound.play(MainActivity.volumeSoundEffect);
            }
        }
    }


    public static void rotationBulldozer(float coordinateX, float coordinateY, GameWorld gameWorld, int direction, Activity context){
        GameObject gameObjectBulldozer = null;

        for(GameObject gameObject: gameWorld.listGameObject){
            if(gameObject.name.equals("bulldozer")){
                gameObjectBulldozer = gameObject;
                break;
            }
        }

        if(gameObjectBulldozer != null){
            List<Component> componentsAi = gameObjectBulldozer.components.get(ComponentType.AI.hashCode());
            List<Component> componentsPhysics = gameObjectBulldozer.components.get(ComponentType.Physics.hashCode());
            gameWorld.listGameObject.remove(gameObjectBulldozer);

            if(componentsPhysics != null){
                for (Component component : componentsPhysics){
                    gameWorld.world.destroyBody(((PhysicsComponent)component).body);
                    ((PhysicsComponent)component).body.setUserData(null);
                    ((PhysicsComponent)component).body.delete();
                    ((PhysicsComponent)component).body = null;
                }
            }
            GameObject.createBulldozer(coordinateX,coordinateY,gameWorld,direction,context,componentsAi,gameWorld.speed);
            gameWorld.verifyAction = false;
        }

    }


    private static void deleteBulldozer(GameWorld gameWorld) {
        GameObject gameObjectBulldozer = null;

        for (GameObject gameObject : gameWorld.listGameObject) {
            if (gameObject.name.equals("bulldozer")) {
                gameObjectBulldozer = gameObject;
                break;
            }
        }

        if (gameObjectBulldozer != null) {
            List<Component> componentsAi = gameObjectBulldozer.components.get(ComponentType.AI.hashCode());
            List<Component> componentsPhysics = gameObjectBulldozer.components.get(ComponentType.Physics.hashCode());
            gameWorld.listGameObject.remove(gameObjectBulldozer);

            if (componentsPhysics != null) {
                for (Component component : componentsPhysics) {
                    gameWorld.world.destroyBody(((PhysicsComponent) component).body);
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


    public  float toPixelsXLength(float x) {
        return x/currentView.width*bufferWidth;
    }


    public  float toPixelsYLength(float y) {
        return y/currentView.height*bufferHeight;
    }


    public static synchronized void setGravity(float x, float y,GameWorld gameWorld) {
        gameWorld.world.setGravity(x, y);
    }


    @Override
    public void finalize() {
        world.delete();
    }





    public static void eventTouch(float coordinateX, float coordinateY,GameWorld gameWorld){
        if(!gameOverFlag) {
            if ((coordinateX <= 13.5f && coordinateX >= 9f) && (coordinateY >= 23f && coordinateY <= 26)) {
                gameWorld.handlerUI.sendEmptyMessage(0);
            }else if(! gameWorld.flagCollisionBarrel && ((coordinateY>=-22 && coordinateY <=21.6))){
                if ( gameWorld.numberBarrel > 0) {
                    gameWorld.flagCollisionBarrel = true;
                    GameObject.createBarrel(13f, coordinateY, gameWorld);
                    gameWorld.numberBarrel =  gameWorld.numberBarrel - 1;
                    gameWorld.numberBarrelText.text=String.format("%02d",  gameWorld.numberBarrel);
                }
            }
        }
        else{
            gameWorld.handlerUI.sendEmptyMessage(1);
            gameWorld.score=0;
            gameWorld.startTime= System.currentTimeMillis();
            gameWorld.listBarrel= new LinkedBlockingDeque<>();
            gameWorld.currentTime=0;
            gameWorld.numberBarrel=5;
            gameWorld.level=1;
            gameWorld.speed=7;
            gameWorld.saveGame();
        }

    }


    public static void handleDeleteBarrel(Collision event,GameWorld gameWorld){
        if(event.a.name.equals("incinerator")  && event.b.name.equals("barrel")){
            gameWorld.listGameObject.remove((GameObject) event.b.owner);
            gameWorld.listBarrel.remove((GameObject) event.b.owner);
            gameWorld.world.destroyBody(event.b.body);
            event.b.body.setUserData(null);
            event.b.body.delete();
            event.b.body = null;
            if(gameWorld.listBarrel.size() == 0){
                gameWorld.verifyAction = false;
            }
        }else if(event.a.name.equals("barrel")  && event.b.name.equals("incinerator")){
            gameWorld.listGameObject.remove((GameObject) event.a.owner);
            gameWorld.listBarrel.remove((GameObject) event.a.owner);
            gameWorld.world.destroyBody(event.a.body);
            event.a.body.setUserData(null);
            event.a.body.delete();
            event.a.body = null;
            if(gameWorld.listBarrel.size() == 0){
                gameWorld.verifyAction = false;
            }
        }
        if(gameWorld.numberBarrel ==0 && gameWorld.listBarrel.isEmpty())
            gameWorld.zeroBarrel=true;
    }


    protected static int searchBarrel(GameWorld gameWorld){
        int contRight = 0;
        int contLeft = 0;
        float positionYBulldozer = gameWorld.bulldozer.body.getPositionY();
        if(gameWorld.listBarrel.size() == 0){
            return 0;
        }else{
            for (GameObject g: gameWorld.listBarrel) {
                List<Component> positionComponents = g.components.get(ComponentType.Position.hashCode());
                PositionComponent positionComponent = (PositionComponent) positionComponents.get(0);
                    if(positionYBulldozer > positionComponent.coordinateY ){
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


    protected static void burnedBarrel(int direction,GameWorld gameWorld,Activity context){
        int invert = ((DynamicPositionComponent) gameWorld.bulldozer.owner.components.get(ComponentType.Position.hashCode()).get(0)).direction;
        float positionYBulldozer = gameWorld.bulldozer.body.getPositionY();
        if(invert != direction){
            float positionXBulldozer = gameWorld.bulldozer.body.getPositionX();
            gameWorld.rotationBulldozer(positionXBulldozer,positionYBulldozer,gameWorld,direction,context);
        }

        for(Component c:gameWorld.bulldozer.owner.components.get(ComponentType.Joint.hashCode())){
            if(c instanceof RevoluteJointComponent) {
                if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                    ((RevoluteJointComponent) c).joint.setMotorSpeed(direction * gameWorld.speed);
                ((RevoluteJointComponent) c).joint.setMaxMotorTorque(gameWorld.torque);
            }
        }
    }


    protected  static void moveToCenter(GameWorld gameWorld, Activity context){
        float positionYBulldozer = gameWorld.bulldozer.body.getPositionY();
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) (gameWorld.gameObjectBulldozer).components.get(ComponentType.Position.hashCode()).get(0);
        int direction = (dynamicPositionComponent.direction);

        if(direction == -1){
            if(positionYBulldozer < 0.1f){
                for(Component componentBulldozer :gameWorld.gameObjectBulldozer.components.get(ComponentType.Joint.hashCode())){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(30f);
                    }
                }
            }else{
                if (positionYBulldozer < -0.5f) {
                    gameWorld.rotationBulldozer(-8f, positionYBulldozer, gameWorld, 1, context);
                    for(Component componentBulldozer :gameWorld.gameObjectBulldozer.components.get(ComponentType.Joint.hashCode())){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(gameWorld.speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(gameWorld.torque);
                        }
                    }
                }else{
                    for(Component componentBulldozer :gameWorld.gameObjectBulldozer.components.get(ComponentType.Joint.hashCode())){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(-gameWorld.speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(gameWorld.torque);
                        }
                    }
                }
            }
        }else{
            if(positionYBulldozer >= -0.1f ){
                for(Component componentBulldozer :gameWorld.gameObjectBulldozer.components.get(ComponentType.Joint.hashCode())){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(30f);
                    }
                }
            }else{
                if (positionYBulldozer > 3) {
                    gameWorld.rotationBulldozer(-8f, positionYBulldozer, gameWorld, -1, context);
                    for(Component componentBulldozer :gameWorld.gameObjectBulldozer.components.get(ComponentType.Joint.hashCode())){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(-gameWorld.speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(gameWorld.torque);
                        }
                    }
                }else{
                    for(Component componentBulldozer :gameWorld.gameObjectBulldozer.components.get(ComponentType.Joint.hashCode())){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(gameWorld.speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(gameWorld.torque);
                        }
                    }
                }
            }
        }
    }


    protected void saveGame(){
        handlerUI.sendEmptyMessage(2);
    }





}
