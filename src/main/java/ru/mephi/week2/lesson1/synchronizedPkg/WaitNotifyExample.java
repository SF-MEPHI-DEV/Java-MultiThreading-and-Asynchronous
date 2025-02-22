package ru.mephi.week2.lesson1.synchronizedPkg;

class Store {
    private int product = 0;

    public synchronized void produce() throws InterruptedException {
        while (product > 0) {
            this.wait();
            System.out.println("is waiting in produce");
        }
        product++;
        System.out.println("Произведён 1 товар");
        notify();
    }

    public synchronized void consume() throws InterruptedException {
        while (product == 0) {
            wait();
            // System.out.println("is waiting in consume");
        }
        product--;
        System.out.println("Потребитель забрал товар");
        this.notify();
        System.out.println("adf");
    }
}

class Producer implements Runnable {
    private final Store store;

    public Producer(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        try {
            while (true) {
                store.produce();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final Store store;

    public Consumer(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        try {
            while (true) {
                store.consume();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class WaitNotifyExample {
    public static void main(String[] args) throws InterruptedException {
        Store store = new Store();
        Thread producer = new Thread(new Producer(store));
        Thread consumer = new Thread(new Consumer(store));

        producer.start();
        consumer.start();

        Thread.sleep(10000);
        producer.interrupt();
        consumer.interrupt();
    }
}
