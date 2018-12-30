package com.example.barakiva.mathmaster;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.barakiva.mathmaster.animations.Tick;
import com.example.barakiva.mathmaster.mathlogic.Calculation;
import com.example.barakiva.mathmaster.mathlogic.Equation;

public class CalculationScreen extends AppCompatActivity implements View.OnClickListener {

    //Elements
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    ImageView testCube;
    //Math Logic
    Calculation calculation = new Calculation();
    Equation equation = new Equation();
    //Buttons
    Button testBtn;
    Button numPad1;
    Button numPad2;
    Button numPad3;
    Button numPad4;
    Button numPad5;
    Button numPad6;
    Button numPad7;
    Button numPad8;
    Button numPad9;
    Button numPad0;
    Button clearAnswer;
    //
    TextView equationView;
    ImageView curvedTick;

    String numbersEntered = "";
    int result;
    int amountOfDrills = 5;
    Flux flux = new Flux();

    float density;

    //Classes
    Tick tick;
    ViewHelper viewHelper = new ViewHelper();
    Exercise exercise = new Exercise();

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_screen);
        String sessionId = getIntent().getStringExtra("Operation");
        tick = new Tick(this.getApplicationContext());
        //Finding elements
        equationView = findViewById(R.id.equationView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        constraintLayout = findViewById(R.id.constraintLayout);
        //Gameification
        curvedTick = new ImageView(this);
        curvedTick.setImageResource(R.drawable.curvedtick);
        curvedTick.setId(curvedTick.generateViewId());
        density = getResources().getDisplayMetrics().density;
        //Testing
        testCube = new ImageView(this);
        testCube.setImageResource(R.drawable.gray25);
        testCube.setId(testCube.generateViewId());
        //Assigning elements to code
        numPad1 = findViewById(R.id.numPad1);
        numPad1.setOnClickListener(this);
        numPad2 = findViewById(R.id.numPad2);
        numPad2.setOnClickListener(this);
        numPad3 = findViewById(R.id.numPad3);
        numPad3.setOnClickListener(this);
        numPad4 = findViewById(R.id.numPad4);
        numPad4.setOnClickListener(this);
        numPad5 = findViewById(R.id.numPad5);
        numPad5.setOnClickListener(this);
        numPad6 = findViewById(R.id.numPad6);
        numPad6.setOnClickListener(this);
        numPad7 = findViewById(R.id.numPad7);
        numPad7.setOnClickListener(this);
        numPad8 = findViewById(R.id.numPad8);
        numPad8.setOnClickListener(this);
        numPad9 = findViewById(R.id.numPad9);
        numPad9.setOnClickListener(this);
        numPad0 = findViewById(R.id.numPad0);
        numPad0.setOnClickListener(this);
        clearAnswer = findViewById(R.id.numPadClear);
        //Helper classes


        clearAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnswer();
            }
        });

        System.out.println(sessionId);

        //Session Over


    }

    public void handleOnClick(View v, ImageView asset) {
        //Check if valid button
        if (v instanceof Button) {
            Button b = (Button) v;
            numbersEntered += b.getText();
            if (!(viewHelper.hasParent(asset, this))) {
                Log.d("GENERATED", "initiated");
                generateAssetOnScreen(v, asset);
            }
            viewHelper.addView(asset, viewHelper.getViewLocation(v),
                    viewHelper.getHeightOffset(this,constraintLayout), constraintLayout);

            if (!tick.isAnimating()) {
                tick.animateCurvedTick(v, asset, progressBar, constraintLayout);
            } else {
                Log.d("Animation state", "Tick is animating, please stand by");
            }

            if (!isUserInTheRightDirection()) {
                System.out.println("Wrong start");
                startVibration();
                clearAnswer();
                equationView.setTextColor(Color.RED);
            }
        } else {
            System.out.println("This was not a button!");
        }

        //Check if answer is true
        Calculation calculation = new Calculation();
        if (calculation.isUserDigitsEqualToEquationResult(numbersEntered, result)) {
            if (Integer.parseInt(numbersEntered) == result) {
                //Correct answer scenario
//                generateAssetOnScreen(v);
//                animateCurvedTick(v);
                Thread t = new Thread(){
                    public void run(){
                        playCorrectAnswerSound();
                    }
                };
                t.start();
                System.out.println("Correct answer!");
                clearAnswer();
                flux.setDrillAmount(flux.getDrillAmount()+1);
                System.out.println("Flux is " + flux.getDrillAmount());
                if (flux.getDrillAmount() == amountOfDrills) {
                    pushTheProgressBar();
                    workoutHasEnded();
                } else {
                    pushTheProgressBar();
                    runOnce();
                }
            } else {
                //Wrong answer scenario
                startVibration();
                System.out.println("Wrong answer nigga");
                clearAnswer();
                equationView.setTextColor(Color.RED);
            }
        }
    }
    @Override
    public void onClick(View v) {
        handleOnClick(v, testCube);
    }

    public void playCorrectAnswerSound() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
        mp.start();
    }
    public void startVibration() {
        System.out.println("*******Vibrator hello!********");
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }
    }
    public void pushTheProgressBar() {
        int progress = (int) (((float)flux.getDrillAmount() / (float) amountOfDrills)* 100);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(progressBar.getProgress(), progress);
        valueAnimator.setInterpolator(new
                AccelerateDecelerateInterpolator());

        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new
            ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float gotProgress = (float) animation.getAnimatedValue();
                progressBar.setProgress((int) gotProgress);
            }
        });
        valueAnimator.start();
    }

    public void generateAssetOnScreen(View v, ImageView asset) {
       //Restart the view
        if (viewHelper.isParentCheck(asset)) {
            viewHelper.restartView(asset, constraintLayout);
        }
//        asset.setX(arr[0]);
//        asset.setY(arr[1] - heightOffset);
//        int[] arr = viewHelper.getViewLocation(v);
//        int heightOffset = viewHelper.getHeightOffset(this , constraintLayout);
    }
    public void clearAnswer() {
        numbersEntered = "";
    }
    public void clearColor() {
        equationView.setTextColor(Color.GRAY);
    }
    public boolean isUserInTheRightDirection() {
        String[] numbersEnteredDigits = numbersEntered.split("");
        String[] resultDigits = Integer.toString(result).split("");

        for(int i = 0; i <resultDigits.length - 1; i++) {
            if (!numbersEnteredDigits[i].equals(resultDigits[i])) {
                return false;
            }
        }
        return true;
    }



    public int generateEquation(int a, int b) {
        if (getIntent().hasExtra("Operation")) {

            String sessionId = getIntent().getStringExtra("Operation");
            int result = 0;
            switch (sessionId) {
                case :
                    result = a + b;
                    break;
                case "subtraction":
                    if (a < b) {
                        result = b - a;
                    } else {
                        result = a - b;
                    }
                    break;
                case "multiplication":
                    result = a * b;
                    break;
                case "division":
                    if (a < b) {
                        result = b / a;
                    } else {
                        result = a / b;
                    }
                    break;
            }

        }
        return result;
    }

    public double runOnce() {
        clearAnswer();
        clearColor();
        equation.generateNumbers(1, this);
        System.out.println(equation.getFirstNumber() + " " + equation.getSecondNumber());
        result = generateEquation(equation.getFirstNumber(), equation.getSecondNumber());
        System.out.println(result);
        equationView.setText(equation.getFirstNumber() + " " + equation.getStringOperator() + " " +
                equation.getSecondNumber());
        return result;

    }

    public void testBtn(View view) {
        exercise.setBeginExerciseTimeStamp(exercise.getCurrentTime());
        runOnce();
       // openDialog();
    }
    public void openDialog() {
        SessionDialog sessionDialog = new SessionDialog();
        sessionDialog.setPassedContext(this);
        sessionDialog.show(getFragmentManager(), "My Dialog");

    }
    public void workoutHasEnded() {
        exercise.setEndExerciseTimeStamp(exercise.getCurrentTime());
        System.out.println("Good job! It only took you " + exercise.getSessionLength() + " seconds!");
        System.out.println("Workout has ended! Good job!");
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
//        Intent previousIntent = getIntent();
//        if (previousIntent != null) {
//            if (previousIntent.getBooleanExtra("replay", true)) {
//                Log.d("modal message", "hey there!");
//                runOnce();
//            }
//        }
    }
}
class Flux {
    private int drillAmount = 0;
    public void setDrillAmount(int da) {
            this.drillAmount = da;
            System.out.println("Drillamount is " + this.drillAmount + " !");
    }

