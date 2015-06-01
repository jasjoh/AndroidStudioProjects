package com.jamescho.simpleandroidgdf;

import android.app.Activity;
import android.os.Bundle;
import android.content.res.AssetManager;

/**
 * Created by J on 5/12/2015.
 */
public class GameMainActivity extends Activity {

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 450;
    public static GameView sGame; // create an instance of our custom View class
    public static AssetManager assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assets = getAssets();
        sGame = new GameView(this, GAME_WIDTH, GAME_HEIGHT); // pass in this Activity as context
        setContentView(sGame); // set the UI
    }
}
