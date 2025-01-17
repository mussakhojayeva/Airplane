package com.mx.krakensoft.opencv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import com.mx.krakensoft.opencv.imageProcessing.ColorBlobDetector;
import com.mx.krakensoft.opencv.patternview.PatternView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.SumPathEffect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.opencv.core.Size;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.os.SystemClock;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.sqrt;

public class MainActivity extends Activity implements OnTouchListener, CvCameraViewListener2 {

    static {
        System.loadLibrary("opencv_java3");
    }

    /*user info variables*/
    private String userName;
    private String userAge;
    private int userGender;
    private int userHand;
    final String username = "smussakhojayeva@gmail.com";

    private Button userInfoSubmit;
    private EditText iuserName;
    private EditText iuserAge;
    private RadioButton keyButtonLeftHanded;
    private RadioButton keyButtonRightHanded;
    private RadioButton keyButtonFemale;
    private RadioButton keyButtonMale;
    private RelativeLayout userInfoLayout;
    /*simplified keyboard buttons */
    private Button keyButton1;
    private Button keyButton2;
    private Button keyButton3;
    private Button keyButton4;
    private Button keyButton5;
    private Button keyButton6;
    private Button keyButton7;
    private Button keyButton8;
    private Button keyButton9;
    private Button keyButton0;
    private Button keyButtonBackspace;
    private Button keyButtonEnter;
    private Button keyButtonReset;
    private Button keyButtonQuit;
    private Button keyButtonStart;
    private Button keyButtonResetTracking;
    private Button keyButtonReject;
    private Button keyButtonAccept;
    private Button keyButtonRefresh;
    private Button keyButtonSavePattern;
    private Button keyButtonRefreshPattern;
    private Button keyButtonConfirmPattern;
    private Button keyButtonTouchmode;




    private RelativeLayout globalLayout;
    private Map<String, ButtonCoordinates> buttons;
    private EditText customEdit;

    private TextView keyboardText;
    private TextView discreteText;
    private PatternView patternView;
    private int randomNumber = 0;
    private int ResetTrackingNumber = 0;
    private int BackspacePressedNumber = 0;
    private int ResetPressedNumber = 0;
    private int RefreshPressedNumber = 0;

    private boolean randomDiscrete = false;
    private double centerX = 0;//Added by ahinsutime
    private double centerY = 0;//Added by ahinsutime
    private double PrevCenterX = 0;//Added by ahinsutime
    private double PrevCenterY = 0;//Added by ahinsutime
    private double mappedX = 0;
    private Spinner spinner;
    private double mappedY = 0;
    private boolean globalLayBoolean = false;
    private List evalPattern;

    int DefaultCursorRadius = 50;

    private List<Point> listPos = new LinkedList<Point>();//Added by ahinsutime
    private List<MatOfPoint> contoursDraw;
    public RelativeLayout RL;//Added by ahinsutime
    public DrawingView DV;//Added by ahinsutime
    public DrawingHandView DHV;//Added by ahinsutime
    public RelativeLayout BL;//Added by ahinsutime
    public RelativeLayout BL2;
    public RelativeLayout BL3;
    public TextView PatternTV;
    public TextView CorrectnessTV;
    public TextView HoldHandTV;


    private static final String TAG = "HandPose::MainActivity";
    public static final int JAVA_DETECTOR = 0;
    public static final int NATIVE_DETECTOR = 1;

    private Mat mRgba;
    private Mat mGray;
    private Mat mIntermediateMat;
    private boolean flag = false;

    private int mDetectorType = JAVA_DETECTOR;

    private CustomSufaceView mOpenCvCameraView;
    private List<Size> mResolutionList;

    private double XOffset = 0;
    private double YOffset = 0;

    private SeekBar minTresholdSeekbar = null;
    private SeekBar maxTresholdSeekbar = null;
    private TextView minTresholdSeekbarText = null;
    private TextView numberOfFingersText = null;


    double iThreshold = 0;

    private Scalar mBlobColorHsv;
    private Scalar mBlobColorRgba;
    private ColorBlobDetector mDetector;
    private Mat mSpectrum;
    private boolean mIsColorSelected = false;

    private Size SPECTRUM_SIZE;
    private Scalar CONTOUR_COLOR;
    private Scalar CONTOUR_COLOR_BLUE;//Added by ahinsutime
    private Scalar CONTOUR_COLOR_GREEN;//Added by ahinsutime
    private Scalar CONTOUR_COLOR_WHITE;
    private Scalar CONTOUR_COLOR_BLACK;//Added by ahinsutime

    final Handler mHandler = new Handler();

    int numberOfFingers = 0;
    boolean pressed = false;
    boolean tracking = false;
    double flickerDist = 0;
    double optimalArea = 4000;
    double currentArea = 4000;
    Point InitialP1 = new Point(0,0);
    Point InitialP2 = new Point(1,1);
    Rect PrevBoundRect = new Rect(InitialP1, InitialP2);
    MotionEvent motionEvent;
    boolean correctness = false;
    boolean transition = false;
    int touchMode = -1;
    int isStart = -1;
    long now=0;
    long mPauseTime;
    long performanceStartTime;
    List<String> keyboardPerformance;
    List<String> phonecallPerformance;
    List<String> patternPerformance;

