package com.atait.exercises.enums;


import java.util.Optional;

public enum OperatorEnum {
    GT,
    LT,
    EQ,
    GTE,
    LTE;


    public static Optional<OperatorEnum> of(String s){
        try {
            return Optional.of(OperatorEnum.valueOf(s.toUpperCase()));
        }catch (IllegalArgumentException e){
            return Optional.empty();
        }
    }
}


