package ru.mephi.week3.lesson2;

import java.util.concurrent.*;

public class FutureExample3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {
            System.out.println(Thread.currentThread().getName() + ": Начинаем вычисление");
            Thread.sleep(2000);
            return 42;
        };

        Future<Integer> future = executor.submit(task);
        System.out.println("Задача запущена, ждём результат");


        Integer result = null;
        try {
            result = future.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Результат вычислений: " + result);
        executor.shutdown();

    }

}
