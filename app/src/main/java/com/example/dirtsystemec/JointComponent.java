package com.example.dirtsystemec;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;

public class JointComponent  extends  Component{

    protected  Body bodyOne;
    protected  Body bodyTwo;

    @Override
    public ComponentType type() {
        return ComponentType.Joint;
    }

}


class RevoluteJointComponent extends JointComponent{
    protected  RevoluteJoint joint;

    public RevoluteJointComponent(GameObject gameObject, Body bodyOne, Body bodyTwo,
                                  float coordinateOneX, float coordinateOneY,
                                  float coordinateTwoX, float coordinateTwoY, float motorTorque,
                                  float upperAngle, float lowerAngle) {
        super();
        this.owner = gameObject;
        this.bodyOne = bodyOne;
        this.bodyTwo = bodyTwo;
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.setBodyA(bodyOne);
        revoluteJointDef.setBodyB(bodyTwo);
        revoluteJointDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        revoluteJointDef.setLocalAnchorB(coordinateTwoX, coordinateTwoY);
        revoluteJointDef.setEnableLimit(true);
        revoluteJointDef.setEnableMotor(true);
        revoluteJointDef.setMaxMotorTorque(motorTorque);
        revoluteJointDef.setUpperAngle(upperAngle);
        revoluteJointDef.setLowerAngle(lowerAngle);
        joint=gameObject.gameWorld.world.createRevoluteJoint(revoluteJointDef);
        revoluteJointDef.delete();
    }
    public   RevoluteJointComponent(GameObject gameObject, Body bodyOne, Body bodyTwo,
                                    float coordinateOneX, float coordinateOneY,
                                    float coordinateTwoX, float coordinateTwoY, float motorTorque,
                                    boolean enableMotor,float motorSpeed) {
        super();
        this.owner = gameObject;
        this.bodyOne = bodyOne;
        this.bodyTwo = bodyTwo;
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.setBodyA(bodyOne);
        revoluteJointDef.setBodyB(bodyTwo);
        revoluteJointDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        revoluteJointDef.setLocalAnchorB(coordinateTwoX, coordinateTwoY);
        revoluteJointDef.setEnableMotor(enableMotor);
        revoluteJointDef.setMotorSpeed(motorSpeed);
        revoluteJointDef.setMaxMotorTorque(motorTorque);
        joint=gameObject.gameWorld.world.createRevoluteJoint(revoluteJointDef);
        revoluteJointDef.delete();

    }
}


class PrismaticJointComponent extends JointComponent{

    public PrismaticJointComponent(GameObject gameObject, Body bodyOne, Body bodyTwo, float coordinateOneX, float coordinateOneY,
                                   float coordinateTwoX, float coordinateTwoY, float localAxisA_x, float localAxisA_y, boolean enableMotor, boolean enableLimit, float upperTranslation, float maxMotorForce){

        super();
        this.owner = gameObject;
        this.bodyOne = bodyOne;
        this.bodyTwo = bodyTwo;
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.setBodyA(bodyOne);
        prismaticJointDef.setBodyB(bodyTwo);
        prismaticJointDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        prismaticJointDef.setLocalAnchorB(coordinateTwoX ,coordinateTwoY);
        prismaticJointDef.setLocalAxisA(localAxisA_x,localAxisA_y);
        prismaticJointDef.setEnableMotor(enableMotor);
        prismaticJointDef.setEnableLimit(enableLimit);
        prismaticJointDef.setUpperTranslation(upperTranslation);
        prismaticJointDef.setMaxMotorForce(maxMotorForce);
        gameObject.gameWorld.world.createJoint(prismaticJointDef);
        prismaticJointDef.delete();
    }
}


class DistanceJointComponent extends  JointComponent{

    public DistanceJointComponent(GameObject gameObject, Body bodyOne, Body bodyTwo, float coordinateOneX, float coordinateOneY,
                                  float coordinateTwoX, float coordinateTwoY, float dampingRatio, float frequencyHz, float length){
        DistanceJointDef SpringDef = new DistanceJointDef();
        SpringDef.setBodyA(bodyOne);
        SpringDef.setBodyB(bodyTwo);
        SpringDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        SpringDef.setLocalAnchorB(coordinateTwoX ,coordinateTwoY);
        SpringDef.setDampingRatio(dampingRatio);
        SpringDef.setFrequencyHz(frequencyHz);
        SpringDef.setLength(length);
        gameObject.gameWorld.world.createJoint(SpringDef);
        SpringDef.delete();
    }
}