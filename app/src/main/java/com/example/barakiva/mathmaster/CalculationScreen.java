package com.example.barakiva.mathmaster;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Random;

public class CalculationScreen extends AppCompatActivity implements View.OnClickListener {
    Random random;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    ImageView placeCube;

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

    TextView equationView;
    ImageView curvedTick;

    String numbersEntered = "";
    int result;
    int amountOfDrills = 5;
    Flux flux = new Flux();;

    float density;

    //Classes
    SessionDetails sessionDetails;

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_screen);
        String sessionId = getIntent().getStringExtra("Operation");
        random = new Random();

        //Finding elements
        equationView = findViewById(R.id.equationView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        placeCube = findViewById(R.id.placeCube);
        //Gameification
        curvedTick = new ImageView(this);
        curvedTick.setImageResource(R.drawable.curvedtick);
        curvedTick.setId(curvedTick.generateViewId());
        density = getResources().getDisplayMetrics().density;
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
        //Info
        sessionDetails = new SessionDetails();

        clearAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnswer();
            }
        });

        System.out.println(sessionId);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button b = (Button) v;
            numbersEntered += b.getText();
            generateCurvedTick(v);
            animateCurvedTick(v, curvedTick);
            if (!isUserInTheRightDirection()) {
                System.out.println("Wrong start");
                startVibration();
                clearAnswer();
                equationView.setTextColor(Color.RED);
            }
        } else {
            System.out.println("This was not a button!");
        }

        if (isUserDigitsEqualToEquationResult()) {
            if (Integer.parseInt(numbersEntered) == result) {
                //Correct answer scenario
//                generateCurvedTick(v);
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
                    sessionDetails.setSessionEndTime(System.currentTimeMillis());
                    System.out.println("Good job, it only took you : " +
                            (sessionDetails.getSessionEndTime() - sessionDetails.getSessionStartTime()));
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


    public boolean isUserDigitsEqualToEquationResult() {
        if (numbersEntered.length() == Integer.toString(result).length()) {
            return true;
        } else {
            return false;
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
    public void generateCurvedTick(View v) {
        constraintLayout = findViewById(R.id.constraintLayout);
        int[] arr = new int[2];
        v.getLocationOnScreen(arr);
        System.out.println(arr[0] + " x and y is " + arr[1]);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightOffset = dm.heightPixels - constraintLayout.getMeasuredHeight();
        //Restart the view
        ConstraintSet removeSet = new ConstraintSet();
        constraintLayout.removeView(curvedTick);

        removeSet.clone(constraintLayout);
        removeSet.clear(curvedTick.getId(), ConstraintSet.TOP);
        removeSet.clear(curvedTick.getId(), ConstraintSet.LEFT);
        removeSet.applyTo(constraintLayout);

        constraintLayout.addView(curvedTick, 0);
        curvedTick.setVisibility(View.VISIBLE);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.connect(curvedTick.getId(),ConstraintSet.TOP, constraintLayout.getId(),ConstraintSet.TOP, arr[1] - heightOffset);
        set.connect(curvedTick.getId(),ConstraintSet.LEFT, constraintLayout.getId(),ConstraintSet.LEFT, arr[0]);
        set.applyTo(constraintLayout);
        curvedTick.setElevation(1);
//        curvedTick.setX(arr[0]);
//        curvedTick.setY(arr[1] - heightOffset);

        //Testing
//        placeCube.setY(arr[1] - heightOffset);
//        placeCube.setX(arr[0]);


    }
    public void animateCurvedTick(View v, ImageView asset) {
        final ImageView assetFinal = asset;
        //Height and width difference between progressbar and clicked btn
        System.out.println("Progress bar Y is : " + progressBar.getY() +
        "current view Y is : " + v.getY());

        final float curvedTickWidth = curvedTick.getDrawable().getIntrinsicWidth();;
        final float curvedTickHeight = curvedTick.getDrawable().getIntrinsicHeight();;
        System.out.println("Curved tick height is  : " + curvedTickHeight);

        final float height = v.getY() - progressBar.getY();
        final float width = v.getX() - (progressBar.getX() + 100);


        //fade

        final ObjectAnimator fadeAlpha = ObjectAnimator.ofFloat(assetFinal,"alpha", 0);
        fadeAlpha.setDuration(1250);

        ValueAnimator myAnimator = ValueAnimator.ofFloat(0,1);
        myAnimator.setDuration(2500);
        myAnimator.setStartDelay(2500);
        myAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue()))
                        .floatValue();
                if (animation.getCurrentPlayTime() >= 1500) {
          //          fadeAlpha.start();
                }
                assetFinal.setTranslationX((float) (150 *Math.sin(value*Math.PI)));
                assetFinal.setTranslationY((float) (150 *Math.cos(value*Math.PI) -100 ));
            }
        });
        myAnimator.start();
        myAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                constraintLayout.removeView(curvedTick);
                curvedTick.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //   constraintLayout.removeView(curvedTick);

