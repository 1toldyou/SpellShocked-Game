package com.spellshocked.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spellshocked.game.Spellshocked;
import com.spellshocked.game.item.CollisionRect;

public class FireballEntity extends ProjectileEntity {
    public static final TextureRegion[][] TEXTURES = TextureRegion.split(new Texture("./image/Entity/Projectile/fireboltNoDiag.png"), 7, 7);
    public FireballEntity() {
        super(2, TEXTURES);
        setSize(0.1f, 0.1f);
        rect = new CollisionRect(this.getX()*4, this.getY()*4, (int)this.getWidth()*4, (int) this.getHeight()*4);
    }


    @Override
    public void periodic() {
        moveToTarget2();
        boolean hit = false;
        for(Entity e : getTile().getOccupants()){
            if(e != this && !e.invincible && e instanceof SkeletonEntity){
                e.modifyHealth(-damage);
                hit = true;
            }
        }
        if(!isGoing || hit || (newX==getX() && newY == getAdjustedY())){
            Spellshocked.getInstance().world.replaceEntity(this, new ExplosionEntity());
        }
        super.periodic();
    }

    @Override
    public void moveToTarget() {

    }
    public void moveToTarget2(){
        super.moveToTarget();
    }
}