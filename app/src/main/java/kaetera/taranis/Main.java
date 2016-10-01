package kaetera.taranis;


import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class Main extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    //GUI items
    private VerticalSeekBar mSeekBarThreshold = null;
    private ImageButton mImgBtnKeepDetected = null;
    private CameraBridgeViewBase mOpenCvCameraView = null;
    private TextView mTxtViewTest = null;
    private RelativeLayout mMainLayout = null;
    private ImageView mImageView1 = null;

    //Other globals
    private int mCounter = 0;
    private static final String TAG = "Taranis::Main";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        //Get java objects
        mMainLayout = (RelativeLayout) this.findViewById(R.id.mainLayout);
        mSeekBarThreshold = (VerticalSeekBar) this.findViewById(R.id.seekBarThreshold);
        mImgBtnKeepDetected = (ImageButton) this.findViewById(R.id.imgBtnKeepDetected);
        mTxtViewTest = (TextView) this.findViewById(R.id.txtViewTest);
        mOpenCvCameraView = (CameraBridgeViewBase) this.findViewById(R.id.cameraViewCV);
        mImageView1 = (ImageView) this.findViewById(R.id.imgView1);

        //Set listeners
        mSeekBarThreshold.setOnSeekBarChangeListener(this.onSeekBarThresholdChange);
        mImgBtnKeepDetected.setOnClickListener(this.onKeepDetectedClick);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) mOpenCvCameraView.disableView();
    }



    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, this.mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            this.mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) mOpenCvCameraView.disableView();
    }



    //Download and install OpenCV if not already present on the device. Avoid including OpenCV
    //lib in the release.
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };



    //Stores the image where a lightning has been detected when the user presses the
    //validation button in manual mode.
    private View.OnClickListener onKeepDetectedClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCounter++;
            mTxtViewTest.setText("mCounter = " + Integer.toString(mCounter));
        }
    };



    //Adjusts the sensitivity threshold of the lightning detection algorithm.
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



    //Convert a Mat to a Bitmap image
    public Bitmap Mat2BitmapImage(Mat m) {
        Bitmap bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bmp);
        return bmp;
    }



    //Implements CvCameraViewListener2
    //Callback function called on each frame received. Returns as a matrix the image that will
    //be displayed in the JavaCameraView surface widget.
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat img = inputFrame.gray();
        int inWidth = img.width();
        int inHeigth = img.height();
        int layWidth = mMainLayout.getWidth();
        int layHeight = mMainLayout.getHeight();
        //Imgproc.putText(m, "Yolo !", new Point(10,10), Core.FONT_HERSHEY_SCRIPT_SIMPLEX, 2, new Scalar(200,200,0), 2);


        //Landscape mode
        if(layWidth>layHeight) {}
            float layoutRatio = layHeight/layWidth;
            //Mat preview1 = img.submat( (int)((inHeigth-inWidth*layoutRatio)/2), (int)((inHeigth+inWidth*layoutRatio)/2), 0, inWidth-1);
            Mat submat = img.submat(0,100,0,100);
            Mat preview = new Mat(img.rows()+1, img.cols(), img.type(), new Scalar(90,50,100));



        Scalar mu = new Scalar(1,3,4,7);

        mu = Core.mean(preview);

        final Mat tempM = img;
        final String s = "lay=" + Integer.toString(layWidth) + "\n in=" + Integer.toString(inWidth);
        runOnUiThread(new Runnable() {
            public void run() {
                //HERE I WANT TO UPDATE MY TEXT VIEW
                mTxtViewTest.setText( s );

                //mImageView1.setImageBitmap(Mat2BitmapImage(tempM));
            }
        });


        return img;
    }



    //Implements CvCameraViewListener2
    public void onCameraViewStarted(int width, int height) {
    }



    //Implements CvCameraViewListener2
    public void onCameraViewStopped() {
    }



}//End Main.java
