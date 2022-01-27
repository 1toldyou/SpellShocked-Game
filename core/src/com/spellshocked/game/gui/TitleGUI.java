package com.spellshocked.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spellshocked.game.Spellshocked;

public class TitleGUI extends GUI {
    public TitleGUI() {
        super("pixthulhu/skin/pixthulhu-ui.json");

        TextButton chooseMode = new TextButton("Game Mode", skin);
        chooseMode.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {Spellshocked.getInstance().setScreen(Spellshocked.getInstance().gameChooserGUI);
            }
        });
//        chooseMode.setSize(1443.6f, 212.5f);
//        chooseMode.setPosition(240f, 531.25f);
        chooseMode.setSize((Gdx.graphics.getWidth()/1.33f), (Gdx.graphics.getHeight()/4.8f));
        chooseMode.setPosition((Gdx.graphics.getWidth()/8f), (Gdx.graphics.getHeight()/1.92f));
        addActor(chooseMode);

        TextButton settings = new TextButton("Settings", skin);
        settings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Spellshocked.getInstance().setScreen(Spellshocked.getInstance().settingsGUI);
            }
        });
//        leaderboard.setSize(1443.6f, 212.5f);
//        leaderboard.setPosition(240f, 286.875f);
        settings.setSize((Gdx.graphics.getWidth()/1.33f), (Gdx.graphics.getHeight()/4.8f));
        settings.setPosition((Gdx.graphics.getWidth()/8f), (Gdx.graphics.getHeight()/3.555f));
        addActor(settings);

        TextButton credits = new TextButton("Credits", skin);
        credits.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Spellshocked.getInstance().setScreen(Spellshocked.getInstance().creditsGUI);
            }
        });
//        setting.setSize(662f, 212.5f);
//        setting.setPosition(240f, 42.5f);
        credits.setSize((Gdx.graphics.getWidth()/2.9f), (Gdx.graphics.getHeight()/4.8f));
        credits.setPosition((Gdx.graphics.getWidth()/8f), (Gdx.graphics.getHeight()/24f));
        addActor(credits);

        TextButton quit = new TextButton("Quit", skin);
        quit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
//        quit.setSize(662f, 212.5f);
//        quit.setPosition(970f, 42.5f);
        quit.setSize((Gdx.graphics.getWidth()/2.9f), (Gdx.graphics.getHeight()/4.8f));
        quit.setPosition((Gdx.graphics.getWidth()/1.88f), (Gdx.graphics.getHeight()/24f));
        addActor(quit);

        Texture logo = new Texture("image/logo.png");
        Image title = new Image(logo);
        title.setSize((Gdx.graphics.getWidth()/3f), (Gdx.graphics.getHeight()/4f));
        title.setPosition((Gdx.graphics.getWidth()/3.13f), (Gdx.graphics.getHeight()/1.34f));
        addActor(title);
    }
}

