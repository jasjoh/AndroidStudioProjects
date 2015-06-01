package firstapp.jamescho.com.firstapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by J on 5/7/2015.
 */
public class SecondActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call the superclass method to make sure basics are still taken care of
        setContentView(new CustomView(this)); // Activity is a subclass of Context so we pass it in the constructor of our Custom View
    }
}
