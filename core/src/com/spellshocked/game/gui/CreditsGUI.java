package com.spellshocked.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spellshocked.game.Spellshocked;

public class CreditsGUI extends GUI {
    public TextButton back;
    public Label instructions;
    public CreditsGUI() {
        super("pixthulhu/skin/pixthulhu-ui.json");

        instructions = new Label("CREDITS: \n\n Framework Coder: Alex \n World Designer: Roy \n Inventory Developer: Jack \n GUI Planner: Lucy \n Texture Artist: David \n\n" +
                "THANK YOU EVERYONE!!! \\0/", skin);





        instructions.setWrap(true);
        instructions.setWidth(1400); // or even as low as 10

        instructions.setPosition((Gdx.graphics.getWidth()/5.5f), (Gdx.graphics.getHeight()/2f));
        addActor(instructions);

        back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Spellshocked.getInstance().setScreen(Spellshocked.getInstance().titleGUI);
            }
        });
        back.setSize((Gdx.graphics.getWidth()/2.9f), (Gdx.graphics.getHeight()/4.8f));
        back.setPosition((Gdx.graphics.getWidth()/1.88f), (Gdx.graphics.getHeight()/24f));
//        titleScreen.setPosition((Gdx.graphics.getWidth()/1.88f), (42.5f));
        addActor(back);

    }
}
