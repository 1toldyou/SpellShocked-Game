package com.spellshocked.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.spellshocked.game.world.Tile;

public abstract class Entity extends Sprite {

    private Camera camera = null;
    private OrthographicCamera oCam = null;


    public enum Direction {
        LEFT(2, -1, 0), RIGHT(1, 1, 0), UP(3, 0, 1), DOWN(0, 0, -1), NONE(-1, 0, 0);
        public int index, xMod, yMod;

        Direction(int i, int x, int y) {
            index = i;
            xMod = x;
            yMod = y;
        }

    }

    public enum Action {
        MOVING, IDLE
    }

    protected float walkspeed;

    private float xMin = 0;
    private float xMax = 1024;
    private float yMin = 0;
    private float yMax = 768;

    private Tile tile;

    private TextureRegion[][] textures;

    private Animation<TextureRegion>[] walkingAnimators;


    private Direction lastDirection;
    private Action lastAction;
    private float stateTime;

    float currentTileZ = 0;
    static final float DEADZONE = 0.2f;

    public Entity(TextureRegion[][] t) {
        this(t, 1);
    }

    public Entity(TextureRegion[][] t, float walkspeed) {
        setWalkspeed(walkspeed);
        setScale(100);
        setSize(16, 24);
        textures = t;
        walkingAnimators = new Animation[t.length];
        for (int i = 0; i < t.length; i++) {
            walkingAnimators[i] = new Animation<>(0.15f, parseWalkingSheetRow(t[i]));
        }
        lastAction = Action.IDLE;
        lastDirection = Direction.UP;
        stateTime = 0f;
        setRegion(t[3][lastDirection.index]);
    }
 
    public float getWalkspeed() {
        return walkspeed;
    }

    public void setWalkspeed(float walkspeed) {
        this.walkspeed = walkspeed;
    }

    protected float moveX = 0, moveY = 0;

    public void move(Direction dir) {
        TextureRegion t;
        if (dir != Direction.NONE) {
            lastDirection = dir;
            lastAction = Action.MOVING;
            t = walkingAnimators[lastDirection.index].getKeyFrame(stateTime, true);
            moveX += walkspeed * dir.xMod;
            moveY += walkspeed * dir.yMod;
        } else {
            lastAction = Action.IDLE;
            t = textures[3][lastDirection.index];
        }
        setRegion(t);
    }

    public void moveLeft() {
        if (tile.left.isStandable()||isSurroundedByObstacle()) move(Direction.LEFT);
    }

    public void moveRight() {
        if (tile.right.isStandable()||isSurroundedByObstacle()) move(Direction.RIGHT);
    }

    public void moveUp() {
        if (tile.back.isStandable()||isSurroundedByObstacle()) move(Direction.UP);
    }

    public void moveDown() {
        if (tile.front.isStandable()||isSurroundedByObstacle()) move(Direction.DOWN);
    }

    public boolean isSurroundedByObstacle(){
        if (!tile.left.isStandable() && !tile.right.isStandable() && !tile.back.isStandable() && !tile.front.isStandable()) return true;
        else if (tile.right.back.isStandable() && !tile.right.isStandable() && !tile.back.isStandable()) return true;
        else if (tile.left.back.isStandable() && !tile.left.isStandable() && !tile.back.isStandable()) return true;
        else if (tile.right.front.isStandable() && !tile.right.isStandable() && !tile.front.isStandable()) return true;
        else if (tile.left.front.isStandable() && !tile.left.isStandable() && !tile.front.isStandable()) return true;
        return false;
    }

    public void stop() {
        move(Direction.NONE);
    }

    public void boundedTranslate(float x, float y) {
        boundedTranslateX(x);
        boundedTranslateY(y);
    }

    public void boundedTranslateX(float x) {
        if (x + getX() > xMax || x + getX() < xMin) return;
        translateX(x);
    }

    public void boundedTranslateY(float y) {
        if (y + getY() > yMax || y + getY() < yMin) return;
        translateY(y);
    }

    private float absY = -1;

    public void periodic() {
        if (absY == -1) absY=getY();
        if (xMax > getX() + moveX && getX() + moveX > xMin) translateX(moveX);
        if (yMax > absY + moveY && absY + moveY > yMin) setY((absY += moveY)+getTerrainHeight()*12);

        stateTime += Gdx.graphics.getDeltaTime();
        if (camera != null) {
            /* transition */
            if (Math.abs(currentTileZ - tile.getZ()) <= DEADZONE){}
            else if (currentTileZ <= tile.getZ()) currentTileZ += 0.05; //note that the value might need some tweaks depend on actual frameRate
            else if (currentTileZ >= tile.getZ()) currentTileZ -= 0.05; //note that the value might need some tweaks depend on actual frameRate
            /* actual camera move */
            if (oCam.zoom <= 1){
                camera.position.set(MathUtils.clamp(getX(), xMin + 0, xMax - 0), MathUtils.clamp(absY, yMin + 0, yMax - 0) + currentTileZ * 16, camera.position.z);
            }
            else if (oCam.zoom <= 1.5){
                camera.position.set(MathUtils.clamp(getX(), xMin + 50, xMax - 50), MathUtils.clamp(absY, yMin + 100, yMax - 100) + currentTileZ*16, camera.position.z);
            }
            else {
                camera.position.set(MathUtils.clamp(getX(), xMin + 100, xMax - 100), MathUtils.clamp(absY, yMin + 200, yMax - 200) + currentTileZ*16, camera.position.z); //the original code
            }
            /* print debug info */
//            System.out.println("imaginary camera Y: " + currentTileZ + " tile z: " + tile.getZ());
        }

        moveX = 0;
        moveY = 0;
    }

    public void setTile(Tile i) {
        tile = i;
    }

    public float getTerrainHeight() {
        return tile!=null ? tile.zValue : 0;
    }


    public TextureRegion[] parseWalkingSheetRow(TextureRegion[] t) {
        return new TextureRegion[]{t[0], t[1], t[0], t[2]};
    }

    public Entity followWithCamera(Camera c) {
        camera = c;
        return this;
    }

    public Entity stopFollowing() {
        camera = null;
        return this;
    }

    public Entity setOrthographicCamera(OrthographicCamera c) {
        oCam = c;
        return this;
    }
}
