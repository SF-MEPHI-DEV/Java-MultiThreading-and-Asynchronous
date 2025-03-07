package ru.mephi.week4.lesson1.cyclicBarrier;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CycleExample {

        public static void main(String[] args) {
            CyclicBarrier barrier = new CyclicBarrier(3, () -> {
                System.out.println("Executes on: " + Thread.currentThread().getName());
                System.out.println("Все потоки собрались");
            });

            Runnable runnable = () -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " подошел к барьеру");
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " вышел из барьера");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            ExecutorService executorService = Executors.newCachedThreadPool();
            List<Future<?>> futureList = new ArrayList<>();

            for (int cycleIdx = 0; cycleIdx < 3; cycleIdx++) {
                for (int i = 0; i < 3; i++) {
                    futureList.add(executorService.submit(runnable));
                }


                while (!futureList.isEmpty()) {
                    /*List<Future<?>> removingItems = futureList.stream()
                            .filter(Future::isDone)
                            .toList();
                    futureList.removeAll(removingItems);*/

                    /*Iterator<Future<?>> listIterator = futureList.iterator();
                    while(listIterator.hasNext()) {
                        Future<?> item = listIterator.next();
                        if (item.isDone()) {
                            listIterator.remove();;
                        }
                    }*/

                    futureList.removeIf(Future::isDone);
                }

                barrier.reset();
            }
        }

    }



