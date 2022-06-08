package com.example.dirtsystemec;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class HandlerUI extends Handler {

    private final MainActivity uiActivity;

    public HandlerUI (MainActivity uiActivity){
        this.uiActivity=uiActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        if(msg.what == 0){
            uiActivity.showMenu(true);
        } else if(msg.what == 1) {
            uiActivity.showMenu(false);
        }else{
            uiActivity.SaveData();
        }
    }


}
