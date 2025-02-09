package ru.mephi.week3.lesson1.impls;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutor {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 3; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Выполняется задача " + taskId + " потоком " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();

    }

}
