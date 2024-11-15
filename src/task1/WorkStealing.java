package task1;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

// Клас задачі для обчислення суми елементів стовпців матриці за допомогою Fork/Join
public class WorkStealing {
    static class ColumnSumTask extends RecursiveTask<int[]> {
        private final int[][] matrix; // Матриця
        private final int start;      // Початковий індекс рядків
        private final int end;        // Кінцевий індекс рядків

        public ColumnSumTask(int[][] matrix, int start, int end) {
            this.matrix = matrix;
            this.start = start;
            this.end = end;
        }

        @Override
        protected int[] compute() {
            // Якщо кількість рядків у задачі мала, обчислюємо без поділу
            if (end - start <= 1) {
                int[] columnSums = new int[matrix[0].length];
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        columnSums[j] += matrix[i][j];
                    }
                }
                return columnSums;
            } else {
                // Ділимо задачу на дві підзадачі
                int mid = (start + end) / 2;
                ColumnSumTask leftTask = new ColumnSumTask(matrix, start, mid);
                ColumnSumTask rightTask = new ColumnSumTask(matrix, mid, end);

                // Виконуємо ліву задачу асинхронно
                leftTask.fork();
                // Виконуємо праву задачу у поточному потоці
                int[] rightResult = rightTask.compute();
                // Чекаємо завершення лівої задачі
                int[] leftResult = leftTask.join();

                // Об'єднуємо результати обох підзадач
                for (int i = 0; i < rightResult.length; i++) {
                    rightResult[i] += leftResult[i];
                }
                return rightResult;
            }
        }
    }

    // Метод для запуску Fork/Join задачі
    public static int[] computeColumnSums(int[][] matrix) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ColumnSumTask(matrix, 0, matrix.length));
    }
}
