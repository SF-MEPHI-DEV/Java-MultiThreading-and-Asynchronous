package ru.mephi.week3.lesson1.impls;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPool {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 1; i <= 5; i++) {
            executor.execute(() -> {
                String name = Thread.currentThread().getName();
                System.out.println(name + " выполняет задачу...");
            });
        }

        executor.shutdown();
    }
}
