package ru.mephi.week2.lesson1;

class SharedResource {
    private boolean available = false;

    public synchronized void produce() throws InterruptedException {
        while (available) {
            wait(); // Ждём, пока потребитель не заберёт ресурс
        }
        available = true;
        System.out.println(Thread.currentThread().getName() + " произвёл ресурс");
        notifyAll(); // Будим всех потребителей
    }

    public synchronized void consume() throws InterruptedException {
        while (!available) {
            wait(); // Ждём, пока появится ресурс
        }
        available = false;
        System.out.println(Thread.currentThread().getName() + " потребил ресурс");
        notifyAll(); // Будим всех производителей
    }
}

public class DeleteMe  {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 3; j++) resource.produce();
                } catch (InterruptedException ignored) {}
            }, "Производитель-" + i).start();

            new Thread(() -> {
                try {
                    for (int j = 0; j < 3; j++) resource.consume();
                } catch (InterruptedException ignored) {}
            }, "Потребитель-" + i).start();
        }
    }
}