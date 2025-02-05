package ru.mephi.week2.lesson1.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedData {
    private int value1 = 100;
    private int value2 = 200;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // Читаем данные с блокировкой чтения
    public void readValues() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " читает: value1=" + value1 + ", value2=" + value2);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Обновляем данные с блокировкой записи
    public void updateValues() {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " обновляет данные...");
            value1 = 500;
            value2 = 600;
            System.out.println(Thread.currentThread().getName() + " обновил данные: value1=" + value1 + ", value2=" + value2);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

public class ReentrantReadWriteLockExample {
    public static void main(String[] args) {
        SharedData data = new SharedData();

        for (int i = 0; i < 3; i++) {
            new Thread(data::readValues, "Читатель-" + i).start();
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                data.updateValues();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Писатель").start();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                data.readValues();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Читатель-новый").start();
    }
}