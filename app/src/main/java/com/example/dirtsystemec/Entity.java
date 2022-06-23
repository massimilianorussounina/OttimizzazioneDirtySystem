package com.example.dirtsystemec;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {

    //protected final Map<ComponentType,ArrayList<Component>> components = new HashMap<>();
    protected final SparseArray<ArrayList<Component>> components = new SparseArray<>();

    public void addComponent(Component component){
        component.owner=this;
        ArrayList<Component> list = components.get(component.type().hashCode());

        if(list == null){
            list = new ArrayList<>();
            list.add(component);
        }else{
            list.add(component);
        }

        components.put(component.type().hashCode(),list);
    }



    

}
