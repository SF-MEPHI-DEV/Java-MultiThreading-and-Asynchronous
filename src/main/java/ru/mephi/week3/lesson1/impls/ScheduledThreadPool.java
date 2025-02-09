package ru.mephi.week3.lesson1.impls;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPool {

    public static void main(String[] args) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        scheduler.schedule(() -> {
            System.out.println("Отложенная задача выполнена!");
        }, 5, TimeUnit.SECONDS);

        scheduler.shutdown();
    }

}
