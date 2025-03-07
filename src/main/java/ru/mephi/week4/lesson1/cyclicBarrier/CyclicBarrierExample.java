package ru.mephi.week4.lesson1.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    private static final int NUM_STAGES = 3;
    private static CyclicBarrier barrier;

    public static void main(String[] args) throws InterruptedException {

        barrier = new CyclicBarrier(NUM_STAGES, () -> {
            System.out.println("Все сервисы завершили этап. Переходим к следующему шагу!");
        });

        Thread stockCheck = new Thread(new OrderStage("Проверка наличия на складе"));
        Thread priceCalculation = new Thread(new OrderStage("Вычисление стоимости"));
        Thread securityCheck = new Thread(new OrderStage("Проверка безопасности"));

        stockCheck.start();
        priceCalculation.start();
        securityCheck.start();

        stockCheck.join();
        priceCalculation.join();
        securityCheck.join();

        System.out.println("Заказ обработан и готов к доставке!");
    }

    static class OrderStage implements Runnable {
        private final String stage;

        public OrderStage(String stage) {
            this.stage = stage;
        }

        @Override
        public void run() {
            boolean success = false;
            while (!success) {
                try {
                    System.out.println(stage + " начинается");
                    Thread.sleep((long) (Math.random() * 2000));

                    if (Math.random() > 0.7) {
                        throw new RuntimeException("Ошибка на этапе: " + stage);
                    }

                    System.out.println(stage + " завершен.");

                    barrier.await();
                    success = true;
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println("Ошибка на этапе: " + stage + ". Перезапускаем этап");
                } catch (RuntimeException e) {
                    System.out.println("Произошла ошибка на этапе: " + stage + ". " + e.getMessage());
                }
            }
        }
    }

}
