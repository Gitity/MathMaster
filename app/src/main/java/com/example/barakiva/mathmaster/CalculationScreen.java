package com.example.barakiva.mathmaster;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
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
import com.example.barakiva.mathmaster.mathlogic.Operator;

public class CalculationScreen extends AppCompatActivity implements View.OnClickListener {

    //Elements
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    ImageView testCube;
    //Math Logic
    Calculation calculation = new Calculation();
    Equation equation;
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

    float density;

    //Classes
    Tick tick;
    ViewHelper viewHelper;
    Exercise exercise = new Exercise();

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_screen);
        tick = new Tick(this.getApplicationContext(), viewHelper);
        //Finding elements
        equationView = findViewById(R.id.equationView);
        progressBar = findViewById(R.id.progressBar);
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
         equation= new Equation(getOperatorType());
        viewHelper = new ViewHelper(constraintLayout);

        clearAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnswer();
            }
        });
    }
    public Operator getOperatorType() {
        Operator operator;
        if(getIntent().hasExtra("Operation")) {
            operator = (Operator) getIntent().getSerializableExtra("Operation");
        } else {
            operator = null;
        }
        return operator;
    }

    public void handleOnClick(View view, ImageView asset) {
        if (view instanceof Button) {
            updateNumberInputWithClick(view);
            if (!(viewHelper.hasParent(asset, this))) {
                Log.d("GENERATED", "initiated");
                generateAssetOnScreen(view, asset);
            }
            viewHelper.addView(asset, viewHelper.getViewLocation(view));

            if (!tick.isAnimating()) {
                tick.animateCurvedTick(view, asset, progressBar, constraintLayout);
            } else {
                Log.d("Animation state", "Tick is animating, please stand by");
            }
            if (!isUserInputInTheRightDirection()) { wrongAnswer(); }
        } else {
            System.out.println("This was not a button!");
        }
        Calculation calculation = new Calculation();
        //Does the length of the user input equal to the equation length?
        if (calculation.isUserInputLengthEqualToEquationLength(numbersEntered, result)) {
            //Does the user input equal to the result?
            isUserInputCorrect();
        }
    }
    public void isUserInputCorrect() {
        if (Integer.parseInt(numbersEntered) == result) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }
    public void correctAnswer() {
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
        exercise.setDrillAmount(exercise.getDrillAmount()+1);
        System.out.println("Flux is " + exercise.getDrillAmount());
        endOfDrillCheck();
    }
    public void wrongAnswer() {
        startVibration();
        System.out.println("Wrong answer nigga");
        clearAnswer();
        equationView.setTextColor(Color.RED);
    }
    public void endOfDrillCheck() {
        if (exercise.getDrillAmount() == amountOfDrills) {
            pushTheProgressBar();
            workoutHasEnded();
        } else {
            pushTheProgressBar();
            runOnce();
        }
    }

    @Override
    public void onClick(View v) {
        handleOnClick(v, testCube);
    }

    public void updateNumberInputWithClick(View v) {
        Button b = (Button) v;
        numbersEntered += b.getText();
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
        int progress = (int) (((float)exercise.getDrillAmount() / (float) amountOfDrills)* 100);
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
            viewHelper.restartView(asset);
        }
//        asset.setX(arr[0]);
//        asset.setY(arr[1] - heightOffset);
//        int[] arr = viewHelper.getViewLocation(v);
//        int heightOffset = viewHelper.getHeightOffset(this , constraintLayout);
    }

    public boolean isUserInputInTheRightDirection() {
        String[] numbersEnteredDigits = numbersEntered.split("");
        String[] resultDigits = Integer.toString(result).split("");

        for(int i = 0; i <resultDigits.length - 1; i++) {
            if (!numbersEnteredDigits[i].equals(resultDigits[i])) {
                return false;
            }
        }
        return true;
    }

    public void runOnce() {
        clearAnswer();
        clearColor();
        //TODO check if I should encapsulate generateNumbers() within generateEquation()
        equation.generateNumbers(1);
        result = equation.generateEquation(equation.getFirstNumber(), equation.getSecondNumber());
        setEquationView();
        System.out.println(equation.getFirstNumber() + " " + equation.getSecondNumber());
        System.out.println(result);
    }
    public void clearAnswer() {
        numbersEntered = "";
    }
    public void clearColor() {
        equationView.setTextColor(Color.GRAY);
    }
    public void setEquationView() {
        equationView.setText(equation.getFirstNumber() + " " + equation.getStringOperator() + " " +
                equation.getSecondNumber());
    }

    public void testBtn(View view) {
        exercise.setBeginExerciseTimeStamp(exercise.getCurrentTime());
        runOnce();
        openDialog();
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
        Intent previousIntent = getIntent();
        if (previousIntent != null) {
            if (previousIntent.getBooleanExtra("replay", true)) {
                Log.d("modal message", "hey there!");
                runOnce();
            }
        }
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