package com.example.barakiva.mathmaster.mathlogic;

import android.util.Log;

import java.util.Random;

public class Equation {

    private int firstNumber;
    private int secondNumber;
    private String stringOperator;
    private Operator operatorType;
    private Random random = new Random();

    public Equation(Operator operatorType) {
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
        this.operatorType = operatorType;
    }

    public void generateRandomNumbers(int difficulty) {
        firstNumber = (random.nextInt(10) + 1);
        secondNumber = (random.nextInt(10) + 1);
        Log.d("Random generation", "This worked");
    }

    public int generateEquation(int firstNumber, int secondNumber) {
        int result = 0;
        switch (operatorType) {
            case ADDITION:
                result = firstNumber + secondNumber;
                break;
            case SUBTRACTION:
                if (firstNumber < secondNumber) {
                    result = secondNumber - firstNumber;
                } else {
                    result = firstNumber - secondNumber;
                }
                break;
            case MULTIPLICATION:
                result = firstNumber * secondNumber;
                break;
            case DIVISION:
                if (firstNumber < secondNumber) {
                    result = secondNumber / firstNumber;
                } else {
                    result = firstNumber / secondNumber;
                }
                break;
        }
        return result;
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
