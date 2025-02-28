package ru.mephi.week2.lesson2.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SynchronizedListExample {

    public static void main(String[] args) throws InterruptedException {

        ThreadGroup threadGroup = new ThreadGroup("ThreadGroup");
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        // List<Integer> list = new ArrayList<>();

        Runnable runnable = () -> {
            list.add((int) (Math.random() * 10));
        };

        for (int i = 0; i < 10; i++) {
            new Thread(threadGroup, runnable).start();
        }

        while (threadGroup.activeCount() > 0) {
            Thread.sleep(10);
        }

        System.out.println(list);
        System.out.println(list.size());

    }

}
