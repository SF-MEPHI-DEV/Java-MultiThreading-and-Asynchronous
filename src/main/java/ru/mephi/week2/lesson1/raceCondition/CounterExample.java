package ru.mephi.week2.lesson1.raceCondition;

public class CounterExample {

    public static class Counter {
        private int count = 0;

        public void increment() {
            count++;
        }

        public void decrement() {
            count--;
        }

        public int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter.decrement();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Ожидалось: 0, получили: " + counter.getCount());
    }

}
