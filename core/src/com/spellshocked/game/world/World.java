package com.spellshocked.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spellshocked.game.Spellshocked;
import com.spellshocked.game.entity.Entity;
import com.spellshocked.game.entity.PlayerEntity;
import com.spellshocked.game.input.FunctionalInput;
import com.spellshocked.game.input.InputScheduler;
import com.spellshocked.game.util.CameraHelper;
import com.spellshocked.game.world.obstacle.Obstacle;


import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.badlogic.gdx.Input.*;
import static com.badlogic.gdx.math.MathUtils.clamp;

public class World implements Screen {
    /**
     * pre-create all types of tile so no need to read JSON file every time
     */
    public static final Tile GRASS = new Tile(-1, -1, -1, "json/Tile/grass.json"); //DO NOT DISPOSE because its static
    public static final Tile SAND = new Tile(-1, -1, -1, "json/Tile/sand.json"); //DO NOT DISPOSE because its static
    public static final Tile LAVA = new Tile(-1, -1, -1, "json/Tile/lava.json"); //DO NOT DISPOSE because its static
    public static final Tile WATER = new Tile(-1, -1, -1, "json/Tile/water.json"); //DO NOT DISPOSE because its static
    public static final Obstacle ROCK = new Obstacle("json/Obstacle/rock.json"); //DO NOT DISPOSE because its static
    public static final Obstacle TREE = new Obstacle("json/Obstacle/tree.json"); //DO NOT DISPOSE because its static

    /**
     * variables that share with child class
     */
    public SpriteBatch spriteBatch;
    public Viewport viewport;
    public OrthographicCamera orthographicCamera;
    public CameraHelper cameraHelper; //for zooming
    protected Tile[][] tiles;
    protected Set<Entity> entities, eAdd, eRemove;
    public static int renderDistance; //increase the rendering distance solved most issues
    protected int xValue, yValue;
    public Vector3 mouse;

    public Map<Stage, Boolean> activeStages;
    /**
     * variable that only used by single methods
     */
    public float VOLUME = 0.75f;

    protected float timeCount;

    public World(int Entity_count_limit, int X_limit, int Y_limit, float viewportWidth, float viewportHeight){
        InputScheduler.resetInstance();
        tiles = new Tile[X_limit+1][Y_limit+1];

        this.entities = new LinkedHashSet<>(Entity_count_limit);
        this.eAdd = new LinkedHashSet<>();
        this.eRemove = new LinkedHashSet<>();
        this.xValue = X_limit;
        this.yValue = Y_limit;

        this.orthographicCamera = new OrthographicCamera(viewportWidth, viewportHeight);
        this.cameraHelper = new CameraHelper(orthographicCamera);

        viewport = new ScreenViewport(orthographicCamera);

        this.spriteBatch = new SpriteBatch();

        this.orthographicCamera.position.set(orthographicCamera.viewportWidth / 2f, orthographicCamera.viewportHeight / 2f, 30);

        /* for more convenience hand position */
        FunctionalInput.fromKeyJustPress(Keys.Q).onTrue(cameraHelper::zoomOut);
        FunctionalInput.fromKeyJustPress(Keys.E).onTrue(cameraHelper::zoomIn);
        FunctionalInput.fromKeyJustPress(Keys.ESCAPE).onTrue(()-> Spellshocked.getInstance().setScreen(Spellshocked.getInstance().pauseGUI));
        FunctionalInput.fromKeyJustPress(Keys.K).onTrue(()-> Spellshocked.getInstance().setScreen(Spellshocked.getInstance().dieGUI));
        FunctionalInput.fromKeyJustPress(Keys.T).onTrue(()-> Spellshocked.getInstance().setScreen(Spellshocked.getInstance().questGUI));
        activeStages = new HashMap<>();
    }

    /**
     * the only way to add new entity to the array
     * will also set some attributes of the new enitty
     * @param e entity will add to the list
     */
    public void addEntity (Entity e){
        e.VOLUME = this.VOLUME; //pass the master volume into entity
        e.set_walk_boundary("Tile", xValue, yValue);
        eAdd.add(e);
    }
    public void removeEntity (Entity e){
        eRemove.add(e);
    }

    public Set<Entity> allEntitiesNear(Tile t, double distance){
        Set<Entity> near = new HashSet<>();
        for(Entity e : entities){
            if(t.distanceFrom(e.getTile()) <= distance) near.add(e);
        }
        return near;
    }

    public void replaceEntity(Entity e, Entity... e2){
        for(Entity ee : e2){
            addEntity(ee);
            ee.setTile(e.getTile());
            ee.setPosition(e.getX(), e.getY());
        }
        removeEntity(e);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        mouse = orthographicCamera.unproject(new Vector3((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0));

        orthographicCamera.update();

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        spriteBatch.begin();
        renderDistance = cameraHelper.get_render_distance();
        int x = (int) orthographicCamera.position.x/16 + xValue/2;
        int y = (int) orthographicCamera.position.y/12 + yValue/2 - renderDistance/3; //changed to 16 then fixed the issue of player standing on void when y=0 but caused same issue when y=64
        for(int i = clamp(x-renderDistance-xValue/2, 0, xValue); i <= clamp(x+renderDistance-xValue/2, 0, xValue); i++){
            for(int j = clamp(y+renderDistance-yValue/2, 0, yValue); j >= clamp(y-renderDistance-yValue/2, 0, yValue); j--){
               tiles[i][j].draw(spriteBatch);
            }
        }

        for(Entity e : entities){
            if(e == null) break;
            Tile t = tiles[(int) (e.getX()+8)/16][clamp((int) ((e.getY()+2)/12-e.getTerrainHeight()), 0, yValue)];
            t.addOccupant(e);
            e.draw(spriteBatch);
            e.periodic();
            print_debug(e, t);
        }
        entities.addAll(eAdd);
        entities.removeAll(eRemove);
        eAdd.clear();
        eRemove.clear();

        spriteBatch.end();

        activeStages.forEach((s, b)->{
            if(b) {
                s.act(Gdx.graphics.getDeltaTime());
                s.draw();
            }

        });


        InputScheduler.getInstance().run();

        update_QuestGUI();
    }

    /*
     * print some useful information
     * override it if don't need it
     */
    public void print_debug(Entity entity, Tile tile){
        if(entity instanceof PlayerEntity){
            System.out.println("X: "+tile.xValue+" Y: "+tile.yValue+" Z: "+tile.zValue);
            System.out.println(" "+(int) (entity.getX()+8)/16+" "+clamp((int) ((entity.getY()+2)/12-entity.getTerrainHeight()), 0, yValue));
            System.out.println(clamp(xValue-renderDistance-xValue/2, 0, xValue)+" " +clamp(xValue+renderDistance-xValue/2, 0, xValue));
            System.out.println(clamp(yValue+renderDistance-yValue/2, 0, yValue)+" " +clamp((yValue-renderDistance-yValue/2), 0, yValue));
            System.out.println(orthographicCamera.zoom);
            System.out.println(cameraHelper.get_zoom_level());
            System.out.println(Gdx.graphics.getWidth() +" "+ Gdx.graphics.getHeight());
            System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        }
    }
    public OrthographicCamera getOrthographicCamera() {
        return orthographicCamera;
    }

    public Vector3 getMouse() {
        return mouse;
    }

    /**
     * intente to be override
     */
    public void update_QuestGUI(){
        Spellshocked.getInstance().questGUI.dummy_text.setText("Frame since started: " + timeCount);
        timeCount++;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
