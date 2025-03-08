package ru.mephi.week3.lesson2.completableFuture;

import java.util.concurrent.CompletableFuture;

public class ThenComparison {

    private static String thenCombineExample() {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task 1 is executing on: " + Thread.currentThread().getName());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "Hello ";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task 2 is execution on: " + Thread.currentThread().getName());

            return "world!";
        });

        CompletableFuture<String> future = future2.thenCombine(future1, (String s1, String s2) -> {
            System.out.println("Task 3 is executing on: " + Thread.currentThread().getName());
            return s1 + s2;
        });

        return future.join();
    }

    private static void thenApplyExample() {

        /**
         * В данном примере используется thenApply(), чтобы трансформировать результат,
         * не создавая новых асинхронных операций. Всё происходит в том же потоке,
         * и результат уже доступен для использования.
         */

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 runs on: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 5;
        }).thenApply(userId -> {
            System.out.println("Task2 runs on: " + Thread.currentThread().getName());
            /*CompletableFuture.runAsync(
                    () -> System.out.println("Inside task runs on: " + Thread.currentThread().getName())
            );*/
            return userId * 2;
        });

        Integer transformedResult = future.join();
        System.out.println("Результат преобразования: " + transformedResult);
    }

    private static void thenComposeExample() {

        CompletableFuture<Integer> futureUserId = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 runs on: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 5;
        });

        CompletableFuture<String> futureUserProfile = futureUserId.thenCompose(userId -> {
            System.out.println("Task2 runs on: " + Thread.currentThread().getName());
            return CompletableFuture.supplyAsync(() -> {
                System.out.println("Inside task runs on: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "Профиль пользователя с ID " + userId;
            });
        });

        String userProfile = futureUserProfile.join();
        System.out.println(userProfile);

    }

    public static void main(String[] args) {

        // System.out.println(thenCombineExample());
        // thenApplyExample();
        thenComposeExample();

    }

}
