package com.example.dirtsystemec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {

    private final Map<ComponentType,ArrayList<Component>> components = new HashMap<>();


    public void addComponent(Component component){
        component.setOwner(this);
        ArrayList<Component> list = components.get(component.type());

        if(list == null){
            list = new ArrayList<>();
            list.add(component);
        }else{
            list.add(component);
        }

        components.put(component.type(),list);
    }


    public List<Component> getComponent(ComponentType type){
        return components.get(type);
    }

}
