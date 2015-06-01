package firstapp.jamescho.com.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by J on 5/6/2015.
 * Here we are creating a custom activity (thus extends Activity)
 * We want it to listen for interaction so we implement an OnClickListener
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // onCreate is called when an Activity is launched
    // if we want to perform anything at this point
    // it's best to override that default method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call the superclass method to make sure basics are still taken care of
        setContentView(R.layout.activity_main); // set the UI using an automatically created variable representing 'activity_main.xml'
        Button button1 = (Button) findViewById(R.id.button1); // define a new button object and associate it w/ the button UI widget in our activity_main layout
        button1.setOnClickListener(this); // set this activity as the listener for the button (since it implements OnClickListener)
    }

    // this is where we implement the onClick method required for an OnClickListener
    @Override
    public void onClick(View v) {
        // create a new intent to launch our second activity
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent); // launch the activity
    }
}