    final Runnable mUpdateFingerCountResults = new Runnable() {
        public void run() {

            patternView.invalidate();
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if(tracking==false && touchMode==-1){

                //mOpenCvCameraView.setVisibility(mOpenCvCameraView.VISIBLE);
                //mOpenCvCameraView.setAlpha(1);
                DHV.setVisibility(DHV.VISIBLE);//Added by ahinsutime
                DV.setVisibility(DV.GONE);
                if (globalLayBoolean) {
                    globalLayout.setVisibility(globalLayout.GONE);
                    globalLayout.invalidate();
                }
                DV.invalidate();//Added by ahinsutime
                DHV.invalidate();//Added by ahinsutime

                TimerTask adTast = new TimerTask() {
                    public void run() {
                        long downTime = SystemClock.uptimeMillis();
                        long eventTime = SystemClock.uptimeMillis() + 100;
                        int metaState = 0;

                        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                        int width = dm.widthPixels;
                        int height = dm.heightPixels;

                        float tx = width / 2;
                        float ty = height / 2;
                        motionEvent = MotionEvent.obtain(
                                downTime,
                                eventTime,
                                MotionEvent.ACTION_UP,
                                tx,
                                ty,
                                metaState
                        );
                        mOpenCvCameraView.dispatchTouchEvent(motionEvent);

                        if (currentArea < ((width / 2.5) * (height / 2.5) / 10000) && currentArea > (width / 20 * height / 20) /10000) {
                            tracking = true;

                            if(now>0){

                            }
                            else{
                                now = SystemClock.elapsedRealtime();
                                fadeAnimation(HoldHandTV,false, 1500);
                                fadeAnimation(HoldHandTV,true, 1500);

                            }

                            optimalArea = currentArea;

                        } else {
                            tracking = false;
                            optimalArea = 4000;
                            currentArea = 4000;
                        }
                    }

                };

                Timer timer = new Timer();
                timer.schedule(adTast, 2000);

            }
            else {

                mPauseTime = SystemClock.elapsedRealtime();
                if((mPauseTime-now)>3000){
                    now=0;
                }
                //mOpenCvCameraView.setAlpha(0);
                DV.setVisibility(DV.VISIBLE);
                DHV.setVisibility(BL.GONE);//Added by ahinsutime

                updateNumberOfFingers();
                DV.invalidate();//Added by ahinsutime
                DHV.invalidate();//Added by ahinsutime
                if (globalLayBoolean) {
                    globalLayout.setVisibility(globalLayout.VISIBLE);
                    globalLayout.invalidate();
                }

                flickerDist = Math.sqrt((PrevCenterX - centerX) * (PrevCenterX - centerX) + (PrevCenterY - centerY) * (PrevCenterY - centerY));

                if (((((int) PrevCenterX)) == 0) && ((((int) PrevCenterY)) == 0)) {//Initial setting
                } else if (flickerDist < 50 && flickerDist >= 0) {//For micro control
                    centerX = PrevCenterX + (centerX - PrevCenterX) / 5;
                    centerY = PrevCenterY + (centerY - PrevCenterY) / 5;
                    PrevCenterX = centerX;
                    PrevCenterY = centerY;
                } else if (flickerDist < 100 && flickerDist >= 50) {//For micro control
                    centerX = PrevCenterX + (centerX - PrevCenterX) / 4;
                    centerY = PrevCenterY + (centerY - PrevCenterY) / 4;
                    PrevCenterX = centerX;
                    PrevCenterY = centerY;
                } else if (flickerDist < 200 && flickerDist >= 100) {//For micro control
                    centerX = PrevCenterX + (centerX - PrevCenterX) / 3;
                    centerY = PrevCenterY + (centerY - PrevCenterY) / 3;
                    PrevCenterX = centerX;
                    PrevCenterY = centerY;
                } else if (flickerDist < 500 && flickerDist >= 200) {//For micro control
                    centerX = PrevCenterX + (centerX - PrevCenterX) / 2;
                    centerY = PrevCenterY + (centerY - PrevCenterY) / 2;
                } else {//Large flickering
                    centerX = PrevCenterX + (centerX - PrevCenterX) / 4;
                    centerY = PrevCenterY + (centerY - PrevCenterY) / 4;
                }
                PrevCenterX = centerX;
                PrevCenterY = centerY;

                //keyButton1.dispatchTouchEvent(motionEvent);
                if (pressed) {
                    DV.invalidate();
                    //  BL.dispatchTouchEvent(motionEvent);
                    if (globalLayBoolean && touchMode==-1)
                        globalLayout.dispatchTouchEvent(motionEvent);

                    Log.d(TAG, "Simulated Touch Pressed=" + pressed);
                } else {
                    //patternView.dispatchTouchEvent(motionEvent);

                    DV.invalidate();
                    Log.d(TAG, "Simulated Touch Released=" + pressed);
                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = SystemClock.uptimeMillis() + 100;
                    float tx = (float) centerX / (float) XOffset;
                    float ty = (float) centerY / (float) YOffset;
                    int metaState = 0;
                    motionEvent = MotionEvent.obtain(
                            downTime,
                            eventTime,
                            MotionEvent.ACTION_UP,
                            tx,
                            ty,
                            metaState
                    );
                    if (globalLayBoolean && touchMode==-1)
                        globalLayout.dispatchTouchEvent(motionEvent);
                }
            }
        }
    };

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                    // 640x480
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public MainActivity() {
        //Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_surface_view);
        if (!OpenCVLoader.initDebug()) {
            Log.e("Test", "man");
        } else {
        }

        mOpenCvCameraView = (CustomSufaceView) findViewById(R.id.main_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(1);

        minTresholdSeekbarText = (TextView) findViewById(R.id.textView3);
        numberOfFingersText = (TextView) findViewById(R.id.numberOfFingers);
        minTresholdSeekbar = (SeekBar) findViewById(R.id.seekBar1);
        minTresholdSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                minTresholdSeekbarText.setText(String.valueOf(progressChanged));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                minTresholdSeekbarText.setText(String.valueOf(progressChanged));
            }
        });
        minTresholdSeekbar.setProgress(8700);
        userInfoLayout = (RelativeLayout) findViewById(R.id.user_info_view);
        userInfoSubmit = (Button) findViewById(R.id.UserInfoButton);
        iuserName = (EditText) findViewById(R.id.user_name_text);
        iuserAge = (EditText) findViewById(R.id.user_age_text);

        RL = (RelativeLayout) findViewById(R.id.main_relative_view);//Added by ahinsutime
        DV = new DrawingView(this);//Added by ahinsutime
        RL.addView(DV);//Added by ahinsutime
        DV.bringToFront();//Added by ahinsutime
        DHV = new DrawingHandView(this);//Added by ahinsutime
        RL.addView(DHV);//Added by ahinsutime
        DHV.bringToFront();//Added by ahinsutime
        BL = (RelativeLayout) findViewById(R.id.buttons_keyboard);
        BL2 = (RelativeLayout) findViewById(R.id.buttons_discrete);
        BL3 = (RelativeLayout) findViewById(R.id.buttons_shape);
        PatternTV = (TextView) findViewById(R.id.PatternText);
        CorrectnessTV = (TextView) findViewById(R.id.CorrectnessText);
        CorrectnessTV.setAlpha(0f);
        HoldHandTV = (TextView) findViewById(R.id.HoldHand);
        HoldHandTV.setAlpha(0f);

        keyboardPerformance = new ArrayList<>();
        phonecallPerformance = new ArrayList<>();
        patternPerformance = new ArrayList<>();

        globalLayout = (RelativeLayout) findViewById(R.id.dflt_lay);
        /*user info submission button */

        userInfoSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                // gender and handedness info are saved in int userGender and int userHand
                userName = iuserName.getText().toString();
                userAge = iuserAge.getText().toString();
           /*     String info ="name:" + userName + "\n" +
                        "age:" + userAge + "\n" +
                        "gender:" + userGender + "\n" +
                        "handedness:" + userHand;*/
                // save it after completion of test
                /*
                try{
                    BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath+userName+".txt", true));
                    bw.write(info);
                    bw.close();
                    } catch(IOException e){}
                */
                userInfoLayout.setVisibility(userInfoLayout.INVISIBLE);
                RL.setVisibility(RL.VISIBLE);
                //   sendEmail(info);

            }
        });
  /*spinner for selecting user design */
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // if (globalLayBoolean)
                globalLayout.setVisibility(View.INVISIBLE);
                switch (String.valueOf(parentView.getItemIdAtPosition(position))) {
                    case "1":
                        globalLayout = BL;
                        globalLayout.setVisibility(View.VISIBLE);

                        randomNumber = randomIntGenerator(4);
                        //evalEdit.setText("Enter this number: "+randomNumber);
                        keyboardText.setText("Enter this number: "+randomNumber);

                        globalLayBoolean = true;

                        break;
                    case "2":
                        globalLayout = BL2;
                        globalLayout.setVisibility(View.VISIBLE);
                        randomDiscrete = randomCallGenerator();
                        if (randomDiscrete==false){
                            discreteText.setText("Follow the instruction:"+" Decline");
                        }
                        else{
                            discreteText.setText("Follow the instruction:"+" Accept");
                        }

                        globalLayBoolean = true;
                        break;
                    case "3":
                        globalLayout = BL3;
                        globalLayout.setVisibility(View.VISIBLE);
                        globalLayBoolean = true;
                        PatternTV.setVisibility(PatternTV.VISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                // Maybe we can put entering info of participant before actual evaluation begin
            }
        });

        mOpenCvCameraView.setAlpha(1);//Added by ahinsutime
        customEdit = (EditText) findViewById(R.id.customEdit);
        //evalEdit = (EditText) findViewById(R.id.TestCaseEdit);
        keyboardText = (TextView) findViewById(R.id.TestCaseKeyboard);
        discreteText = (TextView) findViewById(R.id.TestCaseDiscrete);
        disableSoftInputFromAppearing(customEdit);
        //disableSoftInputFromAppearing_number(iuserAge);
        //disableSoftInputFromAppearing_text(iuserName);
        customEdit.setText("");

        keyButton1 = (Button) findViewById(R.id.button1);
        keyButton2 = (Button) findViewById(R.id.button2);
        keyButton3 = (Button) findViewById(R.id.button3);
        keyButton4 = (Button) findViewById(R.id.button4);
        keyButton5 = (Button) findViewById(R.id.button5);
        keyButton6 = (Button) findViewById(R.id.button6);
        keyButton7 = (Button) findViewById(R.id.button7);
        keyButton8 = (Button) findViewById(R.id.button8);
        keyButton9 = (Button) findViewById(R.id.button9);
        keyButton0 = (Button) findViewById(R.id.button0);
        keyButtonBackspace = (Button) findViewById(R.id.buttonBackspace);
        keyButtonEnter = (Button) findViewById(R.id.buttonEnter);
        keyButtonReset = (Button) findViewById(R.id.buttonReset);
        keyButtonResetTracking = (Button) findViewById(R.id.buttonTracking);
        keyButtonQuit = (Button) findViewById(R.id.buttonQuit);
        keyButtonStart = (Button) findViewById(R.id.buttonStart);
        keyButtonReject = (Button) findViewById(R.id.rejButton);
        keyButtonAccept = (Button) findViewById(R.id.accButton);
        keyButtonRefresh = (Button) findViewById(R.id.buttonRefresh);
        keyButtonSavePattern = (Button) findViewById(R.id.savePatternButton);
        keyButtonRefreshPattern = (Button) findViewById(R.id.refreshPatternButton);
        keyButtonConfirmPattern = (Button) findViewById(R.id.confirmPatternButton);
        keyButtonTouchmode = (Button) findViewById(R.id.buttonTouchmode);

        patternView = (PatternView) findViewById(R.id.patternView);



        buttons = new HashMap<>();
        if (!flag) {
            keyButton1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton1);
                }
            });
            keyButton2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton2);
                }
            });
            keyButton3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton3);
                }
            });
            keyButton4.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton4);
                }
            });
            keyButton5.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton5);
                }
            });
            keyButton6.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton6);
                }
            });
            keyButton7.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton7);
                }
            });
            keyButton8.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton8);
                }
            });
            keyButton9.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton9);
                }
            });
            keyButton0.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButton0);
                }
            });
            keyButtonBackspace.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonBackspace);
                }
            });
            keyButtonEnter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonEnter);
                }
            });
            keyButtonReset.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonReset);
                }
            });

            keyButtonQuit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonQuit);
                }
            });
            keyButtonStart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonStart);
                }
            });
            keyButtonResetTracking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonResetTracking);
                }
            });
            keyButtonReject.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonReject);
                }
            });
            keyButtonResetTracking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonReject);
                }
            });
            keyButtonRefresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonRefresh);
                }
            });
            keyButtonSavePattern.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonSavePattern);
                }
            });
            keyButtonRefreshPattern.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonRefreshPattern);
                }
            });
            keyButtonConfirmPattern.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addButtonCoordinates(keyButtonConfirmPattern);
                }
            });



            flag = true;
        }
        keyButton1.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton1.setTextColor(Color.BLUE);
                        customEdit.append("1");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    default:
                        keyButton1.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton2.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton2.setTextColor(Color.BLUE);
                        customEdit.append("2");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton2.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton2.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton3.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton3.setTextColor(Color.BLUE);
                        customEdit.append("3");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton3.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton3.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton4.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton4.setTextColor(Color.BLUE);
                        customEdit.append("4");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton4.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton4.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButton5.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton5.setTextColor(Color.BLUE);
                        customEdit.append("5");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton5.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton5.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton6.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton6.setTextColor(Color.BLUE);
                        customEdit.append("6");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton6.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton6.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton7.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton7.setTextColor(Color.BLUE);
                        customEdit.append("7");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton7.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton7.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton8.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton8.setTextColor(Color.BLUE);
                        customEdit.append("8");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton8.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton8.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        keyButton9.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton9.setTextColor(Color.BLUE);
                        customEdit.append("9");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton9.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton9.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButton0.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButton0.setTextColor(Color.BLUE);
                        customEdit.append("0");
                        customEdit.setSelection(customEdit.getText().length());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButton0.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButton0.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonBackspace.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonBackspace.setTextColor(Color.BLUE);
                        if(customEdit.getText().toString().length()>0) {
                            String editval = customEdit.getText().toString();
                            editval = editval.substring(0, editval.length() - 1);
                            customEdit.setText(editval);
                            customEdit.setSelection(customEdit.getText().length());
                            BackspacePressedNumber+=1;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonBackspace.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButtonBackspace.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonEnter.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonEnter.setTextColor(Color.BLUE);
                        if(customEdit.getText().toString().length()>0) {
                            if(Integer.parseInt(customEdit.getText().toString())==randomNumber){
                                customEdit.setText("");
                                CorrectnessTV.setText("Correct Answer (Keyboard): ");
                                long stopTime = System.currentTimeMillis();
                                long elapsedTime = stopTime - performanceStartTime;

                                keyboardPerformance.add("Correct Answer (Keyboard):"+String.valueOf(randomNumber));
                                keyboardPerformance.add("Elapsed Time: "+String.valueOf(elapsedTime)+"\n");

                                isStart*=-1;
                                keyButtonStart.setBackgroundResource(R.drawable.round_button6);
                                keyButtonStart.setText("Start Measure");

                                CorrectnessTV.setTextColor(Color.BLUE);
                                fadeAnimation(CorrectnessTV,false, 1500);
                                fadeAnimation(CorrectnessTV,true, 1500);
                            }
                            else{
                                CorrectnessTV.setText("Wrong Answer (Keyboard)\n");
                                CorrectnessTV.setTextColor(Color.RED);
                                keyboardPerformance.add("Wrong Answer\n");
                                fadeAnimation(CorrectnessTV,false, 1500);
                                fadeAnimation(CorrectnessTV,true, 1500);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonEnter.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButtonEnter.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonReset.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonReset.setTextColor(Color.BLUE);
                        if(customEdit.getText().toString().length()>0) {
                            //String editval = customEdit.getText().toString();
                            String editval = "";
                            customEdit.setText(editval);
                            customEdit.setSelection(customEdit.getText().length());
                            ResetPressedNumber+=1;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonReset.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButtonReset.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });


        keyButtonQuit.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonQuit.setTextColor(Color.BLUE);
                        sendEmail();
                        onDestroy();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonQuit.setTextColor(Color.BLUE);
                        //   sendEmail();
                        break;
                    default:
                        keyButtonQuit.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonStart.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        if(isStart==-1) {
                            keyButtonStart.setBackgroundColor(Color.BLUE);
                            isStart *= -1;
                        }
                        /*
                        else{
                            keyButtonStart.setBackgroundResource(R.drawable.round_button6);
                            keyButtonStart.setText("Start Measure");
                            isStart *= -1;
                        }
                        */
                        keyButtonStart.setTextColor(Color.BLUE);
                        //fadeAnimation(CorrectnessTV,true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonStart.setTextColor(Color.BLUE);
                        performanceStartTime = System.currentTimeMillis();
                        break;
                    default:
                        keyButtonStart.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonTouchmode.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonTouchmode.setTextColor(Color.WHITE);
                        if(touchMode==-1) {
                            keyButtonTouchmode.setBackgroundColor(Color.BLUE);
                        }
                        else{

                            keyButtonTouchmode.setBackgroundResource(R.drawable.round_button6);
                            keyButtonTouchmode.setText("Touch Mode");
                        }

                        //fadeAnimation(CorrectnessTV,true);
                        touchMode *= -1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonTouchmode.setTextColor(Color.BLUE);
                        performanceStartTime = System.currentTimeMillis();
                        break;
                    default:
                        keyButtonTouchmode.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonResetTracking.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonResetTracking.setTextColor(Color.BLUE);
                        tracking=false;
                        ResetTrackingNumber+=1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonResetTracking.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButtonResetTracking.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonReject.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonReject.setTextColor(Color.BLUE);
                        if(randomDiscrete==false){
                            //keyButtonReject.setBackgroundColor(Color.BLUE);
                            CorrectnessTV.setText("Correct Answer");
                            isStart *= -1;
                            keyButtonStart.setBackgroundResource(R.drawable.round_button6);
                            keyButtonStart.setText("Start Measure");

                            long stopTime = System.currentTimeMillis();
                            long elapsedTime = stopTime - performanceStartTime;
                            phonecallPerformance.add("Correct Answer (Discrete): "+"Reject");
                            phonecallPerformance.add("Elapsed Time: "+String.valueOf(elapsedTime)+"\n");
                            CorrectnessTV.setTextColor(Color.BLUE);
                            fadeAnimation(CorrectnessTV,false, 1500);
                            fadeAnimation(CorrectnessTV,true, 1500);
                        }
                        else{
                            phonecallPerformance.add("Wrong Answer (Discrete)\n");
                            CorrectnessTV.setText("Wrong Answer");
                            CorrectnessTV.setTextColor(Color.RED);
                            fadeAnimation(CorrectnessTV,false, 1500);
                            fadeAnimation(CorrectnessTV,true, 1500);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonReject.setTextColor(Color.BLUE);
                        break;
                    default:
                        //keyButtonReject.setBackgroundColor(Color.parseColor("#FF0000"));
                        keyButtonReject.setTextColor(Color.WHITE);
                        break;
                }
                return false;
            }
        });

        keyButtonAccept.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonAccept.setTextColor(Color.BLUE);
                        if(randomDiscrete==true){
                            //keyButtonAccept.setBackgroundColor(Color.BLUE);
                            CorrectnessTV.setText("Correct Answer");
                            isStart *= -1;
                            keyButtonStart.setBackgroundResource(R.drawable.round_button6);
                            keyButtonStart.setText("Start Measure");

                            long stopTime = System.currentTimeMillis();
                            long elapsedTime = stopTime - performanceStartTime;
                            phonecallPerformance.add("Correct Answer (Discrete): "+"Accept");
                            phonecallPerformance.add("Elapsed Time: "+String.valueOf(elapsedTime)+"\n");

                            CorrectnessTV.setTextColor(Color.BLUE);
                            fadeAnimation(CorrectnessTV,false, 1500);
                            fadeAnimation(CorrectnessTV,true, 1500);
                        }
                        else{
                            phonecallPerformance.add("Wrong Answer (Discrete)\n");
                            CorrectnessTV.setText("Wrong Answer");
                            CorrectnessTV.setTextColor(Color.RED);
                            fadeAnimation(CorrectnessTV,false, 1500);
                            fadeAnimation(CorrectnessTV,true, 1500);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonAccept.setTextColor(Color.BLUE);
                        break;
                    default:
                        //keyButtonAccept.setBackgroundColor(Color.parseColor("#008000"));
                        keyButtonAccept.setTextColor(Color.WHITE);
                        break;
                }
                return false;
            }
        });

        keyButtonRefresh.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonRefresh.setTextColor(Color.BLUE);
                        randomDiscrete = randomCallGenerator();
                        randomNumber = randomIntGenerator(4);
                        keyboardText.setText("Enter this number: "+randomNumber);
                        if (!randomDiscrete){
                            discreteText.setText("Follow the instruction:"+" Decline");
                        }
                        else{
                            discreteText.setText("Follow the instruction:"+" Accept");
                        }

                        patternView.clearPattern();
                        PatternTV.setText("Draw your pattern and click the save button");
                        PatternTV.setTextColor(Color.parseColor("#FFFF00"));
                        isStart = -1;
                        keyButtonStart.setBackgroundResource(R.drawable.round_button6);
                        keyButtonStart.setText("Start Measure");

                        //fadeAnimation(CorrectnessTV,false);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonRefresh.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButtonRefresh.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });

        keyButtonSavePattern.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        if((patternView.getPattern().size())>0) {
                            //keyButtonSavePattern.setTextColor(Color.BLUE);
                            PatternTV.setText("Draw your pattern again and click confirm");
                            PatternTV.setTextColor(Color.parseColor("#00BFFF"));
                            evalPattern = patternView.getPattern();
                            patternView.clearPattern();
                            break;
                        }
                    case MotionEvent.ACTION_MOVE:
                        keyButtonSavePattern.setTextColor(Color.BLUE);
                        break;

                    default:
                        keyButtonSavePattern.setTextColor(Color.WHITE);
                        break;
                }
                return false;
            }
        });

        keyButtonRefreshPattern.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:


                        patternView.clearPattern();
                        PatternTV.setText("Draw your pattern and click the save button");
                        PatternTV.setTextColor(Color.parseColor("#FFFF00"));
                        RefreshPressedNumber+=1;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        keyButtonRefreshPattern.setTextColor(Color.BLUE);
                        break;

                    default:
                        keyButtonRefreshPattern.setTextColor(Color.WHITE);
                        break;
                }
                return false;
            }
        });

        keyButtonConfirmPattern.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//Added by ahinsutime
                    case MotionEvent.ACTION_DOWN:
                        keyButtonConfirmPattern.setTextColor(Color.BLUE);
                        List tempPattern = patternView.getPattern();
                        if(tempPattern.equals(evalPattern)){
                            PatternTV.setText("Draw your pattern and click the save button");
                            PatternTV.setTextColor(Color.parseColor("#FFFF00"));
                            String tempPatternTrail="";
                            for(int i=0;i<evalPattern.size();i++){
                                tempPatternTrail+=evalPattern.get(i).toString();
                            }
                            long stopTime = System.currentTimeMillis();
                            long elapsedTime = stopTime - performanceStartTime;
                            patternPerformance.add("Correct Answer (Pattern): "+tempPatternTrail);
                            patternPerformance.add("Elapsed Time: "+String.valueOf(elapsedTime)+"\n");
                            isStart *= -1;
                            keyButtonStart.setBackgroundResource(R.drawable.round_button6);
                            keyButtonStart.setText("Start Measure");


                            CorrectnessTV.setText("Correct Answer");
                            CorrectnessTV.setTextColor(Color.BLUE);
                            fadeAnimation(CorrectnessTV,false, 1500);
                            fadeAnimation(CorrectnessTV,true,1500);

                            patternView.clearPattern();
                        }
                        else{
                            patternPerformance.add("Wrong Answer (Pattern)\n");
                            CorrectnessTV.setText("Wrong Answer");
                            CorrectnessTV.setTextColor(Color.RED);
                            fadeAnimation(CorrectnessTV,false, 1500);
                            fadeAnimation(CorrectnessTV,true, 1500);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        keyButtonConfirmPattern.setTextColor(Color.BLUE);
                        break;
                    default:
                        keyButtonConfirmPattern.setTextColor(Color.GREEN);
                        //keyButtonConfirmPattern.setBackgroundColor(Color.YELLOW);
                        break;
                }
                return false;
            }
        });

    }

    public void addButtonCoordinates(Button b) {
        Point point1 = getPointOfView(b);
        double xMax = point1.x + b.getWidth();
        double yMax = point1.y + b.getHeight();
        ButtonCoordinates coo = new ButtonCoordinates();
        coo.setxMin(point1.x);
        coo.setyMin(point1.y);
        coo.setxMax(xMax);
        coo.setyMax(yMax);
        buttons.put(String.valueOf(b.getText()), coo);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
        mIntermediateMat = new Mat();

        Camera.Size resolution = mOpenCvCameraView.getResolution();
        String caption = "Resolution " + Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
        Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();

        Camera.Parameters cParams = mOpenCvCameraView.getParameters();
        cParams.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        mOpenCvCameraView.setParameters(cParams);
        Toast.makeText(this, "Focus mode : " + cParams.getFocusMode(), Toast.LENGTH_SHORT).show();

        //mOpenCvCameraView.setCameraIndex(1);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255, 0, 0, 255);
        CONTOUR_COLOR_WHITE = new Scalar(255, 255, 255, 255);
        CONTOUR_COLOR_BLUE = new Scalar(0, 0, 255, 255);
        CONTOUR_COLOR_GREEN = new Scalar(0, 255, 0, 255);
        CONTOUR_COLOR_BLACK = new Scalar(0, 0, 0, 255);
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int touchedX = (int) Math.floor(event.getX() * cols/width);
        int touchedY = (int) Math.floor(event.getY() * rows/height);

        XOffset =  cols/ (double) width;
        YOffset = rows / (double) height;

        Log.i(TAG, "XOffset="+XOffset+" YOffset="+YOffset);

        int x = (int) event.getX() - xOffset;
        int y = (int) event.getY() - yOffset;

        x = touchedX;
        y = touchedY;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");
        Log.i(TAG, "Touch image coordinates RAW: (" + event.getX() + ", " + event.getY() + ")");
        //Needed to be changed

       /* given (x, y) that was touched, click on according button */
        //touchEventDispatch(event.getX(), event.getY());

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x > 5) ? x - 5 : 0;
        touchedRect.y = (y > 5) ? y - 5 : 0;

        touchedRect.width = (x + 5 < cols) ? x + 5 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y + 5 < rows) ? y + 5 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width * touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        mDetector.setHsvColor(mBlobColorHsv);

        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return true; // don't need subsequent touch events
    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    public class DrawingView extends View {//Added by ahinsutime
        int x = 0, y = 0;

        public DrawingView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            Log.d(TAG, "Inside DrawingView.onDraw");
            Paint paint = new Paint();
            Paint far = new Paint();
            Paint near = new Paint();
            Paint click = new Paint();
            Paint innerMapping = new Paint();
            Paint outerMapping = new Paint();
            Paint ref = new Paint();
            innerMapping.setStyle(Paint.Style.STROKE);
            outerMapping.setStyle(Paint.Style.STROKE);

            paint.setColor(Color.BLUE);
            far.setColor(Color.YELLOW);
            near.setColor(Color.RED);
            click.setColor(Color.WHITE);
            innerMapping.setColor(Color.BLACK);
            outerMapping.setColor(Color.RED);
            ref.setColor(Color.BLACK);

            paint.setAlpha(50);
            far.setAlpha(50);
            near.setAlpha(50);
            innerMapping.setStrokeWidth(10);
            outerMapping.setStrokeWidth(10);

            double optimalDist = optimalArea;
            double currentDist = currentArea;

            x = (int) (centerX / XOffset);
            y = (int) (centerY / YOffset);

            if(optimalDist<=currentDist){//When hand is near

                double tempRadius = (1 - (currentDist - optimalDist) / (optimalDist)) * DefaultCursorRadius;

                Log.d(TAG, "optimalDist=" + optimalDist+" currentDist="+currentDist);
                canvas.drawCircle(x, y, DefaultCursorRadius, near);
                canvas.drawCircle(x, y, (int) Math.abs(tempRadius), paint);
                if(pressed){
                    canvas.drawCircle(x, y, (int) Math.abs(tempRadius), click);
                }
                //canvas.drawCircle((int) (centerX / XOffset), (int) (centerY / YOffset),DefaultCursorRadius,ref);
            }
            else{//When hand is far

                double tempRadius = ((optimalDist - currentDist)/(optimalDist)) * DefaultCursorRadius + DefaultCursorRadius;

                canvas.drawCircle(x, y, (int) Math.abs(tempRadius), far);
                canvas.drawCircle(x, y, DefaultCursorRadius, paint);
                if(pressed){
                    canvas.drawCircle(x, y, DefaultCursorRadius, click);
                }
                //canvas.drawCircle((int) (centerX / XOffset), (int) (centerY / YOffset),DefaultCursorRadius,ref);
            }
        }
    }

    public class DrawingHandView extends View {//Added by ahinsutime
        int x = 0, y = 0;

        public DrawingHandView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            Log.d(TAG, "Inside DrawingHandView.onDraw");
            Paint paint = new Paint();
            Paint textPaint = new Paint();

            paint.setColor(Color.GREEN);
            textPaint.setColor(Color.GREEN);
            textPaint.setTextSize(60);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

            int width = canvas.getWidth();
            int height = canvas.getHeight();
            int rectSize = height/4;
            android.graphics.Rect InitialRect = new android.graphics.Rect(width/2-rectSize,height/2-rectSize, width/2+rectSize,height/2+rectSize);
            canvas.drawRect(InitialRect, paint);
            canvas.drawText("Show your hand in this square for 2 seconds.", width/5, height/5, textPaint);
        }
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        flip(mRgba, mRgba, 1);//Added by ahinsutime
        flip(mGray, mGray, 1);//Added by ahinsutime
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        if(userHand==0 || userHand==2){
            flip(mRgba, mRgba, 1);//Added by ahinsutime
            flip(mGray, mGray, 1);//Added by ahinsutime
        }
        else{
            flip(mRgba, mRgba, 0);//Added by ahinsutime
            flip(mGray, mGray, 0);//Added by ahinsutime
        }



        iThreshold = minTresholdSeekbar.getProgress();

        Imgproc.GaussianBlur(mRgba, mRgba, new org.opencv.core.Size(3, 3), 1, 1);

        if (!mIsColorSelected) {
            tracking=false;
            mHandler.post(mUpdateFingerCountResults);
            return mRgba;
        }

        List<MatOfPoint> contours = mDetector.getContours();
        mDetector.process(mRgba);

        Log.d(TAG, "Contours count: " + contours.size());

        if (contours.size() <= 0) {
            tracking=false;
            mHandler.post(mUpdateFingerCountResults);
            return mRgba;
        }

        RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(0).toArray()));

        double boundWidth = rect.size.width;
        double boundHeight = rect.size.height;
        int boundPos = 0;

        for (int i = 1; i < contours.size(); i++) {
            rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            if (rect.size.width * rect.size.height > boundWidth * boundHeight) {
                boundWidth = rect.size.width;
                boundHeight = rect.size.height;
                boundPos = i;
            }
        }

        Rect boundRect = Imgproc.boundingRect(new MatOfPoint(contours.get(boundPos).toArray()));
        //Imgproc.rectangle(mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR_WHITE, 2, 8, 0);

        Log.d(TAG,
                " (Before)Row start [" +
                        (int) boundRect.tl().y + "] row end [" +
                        (int) boundRect.br().y + "] Col start [" +
                        (int) boundRect.tl().x + "] Col end [" +
                        (int) boundRect.br().x + "]");

        Log.d(TAG,
                " (After)Row start [" +
                        (int) boundRect.tl().y + "] row end [" +
                        (int) boundRect.br().y + "] Col start [" +
                        (int) boundRect.tl().x + "] Col end [" +
                        (int) boundRect.br().x + "]");


        int rectHeightThresh = 0;
        double a = boundRect.br().y - boundRect.tl().y;
        a = a * 0.7;
        a = boundRect.tl().y + a;

        Log.d(TAG,
                " A [" + a + "] br y - tl y = [" + (boundRect.br().y - boundRect.tl().y) + "]");

        //Imgproc.rectangle(mRgba, boundRect.tl(), new Point(boundRect.br().x, a), CONTOUR_COLOR_BLUE, 2, 8, 0);

        MatOfPoint2f pointMat = new MatOfPoint2f();
        Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(boundPos).toArray()), pointMat, 3, true);
        contours.set(boundPos, new MatOfPoint(pointMat.toArray()));

        MatOfInt hull = new MatOfInt();
        MatOfInt4 convexDefect = new MatOfInt4();
        Imgproc.convexHull(new MatOfPoint(contours.get(boundPos).toArray()), hull);

        if (hull.toArray().length < 3) return mRgba;

        Imgproc.convexityDefects(new MatOfPoint(contours.get(boundPos).toArray()), hull, convexDefect);

        List<MatOfPoint> hullPoints = new LinkedList<MatOfPoint>();
        List<Point> listPo = new LinkedList<Point>();
        for (int j = 0; j < hull.toList().size(); j++) {
            listPo.add(contours.get(boundPos).toList().get(hull.toList().get(j)));
        }

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Iterator<Point> iterator = listPo.iterator();


        //List<double> distArray;

        double sigma = standardDeviation(listPo, centerX, centerY, 1);
        sigma=sigma*sigma;

        while (iterator.hasNext()) {
            Point data = iterator.next();
            double dist = Math.pow(data.x-centerX,2)+Math.pow(data.y-centerY,2);
            if (dist>4.0*sigma && Math.sqrt(dist)>300) {
                iterator.remove();
            }
        }

        MatOfPoint e = new MatOfPoint();
        e.fromList(listPo);
        hullPoints.add(e);

        List<MatOfPoint> defectPoints = new LinkedList<MatOfPoint>();
        List<Point> listPoDefect = new LinkedList<Point>();
        for (int j = 0; j < convexDefect.toList().size(); j = j + 4) {
            Point farPoint = contours.get(boundPos).toList().get(convexDefect.toList().get(j + 2));
            Integer depth = convexDefect.toList().get(j + 3);
            if (depth > iThreshold && farPoint.y < a) {
                listPoDefect.add(contours.get(boundPos).toList().get(convexDefect.toList().get(j + 2)));
            }
            Log.d(TAG, "defects [" + j + "] " + convexDefect.toList().get(j + 3));
        }

        MatOfPoint e2 = new MatOfPoint();
        e2.fromList(listPo);
        defectPoints.add(e2);

        Log.d(TAG, "hull: " + hull.toList());
        Log.d(TAG, "defects: " + convexDefect.toList());
        if(tracking==true) {
            Imgproc.drawContours(mRgba, hullPoints, -1, CONTOUR_COLOR_GREEN, 3);
        }

        listPos = listPo;//Added by ahinsutime

        centerX = 0;
        centerY = 0;
        for (int j = 0; j < listPos.size(); j++) {//Added by ahinsutime

            centerX = centerX + listPos.get(j).x;
            centerY = centerY + listPos.get(j).y;
        }
        centerX = centerX / listPos.size();//Added by ahinsutime
        centerY = centerY / listPos.size();//Added by ahinsutime

        int defectsTotal = (int) convexDefect.total();
        Log.d(TAG, "Defect total " + defectsTotal);

        this.numberOfFingers = listPoDefect.size();
        if (this.numberOfFingers > 5) this.numberOfFingers = 5;

        mHandler.post(mUpdateFingerCountResults);

        for (Point p : listPoDefect) {
            Imgproc.circle(mRgba, p, 6, new Scalar(0, 0, 255));
        }

        int maxY = 0;
        int AX =0;

        for (int i=0; i<listPo.size(); i++){
            if(maxY<=listPo.get(i).y){
                maxY = (int) listPo.get(i).y;
            }
        }
        double tlDelX, tlDelY, brDelX, brDelY, tlDel, brDel;
        double temptlX=0, temptlY=0, tempbrX=0, tempbrY=0;
        tlDelX = boundRect.tl().x-PrevBoundRect.tl().x;
        tlDelY = boundRect.tl().y-PrevBoundRect.tl().y;
        tlDel = Math.sqrt(tlDelX*tlDelX+tlDelY*tlDelY);

        brDelX = boundRect.br().x-PrevBoundRect.br().x;
        brDelY = maxY-PrevBoundRect.br().y;
        brDel = Math.sqrt(brDelX*brDelX+brDelY*brDelY);

        if (PrevBoundRect.tl()==InitialP1 || PrevBoundRect.br()==InitialP2){
        }
        else if(Math.abs(tlDel)>200){
            temptlX=PrevBoundRect.tl().x+tlDelX/5;
            temptlY=PrevBoundRect.tl().y+tlDelY/5;
        }
        else if(Math.abs(tlDel)<=200 && Math.abs(tlDel)>100){
            temptlX=PrevBoundRect.tl().x+tlDelX/4;
            temptlY=PrevBoundRect.tl().y+tlDelY/4;
        }
        else if(Math.abs(tlDel)<=100 && Math.abs(tlDel)>50){
            temptlX=PrevBoundRect.tl().x+tlDelX/3;
            temptlY=PrevBoundRect.tl().y+tlDelY/3;
        }
        else{
            temptlX=PrevBoundRect.tl().x+tlDelX/2;
            temptlY=PrevBoundRect.tl().y+tlDelY/2;
        }

        if (PrevBoundRect.tl()==InitialP1 || PrevBoundRect.br()==InitialP2){
        }
        else if(Math.abs(brDel)>200){
            tempbrX=PrevBoundRect.br().x+brDelX/5;
            tempbrY=PrevBoundRect.br().y+brDelY/5;
        }
        else if(Math.abs(brDel)<=200 && Math.abs(brDel)>100){
            tempbrX=PrevBoundRect.br().x+brDelX/4;
            tempbrY=PrevBoundRect.br().y+brDelY/4;
        }
        else if(Math.abs(brDel)<=100 && Math.abs(brDel)>50){
            tempbrX=PrevBoundRect.br().x+brDelX/3;
            tempbrY=PrevBoundRect.br().y+brDelY/3;
        }
        else{
            tempbrX=PrevBoundRect.br().x+brDelX/2;
            tempbrY=PrevBoundRect.br().y+brDelY/2;
        }

        Rect tempRect = new Rect(new Point(temptlX, temptlY), new Point(tempbrX, tempbrY));
        boundRect = tempRect;
        if(tracking==true) {
            Imgproc.rectangle(mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR_BLACK, 2, 8, 0);
        }

        //Imgproc.rectangle(mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR_BLACK, 2, 8, 0);
        PrevBoundRect = boundRect;

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        //float tx = (float) mappedX;
        //float ty = (float) mappedY;
        float tx = (float) centerX / (float) XOffset;
        float ty = (float) centerY / (float) YOffset;
        int metaState = 0;

        double horizontal = boundRect.br().x-boundRect.tl().x;
        double vertical = boundRect.br().y-boundRect.tl().y;
        currentArea = (horizontal)*(vertical) / 10000;


        if(currentArea>((width/2.5)*(height/2.5)/10000) ||  currentArea<(width/20*height/20 /10000) || horizontal<=30 || vertical <= 30){
            tracking = false;
            optimalArea = 4000;
            currentArea = 4000;
        }

        if(numberOfFingers<5 && vertical*1.7<horizontal && pressed==false) {//For when start touching
            motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_DOWN,
                    tx,
                    ty,
                    metaState
            );
            pressed = true;
        }
        else if(numberOfFingers<5 && vertical*1.7<horizontal && pressed==true){//For dragging
            motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_MOVE,
                    tx,
                    ty,
                    metaState
            );
            pressed = true;
        }

        else{
            motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_UP,
                    tx,
                    ty,
                    metaState
            );
            pressed = false;
        }
        return mRgba;
    }

    public void updateNumberOfFingers() {
        numberOfFingersText.setText(String.valueOf(this.numberOfFingers));
    }

    private Point getPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    public int randomIntGenerator(int digits) {
        int output = (int) (Math.random() * Math.pow(10,digits));

        return output;
    }

    public boolean randomCallGenerator() {
        double output = Math.abs((Math.random())) *(1.0);
        if(output>0.5){
            return true;
        }
        else {
            return false;
        }
    }


    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    public static void disableSoftInputFromAppearing_number(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    public static void disableSoftInputFromAppearing_text(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }


    /* 1 - female, 0 - male */
    public void onRadioButtonClickedGender(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_female:
                if (checked)
                    userGender = 1;
                break;
            case R.id.radio_male:
                if (checked)
                    userGender = 0;
                break;
        }
    }
        /* 1 - right, 0 - left */

    public void onRadioButtonClickedHand(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_right_hand:
                if (checked)
                    userHand = 1;
                break;
            case R.id.radio_left_hand:
                if (checked)
                    userHand = 0;
                break;
            case R.id.radio_both_hand:
                if (checked)
                    userHand = 2;
                break;
        }
    }




    public static double standardDeviation(List<Point> array, double centerX, double centerY, int option) {
        if (array.size() < 2) return Double.NaN;

        double sum = 0.0;
        double sd = 0.0;
        double diff;
        double meanValue = mean(array, centerX, centerY);

        for (int i = 0; i < array.size(); i++) {
            diff = Math.sqrt(Math.pow(array.get(i).x-centerX, 2)+Math.pow(array.get(i).y-centerX, 2)) - meanValue;
            sum += diff * diff;
        }
        sd = Math.sqrt(sum / (array.size() - option));

        return sd;
    }


    public static double mean(List<Point> array, double centerX, double centerY) {  // 산술 평균 구하기
        double sum = 0.0;

        for (int i = 0; i < array.size(); i++)
            sum += Math.sqrt(Math.pow(array.get(i).x-centerX, 2)+Math.pow(array.get(i).y-centerX, 2));

        return sum / array.size();
    }




    public void fadeAnimation(final View tv, final boolean isfadeOut, int duration) {

        final Animation animationFadeOut, animationFadeIn;


        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        mHandler.postDelayed(new Runnable() {

            @Override

            public void run() {

                // TODO Auto-generated method stub

                if (isfadeOut) {
                    //tv.setAlpha(1f);
                    tv.startAnimation(animationFadeOut);
                }
                else {
                    tv.setAlpha(1f);
                    tv.startAnimation(animationFadeIn);
                }
            }
        }, 0);

        mHandler.postDelayed(new Runnable() {

            @Override

            public void run() {

                // TODO Auto-generated method stub


                if(isfadeOut){
                    tv.setAlpha(0f);
                }
                else{
                    tv.setAlpha(1f);
                }

            }
        }, duration);
    }

    private void sendEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "smussakhojayeva@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_CC, "ahinsutime@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
        String info = "name:" + userName + "\n" +
                "age:" + userAge + "\n" +
                "gender:" + userGender + "\n" +
                "handedness:" + userHand;
        /*
        String s = "[";
        String p = "[";
        for (long l : keyboardPerformance) {
            s = s + l + " ";
        }
        s = s + "]";
        for (long l : phonecallPerformance) {
            p = p + l + " ";
        }
        p = p + "]";
        Log.d("MAILCHECK", info + "\nkeyboard: " + s + "\nphonecall: " + p);
        emailIntent.putExtra(Intent.EXTRA_TEXT, info + "\nkeyboard: " + s + "\nphonecall: " + p);
        */
        emailIntent.putExtra(Intent.EXTRA_TEXT, info + "\nkeyboard: " + keyboardPerformance + "\nNumber of backspace pressed: "
                +BackspacePressedNumber+"\nNumber of reset pressed: "+ResetPressedNumber+"\nphonecall: " + phonecallPerformance+
                "\npattern: "+patternPerformance+"\nNumber of refresh patter pressed:"+RefreshPressedNumber+
                "\nNumber of manual tracking reset: "+ResetTrackingNumber+"\nTouchMode: "+touchMode);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

