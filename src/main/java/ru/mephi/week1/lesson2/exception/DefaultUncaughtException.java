package ru.mephi.week1.lesson2.exception;

public class DefaultUncaughtException {

    public static class MyThread extends Thread {

        MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            throw new RuntimeException("Исключение в потоке " + getName());
        }
    }

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Глобальный обработчик исключений:");
            System.out.println("Поток: " + t.getName());
            System.out.println("Сообщение исключения: " + e.getMessage());
        });


        Thread thread1 = new MyThread("My thread 1");
        Thread thread2 = new MyThread("My thread 2");
        thread1.start();
        thread2.start();
    }

}
