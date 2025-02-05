package ru.mephi.week2.lesson1.synchronizedPkg;

public class SynchronizedBlockTrouble {

    public static class OnlineStore {
        private int userBalance = 100;
        private int stockCount = 50;

        // Плохо: Блокируем всё, даже если операции независимые!
        public synchronized void purchase(int price) {
            if (userBalance >= price) {
                userBalance -= price;
                stockCount--;
            }
        }

        public synchronized void returnItem(int price) {
            userBalance += price;
            stockCount++;
        }

        public synchronized int getStockCount() {
            return stockCount;
        }

        public synchronized int getUserBalance() {
            return userBalance;
        }
    }

}
