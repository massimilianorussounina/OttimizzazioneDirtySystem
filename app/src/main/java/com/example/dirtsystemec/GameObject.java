package com.example.dirtsystemec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.fpl.liquidfun.BodyType;

import java.util.ArrayList;
import java.util.List;

public class GameObject extends Entity{

    protected String name;
    protected GameWorld gameWorld;

    GameObject(GameWorld gameWorld,String name){
        super();
        this.name= name;
        this.gameWorld = gameWorld;
    }

    public static void createBarrel(float coordinateX, float coordinateY,GameWorld gameWorld) {
        GameObject gameObjectBarrel = new GameObject(gameWorld,"barrel");

        DynamicPositionComponent dynamicPositionComponent = new DynamicPositionComponent("barrel",coordinateX,coordinateY,gameObjectBarrel);
        BitmapDrawableComponent bitmapDrawableComponent = new BitmapDrawableComponent("barrel",gameObjectBarrel,0.8f,0.8f,R.drawable.barrel,0,0,184,187);
        CirclePhysicsComponent circlePhysicsComponent = new CirclePhysicsComponent("barrel",gameObjectBarrel,BodyType.dynamicBody,coordinateX,coordinateY,bitmapDrawableComponent.width/2f,bitmapDrawableComponent.height/2f,2f,0.1f,6f);


        gameObjectBarrel.addComponent(dynamicPositionComponent);
        gameObjectBarrel.addComponent(bitmapDrawableComponent);
        gameObjectBarrel.addComponent(circlePhysicsComponent);


        gameWorld.addGameObject(gameObjectBarrel);
    }


    public static void createIncinerator(float coordinateX, float coordinateY,float fireCoordinateX, float fireCoordinateY,GameWorld gameWorld) {
        GameObject gameObjectIncinerator = new GameObject(gameWorld,"incinerator");

        BitmapDrawableComponent bitmapDrawableComponent = new BitmapDrawableComponent("incinerator",gameObjectIncinerator,2.5f,2.7f,R.drawable.incinerator,0,0,37,54);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("incinerator",coordinateX,coordinateY,gameObjectIncinerator);
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("incinerator",gameObjectIncinerator,BodyType.staticBody,coordinateX,coordinateY, bitmapDrawableComponent.width/2, bitmapDrawableComponent.height/2, 2f);

        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.fire_spritesheet,bitmapFactory );
        SpriteDrawableComponent spriteDrawableComponent = new SpriteDrawableComponent("incinerator",gameObjectIncinerator,new FireSprite(gameWorld,new SpriteSheet(bitmap,8),fireCoordinateX,fireCoordinateY,
                2.5f,2.0f,49,75,200,8));


