package ru.mephi.week2.lesson2.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class BankAccount {
    private final AtomicInteger balance;

    public BankAccount(int initialBalance) {
        this.balance = new AtomicInteger(initialBalance);
    }

    public void deposit(int amount) {
        if (amount > 0) {
            balance.addAndGet(amount);
            System.out.println(Thread.currentThread().getName() + " пополнил счёт на " + amount + ". Новый баланс: " + balance.get());
        }
    }

    public void withdraw(int amount) {
        while (true) {
            int currentBalance = balance.get();
            if (currentBalance < amount) {
                System.out.println(Thread.currentThread().getName() + " попытался снять " + amount + ", но недостаточно средств.");
                return;
            }

            if (balance.compareAndSet(currentBalance, currentBalance - amount)) {
                System.out.println(Thread.currentThread().getName() + " снял " + amount + ". Новый баланс: " + balance.get());
                return;
            }
        }
    }

    public int getBalance() {
        return balance.get();
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(1000);

        Thread depositor = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.deposit(200);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }, "Пополняющий поток");

        Thread withdrawer = new Thread(() -> {

            for (int i = 0; i < 5; i++) {
                account.withdraw(150);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
            }
        }, "Снимающий поток");

        depositor.start();
        withdrawer.start();

        depositor.join();
        withdrawer.join();

        System.out.println("Итоговый баланс: " + account.getBalance());
    }
}
