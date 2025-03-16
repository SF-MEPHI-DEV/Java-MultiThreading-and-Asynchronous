package ru.mephi.week5.lesson1;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;


public class Experimental {

    public static void main(String[] args) throws InterruptedException {

        /*Observer<Integer> observer = new Observer<Integer>() {

            List<Disposable> disposableList = new ArrayList<>();

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposableList.add(d);
            }

            @Override
            public void onNext(@NonNull Integer integer) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };*/

        Observable<Integer> observable = Observable.create(emitter -> {
            System.out.println("Hello");
            Thread th = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    emitter.onNext(i);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("For was ended");
            });

            th.start();

            System.out.println("end of Observer");
            emitter.onNext(12);
        });


        Disposable disposable = observable.subscribe(
                value -> {
                    System.out.println("Value on observer: " + value);
                },
                exception -> {
                    System.out.println("Error: " + exception.getMessage());
                }
        );

        Thread.sleep(700);

        disposable.dispose();

        System.out.println("dispose item");
        Thread.sleep(1000);

        Disposable disposabl1 = observable.subscribe(
                value -> {
                    System.out.println("Value on observer: " + value);
                },
                exception -> {
                    System.out.println("Error: " + exception.getMessage());
                }
        );

    }

}
