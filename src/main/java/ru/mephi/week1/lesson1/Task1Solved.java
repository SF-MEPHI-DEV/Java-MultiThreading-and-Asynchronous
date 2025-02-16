package ru.mephi.week1.lesson1;

import java.util.*;

public class Task1Solved {

    /**
     * <H2>Задача: Гонка автомобилей</H2>
     * <br>
     * <H2>📌 Задание</H2>
     * <p>Вам необходимо разработать программу, которая симулирует гонку между несколькими автомобилями, каждый из которых будет представлен отдельным потоком. Каждый поток (автомобиль) будет двигаться с разной скоростью, и программа должна определить победителя гонки. Гонки должны начинаться одновременно, и каждый поток должен двигаться с разной скоростью. Программа должна объявить победителя, который первым преодолел дистанцию.</p>
     * <br>
     * <H2>Условия</H2>
     * <ol>
     *    <li>Количество автомобилей (потоков) задаётся в программе (например, 3 автомобиля).</li>
     *    <li>Каждый поток представляет собой автомобиль, который двигается с разной скоростью (используйте задержку с помощью метода `Thread.sleep()`).</li>
     *    <li>Дистанция гонки составляет 100 метров.</li>
     *    <li>После того как все потоки завершат свою работу, программа должна объявить победителя — тот, кто первым преодолел дистанцию.</li>
     *    <li>Используйте метод `join()` для того, чтобы главный поток дождался завершения всех потоков.</li>
     *    <li>Победитель должен быть объявлен главным потоком, который будет анализировать время завершения каждого потока.</li>
     * </ol>
     */

    static class Car extends Thread {
        private String name;
        private int distance = 100;
        private long[] finishTimes;
        private int index;

        public Car(String name, long[] finishTimes, int index) {
            this.name = name;
            this.finishTimes = finishTimes;
            this.index = index;
        }

        @Override
        public void run() {
            Random rand = new Random();
            int progress = 0;

            while (progress < distance) {
                try {
                    Thread.sleep(rand.nextInt(200) + 100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                progress += rand.nextInt(10) + 1;
                System.out.println(name + " проехал " + progress + " м.");
            }

            finishTimes[index] = System.currentTimeMillis();
            System.out.println("\n " + name + " ФИНИШИРОВАЛ!");
        }
    }


    public static void main(String[] args) {

        String[] carNames = {"Ferrari", "Lamborghini", "Porsche"};
        int numCars = carNames.length;
        long[] finishTimes = new long[numCars];
        Car[] cars = new Car[numCars];

        for (int i = 0; i < numCars; i++) {
            cars[i] = new Car(carNames[i], finishTimes, i);
            cars[i].start();
        }

        for (Car car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int winnerIndex = 0;
        for (int i = 1; i < numCars; i++) {
            if (finishTimes[i] < finishTimes[winnerIndex]) {
                winnerIndex = i;
            }
        }

        System.out.println("\n Победитель гонки: " + carNames[winnerIndex] + "! 🏁");
        System.out.println("Гонка завершена!");

    }

}
