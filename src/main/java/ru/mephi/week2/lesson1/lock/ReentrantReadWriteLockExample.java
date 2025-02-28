package ru.mephi.week2.lesson1.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedData {

    private int value1 = 100;
    private int value2 = 200;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void readValues() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " читает: value1=" + value1 + ", value2=" + value2);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateValues() {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " обновляет данные");
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
            data.updateValues();
        }, "Писатель").start();

        new Thread(() -> {
            data.readValues();
        }, "Читатель-новый").start();

    }
}