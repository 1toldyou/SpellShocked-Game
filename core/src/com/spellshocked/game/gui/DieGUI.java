package com.spellshocked.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spellshocked.game.Spellshocked;
import com.spellshocked.game.entity.SkeletonEntity;

public class DieGUI extends GUI{
    public Label reason, score_text, score_number, time_display, time_value;

    public void setTexture(TextureRegion s){
        Image wand = new Image(s);
        wand.setScale(10f);
        wand.setPosition((Gdx.graphics.getWidth()/4f), (Gdx.graphics.getHeight()/1.7f));
        addActor(wand);
    }


    public DieGUI(Spellshocked g){
        super("pixthulhu/skin/pixthulhu-ui.json");

        reason = new Label("you died because you are a bozo ", skin);
        reason.setPosition(800, 700);
        addActor(reason);

        score_text = new Label("your score is: ", skin);
        score_text.setPosition(800, 630);
        addActor(score_text);

        score_number = new Label("n/a", skin);
        score_number.setPosition(1000, 630);
        addActor(score_number);

        time_display = new Label("time elapsed:", skin);
        time_display.setPosition(800, 600);
        addActor(time_display);

        time_value = new Label("n/a", skin);
        time_value.setPosition(1000, 600);
        addActor(time_value);

        TextButton newGame = new TextButton("New Game", skin);
        newGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                g.setScreen(g.gameChooserGUI);
                g.world.dispose();
                g.dieGUI = new DieGUI(g);
            }
        });
        newGame.setSize((Gdx.graphics.getWidth()/1.33f), (Gdx.graphics.getHeight()/4.8f));
        newGame.setPosition((Gdx.graphics.getWidth()/8f), (Gdx.graphics.getHeight()/3f));
        addActor(newGame);


        TextButton back = new TextButton("Title Screen", skin);
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                g.setScreen(g.titleGUI);
                g.world.dispose();
                g.dieGUI = new DieGUI(g);
            }
        });
        back.setSize((Gdx.graphics.getWidth()/1.33f), (Gdx.graphics.getHeight()/4.8f));
        back.setPosition((Gdx.graphics.getWidth()/8f), (Gdx.graphics.getHeight()/16f));
        addActor(back);
    }
}