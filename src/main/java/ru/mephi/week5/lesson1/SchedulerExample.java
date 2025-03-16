package ru.mephi.week5.lesson1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulerExample {

    public static void main(String[] args) {

        Observable<Integer> observable = Observable.create(emitter -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Emitting " + i + " on: " + Thread.currentThread().getName());
                emitter.onNext(i);
                Thread.sleep(500);
            }
            emitter.onComplete();
        });

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(number -> number * 2)
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("Subscribed on: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer number) {
                        System.out.println("Received: " + number + " on: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.err.println("Error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Completed on: " + Thread.currentThread().getName());
                    }
                });


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
