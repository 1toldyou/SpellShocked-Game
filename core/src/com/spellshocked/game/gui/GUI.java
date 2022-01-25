package com.spellshocked.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GUI extends Stage implements Screen {
    public Skin skin;
    public GUI(String skin_path){
        super(new ScreenViewport());
        if(!skin_path.equals("")) skin = new Skin(Gdx.files.internal(skin_path));
        skin = new Skin(Gdx.files.internal(skin_path));
    }
    public Skin getSkin(){
        return skin;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0, 0, 1);
        act(Gdx.graphics.getDeltaTime());
        super.draw();
    }

    @Override
    public void resize(int width, int height) {

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
}
