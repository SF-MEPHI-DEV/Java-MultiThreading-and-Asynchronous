package ru.mephi.week4.lesson1.phaser;

import java.util.concurrent.Phaser;

public class SimplePhaserExample {

    public static void main(String[] args) throws InterruptedException {

        Phaser phaser = new Phaser(1);

        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " начал выполнение.");
            try {
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " достиг фазы 1.");
                phaser.arriveAndAwaitAdvance();
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " завершил фазу 1.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < 3; i++) {
            phaser.register();
            new Thread(task).start();
        }

        phaser.arriveAndAwaitAdvance();

        System.out.println("Все потоки достигли фазы 1, продолжаем выполнение.");
    }
}
