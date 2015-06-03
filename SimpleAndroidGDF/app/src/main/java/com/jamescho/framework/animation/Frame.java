package com.jamescho.framework.animation;

/**
 * Created by J on 6/3/2015.
 */
import android.graphics.Bitmap;

public class Frame {
    private Bitmap image;
    private double duration;

    public Frame(Bitmap image, double duration) {
        this.image = image;
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public Bitmap getImage() {
        return image;
    }
}