    public int getDrillAmount() {
        return drillAmount;
    }
}


    //Chest

//
//    public enum Operator
//    {
//        ADDITION("+") {
//            @Override public double apply(double x1, double x2) {
//                return x1 + x2;
//            }
//        },
//        SUBTRACTION("-") {
//            @Override public double apply(double x1, double x2) {
//                return x1 - x2;
//            }
//        },
//        MULTIPLICATION("*") {
//            @Override public double apply(double x1, double x2) {
//                return x1 * x2;
//            }
//        },
//        DIVISION("/") {
//            @Override public double apply(double x1, double x2) {
//                return x1 / x2;
//            }
//        };
//
//        // You'd include other operators too...
//
//        private final String text;
//
//        private Operator(String text) {
//            this.text = text;
//        }
//
//        // Yes, enums *can* have abstract methods. This code compiles...
//        public abstract double apply(double x1, double x2);
//
//        @Override public String toString() {
//            return text;
//        }
//    }
//    public String calculate(Operator op, double x1 , double x2) {
//        return String.valueOf(op.apply(x1,x2));
//    }
//}
//class Flux extends Observable {
//    private int drillAmount = 0;
//    // CalculationScreen screen = new CalculationScreen();
//    public void setDrillAmount(int da) {
//        synchronized (this) {
//            this.drillAmount = da;
//            System.out.println("Drillamount is " + drillAmount + " !");
////            if (drillAmount == 10) {
////                screen.workoutHasEnded();
////            }
//        }
//        setChanged();
//        notifyObservers();
//    }
//
//    public synchronized int getDrillAmount() {
//        return drillAmount;
//    }
//}
//
//class Heraclitus implements Observer {
//    public void observe (Observable o) {
//        o.addObserver(this);
//    }
//
//    @Override
//    public void update(Observable o, Object arg) {
//        int drillAmount = ((Flux)o).getDrillAmount();
//        System.out.println("All is flux! " + drillAmount);
//    }
//}
