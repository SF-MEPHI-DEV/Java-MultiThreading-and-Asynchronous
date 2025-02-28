package ru.mephi.week2.lesson1.volatilePkg;

public class VolatileExample {


    private static boolean isJobDone = false;

    public static void main(String[] args) throws InterruptedException {

        Thread workerThread = new Thread(() -> {
            try {
                System.out.println("Поток A начинает работу...");
                Thread.sleep(3000);
                System.out.println("Поток A завершил работу.");
                isJobDone = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread watcherThread = new Thread(() -> {
            while (!isJobDone) {
                Thread.yield();
            }
            System.out.println("Поток B обнаружил, что работа завершена!");
        });

        workerThread.start();
        watcherThread.start();

        workerThread.join();
        watcherThread.join();

        System.out.println("Программа завершена.");
    }
}