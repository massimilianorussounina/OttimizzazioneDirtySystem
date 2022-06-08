package com.example.dirtsystemec;


enum ComponentType{
    Physics,
    AI,
    Drawable,
    Position,
    Joint
}


public abstract class Component {

    protected Entity owner;


    public abstract ComponentType type();


    public Entity getOwner() {
        return owner;
    }


    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}
