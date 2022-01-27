package com.spellshocked.game.entity;

import com.spellshocked.game.world.obstacle.Pumpkin;
import com.badlogic.gdx.math.MathUtils;
public class PumpkinSkeletonEntity extends SkeletonEntity{
    Pumpkin pumpkin;

    public PumpkinSkeletonEntity(Pumpkin p){
        super();
        pumpkin = p;

    }
    public Pumpkin getPumpkin(){
        return pumpkin;
    }
    public void setPumpkin(Pumpkin pumpkinNew){
        pumpkin = pumpkinNew;
    }

}

