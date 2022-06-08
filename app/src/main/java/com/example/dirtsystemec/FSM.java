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


    public Action activeAction() {
        return activeAction;
    }


    public List<Transition> outGoingTransitions() {
        return transitionsOut;
    }


    public void addTransitionsOut(Transition transition) {
        this.transitionsOut.add(transition);
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


    public boolean isTriggered(GameWorld gameWorld) {
       Action action = targetState.activeAction();

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


    public Action stepAndGetAction(GameWorld gameWorld){
        Transition transitionTrigger = null;

        for (Transition transition: currentState.outGoingTransitions()) {
            if(transition.isTriggered(gameWorld)){
                transitionTrigger = transition;
                break;
            }
        }

        if(transitionTrigger != null){
            currentState = transitionTrigger.targetState();
            return transitionTrigger.action;
        }
        else{
            return currentState.activeAction;
        }
    }

    public State getCurrentState() {
        return currentState;
    }

}
