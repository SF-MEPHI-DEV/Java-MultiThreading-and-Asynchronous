package ru.mephi.week2.lesson1.volatilePkg;

public class VolatileProblem {

    static class A {
        int number;
        int number2;
        volatile boolean ready;
    }

/*
    private static int number;
    private static boolean ready = false;
*/

    private static class Reader extends Thread {
        A a;
        public Reader(A a) {this.a = a;}
        @Override
        public void run() {
            while (!a.ready) {
                Thread.yield();
            }
            System.out.println(a.number);
        }
    }

    public static void main(String[] args) {
        A a = new A();
        new Reader(a).start();
        a.number = 10;
        a.ready = true;

       /* number = 42;
        ready = true;*/
    }

}
