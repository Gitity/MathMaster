package com.example.barakiva.mathmaster;

import android.app.Activity;
import android.content.Intent;

public class Exercise {
    Intent intent;
    private long beginExerciseTimeStamp;
    private long endExerciseTimeStamp;
    private int drillAmount = 0;



//    public void endExercise() {
//        initiateSessionOverScreen();
//    }
//
//    private void initiateSessionOverScreen(Activity context) {
//        intent = new Intent(context);
//
//    }
    public void setDrillAmount(int da) {
        this.drillAmount = da;
        System.out.println("Drillamount is " + this.drillAmount + " !");
    }

    public int getDrillAmount() {
        return drillAmount;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
    public long getSessionLength() {
        return ((getEndExerciseTimeStamp() - getBeginExerciseTimeStamp())/1000);

    }

    public long getBeginExerciseTimeStamp() {
        return beginExerciseTimeStamp;
    }

    public void setBeginExerciseTimeStamp(long beginExerciseTimeStamp) {
        this.beginExerciseTimeStamp = beginExerciseTimeStamp;
    }

    public long getEndExerciseTimeStamp() {
        return endExerciseTimeStamp;
    }

    public void setEndExerciseTimeStamp(long endExerciseTimeStamp) {
        this.endExerciseTimeStamp = endExerciseTimeStamp;
    }
}
