package task1;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Клас задачі для обчислення суми одного стовпця матриці
public class WorkDealing {
    static class ColumnSumWorker implements Callable<int[]> {
        private final int[][] matrix; // Матриця
        private final int col;        // Індекс стовпця для обчислення

        public ColumnSumWorker(int[][] matrix, int col) {
            this.matrix = matrix;
            this.col = col;
        }

        @Override
        public int[] call() {
            int sum = 0;
            // Обчислення суми елементів у заданому стовпці
            for (int[] row : matrix) {
                sum += row[col];
            }
            return new int[]{col, sum}; // Повертаємо індекс стовпця та його суму
        }
    }

    // Метод для обчислення сум стовпців матриці через ExecutorService
    public static int[] computeColumnSums(int[][] matrix) throws Exception {
        int cols = matrix[0].length; // Кількість стовпців у матриці
        ExecutorService executor = Executors.newFixedThreadPool(10); // Фіксований пул потоків
        Future<int[]>[] futures = new Future[cols]; // Массив задач для стовпців
        int[] result = new int[cols]; // Масив для збереження результатів

        // Створюємо задачі для кожного стовпця
        for (int i = 0; i < cols; i++) {
            futures[i] = executor.submit(new ColumnSumWorker(matrix, i));
        }

        // Читаємо результати задач
        for (Future<int[]> future : futures) {
            int[] partialResult = future.get(); // Отримуємо результат задачі
            result[partialResult[0]] = partialResult[1]; // Записуємо у відповідний індекс
        }

        executor.shutdown(); // Завершуємо роботу пулу потоків
        return result;
    }
}
