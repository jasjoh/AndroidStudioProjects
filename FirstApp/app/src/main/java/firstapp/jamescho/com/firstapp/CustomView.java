package firstapp.jamescho.com.firstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by J on 5/7/2015.
 */
public class CustomView extends View {

    private Rect myRect;
    private Paint myPaint;
    private static final int SQUARE_SIDE_LENGTH = 200;

    public CustomView(Context context) {

        // call the constructor of the superclass, View, via 'super()'
        // this is needed to make sure important initializations occur which require the 'context'
        super(context); // calls View(context) to instantiate a new 'View'

        myRect = new Rect(30, 30, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
        myPaint = new Paint();
        myPaint.setColor(Color.MAGENTA);

    }

    // onDraw() is called after it's View is invalidate()'d
    // it is also called when the view is first initiated
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRGB(39, 111, 184);
        canvas.drawRect(myRect, myPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myRect.left = (int)event.getX() - (SQUARE_SIDE_LENGTH / 2);
        myRect.top = (int)event.getY() - (SQUARE_SIDE_LENGTH / 2);
        myRect.right = myRect.left + SQUARE_SIDE_LENGTH;
        myRect.bottom = myRect.top + SQUARE_SIDE_LENGTH;
        invalidate(); // indicates the object was updated; calls the object's onDraw() method
        return true; // indicates that a touch event was handled
    }

}

