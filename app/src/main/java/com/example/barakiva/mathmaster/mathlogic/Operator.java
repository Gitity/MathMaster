package com.example.barakiva.mathmaster.mathlogic;

public enum Operator {
        ADDITION(1),
        SUBTRACTION(2),
        MULTIPLICATION(3),
        DIVISION(4);

        private int id;
        Operator(int id) {
                this.id = id;
        }

        public static Operator fromId(int id) {
                for (Operator type : values()) {
                        if (type.getId() == id) {
                                return type;
                        }
                }
                return null;
        }

        public int getId() {
                return id;
        }
}