        gameObjectIncinerator.addComponent(spriteDrawableComponent);
        gameObjectIncinerator.addComponent(staticPositionComponent);
        gameObjectIncinerator.addComponent(bitmapDrawableComponent);
        gameObjectIncinerator.addComponent(polygonPhysicsComponent);
        gameWorld.addGameObject(gameObjectIncinerator);
    }


    public static void createSea(float coordinateX, float coordinateY,float seaCoordinateX, float seaCoordinateY,GameWorld gameWorld) {

        GameObject gameObjectSea = new GameObject(gameWorld,"sea");

        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("sea",coordinateX,coordinateY,gameObjectSea);

        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.sea,bitmapFactory );
        SpriteDrawableComponent spriteDrawableComponent = new SpriteDrawableComponent("sea",gameObjectSea,new SeaSprite(gameWorld,new SpriteSheet(bitmap,15),seaCoordinateX,seaCoordinateY,
                5.3f,2.0f,500,108,500,15));
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("sea",gameObjectSea,BodyType.staticBody,coordinateX,coordinateY,spriteDrawableComponent.width, spriteDrawableComponent.height,2f);

        gameObjectSea.addComponent(staticPositionComponent);
        gameObjectSea.addComponent(spriteDrawableComponent);
        gameObjectSea.addComponent(polygonPhysicsComponent);


        gameWorld.addGameObject(gameObjectSea);
    }


    public static void createGround(float coordinateX, float coordinateY,GameWorld gameWorld) {
        GameObject gameObjectGround = new GameObject(gameWorld,"ground");
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("ground",coordinateX,coordinateY,gameObjectGround);
        BitmapDrawableComponent bitmapDrawableComponent = new BitmapDrawableComponent("ground",gameObjectGround,9.0f,4.0f,R.drawable.ground,0,0,200,82);
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("ground",gameObjectGround,BodyType.staticBody,coordinateX,coordinateY, bitmapDrawableComponent.width/2, bitmapDrawableComponent.height/2, 1f,0.0f,0.1f);


        gameObjectGround.addComponent(staticPositionComponent);
        gameObjectGround.addComponent(bitmapDrawableComponent);
        gameObjectGround.addComponent(polygonPhysicsComponent);

        gameWorld.addGameObject(gameObjectGround);
    }


    public static void createBridge(float bridgeCoordinateX, float bridgeCoordinateY,
                                    float towerCoordinateXRight, float towerCoordinateYRight,
                                    float towerBodyCoordinateX1Right, float towerBodyCoordinateY1Right,
                                    float towerBodyCoordinateX2Right, float towerBodyCoordinateY2Right,
                                    float towerBodyCoordinateX3Right, float towerBodyCoordinateY3Right,
                                    float towerCoordinateXLeft, float towerCoordinateYLeft,
                                    float towerBodyCoordinateX1Left, float towerBodyCoordinateY1Left,
                                    float towerBodyCoordinateX2Left, float towerBodyCoordinateY2Left,
                                    float towerBodyCoordinateX3Left, float towerBodyCoordinateY3Left,
                                    GameWorld gameWorld){


        /* Costruzione torre right */

        GameObject gameObjectTowerRight = new GameObject(gameWorld,"torreRight");
        TrianglePositionComponent trianglePositionComponentRight = new TrianglePositionComponent("torreRight",towerCoordinateXRight,towerCoordinateYRight,towerBodyCoordinateX1Right,towerBodyCoordinateY1Right,
                towerBodyCoordinateX2Right,towerBodyCoordinateY2Right,towerBodyCoordinateX3Right,towerBodyCoordinateY3Right,gameObjectTowerRight);
        BitmapDrawableComponent bitmapDrawableComponentRight = new BitmapDrawableComponent("torreRight",gameObjectTowerRight,4.0f,2.0f,R.drawable.right_bridge2,0,0,423,208);
        PolygonPhysicsComponent polygonPhysicsComponentRight = new PolygonPhysicsComponent("torreRight",gameObjectTowerRight, BodyType.staticBody,towerCoordinateXRight,towerCoordinateYRight,
                towerBodyCoordinateX1Right,towerBodyCoordinateY1Right,towerBodyCoordinateX2Right,towerBodyCoordinateY2Right,towerBodyCoordinateX3Right,towerBodyCoordinateY3Right,
                1f,0.4f,10f);

        gameObjectTowerRight.addComponent(trianglePositionComponentRight);
        gameObjectTowerRight.addComponent(bitmapDrawableComponentRight);
        gameObjectTowerRight.addComponent(polygonPhysicsComponentRight);
        gameWorld.addGameObject(gameObjectTowerRight);




        /* Costruzione torre left */

        GameObject gameObjectTowerLeft = new GameObject(gameWorld,"torreLeft");
        TrianglePositionComponent trianglePositionComponentLeft = new TrianglePositionComponent("torreLeft",towerCoordinateXLeft,towerCoordinateYLeft,towerBodyCoordinateX1Left,towerBodyCoordinateY1Left,
                towerBodyCoordinateX2Left,towerBodyCoordinateY2Left,towerBodyCoordinateX3Left,towerBodyCoordinateY3Left,gameObjectTowerLeft);
        BitmapDrawableComponent bitmapDrawableComponentLeft = new BitmapDrawableComponent("torreLeft",gameObjectTowerLeft,4.0f,2.0f,R.drawable.left_bridge2,0,0,423,208);
        PolygonPhysicsComponent polygonPhysicsComponentLeft = new PolygonPhysicsComponent("torreLeft",gameObjectTowerLeft, BodyType.staticBody,towerCoordinateXLeft,towerCoordinateYLeft,
                towerBodyCoordinateX1Left,towerBodyCoordinateY1Left,towerBodyCoordinateX2Left,towerBodyCoordinateY2Left,towerBodyCoordinateX3Left,towerBodyCoordinateY3Left,
                1f,0.4f,20f);
        gameObjectTowerLeft.addComponent(trianglePositionComponentLeft);
        gameObjectTowerLeft.addComponent(bitmapDrawableComponentLeft);
        gameObjectTowerLeft.addComponent(polygonPhysicsComponentLeft);
        gameWorld.addGameObject(gameObjectTowerLeft);





        /* Costruzione ponte */

        GameObject gameObjectBridge = new GameObject(gameWorld,"bridge");
        DynamicPositionComponent dynamicPositionComponent = new DynamicPositionComponent("bridge",bridgeCoordinateX,bridgeCoordinateY,gameObjectBridge);
        RectDrawableComponent rectDrawableComponent = new RectDrawableComponent("bridge",gameObjectBridge,4.8f,0.2f, Color.argb(255, 32, 32, 32));
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("bridge",gameObjectBridge,BodyType.staticBody,bridgeCoordinateX-1f,bridgeCoordinateY,rectDrawableComponent.width/2, rectDrawableComponent.height/2,4f,0.0f,0.4f);
        gameObjectBridge.addComponent(dynamicPositionComponent);
        gameObjectBridge.addComponent(rectDrawableComponent);
        gameObjectBridge.addComponent(polygonPhysicsComponent);
        gameWorld.addGameObject(gameObjectBridge);

    }



    public static void createEnclosure(float coordinateXMax, float coordinateXMin,float coordinateYMax,float coordinateYMin,GameWorld gameWorld){
        GameObject gameObjectEnclosure = new GameObject(gameWorld,"enclosure");
        PolygonPhysicsComponent polygonPhysicsComponentTop = new PolygonPhysicsComponent("top",gameObjectEnclosure,BodyType.staticBody,
                (coordinateXMin + (coordinateXMax - coordinateXMin)/2), coordinateYMin,(coordinateXMax - coordinateXMin),0.1f, 0,1);


        PolygonPhysicsComponent polygonPhysicsComponentBottom =  new PolygonPhysicsComponent("bot",gameObjectEnclosure,BodyType.staticBody,
                (coordinateXMin + (coordinateXMax - coordinateXMin)/2), coordinateYMax,(coordinateXMax - coordinateXMin),0.1f, 0,1);


        gameObjectEnclosure.addComponent(polygonPhysicsComponentTop);
        gameObjectEnclosure.addComponent(polygonPhysicsComponentBottom);
        gameWorld.addGameObject(gameObjectEnclosure);
    }


    public static void createBulldozer(float coordinateX, float coordinateY, GameWorld gameWorld, int invert, Context context, List<Component> componentAi,float speed){
        float width=2.8f;
        int photochassis;
        int photocabin;
        gameWorld.setGravity(0,0);
        GameObject gameObjectBulldozer = new GameObject(gameWorld,"bulldozer");
        DynamicPositionComponent dynamicPositionComponentBulldozer = new DynamicPositionComponent("bulldozer",coordinateX, coordinateY,gameObjectBulldozer,invert);
        gameObjectBulldozer.addComponent(dynamicPositionComponentBulldozer);


        PolygonPhysicsComponent polygonPhysicsComponentChassis = new PolygonPhysicsComponent("chassis",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,(width/2),proportionalToBulldozer(1.2f,width)/2,4f,0.1f,0);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentChassis);


        PolygonPhysicsComponent polygonPhysicsComponentCabin = new PolygonPhysicsComponent("cabin",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,(proportionalToBulldozer(2.3f,width))/2,proportionalToBulldozer(1.5f,width)/2,0.1f,0.1f,0f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentCabin);


        RevoluteJointComponent revoluteJointComponentBulldozer =  new RevoluteJointComponent(gameObjectBulldozer,polygonPhysicsComponentChassis.body,
                polygonPhysicsComponentCabin.body,(invert*-proportionalToBulldozer(0.5f,width)),-proportionalToBulldozer(1.3f,width),0,0,0,
                0,0);
        gameObjectBulldozer.addComponent(revoluteJointComponentBulldozer);


        if(invert == 1){
            photochassis= R.drawable.chassis_dx;
            photocabin = R.drawable.cabin_dx;
        } else {
            photochassis= R.drawable.chassis_sx;
            photocabin = R.drawable.cabin_sx;
        }

        BitmapDrawableComponent bitmapDrawableComponentChassis = new BitmapDrawableComponent("chassis",gameObjectBulldozer,width,proportionalToBulldozer(1.2f,width),photochassis,0,0,100,27);
        BitmapDrawableComponent bitmapDrawableComponentCabin = new BitmapDrawableComponent("cabin",gameObjectBulldozer,proportionalToBulldozer(2.3f,width),proportionalToBulldozer(1.5f,width),photocabin,0,0,100,65);
        gameObjectBulldozer.addComponent(bitmapDrawableComponentCabin);


        String[] damperName = {"damperOne","damperTwo","damperThree","damperFour"};
        ArrayList<PolygonPhysicsComponent> listPolygonDamper = new ArrayList<>();
        float[] coordinateSpawnDamper ={-proportionalToBulldozer(1.7f,width),+proportionalToBulldozer(1.7f,width),+proportionalToBulldozer(0.8f,width),-proportionalToBulldozer(0.8f,width)};
        float[] coordinateXPrismaticChassis = {-width/2+proportionalToBulldozer(0.5f,width),  +width/2-proportionalToBulldozer(0.5f,width),  +width/2-(proportionalToBulldozer(0.5f,width)*2)- proportionalToBulldozer(0.6f,width),  -width/2+(proportionalToBulldozer(0.5f,width)*2)+ proportionalToBulldozer(0.6f,width)};
        for(int i=0;i<4;i++){

            PolygonPhysicsComponent polygonPhysicsComponentDamper = new PolygonPhysicsComponent(damperName[i],gameObjectBulldozer,BodyType.dynamicBody
                    ,coordinateX-proportionalToBulldozer(0.5f,width),coordinateY+coordinateSpawnDamper[i],proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(1f,width)/2,1,0.1f,0.5f, true);
            gameObjectBulldozer.addComponent(polygonPhysicsComponentDamper);
            listPolygonDamper.add(polygonPhysicsComponentDamper);

            PrismaticJointComponent prismaticJointComponent = new PrismaticJointComponent(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamper.body,
                    coordinateXPrismaticChassis[i],proportionalToBulldozer(1f,width)/2+proportionalToBulldozer(0.0f,width),
                    0, proportionalToBulldozer(0.9f,width)
                    ,0,1,false,false,
                    0,polygonPhysicsComponentChassis.body.getMass()*8.5f );

            gameObjectBulldozer.addComponent(prismaticJointComponent);

            DistanceJointComponent distanceJointComponent = new DistanceJointComponent(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamper.body,
                    coordinateXPrismaticChassis[i],proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.0f,width),
                    0,proportionalToBulldozer(0.9f,width),
                    1f,4f,-proportionalToBulldozer(0.2f,width));
            gameObjectBulldozer.addComponent(distanceJointComponent);

            BitmapDrawableComponent bitmapDrawableComponentDamper = new BitmapDrawableComponent(damperName[i],gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(1f,width),R.drawable.damper,0,0,31,63);
            gameObjectBulldozer.addComponent(bitmapDrawableComponentDamper);
        }

        gameObjectBulldozer.addComponent(bitmapDrawableComponentChassis);

        String[] wheelName = {"wheelOne","wheelTwo","wheelThree","wheelFour"};
        for(int i=0;i<4;i++){
            CirclePhysicsComponent circlePhysicsComponentWheel = new CirclePhysicsComponent(wheelName[i],gameObjectBulldozer,BodyType.dynamicBody,coordinateX-proportionalToBulldozer(0.7f,width),coordinateY+coordinateSpawnDamper[i],proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),4,0.1f,1f);
            gameObjectBulldozer.addComponent(circlePhysicsComponentWheel);



            RevoluteJointComponent revoluteJointComponentOne = new RevoluteJointComponent(gameObjectBulldozer, listPolygonDamper.get(i).body, circlePhysicsComponentWheel.body,0,proportionalToBulldozer(0.7f,width),0,0,gameWorld.torque,true,(float) invert*speed);
            gameObjectBulldozer.addComponent(revoluteJointComponentOne);


            // CircleDrawableComponent circleDrawableComponentOne= new CircleDrawableComponent("wheelOne",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),Color.argb(255,255,0,0));
            BitmapDrawableComponent bitmapDrawableComponentWheel = new BitmapDrawableComponent(wheelName[i],gameObjectBulldozer,proportionalToBulldozer(1f,width),proportionalToBulldozer(1f,width),R.drawable.wheel,0,0,132,132);
            gameObjectBulldozer.addComponent(bitmapDrawableComponentWheel);


        }


        PolygonPhysicsComponent polygonPhysicsComponentShovel1 = new PolygonPhysicsComponent("shovel1",gameObjectBulldozer,BodyType.dynamicBody,coordinateX,coordinateY+invert*proportionalToBulldozer(3f,width),proportionalToBulldozer(1f,width)/2,proportionalToBulldozer(0.5f,width)/2,4f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentShovel1);

        if (invert==1){
            RevoluteJointComponent revoluteJointComponent7 = new RevoluteJointComponent(gameObjectBulldozer,
                    polygonPhysicsComponentChassis.body,
                    polygonPhysicsComponentShovel1.body,
                    invert*(width-proportionalToBulldozer(2,width))+invert*0.05f,
                    -proportionalToBulldozer(0.7f,width),
                    invert*(-proportionalToBulldozer(0.2f,width)),
                    -proportionalToBulldozer(0.6f,width),0,0,-1.5708f);
            gameObjectBulldozer.addComponent(revoluteJointComponent7);
        }
        else{
            RevoluteJointComponent revoluteJointComponent7 = new RevoluteJointComponent(gameObjectBulldozer,
                    polygonPhysicsComponentChassis.body,
                    polygonPhysicsComponentShovel1.body,
                    invert*(width-proportionalToBulldozer(2,width))+invert*0.05f,
                    -proportionalToBulldozer(0.1f,width),
                    invert*(-proportionalToBulldozer(0.2f,width)),
                    0,0,1.5708f,0);
            gameObjectBulldozer.addComponent(revoluteJointComponent7);
        }

        BitmapDrawableComponent bitmapDrawableComponentShovel1 = new BitmapDrawableComponent("shovel1",gameObjectBulldozer,proportionalToBulldozer(1f,width),proportionalToBulldozer(0.5f,width),R.drawable.shovel1,0,0,63,31);
        gameObjectBulldozer.addComponent(bitmapDrawableComponentShovel1);

        PolygonPhysicsComponent polygonPhysicsComponentShovel2 = new PolygonPhysicsComponent("shovel2",gameObjectBulldozer,BodyType.dynamicBody,coordinateX,coordinateY+invert*proportionalToBulldozer(3f,width),proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(2.1f,width)/2,4f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentShovel2);

        RevoluteJointComponent revoluteJointComponent8 = new RevoluteJointComponent(gameObjectBulldozer, polygonPhysicsComponentShovel1.body,polygonPhysicsComponentShovel2.body,0,0,invert*-proportionalToBulldozer(0.5f,width),-proportionalToBulldozer(0.7f,width),0,0,0);
        gameObjectBulldozer.addComponent(revoluteJointComponent8);

        BitmapDrawableComponent bitmapDrawableComponentShovel2 = new BitmapDrawableComponent("shovel2",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(2.1f,width),R.drawable.shovel2,0,0,31,133);


        CirclePhysicsComponent circlePhysicsComponentShovel = new CirclePhysicsComponent("wheelShovel",gameObjectBulldozer,BodyType.dynamicBody,coordinateX,coordinateY+invert*proportionalToBulldozer(3f,width),proportionalToBulldozer(1f,width)/2,0,0,0.1f,4f);
        gameObjectBulldozer.addComponent(circlePhysicsComponentShovel);

        RevoluteJointComponent revoluteJointComponentShovel = new RevoluteJointComponent(gameObjectBulldozer,polygonPhysicsComponentShovel2.body,circlePhysicsComponentShovel.body,0,proportionalToBulldozer(0.9f,width),0,0,0,0,0);
        gameObjectBulldozer.addComponent(revoluteJointComponentShovel);


        BitmapDrawableComponent bitmapDrawableComponentShovelWheel = new BitmapDrawableComponent("wheelShovel",gameObjectBulldozer,proportionalToBulldozer(1f,width),proportionalToBulldozer(1f,width),R.drawable.shovelwheel,0,0,50,50);
        gameObjectBulldozer.addComponent(bitmapDrawableComponentShovelWheel);
        gameObjectBulldozer.addComponent(bitmapDrawableComponentShovel2);


        if(componentAi != null){
            for (Component component: componentAi) {
                gameObjectBulldozer.addComponent(component);
            }
        }else {
            FsmAIComponent fsmAIComponent = new FsmAIComponent(context,"bulldozer.json",gameWorld);
            gameObjectBulldozer.addComponent(fsmAIComponent);
        }


        gameWorld.addGameObject(gameObjectBulldozer);

        gameWorld.setGravity(-10,0);

    }


    public static float proportionalToBulldozer(float number,float constant){
        float percent;
        percent = (number * 100) / 4.4f;
        return Math.round(((constant*percent) / 100f) * 100f) / 100f;
    }


    public static void createBarrelIcon(float coordinateX, float coordinateY,GameWorld gameWorld){
        GameObject gameObjectBarrelIcon = new GameObject(gameWorld,"barrelIcon");
        BitmapDrawableComponent bitmapDrawableComponent;
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("barrelIcon",coordinateX,coordinateY,gameObjectBarrelIcon);
        bitmapDrawableComponent = new BitmapDrawableComponent("barrelIcon",gameObjectBarrelIcon,1.8f,2.5f,R.drawable.barrel_icon,0,0,58,80);
        gameObjectBarrelIcon.addComponent(staticPositionComponent);
        gameObjectBarrelIcon.addComponent(bitmapDrawableComponent);
        gameWorld.addGameObject(gameObjectBarrelIcon);
    }


    public static void createTimer(float coordinateX, float coordinateY,GameWorld gameWorld){
        GameObject gameObjectTimer = new GameObject(gameWorld,"timer");
        TextDrawableComponent textDrawableComponentTimer = new TextDrawableComponent("timer",gameObjectTimer,"05:00",Color.argb(255,255,255,255), gameWorld.activity, 40);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("timer",coordinateX,coordinateY,gameObjectTimer);
        gameObjectTimer.addComponent(textDrawableComponentTimer);
        gameObjectTimer.addComponent(staticPositionComponent);
        gameWorld.addGameObject(gameObjectTimer);
    }


    public static void createTextNumberBarrel(float coordinateX, float coordinateY,GameWorld gameWorld){
        GameObject gameObjectTimer = new GameObject(gameWorld,"numberBarrel");
        TextDrawableComponent textDrawableComponentTimer = new TextDrawableComponent("numberBarrel",gameObjectTimer,"5",Color.argb(255,255,255,255), gameWorld.activity, 30);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("numberBarrel",coordinateX,coordinateY,gameObjectTimer);
        gameObjectTimer.addComponent(textDrawableComponentTimer);
        gameObjectTimer.addComponent(staticPositionComponent);
        gameWorld.addGameObject(gameObjectTimer);
    }


    public static void createTextScore(float coordinateX, float coordinateY, GameWorld gameWorld){
        GameObject gameObjectTimer = new GameObject(gameWorld,"textScore");
        TextDrawableComponent textDrawableComponentTimer = new TextDrawableComponent("textScore",gameObjectTimer,"0",Color.argb(255,255,0,0), gameWorld.activity, 25);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("textScore",coordinateX,coordinateY,gameObjectTimer);
        gameObjectTimer.addComponent(textDrawableComponentTimer);
        gameObjectTimer.addComponent(staticPositionComponent);
        gameWorld.addGameObject(gameObjectTimer);
    }


    public static GameObject createTextGameOver(float coordinateX, float coordinateY,GameWorld gameWorld,String text){
        GameObject gameObjectTimer = new GameObject(gameWorld,"gameOver");
        TextDrawableComponent textDrawableComponentTimer = new TextDrawableComponent("gameOver",gameObjectTimer,text,Color.argb(255,255,0,0), gameWorld.activity, 70);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("gameOver",coordinateX,coordinateY,gameObjectTimer);
        gameObjectTimer.addComponent(textDrawableComponentTimer);
        gameObjectTimer.addComponent(staticPositionComponent);
        gameWorld.addGameObject(gameObjectTimer);
        return  gameObjectTimer;
    }


    public static void createScoreBar(float coordinateX, float coordinateY,GameWorld gameWorld,int lastValue) {

        GameObject gameObjectSea = new GameObject(gameWorld,"scoreBar");

        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("scoreBar",coordinateX,coordinateY,gameObjectSea);

        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.scorebar,bitmapFactory );
        SpriteDrawableComponent spriteDrawableComponent = new SpriteDrawableComponent("scoreBar",gameObjectSea,new ScoreSprite(gameWorld,new SpriteSheet(bitmap,10),coordinateX,coordinateY,
                2f,12f,66,405,1,10,lastValue));

        gameObjectSea.addComponent(staticPositionComponent);
        gameObjectSea.addComponent(spriteDrawableComponent);


        gameWorld.addGameObject(gameObjectSea);
    }


    public static void createButtonPause(float coordinateX, float coordinateY,GameWorld gameWorld){
        GameObject gameObjectButtonPause = new GameObject(gameWorld,"buttonPause");
        BitmapDrawableComponent bitmapDrawableComponent;
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("buttonPause",coordinateX,coordinateY,gameObjectButtonPause);
        bitmapDrawableComponent = new BitmapDrawableComponent("buttonTrash",gameObjectButtonPause,2f,2f,R.drawable.button_pause,0,0,50,50);
        gameObjectButtonPause.addComponent(staticPositionComponent);
        gameObjectButtonPause.addComponent(bitmapDrawableComponent);
        gameWorld.addGameObject(gameObjectButtonPause);
    }

}
