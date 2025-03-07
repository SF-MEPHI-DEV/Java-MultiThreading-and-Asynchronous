package ru.mephi.week4.lesson1.phaser;

import java.util.concurrent.Phaser;

public class PhaserParentExample {

    public static void main(String[] args) {
        Phaser mainPhaser = new Phaser(1);

        Phaser sportsCarPhaser = new Phaser(mainPhaser);
        Phaser suvPhaser = new Phaser(mainPhaser);

        startCarRace(sportsCarPhaser, "SportCar-1");
        startCarRace(sportsCarPhaser, "SportCar-2");
        startCarRace(suvPhaser, "SUV-1");
        startCarRace(suvPhaser, "SUV-2");

        for (int i = 0; i < 3; i++) {
            mainPhaser.arriveAndAwaitAdvance();
            System.out.println("Главный фазер: Все машины завершили фазу " + (i + 1) + ". Продолжаем...");
        }

        mainPhaser.arriveAndDeregister();
        System.out.println("Гонка завершена!");
    }

    private static void startCarRace(Phaser phaser, String carName) {
        phaser.register();
        new Thread(() -> {
            performPhase(phaser, carName, "Подготовка к старту");
            performPhase(phaser, carName, "Старт гонки");
            performPhase(phaser, carName, "Финиш!");

            phaser.arriveAndDeregister();
        }).start();
    }

    private static void performPhase(Phaser phaser, String car, String message) {
        try {
            Thread.sleep((long) (Math.random() * 500));
            System.out.println(car + ": " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        phaser.arriveAndAwaitAdvance();
    }
}
