package ru.mephi.week4.lesson2.forkjoin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class RecursiveTaskExample {

    public static class ParallelWordCounter extends RecursiveTask<Map<String, Integer>> {

        private static final int THRESHOLD = 4;
        private final String[] words;
        private final int start;
        private final int end;

        public ParallelWordCounter(String[] words, int start, int end) {
            this.words = words;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Integer> compute() {
            if (end - start <= THRESHOLD) {
                return countWordsSequentially();
            } else {
                int mid = (start + end) / 2;

                ParallelWordCounter leftTask = new ParallelWordCounter(words, start, mid);
                ParallelWordCounter rightTask = new ParallelWordCounter(words, mid, end);

                leftTask.fork();

                Map<String, Integer> rightResult = rightTask.compute();
                Map<String, Integer> leftResult = leftTask.join();

                return mergeResults(leftResult, rightResult);
            }
        }

        private Map<String, Integer> countWordsSequentially() {
            Map<String, Integer> wordCount = new HashMap<>();
            for (int i = start; i < end; i++) {
                wordCount.put(
                        words[i],
                        wordCount.getOrDefault(words[i], 0) + 1
                );
            }
            return wordCount;
        }

        private Map<String, Integer> mergeResults(Map<String, Integer> left, Map<String, Integer> right) {
            for (Map.Entry<String, Integer> entry : right.entrySet()) {
                left.put(
                        entry.getKey(),
                        left.getOrDefault(entry.getKey(), 0) + entry.getValue()
                );
            }
            return left;
        }
    }

    public static void main(String[] args) {

        String text = "Parallel Java computing is powerful. Java Parallel computing is efficient. Java is powerful. ForkJoinPool helps. Java";
        String[] words = text.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");

        ForkJoinPool pool = new ForkJoinPool();
        ParallelWordCounter task = new ParallelWordCounter(words, 0, words.length);

        Map<String, Integer> wordCount = pool.invoke(task);

        System.out.println("Частота слов: " + wordCount);

    }

}
