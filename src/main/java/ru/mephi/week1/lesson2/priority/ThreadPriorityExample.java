package ru.mephi.week1.lesson2.priority;

public class ThreadPriorityExample {

    public static class MyThread extends Thread {

        long counter;

        public MyThread(String name, int priority) {
            super(name);
            setPriority(priority);
        }

        @Override
        public void run() {
            while (true) {
                counter += 1;
            }
        }

        @Override
        public void interrupt() {
            System.out.println("Thread " + getName() + " counted to: " + counter);
            super.interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        MyThread minPriorityThread = new MyThread("Min priority thread", Thread.MIN_PRIORITY);
        MyThread maxPriorityThread = new MyThread("Max priority thread", Thread.MAX_PRIORITY);

        minPriorityThread.start();
        maxPriorityThread.start();

        Thread.sleep(5000);

        minPriorityThread.interrupt();
        maxPriorityThread.interrupt();

    }
}