package com.spellshocked.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spellshocked.game.Spellshocked;

public class TutorialGUI extends GUI {
    public TextButton back;
    public Label instructions;
    public TutorialGUI() {
        super("pixthulhu/skin/pixthulhu-ui.json");

        instructions = new Label("General: WASD,  1-9 picks up the corresponding item in your inventory, and left click shoots your wand. Right opens chests and picks things up \n\nShock Wave: In this game mode, your goal is to last as long as possible throughout the waves. Monsters two at a time come up when the right progress bar is full. They will chase you, but only if you are in a certain range. \n\n\n" +
                "Pumpkin Rush: In this game mode, your goal is to take all the pumpkins off screen, each guarded by a skeleton. A skeleton has a range of attack by its pumpkin, and it will try to kill you if you go by its pumpkin. When you remove a pumpkin (left click while mouse is over it and player is nearby) but not its guard, the guard gets assigned to the nearest pumpkin. Once all pumpkins are removed, you win.  ", skin);
        instructions.setWrap(true);
        instructions.setWidth(Gdx.graphics.getWidth()/2f); // or even as low as 10

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
