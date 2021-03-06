package com.example.barakiva.mathmaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barakiva.mathmaster.mathlogic.Calculation;

import java.lang.reflect.Field;

public class SessionDialog extends DialogFragment implements View.OnClickListener {

    Activity context;

    public SessionDialog() {
        this.context = getPassedContext();
    }

    public void setPassedContext(Activity activity) {
        this.context = activity;
    }
    public Activity getPassedContext() {
        return this.context;
    }


    //Longer way
    public interface OnInputListener {
        void sendInput(String input);
    }

    //widgets
    private EditText input;
    private Button playAgainBtn;

    //Score
    TextView bestScoreText;
    TextView yourScoreText;
    //Buttons
    TextView replaySessionLabel;
    ImageView replaySessionBtn;
    Button nextLevelBtn;
    Button backToMenuBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.session_over, container , false);
        //Score textviews
        bestScoreText = view.findViewById(R.id.bestScoreText);
        yourScoreText = view.findViewById(R.id.yourScoreText);

        //Buttons
        replaySessionBtn = view.findViewById(R.id.replaySessionBtn);
        replaySessionLabel = view.findViewById(R.id.replaySessionLabel);
        nextLevelBtn = view.findViewById(R.id.nextLevelBtn);
        backToMenuBtn = view.findViewById(R.id.backToMenuBtn);


        replaySessionLabel.setOnClickListener(this);
        replaySessionBtn.setOnClickListener(this);

        //AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //                getDialog().dismiss();
        //Assigning listeners to btns

        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMenu();
            }
        });
        nextLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNextLevel();
            }
        });



        return view;
    }

    @Override
    public void onClick(View v) {
        replayPreviousSession();
    }
    public void replayPreviousSession() {
        getDialog().dismiss();
    }
    public void goBackToMenu() {
        Intent goBackToMenuIntent = new Intent (getActivity(), MainActivity.class);
        startActivity(goBackToMenuIntent);
    }
    public void goNextLevel() {

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.MATCH_PARENT );
                    //ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        CalculationScreen calculation = new CalculationScreen();
        calculation.onResume();
    }


    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }
}
