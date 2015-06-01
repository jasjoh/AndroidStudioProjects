package com.jamescho.game.state;

/**
 * Created by J on 5/15/2015.
 */

import android.view.MotionEvent;
import com.jamescho.simpleandroidgdf.Assets;
import com.jamescho.framework.util.Painter;

public class LoadState extends State {
    @Override
    public void init() {
        Assets.load();
    }

    @Override
    public void update(float delta) {
        setCurrentState(new MenuState());
    }

    @Override
    public void render(Painter g) {
        // TODO
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
    }
}
