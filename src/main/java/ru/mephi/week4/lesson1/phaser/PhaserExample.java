package ru.mephi.week4.lesson1.phaser;

import java.util.concurrent.Phaser;

public class PhaserExample {
    public static void main(String[] args) {
        Phaser phaser = new Phaser();

        phaser.bulkRegister(3);

        for (int i = 1; i <= 3; i++) {
            int taskId = i;
            new Thread(() -> {
                printInfo("Задача " + taskId + " зарегистрирована.");

                doWork(taskId, 1);
                phaser.arriveAndAwaitAdvance();

                doWork(taskId, 2);
                if (taskId == 2) {
                    System.out.println("Задача " + taskId + " завершает работу и снимается с регистрации.");
                    phaser.arriveAndDeregister();
                    return;
                }
                phaser.arriveAndAwaitAdvance();

                doWork(taskId, 3);
                phaser.arriveAndAwaitAdvance();
            }).start();
        }
    }

    private static void doWork(int taskId, int phase) {
        System.out.println("Задача " + taskId + " выполняет фазу " + phase);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printInfo(String info) {
        System.out.println(info);
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {

        }
    }

}
