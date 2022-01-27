package com.spellshocked.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.spellshocked.game.Spellshocked;
import com.spellshocked.game.entity.Entity;
import com.spellshocked.game.entity.PlayerEntity;
import com.spellshocked.game.entity.SkeletonEntity;
import com.spellshocked.game.entity.SlimeEntity;
import com.spellshocked.game.gui.BlockInventoryGUI;
import com.spellshocked.game.gui.ClickGUI;
import com.spellshocked.game.input.ConditionalRunnable;
import com.spellshocked.game.input.FunctionalInput;
import com.spellshocked.game.input.InputScheduler;
import com.spellshocked.game.input.action.AttackAction;
import com.spellshocked.game.input.action.ConsumeAction;
import com.spellshocked.game.input.action.PlaceAction;
import com.spellshocked.game.world.obstacle.Chest;
import com.spellshocked.game.world.obstacle.ObstacleEntity;

import static com.spellshocked.game.world.Perlin.GenerateWhiteNoise;
import static com.spellshocked.game.world.Perlin.GenerateSmoothNoise;
import static com.spellshocked.game.world.Perlin.GeneratePerlinNoise;

import java.util.ArrayList;
import java.util.Random;

public class ShockWaveMode extends World{
    Random randomSeed;

    private PlayerEntity player;

    private ClickGUI previousChestGUI;

    float[][] perlinNoise;

    long worldTimer;
    long startTime;
    TextButton score_Label;
    protected Stage stage;

    public Texture healthBarBorder = new Texture("image/World/healthBars/healthBarBorder.png");
    public Texture healthBarTexture = new Texture("image/World/healthBars/healthBarGreen.png");

    boolean increase_raid = true;
    float raid_counter = 0;
    float score_counter = 0;
    int enemies_counter = 0;
    int wave_counter = 0;
    int enemies_kill_counter = 0;

