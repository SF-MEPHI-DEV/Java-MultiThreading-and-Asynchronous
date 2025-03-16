package ru.mephi.week5.lesson1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.List;

public class ParserExample {

    public static void main(String[] args) {

        String apiUrl = "https://jsonplaceholder.typicode.com/users";

        Observable.fromCallable(() -> fetchDataFromServer(apiUrl))
                .subscribeOn(Schedulers.io())
                .flatMap(response -> Observable.fromIterable(parseUserNames(response)))
                .observeOn(Schedulers.newThread())
                .subscribe(
                        name -> System.out.println("User name: " + name),
                        throwable -> System.err.println("Error: " + throwable.getMessage()),
                        () -> System.out.println("Completed!")
                );

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String fetchDataFromServer(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response.code());
            }
            return response.body().string();
        }
    }

    private static List<String> parseUserNames(String json) {
        JsonArray users = JsonParser.parseString(json).getAsJsonArray();
        System.out.println("len of users array: " + users.size());
        return Observable.fromIterable(users)
                .map(user -> user.getAsJsonObject().get("name").getAsString())
                .toList()
                .blockingGet();
    }
}