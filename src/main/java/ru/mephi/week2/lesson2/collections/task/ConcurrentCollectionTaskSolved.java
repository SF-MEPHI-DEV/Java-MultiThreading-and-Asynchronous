package ru.mephi.week2.lesson2.collections.task;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentCollectionTaskSolved {

    /**
     * <H2>Задача: Потокобезопасная обработка заказов в интернет-магазине</H2>
     * <br>
     * <H2>📌 Задание</H2>
     * <p>Вам необходимо реализовать систему обработки заказов в интернет-магазине,
     * которая корректно работает в многопоточной среде.</p>
     * <p>Заказы поступают в специальную очередь от пользователей, после чего несколько потоков-обработчиков
     * забирают заказы и выполняют их.</p>
     * <br>
     * <H2>Условия</H2>
     * <ol>
     *    <li>Несколько потоков могут добавлять заказы в очередь одновременно.</li>
     *    <li>Несколько потоков-обработчиков параллельно забирают заказы из очереди и обрабатывают их.</li>
     *    <li>Каждый заказ должен быть обработан ровно один раз.</li>
     *    <li>Обработчики не должны простаивать, если в очереди есть заказы.</li>
     *    <li>Выбор коллекции должен обеспечить потокобезопасность и высокую производительность.</li>
     * </ol>
     * <br>
     * <H2>Пример работы:</H2>
     * <pre>
     * Добавитель-1 добавил заказ: Заказ #5a3b4e89
     * Добавитель-2 добавил заказ: Заказ #f67c8d12
     * Обработчик-1 обрабатывает заказ: Заказ #5a3b4e89
     * Обработчик-2 обрабатывает заказ: Заказ #f67c8d12
     * Добавитель-1 добавил заказ: Заказ #a9e12d34
     * Добавитель-2 добавил заказ: Заказ #c56b7f99
     * ...
     * Все заказы обработаны!
     * </pre>
     */

    private final static ConcurrentLinkedQueue<String> orderQueue = new ConcurrentLinkedQueue<>();

    public static void addOrder(String order) {
        orderQueue.offer(order);
        System.out.println(Thread.currentThread().getName() + " добавил заказ: " + order);
    }

    public static void processOrders() {
        String order;
        while ((order = orderQueue.poll()) != null) {
            System.out.println(Thread.currentThread().getName() + " обрабатывает заказ: " + order);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable orderAdder = () -> {
            for (int i = 0; i < 5; i++) {
                addOrder("Заказ #" + UUID.randomUUID());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable orderProcessor = ConcurrentCollectionTaskSolved::processOrders;

        Thread adder1 = new Thread(orderAdder, "Поставщик-1");
        Thread adder2 = new Thread(orderAdder, "Поставщик-2");
        Thread processor1 = new Thread(orderProcessor, "Обработчик-1");
        Thread processor2 = new Thread(orderProcessor, "Обработчик-2");

        adder1.start();
        adder2.start();

        Thread.sleep(500);

        processor1.start();
        processor2.start();

        adder1.join();
        adder2.join();
        processor1.join();
        processor2.join();

        System.out.println("Все заказы обработаны");
    }

}