    public ShockWaveMode() {
        super( 100, 64, 64, 400, 240);

        this.randomSeed = new Random();
        this.perlinNoise = GeneratePerlinNoise(GenerateSmoothNoise(GenerateWhiteNoise(this.randomSeed ,super.xValue+1, super.yValue+1), 4), 6);

        this.player = new PlayerEntity(2);
        this.player.followWithCamera(super.orthographicCamera);
        this.player.setOrthographicCamera(super.orthographicCamera); //to get current zoom
        super.addEntity(this.player);

        stage = new Stage(this.viewport, this.spriteBatch);
        startTime = System.currentTimeMillis();
        score_Label = new TextButton(String.format("%03d", worldTimer), new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json")));
        score_Label.getLabel().setFontScale(0.5f, 0.5f);
        score_Label.setSize(140,70);
//        stage.addActor(score_Label);
        activeStages.put(stage, true);

        create_Tile_with_Perlin(this.perlinNoise);

        FunctionalInput.fromButtonPress(Input.Buttons.LEFT).onTrue(new ConditionalRunnable(new AttackAction(player), ()-> !InputScheduler.getInstance().buttonPressedThisLoop.getOrDefault(Input.Buttons.LEFT, false)));
        FunctionalInput.fromButtonJustPress(Input.Buttons.LEFT).onTrue(new ConditionalRunnable(new ConsumeAction(player), ()-> !InputScheduler.getInstance().buttonPressedThisLoop.getOrDefault(Input.Buttons.LEFT, false)));
        FunctionalInput.fromButtonJustPress(Input.Buttons.RIGHT).onTrue(new ConditionalRunnable(new PlaceAction(player), ()->!InputScheduler.getInstance().buttonPressedThisLoop.getOrDefault(Input.Buttons.RIGHT, false)));

    }

    public void create_Tile_with_Perlin(float[][] perlinNoise){
        /**
         * even Z tile - main tile
         * odd Z tile - transitional tile - might be two types
         * for the random Obstacle must use nextFloat same as when generating Perlin noise otherwise will cause different map from the same seed
         */
        for(int j = 0; j <= super.xValue; j++) {
            for (int i = 0; i <= super.yValue; i++) {
                switch ((int) (perlinNoise[j][i] * 20)) {
                    case 0:
                    case 1:
                        super.tiles[j][i] = new Tile(j, i, 0, World.WATER);
                        break;
                    case 2:
                        super.tiles[j][i] = new Tile(j, i, 1, World.WATER);
                        break;
                    case 3:
                        super.tiles[j][i] = new Tile(j, i, 1, World.SAND);
                        break;
                    case 4:
                    case 5:
                        super.tiles[j][i] = new Tile(j, i, 2, World.SAND);
                        break;
                    case 6:
                        super.tiles[j][i] = new Tile(j, i, 3, World.SAND);
                        break;
                    case 7:
                        super.tiles[j][i] = new Tile(j, i, 3, World.GRASS);
                        break;
                    case 8:
                    case 9:
                        super.tiles[j][i] = new Tile(j, i, 4, World.GRASS);
                        break;
                    case 10:
                    case 11:
                        super.tiles[j][i] = new Tile(j, i, 5, World.GRASS);
                        break;
                    case 12:
                    case 13:
                        super.tiles[j][i] = new Tile(j, i, 6, World.GRASS);
                        break;
                    case 14:
                        super.tiles[j][i] = new Tile(j, i, 7, World.GRASS);
                        break;
                    case 15:
                        super.tiles[j][i] = new Tile(j, i, 7, World.LAVA);
                        break;
                    case 16:
                    case 17:
                        super.tiles[j][i] = new Tile(j, i, 8, World.LAVA);
                        break;
                    case 18:
                    case 19:
                        super.tiles[j][i] = new Tile(j, i, 9, World.LAVA);
                        break;
                }

                if (super.tiles[j][i].Obstacle_onTop){
                    if (randomSeed.nextInt(100) < 1){
                        if (randomSeed.nextBoolean()) {
                            tiles[j][i].setObstacle(ROCK);
                        }
                        else {
                            tiles[j][i].setObstacle(new Chest(player));
                        }
                    }
                }
            }
        }

        /*
         * set neighbor Tile
         */
        for (int i = 0; i < super.tiles.length; i++) {
            for (int j = 0; j < super.tiles[i].length; j++) {
                super.tiles[i][j].setNeighbors(super.tiles[Math.max(0,i-1)][j], super.tiles[Math.min(super.xValue,i+1)][j],
                        super.tiles[i][Math.min(super.yValue,j+1)], super.tiles[i][Math.max(0,j-1)]);
            }
        }
    }

    @Override
    public void render(float delta) {
        if(player.obstacleNear() != null && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            ArrayList<Tile> tiles = player.obstacleNear();
            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).obstacle instanceof ObstacleEntity<?> && ((ObstacleEntity<?>) tiles.get(i).obstacle).getGui().wasClicked(mouse, tiles.get(i))) {
                    //    if (tiles.size() != 0) {
                    //        ClickGUI chestGUI = ((ObstacleEntity<?>) tiles.get(i).obstacle).getGui();
                    //        if (chestGUI.isDisplaying()) {
                    //            if (previousChestGUI != null && previousChestGUI != chestGUI && previousChestGUI.isDisplaying()) {
                    //                previousChestGUI.changeDisplay();
                    //            }
                    //            previousChestGUI = chestGUI;
                    //        }
                    //        break;
                    //    }
                }
            }
        }

        super.render(delta);
        spriteBatch.begin();

        for (Entity e: entities){
            if (e instanceof SkeletonEntity || e instanceof SlimeEntity){
                (e).getRect().move(e.getX(), e.getY());
                e.targetTile(player.getTile());
                if (Math.abs(e.getX()- player.getX())<200 &&Math.abs(e.getY()- player.getY())<200){
                    e.startMoving();
                } else {
                    e.stopMoving();
                }
                e.drawHealthBar(player, this);
                if (player.getRect().collidesWith(e.getRect())){
                    player.modifyHealth(-2);
                }
                if (e.health <= 0) {
                    enemies_counter--;
                    enemies_kill_counter++;
                    super.removeEntity(e);
                    score_counter+=5;
                }
            }
        }
        player.hotbar.draw(super.spriteBatch, orthographicCamera.position.x - 144, orthographicCamera.position.y - orthographicCamera.zoom * 400);

        if (player.health<=0){
            Spellshocked.getInstance().dieGUI.reason.setText("you ran out of HP");
            Spellshocked.getInstance().dieGUI.setTexture(SkeletonEntity.TEXTURES[0][0]);
            Spellshocked.getInstance().setScreen(Spellshocked.getInstance().dieGUI);
            Spellshocked.getInstance().dieGUI.time_value.setText((System.currentTimeMillis()-startTime)/1000+"");
            player.health = 10;
        }
        if (wave_counter > 3 && enemies_counter <= 0){
            Spellshocked.getInstance().dieGUI.setTexture(PlayerEntity.TEXTURES[0][0]);
            Spellshocked.getInstance().dieGUI.reason.setText("you played enough waves");
            Spellshocked.getInstance().setScreen(Spellshocked.getInstance().dieGUI);
            Spellshocked.getInstance().dieGUI.time_value.setText((System.currentTimeMillis()-startTime)/1000+"");
        }

        increase_raid = enemies_counter < 3;
        if (raid_counter <= 1){
            if (increase_raid){
                raid_counter += 0.002;
            }
        }
        else {
            score_counter += 100;
            raid_counter = 0;
            wave(1);
        }


        super.spriteBatch.draw(healthBarTexture, orthographicCamera.position.x-Gdx.graphics.getWidth()/6f,
                    orthographicCamera.position.y-orthographicCamera.zoom*-400-Gdx.graphics.getHeight()/25f,
                    (healthBarTexture.getWidth()* player.health)/40, healthBarTexture.getHeight()/4f);
        super.spriteBatch.draw(healthBarBorder, orthographicCamera.position.x-Gdx.graphics.getWidth()/6f,
                orthographicCamera.position.y-orthographicCamera.zoom*-400-Gdx.graphics.getHeight()/25f,
                (healthBarTexture.getWidth())/4f, healthBarTexture.getHeight()/4f);

        super.spriteBatch.draw(healthBarTexture, orthographicCamera.position.x+Gdx.graphics.getWidth()/6f-healthBarTexture.getWidth()/4f,
                orthographicCamera.position.y-orthographicCamera.zoom*-400-Gdx.graphics.getHeight()/25f,
                (healthBarTexture.getWidth()*raid_counter)/4, healthBarTexture.getHeight()/4f);
        super.spriteBatch.draw(healthBarBorder, orthographicCamera.position.x+Gdx.graphics.getWidth()/6f-healthBarTexture.getWidth()/4f,
                orthographicCamera.position.y-orthographicCamera.zoom*-400-Gdx.graphics.getHeight()/25f,
                (healthBarTexture.getWidth())/4f, healthBarTexture.getHeight()/4f);
        score_Label.setText(String.valueOf((int) score_counter));
        score_Label.setPosition(orthographicCamera.position.x-score_Label.getWidth()/2, orthographicCamera.position.y-orthographicCamera.zoom*-400-Gdx.graphics.getHeight()/15f);
        score_Label.draw(super.spriteBatch, 1f);
        spriteBatch.end();
        if (player.getTile() != null){
            switch (player.getTile().name) {
                case "grass":
                    player.setWalkSpeed(1.5f);
                    break;
                case "sand":
                    player.setWalkSpeed(1.0f);
                    break;
                case "lava":
                    player.setWalkSpeed(1.25f);
                    break;
                case "water":
                    player.setWalkSpeed(0.5f);
                    break;
                default:
                    player.setWalkSpeed(1f);
                    break;
            }
        }
    }

    @Override
    public void update_QuestGUI() {
        Spellshocked.getInstance().questGUI.title.setText("shockwave mode");
        Spellshocked.getInstance().questGUI.task_1_name.setText("survive 100 frames");
        Spellshocked.getInstance().questGUI.task_1_description.setText("just stand there");
        Spellshocked.getInstance().questGUI.task_1_progress.setText(Spellshocked.getInstance().world.timeCount+" / 100");
        Spellshocked.getInstance().questGUI.task_2_name.setText("survive 3 waves");
        Spellshocked.getInstance().questGUI.task_2_description.setText("you can't just stand there");
        Spellshocked.getInstance().questGUI.task_2_progress.setText(this.wave_counter+" / 3");
        Spellshocked.getInstance().questGUI.task_3_name.setText("kill 3 monsters");
        Spellshocked.getInstance().questGUI.task_3_description.setText("attack!");
        Spellshocked.getInstance().questGUI.task_3_progress.setText(this.enemies_kill_counter+" / 3");
        Spellshocked.getInstance().dieGUI.score_number.setText(String.valueOf(this.score_counter));
        super.update_QuestGUI();
    }

    @Override
    public void print_debug(Entity entity, Tile tile) {
    }
    public void wave(int mob_generation_count){
        wave_counter++;
        int positionX, positionY;
        for (int i = 0; i < mob_generation_count; i++){
            positionX = (int)MathUtils.clamp(player.getTile().xValue + (Math.random() * 20 - 10), 0, xValue);
            positionY = (int) MathUtils.clamp(player.getTile().yValue+ (Math.random() * 20 - 10), 0 ,yValue);
            //SkeletonEntity monster = new SkeletonEntity();
            addMonster(new SkeletonEntity(), positionX, positionY);
            positionX = (int)MathUtils.clamp(player.getTile().xValue + (Math.random() * 20 - 10), 0, xValue);
            positionY = (int) MathUtils.clamp(player.getTile().yValue+ (Math.random() * 20 - 10), 0 ,yValue);
            addMonster(new SlimeEntity(2), positionX, positionY);
        }
    }
    public void addMonster(Entity monster, int positionX, int positionY){
        monster.setPosition(positionX*16, (positionY+tiles[positionX][positionY].zValue)*12);
        monster.setTile(tiles[positionX][positionY]);
        super.addEntity(monster);
        enemies_counter++;
    }
}
