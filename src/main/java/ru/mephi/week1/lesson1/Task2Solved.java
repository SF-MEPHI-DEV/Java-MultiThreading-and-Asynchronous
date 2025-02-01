package ru.mephi.week1.lesson1;

public class Task2Solved {


    /**
     * <H2>Задача: Параллельный подсчёт простых чисел</H2>
     * <br>
     * <H2>📌 Задание</H2>
     * <p>Вам необходимо разработать программу, которая будет вычислять количество простых чисел в заданном диапазоне чисел от 1 до N. Задача заключается в том, чтобы использовать несколько потоков для параллельного поиска простых чисел, каждый поток будет обрабатывать часть диапазона чисел. По окончании работы всех потоков, программа должна вывести общее количество простых чисел.</p>
     * <br>
     * <H2>Условия</H2>
     * <ol>
     *    <li>Число N задаётся пользователем (например, N = 10000).</li>
     *    <li>Каждый поток будет работать с определённым диапазоном чисел.</li>
     *    <li>Потоки должны работать параллельно, вычисляя количество простых чисел в своих диапазонах.</li>
     *    <li>После того как все потоки завершат свою работу, программа должна вывести общее количество простых чисел.</li>
     * </ol>
     */

    static class PrimeCounterThread extends Thread {
        private int start, end;
        private int count = 0;

        public PrimeCounterThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                if (isPrime(i)) {
                    count++;
                }
            }
        }

        private boolean isPrime(int num) {
            if (num <= 1) return false;
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    return false;
                }
            }
            return true;
        }

        public int getCount() {
            return count;
        }
    }


    public static void main(String[] args) {
        int N = 10000;
        int numThreads = 4;

        PrimeCounterThread[] threads = new PrimeCounterThread[numThreads];
        int rangeSize = N / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * rangeSize + 1;
            int end = (i == numThreads - 1) ? N : (i + 1) * rangeSize;
            threads[i] = new PrimeCounterThread(start, end);
            threads[i].start();
        }

        int totalPrimes = 0;
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                totalPrimes += threads[i].getCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Общее количество простых чисел до " + N + ": " + totalPrimes);
    }

}
