package kaetera.taranis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

public class Main extends AppCompatActivity {
    //GUI items
    VerticalSeekBar seekBarThreshold = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get java objects
        seekBarThreshold = (VerticalSeekBar) this.findViewById(R.id.seekBarThreshold);


        //Set listeners
        seekBarThreshold.setOnSeekBarChangeListener(this.onSeekBarThresholdChange);

    }






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



}//End Main.java
