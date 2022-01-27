package com.spellshocked.game.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spellshocked.game.Spellshocked;
import com.spellshocked.game.input.FunctionalInput;
import com.spellshocked.game.item.CollisionRect;
import com.spellshocked.game.item.Item;
import com.spellshocked.game.item.WandItem;
import com.spellshocked.game.item.inventory.Hotbar;

public class PlayerEntity extends Entity {
    public static final TextureRegion[][] TEXTURES = TextureRegion.split(new Texture("image/Entity/PlayerEntity/player.png"), 16, 24);
    public static final float WALKSPEED = 1;


    public Hotbar hotbar;

    public PlayerEntity(float walk_speed) {
        super(TEXTURES, walk_speed);
        hotbar = new Hotbar(9);
        setSize(0.2f, 0.4f);
        setPosition(200, 120);
        hotbar.set(0, new WandItem());
        playerControls();
        health = 10;

    }

    public PlayerEntity() {
        super(TEXTURES, WALKSPEED);
        hotbar = new Hotbar(9);
        setSize(0.2f, 0.4f);
        setPosition(200, 120);
        hotbar.set(0, new WandItem());
        playerControls();
        health = 10;



    }
    public void playerControls(){
        FunctionalInput.fromKeyPress(Input.Keys.W).onTrue(this::moveUp);
        FunctionalInput.fromKeyPress(Input.Keys.S).onTrue(this::moveDown);
        FunctionalInput.fromKeyPress(Input.Keys.A).onTrue(this::moveLeft);
        FunctionalInput.fromKeyPress(Input.Keys.D).onTrue(this::moveRight);
        FunctionalInput.keyJustPressedMultiplexer(hotbar::setActiveSlot,
                Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7, Input.Keys.NUM_8, Input.Keys.NUM_9);
    }
@Override
    public void draw(Batch batch) {
        if((getLastDirection() == Direction.UP || getLastDirection() == Direction.LEFT) && hotbar.getActiveSlot() != null) hotbar.getActiveSlot().drawInHand(batch, this);
        super.draw(batch);
        if((getLastDirection() == Direction.DOWN || getLastDirection() == Direction.RIGHT) && hotbar.getActiveSlot() != null) hotbar.getActiveSlot().drawInHand(batch, this);
        //hotbar.draw(batch, ortCam.position.x-144, ortCam.position.y-ortCam.zoom*400);
    }
    long damageTime = 0;
    public TextureRegion[] parseWalkingSheetRow(TextureRegion[] t) {
        return new TextureRegion[]{t[0], t[1], t[0], t[2]};
    }
    @Override
    public Animation<TextureRegion>[] getAnimations() {
        Animation<TextureRegion>[] a = new Animation[TEXTURES.length];
        for (int i = 0; i < TEXTURES.length; i++) {
            a[i] = new Animation<>(0.1f, parseWalkingSheetRow(TEXTURES[i]));
        }
        return a;
    }

    public Hotbar getHotbar() {
        return hotbar;
    }

    public CollisionRect getRect(){ return rect;}
    public OrthographicCamera getOrtCam(){
        return ortCam;
    }

    @Override
    public void modifyHealth(double change) {
        if(System.currentTimeMillis()-damageTime-500 < 0) return;
        super.modifyHealth(change);
        if(change < 0){
            damageTime = System.currentTimeMillis();
        }
    }

    @Override
    public void periodic() {
        if(System.currentTimeMillis()-damageTime-100 < 0) setRegion(TEXTURES[getLastDirection().index][3]);
        else setRegion(TEXTURES[getLastDirection().index][0]);
        super.periodic();
    }
}
