package ru.mephi.week2.lesson2.collections;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerExample {

    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(3);
        new ReentrantLock(true);

        // Поток-производитель
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    System.out.println("Производитель добавляет: " + i);
                    queue.put(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Поток-потребитель
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    Integer value = queue.take();
                    System.out.println("Потребитель забрал: " + value);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();
        consumer.start();
    }

}
