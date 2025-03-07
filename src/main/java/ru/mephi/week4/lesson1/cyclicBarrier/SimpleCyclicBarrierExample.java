package ru.mephi.week4.lesson1.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

public class SimpleCyclicBarrierExample {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("Executes on: " + Thread.currentThread().getName());
            System.out.println("Все потоки собрались");
        });

        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " подошел к барьеру");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " вышел из барьера");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }
    }

}
