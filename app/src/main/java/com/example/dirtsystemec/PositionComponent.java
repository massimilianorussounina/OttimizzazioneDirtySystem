package com.example.dirtsystemec;


public abstract class PositionComponent extends Component{
    protected String name;
    protected float coordinateX, coordinateY;
    protected float coordinateLocalOneX, coordinateLocalOneY, coordinateLocalTwoX,
                coordinateLocalTwoY, coordinateLocalThreeX, coordinateLocalThreeY;

    PositionComponent(String name){
        this.name = name;
    }

        @Override
        public ComponentType type(){
            return ComponentType.Position;
        }


    public float getCoordinateX() {
        return coordinateX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }
}

class StaticPositionComponent extends PositionComponent{

    StaticPositionComponent(String name,float coordinateX,float coordinateY,GameObject gameObject){
        super(name);
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.owner = gameObject;
    }

}

class DynamicPositionComponent extends PositionComponent{

    protected  int direction;


    DynamicPositionComponent(String name,float coordinateX,float coordinateY, GameObject gameObject, int direction){
        super(name);
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.owner = gameObject;
        this.direction = direction;
    }


    DynamicPositionComponent(String name,float coordinateX,float coordinateY, GameObject gameObject){
        super(name);
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.owner = gameObject;
    }

}
class TrianglePositionComponent extends PositionComponent{

    public TrianglePositionComponent(String name,float coordinateX,float coordinateY, float coordinateLocalOneX, float coordinateLocalOneY,
                                    float coordinateLocalTwoX, float coordinateLocalTwoY,float coordinateLocalThreeX, float coordinateLocalThreeY,GameObject gameObject) {

        super(name);
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.coordinateLocalOneX = coordinateLocalOneX;
        this.coordinateLocalOneY = coordinateLocalOneY;
        this.coordinateLocalTwoX = coordinateLocalTwoX;
        this.coordinateLocalTwoY = coordinateLocalTwoY;
        this.coordinateLocalThreeX = coordinateLocalThreeX;
        this.coordinateLocalThreeY = coordinateLocalThreeY;
        this.owner = gameObject;
    }

}