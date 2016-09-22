package kaetera.taranis;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class Main extends AppCompatActivity {
    //GUI items
    private VerticalSeekBar seekBarThreshold = null;
    private Camera camera = null;
    private CameraView cameraView = null;
    private FrameLayout cameraViewFrameLayout = null;
    private ImageButton imgBtnKeepDetected = null;
    private TextView txtViewTest = null;

    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get java objects
        seekBarThreshold = (VerticalSeekBar) this.findViewById(R.id.seekBarThreshold);
        cameraViewFrameLayout = (FrameLayout) this.findViewById(R.id.cameraViewFrame);
        imgBtnKeepDetected = (ImageButton) this.findViewById(R.id.imgBtnKeepDetected);
        txtViewTest = (TextView) this.findViewById(R.id.txtViewTest);


        //Set listeners
        seekBarThreshold.setOnSeekBarChangeListener(this.onSeekBarThresholdChange);
        imgBtnKeepDetected.setOnClickListener(this.onKeepDetectedClick);


        //Open camera
        try { camera = Camera.open(); }
        catch(Exception e) { Log.d("ERROR", "Failed to get camera (in onCreate): " + e.getMessage()); }

        if(camera != null) {
            cameraView = new CameraView(this, camera);//create a SurfaceView to show camera data
            cameraViewFrameLayout.addView(cameraView);//add the SurfaceView to the layout
        }

    }




    private View.OnClickListener onKeepDetectedClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            counter++;
            txtViewTest.setText(Integer.toString(counter));
        }
    };






    //The custom implementation of VerticalSeekBar need OnSeekBarChangeListener before using it in
    //the app, otherwise interacting with the VerticalSeekBar will produce NullPointerException.
    private SeekBar.OnSeekBarChangeListener onSeekBarThresholdChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {/* Just override */}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {/* Just override */}
    };



    //Proto for image processing tests



}//End Main.java
