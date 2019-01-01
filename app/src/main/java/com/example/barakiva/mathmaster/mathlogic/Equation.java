package com.example.barakiva.mathmaster.mathlogic;

import android.app.Activity;

import java.util.Random;

public class Equation {

    private int firstNumber;
    private int secondNumber;
    private String stringOperator;
    private Operator operatorType;
    private Random random = new Random();

    public Equation(Operator operatorType) {
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
        this.operatorType = operatorType;
    }

    public void generateNumbers(int difficulty) {
        //Assigning random numbers
        firstNumber = (random.nextInt(10) + 1);
        secondNumber = (random.nextInt(10) + 1);
    }

    public int generateEquation(int a, int b) {
        int result = 0;
        switch (operatorType) {
            case ADDITION:
                result = a + b;
                break;
            case SUBTRACTION:
                if (a < b) {
                    result = b - a;
                } else {
                    result = a - b;
                }
                break;
            case MULTIPLICATION:
                result = a * b;
                break;
            case DIVISION:
                if (a < b) {
                    result = b / a;
                } else {
                    result = a / b;
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
