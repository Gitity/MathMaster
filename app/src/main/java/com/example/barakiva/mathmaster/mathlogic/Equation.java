package com.example.barakiva.mathmaster.mathlogic;

import android.app.Activity;

import java.util.Random;

public class Equation {

    private int firstNumber;
    private int secondNumber;
    private String stringOperator;
    private Operator operatorType;
    Random random = new Random();

    public Equation() {
        if (operatorType != null) {
            switch(operatorType){
                case ADDITION:
                    stringOperator = "+";
                    break;
                case SUBTRACTION:
                    stringOperator = "-";
                    break;
                case MULTIPLICATION:
                    stringOperator = "*";
                    break;
                case DIVISION:
                    stringOperator = "/";
                    break;
            }
        }
    }

    public Equation generateNumbers(int difficulty, Activity activity) {
        //Accessing the class
        Equation equation = new Equation();
        //Assigning random numbers
        equation.firstNumber = (random.nextInt(10) + 1);
        equation.secondNumber = (random.nextInt(10) + 1);

//        if (activity.getIntent().hasExtra("Operation")) {
//            String sessionId = activity.getIntent().getStringExtra("Operation");
//            switch (sessionId) {
//                case "addition":
//                    equation.stringOperator = '+';
//                    break;
//                case "subtraction":
//                    equation.stringOperator = '-';
//                    break;
//                case "multiplication":
//                    equation.stringOperator = '*';
//                    break;
//                case "division":
//                    equation.stringOperator = '/';
//                    break;
//            }
//        }

        return equation;
    }
    public int getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(int firstNumber) {
        this.firstNumber = firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(int secondNumber) {
        this.secondNumber = secondNumber;
    }

    public String getStringOperator() {
        return stringOperator;
    }

    public void setStringOperator(String stringOperator) {
        this.stringOperator = stringOperator;
    }
}
