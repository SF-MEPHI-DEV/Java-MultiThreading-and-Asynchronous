package ru.mephi.week3.lesson2.task1;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class AsyncLoggingSystemSolved {

    /**
     * <H2>Задача: Асинхронная система логирования</H2>
     * <br>
     * <H2>📌 Задание</H2>
     * <p>Вам необходимо разработать систему логирования, в которой логи записываются асинхронно,
     * чтобы не блокировать основной поток выполнения программы.</p>
     * <p>Каждое сообщение лога обрабатывается в отдельном потоке с использованием <b>Callable</b> и <b>Future</b>.</p>
     * <br>
     * <H2>Условия</H2>
     * <ol>
     *    <li>Есть несколько потоков, генерирующих сообщения лога (например, события системы).</li>
     *    <li>Каждое сообщение должно асинхронно записываться в файл `logs.txt`.</li>
     *    <li>Использовать {@code Callable<String>} для обработки и `Future<String>` для получения результата.</li>
     *    <li>Основной поток не должен ждать завершения записи каждого лога.</li>
     *    <li>Использовать `ExecutorService` для управления потоками.</li>
     * </ol>
     * <br>
     * <H2>Пример работы:</H2>
     * <pre>
     * Генерация событий...
     * [INFO] 2025-02-10 12:00:01 - Пользователь вошел в систему.
     * [WARNING] 2025-02-10 12:00:03 - Низкий уровень памяти.
     * [ERROR] 2025-02-10 12:00:05 - Ошибка подключения к серверу.
     * Логирование завершено.
     * </pre>
     * <br>
     * <H2>Ожидаемый результат</H2>
     * <p>После выполнения программы в файле `logs.txt` должны находиться все логи, записанные в асинхронном режиме.</p>
     */

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        LogConsoleDuplicator<String> logConsoleDuplicator = new LogConsoleDuplicator<>();

        System.out.println("Генерация событий");

        Future<String> future1 = executorService.submit(new LogTask("[INFO] Пользователь вошел в систему."));
        Future<String> future2 = executorService.submit(new LogTask("[WARNING] Низкий уровень памяти."));
        Future<String> future3 = executorService.submit(new LogTask("[ERROR] Ошибка подключения к серверу."));

        logConsoleDuplicator.addLog(future1);
        logConsoleDuplicator.addLog(future2);
        logConsoleDuplicator.addLog(future3);

        System.out.println("Основной поток продолжает работу...");

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Логирование завершено.");
        logConsoleDuplicator.shutdown();
    }
}

class LogTask implements Callable<String> {
    private final String message;

    public LogTask(String message) {
        this.message = message;
    }

    @Override
    public String call() throws Exception {

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = message + " - " + timestamp;

        try (FileWriter writer = new FileWriter("logs.txt", true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logEntry;
    }
}

class LogConsoleDuplicator<T> extends Thread {

    private final ConcurrentLinkedQueue<Future<String>> logs;
    private boolean shouldShutdown;

    public LogConsoleDuplicator() {
        logs = new ConcurrentLinkedQueue<>();
        // setDaemon(true);
        shouldShutdown = false;
        start();
    }

    public void addLog(Future<String> log) {
        logs.add(log);
    }

    public void shutdown() {
        shouldShutdown = true;
    }


    @Override
    public void run() {
        while (!shouldShutdown) {
            while (!logs.isEmpty()) {

                var removingItems = logs.stream().filter(Future::isDone).toList();
                removingItems.forEach(it -> {
                    try {
                        System.out.println("From duplication module" + it.get());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
                logs.removeAll(removingItems);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    shouldShutdown = true;
                    throw new RuntimeException(e);
                }

            }
        }
        System.out.println("out of while");
    }
}