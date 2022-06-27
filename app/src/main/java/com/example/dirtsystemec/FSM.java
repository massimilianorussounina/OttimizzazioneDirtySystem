package com.example.dirtsystemec;

import java.util.ArrayList;
import java.util.List;


enum Action{
    burned,waited
}


class State{

    protected String name;
    protected Action activeAction;
    protected List<Transition> transitionsOut;

    public State(String name, Action activeAction){
        this.name = name;
        this.activeAction = activeAction;
        this.transitionsOut = new ArrayList<>();
    }










}


class Transition{

    protected Action action;
    protected State fromState;
    protected State targetState;


    public Transition(State fromState,State targetState,Action action){
        this.fromState = fromState;
        this.targetState = targetState;
        this.action = action;
    }


    public static boolean isTriggered(GameWorld gameWorld,Transition transition) {
       Action action = transition.targetState.activeAction;

        if(action.equals(Action.burned)){
            return gameWorld.listBarrel.size() != 0;
        }else if(action.equals(Action.waited)){
            return gameWorld.listBarrel.size() == 0;
        }

        return false;
    }


    public State targetState() {
        return targetState;
    }

}

public class FSM {

    private State currentState;


    public FSM(State state){
        this.currentState = state;
    }


    public static Action stepAndGetAction(GameWorld gameWorld,FSM fsm){
        Transition transitionTrigger = null;

        for (Transition transition: fsm.currentState.transitionsOut) {
            if(transition.isTriggered(gameWorld,transition)){
                transitionTrigger = transition;
                break;
            }
        }

        if(transitionTrigger != null){
            fsm.currentState = transitionTrigger.targetState();
            return transitionTrigger.action;
        }
        else{
            return fsm.currentState.activeAction;
        }
    }

}
