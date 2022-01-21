package com.geekbrains.cloud.stream;

public class Test {

    private void action() {
        System.out.println("Hello world " + this);
    }

    private F test() {
        return Test::action;
    }

    private int f(int a, int b) {
        return a + b + 1;
    }

    private static int ff(int a, int b) {
        return a * b;
    }

    public void foo() {
        Func f = this::f;
        Func f1 = Test::ff;
    }

    public static void main(String[] args) {
        Func f = new Func() {
            @Override
            public int apply(int arg1, int arg2) {
                return arg1 + arg2;
            }
        };
        System.out.println(f.getClass());

        Func f1 = (x, y) -> x + y; // lambda expression
        System.out.println(f1.getClass());

        Func f2 = Integer::sum; // method reference
        System.out.println(f2.getClass());

        System.out.println(f.apply(1, 2));
        System.out.println(f1.apply(1, 2));
        System.out.println(f2.apply(1, 2));

        new Test().test().action(new Test());
        new Test().test().action(new Test());
    }
}
