package ru.mephi.week1.lesson2.threadGroup;

public class ThreadGroupExample {

    static class PrimeFinderThread extends Thread {

        private final int start;
        private final int end;

        public PrimeFinderThread(ThreadGroup group, String name, int start, int end) {
            super(group, name);
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            System.out.println(getName() + " ищет простые числа в диапазоне " + start + " - " + end);
            for (int i = start; i <= end; i++) {
                if (isPrime(i)) {
                    System.out.println(getName() + " нашёл простое число: " + i);
                }
            }
        }

        private boolean isPrime(int num) {
            if (num < 2) return false;
            for (int i = 2; i * i <= num; i++) {
                if (num % i == 0) return false;
            }
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup primeGroup = new ThreadGroup("PrimeGroup");

        Thread t1 = new PrimeFinderThread(primeGroup, "Поток-1", 1, 50);
        Thread t2 = new PrimeFinderThread(primeGroup, "Поток-2", 51, 100);
        Thread t3 = new PrimeFinderThread(primeGroup, "Поток-3", 101, 150);

        t1.start();
        t2.start();
        t3.start();

        while (primeGroup.activeCount() > 0) {
            System.out.println("Ожидание завершения потоков. Активные потоки: " + primeGroup.activeCount());
            Thread.sleep(500);
        }

        System.out.println("Все потоки завершены.");
    }

}
