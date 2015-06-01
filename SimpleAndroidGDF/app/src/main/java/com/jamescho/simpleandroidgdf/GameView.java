package com.jamescho.simpleandroidgdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jamescho.framework.util.InputHandler;
import com.jamescho.framework.util.Painter;
import com.jamescho.game.state.LoadState;
import com.jamescho.game.state.State;


/**
 * Created by J on 5/12/2015.
 *
 * Understanding Volatile Variables
 * - when two threads share the same variable, they can create copies of it
 * - therefore, changing the value of a variable in one thread may not change it for the other
 * - making a variable 'volatile' prevents this from occurring by indicating:
 * -- the variable will never be cached thread-locally; all read / write is in main memory
 * -- access to the variable acts as though it's enclosed in a 'synchronized' block
 * - you would instead use 'synchronized blocks' if a variable was needed for complex operations with extended durations
 */
public class GameView extends SurfaceView implements Runnable {
    private Bitmap gameImage;
    private Rect gameImageSrc;
    private Rect gameImageDst;
    private Canvas gameCanvas;
    private Painter graphics;

    private Thread gameThread;
    private volatile boolean running = false;
    private volatile State currentState;

    private InputHandler inputHandler;

    public GameView(Context context, int gameWidth, int gameHeight) {
        super(context);
        gameImage = Bitmap.createBitmap(gameWidth, gameHeight, Bitmap.Config.RGB_565);
        gameImageSrc = new Rect(0,0,gameImage.getWidth(), gameImage.getHeight()); // we will draw the entire image
        gameImageDst = new Rect(); // for scaling the image we eventually draw
        gameCanvas = new Canvas(gameImage);  // create the canvas associated with our gameImage bitmap;  we will draw on this
        graphics = new Painter(gameCanvas);  // create our Painter proxy for the canvas we just created

        // our SurfaceHolder is where we define code that runs when the app is activated or de-activated
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            // this method is called by Android when our app is activated
            // this is similar to addNotify() method in Java
            public void surfaceCreated(SurfaceHolder holder) {
                // Log.d("GameView", "Surface Created");
                initInput(); // setup our input handler to watch for touch events
                if (currentState == null) { // only load assets if app is opened for first time
                    setCurrentState(new LoadState());
                }
                initGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // TODO
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // Log.d("GameView", "Surface Destroyed");
                pauseGame();
            }
        });
    }

    // required constructor which we won't actually use
    public GameView(Context context) {
        super(context);
    }

    public void setCurrentState(State newState) {
        System.gc();
        newState.init();
        currentState = newState;
        inputHandler.setCurrentState(currentState);
    }

    private void initInput() {
        if (inputHandler == null) {
            inputHandler = new InputHandler();
        }
        setOnTouchListener(inputHandler);
    }

    private void initGame() {
        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    private void pauseGame() {
        running = false;
        while (gameThread.isAlive()) {
            try {
                gameThread.join(); // tells the thread to stop executing while app is paused
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // TODO
    }

    private void updateAndRender(long delta) {
        currentState.update(delta / 1000f);
        currentState.render(graphics);
        renderGameImage();
    }

    private void renderGameImage() {
        Canvas screen = getHolder().lockCanvas();
        if (screen != null) {
            screen.getClipBounds(gameImageDst);
            screen.drawBitmap(gameImage, gameImageSrc, gameImageDst, null);
            getHolder().unlockCanvasAndPost(screen);
        }
    }
}
