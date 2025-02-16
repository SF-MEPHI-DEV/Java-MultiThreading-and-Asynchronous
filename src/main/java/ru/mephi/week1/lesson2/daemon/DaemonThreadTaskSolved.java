package ru.mephi.week1.lesson2.daemon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DaemonThreadTaskSolved {

    /**
     * <H2>Задача: Фоновое автосохранение текста</H2>
     * <br>
     * <H2>📌 Задание</H2>
     * <p>Вам необходимо разработать текстовый редактор в консоли, который позволяет пользователю вводить текст,
     * а в фоновом режиме daemon-поток будет автоматически сохранять введённые данные в файл `autosave.txt` каждые 3 секунды.</p>
     * <p>Программа должна завершаться, когда пользователь вводит команду `exit`. При этом daemon-поток автоматически остановится.</p>
     * <br>
     * <H2>Условия</H2>
     * <ol>
     *    <li>Пользователь вводит текст в консоли, строки сохраняются в памяти.</li>
     *    <li>Фоновый поток-демон автоматически сохраняет текст в `autosave.txt` раз в 3 секунды.</li>
     *    <li>При вводе `exit` программа завершается, а daemon-поток прерывается.</li>
     *    <li>При запуске программы предыдущий текст не загружается (начинается с чистого листа).</li>
     * </ol>
     * <br>
     * <H2>Пример работы:</H2>
     * <pre>
     * Введите текст (для выхода введите 'exit'):
     * Привет, мир!
     * Как дела?
     * Автосохранение выполнено! (autosave.txt)
     * Всё хорошо!
     * exit
     * Завершение работы редактора...
     * Ваш текст был сохранён в 'autosave.txt'. До встречи!
     * </pre>
     * <br>
     * <H2>Ожидаемый результат</H2>
     * <p>После выхода из программы в файле `autosave.txt` должен быть сохранён весь введённый текст.</p>
     */

    static class AutoSaveDaemon extends Thread {
        private final List<String> textBuffer;

        public AutoSaveDaemon(List<String> textBuffer) {
            this.textBuffer = textBuffer;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                    saveToFile();
                } catch (InterruptedException e) {
                    System.out.println("Автосохранение прервано.");
                    return;
                }
            }
        }

        private void saveToFile() {
            try (FileWriter writer = new FileWriter("autosave.txt")) {
                for (String line : textBuffer) {
                    writer.write(line + "\n");
                }
                System.out.println("Автосохранение выполнено! (autosave.txt)");
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении файла: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        List<String> textBuffer = new ArrayList<>();
        AutoSaveDaemon autoSaveThread = new AutoSaveDaemon(textBuffer);
        autoSaveThread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст (для выхода введите 'exit'):");

        while (true) {
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Завершение работы редактора...");
                break;
            }
            textBuffer.add(input);
        }

        System.out.println("Ваш текст был сохранён в 'autosave.txt'. До встречи!");
    }


}
