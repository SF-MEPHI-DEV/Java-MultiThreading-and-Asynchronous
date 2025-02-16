package ru.mephi.week3.lesson2;

import java.util.concurrent.*;

public class FutureExample2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {
            System.out.println(Thread.currentThread().getName() + ": Начинаем вычисление");
            Thread.sleep(2000);
            return 42;
        };

        Future<Integer> future = executor.submit(task);
        System.out.println("Задача запущена, ждём результат");

        while (!future.isDone()) {
            // работа в фоне
            Thread.sleep(300);
        }
        Integer result = future.get();

        System.out.println("Результат вычислений: " + result);
        executor.shutdown();

    }

}
