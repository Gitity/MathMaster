package com.example.barakiva.mathmaster.animations;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.barakiva.mathmaster.ViewHelper;

public class Tick {
    private boolean isAnimating = false;
    private ViewHelper viewHelper;

    public Context context;
    public Tick (Context context, ViewHelper viewHelper) {
        this.context = context;
        this.viewHelper = viewHelper;
    }

    public void animateCurvedTick(View v,final ImageView asset, ProgressBar progressBar,final ConstraintLayout constraintLayout) {
        //Height and width difference between progressbar and clicked btn
        System.out.println("Progress bar Y is : " + progressBar.getY() +
                "current view Y is : " + v.getY());

        final float assetWidth = asset.getDrawable().getIntrinsicWidth();;
        final float assetHeight = asset.getDrawable().getIntrinsicHeight();;
        System.out.println("Curved tick height is  : " + assetHeight);
        System.out.println("Curved tick width is  : " + assetWidth);

//        getDimensionsHeight(v);

        final float height = v.getY() - progressBar.getY();
        final float width = v.getX() - (progressBar.getX() + 100);


        //Fade Animation
        //fadeObject(v, asset);
        setCurvedMotion(asset, constraintLayout, v);
//        if ( viewHelper.isParentCheck(asset)) {
//            viewHelper.restartView(asset, constraintLayout);
//        }

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
//    public int getDimensionsHeight(View v) {
//        //object to store display information
//        DisplayMetrics metrics = new DisplayMetrics();
//        //get display information
//        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        //show display width and height
//        Log.d("Context check", context.toString());
////        public void onWindowFocusChanged(boolean hasFocus){
////            super.onWindowFocusChanged(hasFocus);
////            String x = Integer.toString(v.getWidth());
////            String y = Integer.toString(v.getHeight());
////            //show ImageView width and height
////        }
//        return 5;
//    }
    public void fadeObject(View v, ImageView asset) {
        final ObjectAnimator fadeAlpha = ObjectAnimator.ofFloat(asset,"alpha", 0);
        fadeAlpha.setDuration(1250);
        //TODO Set a listener that sets isAnimating to true when running
    }
//    public boolean isItRightEdge(View v) {
//
//    }

    public void setCurvedMotion(final ImageView asset,final ConstraintLayout constraintLayout,final View v) {

        ValueAnimator myAnimator = ValueAnimator.ofFloat(0,1);
        myAnimator.setDuration(2500);
        myAnimator.setStartDelay(110);
        myAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue()))
                        .floatValue();
                if (animation.getCurrentPlayTime() >= 1500) {
                    //          fadeAlpha.start();
                }
                //Value for precise positioning is 110
                //Used to use - 100 after the Math.cos
                //TODO add a conditional for the edge cases
               // System.out.println("HEIGHT of view is : " + v.getY());
                asset.setTranslationX((float) (151 *Math.sin(value*Math.PI)));
                asset.setTranslationY((float) (v.getY() *Math.cos(value*Math.PI)) - 150);
            }
        });
        myAnimator.start();
        myAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setAnimating(true);
                Log.d("ANIMATION", "animation commencing!");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setAnimating(false);
                Log.d("ANIMATION", "animation ended!");
                constraintLayout.removeView(asset);
                asset.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setAnimating(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }
}
