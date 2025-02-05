package ru.mephi.week2.lesson1.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {

    private double balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public boolean transfer(BankAccount target, double amount) {
        try {
            if (lock.tryLock(1, TimeUnit.SECONDS)) { // Пробуем захватить блокировку
                try {
                    if (target.lock.tryLock(1, TimeUnit.SECONDS)) { // Пробуем заблокировать целевой аккаунт
                        // Thread.sleep(1100);
                        try {
                            if (balance >= amount) {
                                balance -= amount;
                                target.balance += amount;
                                System.out.println(Thread.currentThread().getName() +
                                        " перевёл " + amount + " с " + this + " на " + target);
                                return true;
                            } else {
                                System.out.println(Thread.currentThread().getName() + " недостаточно средств");
                                return false;
                            }
                        } finally {
                            target.lock.unlock();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() +
                                " не смог получить блокировку на целевой аккаунт, откат операции");
                        return false;
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " не смог получить блокировку, отмена операции");
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " был прерван во время ожидания блокировки");
            return false;
        }
    }

    @Override
    public String toString() {
        return "BankAccount@" + Integer.toHexString(hashCode()) + " (Баланс: " + balance + ")";
    }
}

public class ReentrantLockExample {

    public static void main(String[] args) {
        BankAccount account1 = new BankAccount(1000);
        BankAccount account2 = new BankAccount(2000);

        Thread t1 = new Thread(() -> account1.transfer(account2, 500), "Поток-1");
        Thread t2 = new Thread(() -> account2.transfer(account1, 300), "Поток-2");

        t1.start();
        t2.start();
    }

}