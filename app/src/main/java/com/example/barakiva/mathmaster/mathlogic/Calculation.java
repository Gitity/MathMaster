package com.example.barakiva.mathmaster.mathlogic;

public class Calculation {

    public boolean isUserDigitsEqualToEquationResult(String numbersEntered, int result) {
        if (numbersEntered.length() == Integer.toString(result).length()) {
            return true;
        } else {
            return false;
        }
    }

}
