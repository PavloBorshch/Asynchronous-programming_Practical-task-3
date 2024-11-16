package task2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FileSearch extends RecursiveTask<Integer> {
    private final File directory;
    private final long minSize;

    public FileSearch(File directory, long minSize) {
        this.directory = directory;
        this.minSize = minSize;
    }

    @Override
    protected Integer compute() {
        int count = 0;
        List<FileSearch> subTasks = new ArrayList<>();

        // Перегляд вмісту директорії
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Якщо це піддиректорія, створюємо нову задачу
                    FileSearch subTask = new FileSearch(file, minSize);
                    subTasks.add(subTask);
                    subTask.fork(); // Асинхронний запуск задачі
                } else if (file.isFile() && file.length() > minSize) {
                    count++; // Якщо файл підходить за розміром, збільшуємо лічильник
                }
            }
        }

        // Очікування виконання підзадач і збір результатів
        for (FileSearch task : subTasks) {
            count += task.join();
        }

        return count;
    }
}
