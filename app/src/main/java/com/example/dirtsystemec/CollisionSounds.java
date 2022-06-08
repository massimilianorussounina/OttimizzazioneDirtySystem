package com.example.dirtsystemec;

import android.util.SparseArray;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Sound;

public class CollisionSounds {

    private static SparseArray<Sound> map;
    private static int myHash(String a, String b){return a.hashCode() ^ b.hashCode();}


    public static void init(Audio audio){

        Sound dropGroundSound = audio.newSound("drop_ground_sound.mp3");
        Sound metallicSound = audio.newSound("metallic_sound.mp3");
        Sound burnedSound = audio.newSound("burned_sound.mp3");
        map = new SparseArray<>();

        map.put(myHash("ground","barrel"), dropGroundSound);
        map.put(myHash("bulldozer","barrel"), metallicSound);
        map.put(myHash("incinerator","barrel"), burnedSound);
        map.put(myHash("barrel","barrel"), metallicSound);

        map.put(myHash("barrel","ground"), dropGroundSound);
        map.put(myHash("barrel","bulldozer"), metallicSound);
        map.put(myHash("barrel","incinerator"), burnedSound);

        map.put(myHash("torreLeft","barrel"), dropGroundSound);
        map.put(myHash("bridge","barrel"), dropGroundSound);
        map.put(myHash("torreRight","barrel"), dropGroundSound);

        map.put(myHash("barrel","torreLeft"), dropGroundSound);
        map.put(myHash("barrel","bridge"), dropGroundSound);
        map.put(myHash("barrel","torreRight"), dropGroundSound);

    }


    public static Sound getSound(String a, String b) {

        int hash = myHash(a, b);
        return map.get(hash);
    }

}
