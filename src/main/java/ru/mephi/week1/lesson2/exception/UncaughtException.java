package ru.mephi.week1.lesson2.exception;

public class UncaughtException {

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            throw new RuntimeException("Исключение в user потоке");
        });

        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Обработано исключение в потоке: " + t.getName());
            System.out.println("Сообщение исключения: " + e.getMessage());
        });

        thread.start();
    }

}
