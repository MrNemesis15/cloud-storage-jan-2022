package com.geekbrains.cloud.stream;

public class Sum implements Func {

    @Override
    public int apply(int arg1, int arg2) {
        return arg1 + arg2;
    }

}
