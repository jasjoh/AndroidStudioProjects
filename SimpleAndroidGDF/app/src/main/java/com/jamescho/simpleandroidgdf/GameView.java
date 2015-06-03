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

        // When a surface like our GameView is created or destroyed, methods within a Callback of it's SurfaceHolder are called
        // If we want to implement behavior that occurs during these events, we need to:
        // a. add a Callback object to the SurfaceHolder of our GameView
        // b. override the methods within that Callback to do what we want

        // First, we retrieve the SurfaceHolder for our GameView so that we can add a Callback to it
        // getHolder() is a built in method of a SurfaceView which returns it's holder object
        SurfaceHolder holder = getHolder();

        // Next, we need to create a Callback object and add it to the SurfaceHolder via addCallback()
        // the addCallback() method expects a Callback object (an object that implements the Callback interface)
        // we leverage an Anonymous Inner Class to define an object that implements the Callback interface within the addCallBack() method call itself
        // alternatively we could have had our GameView class implement SurfaceHolder.Callback ...
        // ... and then defined the overridden methods within the GameView class, passing 'this' into the addCallback() method

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            // this method is called by Android when our app is activated
            // this is similar to addNotify() method in Java
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("GameView", "Surface Created");
                initInput(); // setup our input handler to watch for touch events
                if (currentState == null) { // only load assets if app is opened for first time
                    setCurrentState(new LoadState()); // load assets
                }
                initGame(); // start the game thread
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // TODO
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("GameView", "Surface Destroyed");
                pauseGame(); // pause the thread
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
        try{
            Log.d("initGame()", "gameThread.getName()" + gameThread.getName());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    private void pauseGame() {
        running = false;
        while (gameThread.isAlive()) { // if a thread
            try {
                gameThread.join(); // tells the thread to stop executing while app is paused
                Log.d("gameThreadJoined", "gameThread.isAlive()" + gameThread.isAlive());
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("pauseGameFinished", "gameThread.isAlive()" + gameThread.isAlive());
        try{
            Log.d("pauseGameFinished", "gameThread.getName()" + gameThread.getName());
        } catch (NullPointerException e) {
            e.printStackTrace();
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