//        AnimatorSet fadeOut = new AnimatorSet();
//        fadeOut.play(fadeAlpha);
//        fadeOut.start();
        //
//        ObjectAnimator someAnimation = ObjectAnimator.ofFloat(imgView, "translationX",0);
//        someAnimation.setDuration(1000);

        //
//        curvedTick.animate()
//                .scaleX(0.3f)
//                .scaleY(0.3f)
//                .setDuration(2500)
//                .withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    curvedTick.animate().scaleX(1f).scaleY(1f).setDuration(1).start();
//                }
//               })
//                .start();
      //  constraintLayout.removeView(curvedTick);
//        curvedTick.animate().scaleX(curvedTickWidth).scaleY(curvedTickHeight).setDuration(1).start();


    }

    public class Equation {
        //Storing the equation numbers and operator
        int firstNumber;
        int secondNumber;
        char operator;
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

    public Equation generateNumbers(int difficulty) {
        //Accessing the class
        Equation equation = new Equation();
        //Assigning random numbers
        equation.firstNumber = (random.nextInt(10) + 1);
        equation.secondNumber = (random.nextInt(10) + 1);

        String sessionId = getIntent().getStringExtra("Operation");
        switch (sessionId) {
            case "addition":
                equation.operator = '+';
                break;
            case "subtraction":
                equation.operator = '-';
                break;
            case "multiplication":
                equation.operator = '*';
                break;
            case "division":
                equation.operator = '/';
                break;
        }
        return equation;
    }

    public int generateEquation(int a, int b) {
        String sessionId = getIntent().getStringExtra("Operation");
        int result = 0;
        switch (sessionId) {
            case "addition":
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
        return result;
    }

    public double runOnce() {
        clearAnswer();
        clearColor();
        Equation equation = new Equation();
        equation = generateNumbers(1);
        System.out.println(equation.firstNumber + " " + equation.secondNumber);
        result = generateEquation(equation.firstNumber, equation.secondNumber);
        System.out.println(result);
        equationView.setText(equation.firstNumber + " " + equation.operator + " " +
                equation.secondNumber);
        return result;

    }

    class SessionDetails{
        private long sessionStartTime;
        private long sessionEndTime;

        public long getSessionEndTime() {
            return sessionEndTime;
        }
        public void setSessionEndTime(long sessionEndTime) {
            this.sessionEndTime = sessionEndTime;
        }

        public long getSessionStartTime() {
            return sessionStartTime;
        }
        public void setSessionStartTime(long sessionStartTime) {
            this.sessionStartTime = sessionStartTime;
        }
    }

    public void testBtn(View view) {
        sessionDetails.setSessionStartTime(System.currentTimeMillis());
        runOnce();
    }
    public void workoutHasEnded() {
        System.out.println("Workout has ended! Good job!");
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
