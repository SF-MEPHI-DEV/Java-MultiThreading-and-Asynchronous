package ru.mephi.week3.lesson2.completableFuture;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AsyncOrderSystem {


    static class OrderProcessor {

        private static final Logger logger = Logger.getLogger(OrderProcessor.class.getName());

        static {
            try {
                FileHandler fileHandler = new FileHandler("orders.log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
                logger.setUseParentHandlers(false);
            } catch (IOException e) {
                System.err.println("Ошибка настройки логирования: " + e.getMessage());
            }
        }

        public CompletableFuture<String> processOrder(String orderId) {
            return validateOrder(orderId)
                    .thenCompose(this::checkStock)
                    .thenCompose(this::processPayment)
                    .thenCompose(this::arrangeDelivery);
        }


        private CompletableFuture<String> validateOrder(String orderId) {
            return CompletableFuture.supplyAsync(() -> {
                log("Валидация заказа #" + orderId);
                sleep(1);
                if (Math.random() < 0.1) throw new RuntimeException("Некорректные данные заказа!");
                return orderId;
            });
        }

        private CompletableFuture<String> checkStock(String orderId) {
            return CompletableFuture.supplyAsync(() -> {
                log("Проверка наличия товара #" + orderId);
                sleep(2);
                if (Math.random() < 0.2) throw new RuntimeException("Товара нет в наличии!");
                return orderId;
            });
        }

        private CompletableFuture<String> processPayment(String orderId) {
            return CompletableFuture.supplyAsync(() -> {
                log("Оплата заказа #" + orderId);
                sleep(3);
                if (Math.random() < 0.15) throw new RuntimeException("Ошибка при оплате!");
                return orderId;
            });
        }


        private CompletableFuture<String> arrangeDelivery(String orderId) {
            return CompletableFuture.supplyAsync(() -> {
                log("Доставка заказа #" + orderId);
                sleep(2);
                if (Math.random() < 0.1) throw new RuntimeException("Ошибка доставки!");
                return orderId;
            });
        }


        private void log(String message) {
            logger.info(message);
        }

        private void sleep(int seconds) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                throw new RuntimeException("Поток был прерван!");
            }
        }
    }

    private static final Logger logger = Logger.getLogger(AsyncOrderSystem.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderProcessor processor = new OrderProcessor();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        logger.info("Запуск системы заказов...");
        System.out.println("Введите номер заказа (или 'exit' для выхода):");

        while (true) {
            System.out.print("Введите номер заказа: ");
            String orderId = scanner.nextLine();

            if ("exit".equalsIgnoreCase(orderId)) {
                System.out.println("Завершение работы");
                break;
            }

            CompletableFuture.runAsync(() -> {
                processor.processOrder(orderId)
                        .thenAccept(finalOrder -> logger.info("Заказ #" + finalOrder + " успешно обработан!"))
                        .exceptionally(ex -> {
                            logger.severe("Ошибка обработки заказа: " + ex.getMessage());
                            return null;
                        });
            }, executor);
        }

        executor.shutdown();
        scanner.close();
    }

}
