package ru.mephi.week3.lesson2;

import java.util.concurrent.*;

public class FutureExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {
            System.out.println(Thread.currentThread().getName() + ": Начинаем вычисление");
            Thread.sleep(2000);
            return 42;
        };

        Future<Integer> future = executor.submit(task);
        System.out.println("Задача запущена, ждём результат");

        Integer result = future.get(); // Блокирующий вызов

        System.out.println("Результат вычислений: " + result);
        executor.shutdown();

    }

}
