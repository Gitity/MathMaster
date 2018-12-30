package com.example.barakiva.mathmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arithmetic_screen);
        intent = new Intent(MainActivity.this, CalculationScreen.class);
    }


    public void addition(View view) {
        intent.putExtra("Operation", "addition");
        startActivity(intent);
    }
    public void subtraction(View view) {
        intent.putExtra("Operation", "subtraction");
        startActivity(intent);
    }
    public void multiplication(View view) {
        intent.putExtra("Operation", "multiplication");
        startActivity(intent);
    }
    public void division(View view) {
        intent.putExtra("Operation", "division");
        startActivity(intent);
    }

}
