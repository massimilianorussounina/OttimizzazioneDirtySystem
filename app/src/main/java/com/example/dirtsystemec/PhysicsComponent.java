package com.example.dirtsystemec;


import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;


public class PhysicsComponent  extends Component{

    protected Body body;
    protected String name;

    PhysicsComponent(String name){
        this.name = name;
    }

    @Override
    public ComponentType type(){
        return ComponentType.Physics;
    }

    public float getBodyPositionX() {
        return body.getPositionX();
    }

    public float getBodyPositionY() {
        return body.getPositionY();
    }

    public float getBodyAngle(){
        return body.getAngle();
    }
}



class CirclePhysicsComponent extends PhysicsComponent {

    CirclePhysicsComponent(String name,GameObject gameObject, BodyType bodyType, float coordinateX, float coordinateY, float width, float height,
                           float friction, float restitution, float density) {
        super(name);
        this.name = name;
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);
        bodyDef.setAllowSleep(false);
        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(width);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circleShape);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bodyDef.delete();
        circleShape.delete();
    }

}


class PolygonPhysicsComponent extends PhysicsComponent{


    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,float width, float height, float density){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);
        bodyDef.setAllowSleep(false);


        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width , height);
        body.createFixture(box, density);
        bodyDef.delete();
        box.delete();
    }


    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,float width, float height, float density,float restitution,
                            float friction){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);

        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width , height);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        bodyDef.delete();
        box.delete();
    }


    PolygonPhysicsComponent(String name, GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,
                            float coordinateLocalOneX, float coordinateLocalOneY,
                            float coordinateLocalTwoX, float coordinateLocalTwoY,
                            float coordinateLocalThreeX, float coordinateLocalThreeY,
                            float density,float restitution, float friction){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(coordinateX,coordinateY);
        bodyDef.setType(bodyType);
        bodyDef.setAngle(1.5708f);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        body.setUserData(this);
        PolygonShape triangle = new PolygonShape();
        triangle.setAsTriangle(coordinateLocalOneX,coordinateLocalOneY,
                coordinateLocalTwoX, coordinateLocalTwoY,
                coordinateLocalThreeX, coordinateLocalThreeY);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(triangle);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bodyDef.delete();
        triangle.delete();
    }


    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float centerX, float centerY,float width, float height, float angle,float density){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(bodyType);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);
        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height, centerX, centerY, angle);
        body.createFixture(box, density);
        bodyDef.delete();
        box.delete();
    }


    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,float width, float height, float density,float restitution,
                            float friction,boolean isSensor) {
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);

        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        body.getFixtureList().setSensor(isSensor);

        bodyDef.delete();
        box.delete();
        }



}





