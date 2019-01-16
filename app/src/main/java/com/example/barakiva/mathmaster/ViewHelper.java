package com.example.barakiva.mathmaster;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class ViewHelper {

    Activity activity;
    ConstraintLayout constraintLayout;


    public ViewHelper (ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
        this.activity =  (Activity) constraintLayout.getContext();
    }

    public boolean hasParent (ImageView asset, Activity activity) {
        if(activity.findViewById(asset.getId()) == null) {
            return false;
        } else {
            return true;
        }
    }

    private int getHeightOffset() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels - constraintLayout.getMeasuredHeight();
    }
    public int[] getViewLocation(View v) {
        int[] arr = new int[2];
        v.getLocationOnScreen(arr);
        System.out.println("Location of clicked View on screen is " + arr[0] + " x and y is " + arr[1]);
        return arr;
    }
    public boolean isParentCheck(ImageView asset) {
        if (asset.getParent() != null) {
            Log.d("Asset parent", "is NOT null");
            return true;
        } else {
            Log.d("Asset parent", "is null");
            return false;
        }
    }
    public void restartView(ImageView asset) {
        ConstraintSet removeSet = new ConstraintSet();
        Log.d("Asset Check", asset.toString());

        if (asset != null){
            Log.d("Asset Check", "Asset was NOT null");
            //((ViewGroup)asset.getParent()).removeView(asset);
        }
        ConstraintLayout parent = ((ConstraintLayout)asset.getParent());
        parent.removeView(asset);
        removeSet.clone(parent);
        removeSet.clear(asset.getId(), ConstraintSet.TOP);
        removeSet.clear(asset.getId(), ConstraintSet.LEFT);
        removeSet.applyTo(parent);
    }
    public void addView(ImageView asset, int[] viewCoordinates) {
        constraintLayout.removeView(asset);
        constraintLayout.addView(asset, 0);
        asset.setVisibility(View.VISIBLE);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.connect(asset.getId(),ConstraintSet.TOP, constraintLayout.getId(),
                ConstraintSet.TOP, viewCoordinates[1] - getHeightOffset());
        set.connect(asset.getId(),ConstraintSet.LEFT,
                constraintLayout.getId(),ConstraintSet.LEFT, viewCoordinates[0]);
        set.applyTo(constraintLayout);
        asset.setElevation(1);
    }
    public Activity getActivity() {
        return activity;
    }


}
