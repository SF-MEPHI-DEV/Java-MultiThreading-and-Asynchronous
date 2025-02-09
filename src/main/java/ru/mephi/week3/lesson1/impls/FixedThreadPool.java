package ru.mephi.week3.lesson1.impls;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + " выполняет задачу...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " завершил работу.");
        };

        for (int i = 1; i <= 5; i++) {
            executor.submit(task);
        }

        executor.shutdown();

    }
}
