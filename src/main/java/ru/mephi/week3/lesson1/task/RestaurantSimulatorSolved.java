package ru.mephi.week3.lesson1.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RestaurantSimulatorSolved {

    /**
     * <H2>Задача: Система обработки заказов ресторана</H2>
     * <br>
     * <H2>📌 Задание</H2>
     * <p>Представьте, что вы разрабатываете систему обработки заказов для ресторана.
     * Заказы поступают от клиентов, затем передаются на приготовление,
     * после чего курьеры их доставляют. </p>
     *
     * <p>Ваша задача – реализовать многопоточное выполнение этой системы
     * с помощью `ExecutorService`, так чтобы:</p>
     * <ul>
     *    <li>Заказы обрабатывались и передавались на кухню</li>
     *    <li>Кухня готовила блюда в нескольких потоках</li>
     *    <li>Курьеры доставляли готовые заказы</li>
     * </ul>
     *
     * <H2>💡 Условия:</H2>
     * <ol>
     *    <li>Заказы поступают каждую секунду.</li>
     *    <li>Приготовление каждого заказа занимает 3 секунды.</li>
     *    <li>Доставка занимает 2 секунды.</li>
     *    <li>Общее количество заказов – 10.</li>
     * </ol>
     *
     * <br>
     */

    private static final int ORDER_COUNT = 10;

    public static void main(String[] args) {
        ExecutorService orderService = Executors.newSingleThreadExecutor();
        ExecutorService kitchenService = Executors.newFixedThreadPool(2);
        ExecutorService deliveryService = Executors.newCachedThreadPool();

        for (int i = 1; i <= ORDER_COUNT; i++) {
            int orderId = i;
            orderService.submit(() -> {
                System.out.println("Новый заказ #" + orderId);
                kitchenService.submit(() -> prepareOrder(orderId, deliveryService));
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        shutdownAndAwaitTermination(orderService);
        shutdownAndAwaitTermination(kitchenService);
        deliveryService.shutdown();
    }

    private static void prepareOrder(int orderId, ExecutorService deliveryService) {
        System.out.println("Приготовление заказа #" + orderId);
        try {
            Thread.sleep(3000); // Время готовки
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Заказ #" + orderId + " готов! Передаём курьеру.");
        deliveryService.submit(() -> deliverOrder(orderId));
    }

    private static void deliverOrder(int orderId) {
        System.out.println("Курьер доставляет заказ #" + orderId);
        try {
            Thread.sleep(2000); // Время доставки
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Заказ #" + orderId + " доставлен!");
    }

    private static void shutdownAndAwaitTermination(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
