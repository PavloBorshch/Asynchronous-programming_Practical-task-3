package task2;


import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Введення директорії
        System.out.print("Введіть шлях до директорії: ");
        String directoryPath = scanner.nextLine();

        // Введення розміру файлу
        System.out.print("Введіть мінімальний розмір файлу (в байтах): ");
        long minSize = scanner.nextLong();

        File directory = new File(directoryPath);

        // Перевірка існування директорії
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Помилка: вказаний шлях не є директорією.");
            return;
        }

        // Використання Fork/Join Framework
        long start = System.nanoTime();
        FileSearch task = new FileSearch(directory, minSize);
        int fileCount = task.compute();
        long end = System.nanoTime();

        // Вивід результатів
        System.out.println("Кількість файлів більших за " + minSize + " байтів: " + fileCount);
        System.out.println("Час виконання: " + (end - start) / 1e6 + " мс");
    }
}
