package ru.mephi.week2.lesson1.synchronizedPkg;

public class SynchronizedBlockExample {

    public static class OnlineStore {
        private int userBalance = 100;
        private int stockCount = 50;

        private final Object balanceLock = new Object();  // Блокировка для баланса
        private final Object stockLock = new Object();    // Блокировка для склада

        public void purchase(int price) {
            boolean canPurchase;

            synchronized (balanceLock) {
                canPurchase = userBalance >= price;
                if (canPurchase) {
                    userBalance -= price;
                }
            }

            synchronized (stockLock) {
                if (canPurchase) {
                    stockCount--;
                }
            }
        }

        public void returnItem(int price) {
            synchronized (balanceLock) {
                userBalance += price;
            }
            synchronized (stockLock) {
                stockCount++;
            }
        }

        public int getStockCount() {
            synchronized (stockLock) {
                return stockCount;
            }
        }

        public int getUserBalance() {
            synchronized (balanceLock) {
                return userBalance;
            }
        }
    }

    public static void main(String[] args) {
        OnlineStore store = new OnlineStore();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                store.purchase(10);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                store.returnItem(10);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println("Balance: " + store.getUserBalance());
                System.out.println("Stock: " + store.getStockCount());
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final balance: " + store.getUserBalance());
        System.out.println("Final stock count: " + store.getStockCount());

    }

    public int count;

    public void increment() {
        synchronized (this) {  // Блокировка объекта this
            count++;
            synchronized (this) {  // Повторный вход в ту же блокировку
                count++;
            }
        }
    }

    class Counter {
        private int count = 0;

        public synchronized void increment() {  // Первый уровень блокировки
            count++;
            doubleIncrement();  // Вызываем другой synchronized-метод
        }

        public synchronized void doubleIncrement() {  // Второй уровень блокировки
            count++;
        }
    }

}
