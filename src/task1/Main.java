import java.util.Random;
import java.util.concurrent.*;

public class Main {

    // Метод для генерації матриці
    public static int[][] generateMatrix(int rows, int cols, int min, int max) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(max - min + 1) + min;
            }
        }
        return matrix;
    }

    // Метод для виводу матриці на екран
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    // Work Stealing: Fork/Join Framework
    static class ColumnSumTask extends RecursiveTask<int[]> {
        private final int[][] matrix;
        private final int start;
        private final int end;

        public ColumnSumTask(int[][] matrix, int start, int end) {
            this.matrix = matrix;
            this.start = start;
            this.end = end;
        }

        @Override
        protected int[] compute() {
            if (end - start <= 1) { // Базовий випадок
                int[] columnSums = new int[matrix[0].length];
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        columnSums[j] += matrix[i][j];
                    }
                }
                return columnSums;
            } else { // Рекурсивний розподіл завдання
                int mid = (start + end) / 2;
                ColumnSumTask leftTask = new ColumnSumTask(matrix, start, mid);
                ColumnSumTask rightTask = new ColumnSumTask(matrix, mid, end);
                leftTask.fork();
                int[] rightResult = rightTask.compute();
                int[] leftResult = leftTask.join();

                // Об'єднання результатів
                for (int i = 0; i < rightResult.length; i++) {
                    rightResult[i] += leftResult[i];
                }
                return rightResult;
            }
        }
    }

    // Work Dealing: ExecutorService
    static class ColumnSumWorker implements Callable<int[]> {
        private final int[][] matrix;
        private final int col;

        public ColumnSumWorker(int[][] matrix, int col) {
            this.matrix = matrix;
            this.col = col;
        }

        @Override
        public int[] call() {
            int sum = 0;
            for (int[] row : matrix) {
                sum += row[col];
            }
            return new int[]{col, sum};
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int rows = 50;  // Кількість рядків
        int cols = 20;   // Кількість стовпців
        int min = 0;      // Мінімальне значення
        int max = 100;     // Максимальне значення

        // Генерація матриці
        int[][] matrix = generateMatrix(rows, cols, min, max);

        System.out.println("Згенерована матриця:");
        printMatrix(matrix);

        // Work Stealing: Fork/Join
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.nanoTime();
        int[] workStealingResult = forkJoinPool.invoke(new ColumnSumTask(matrix, 0, rows));
        long end = System.nanoTime();
        System.out.println("Work Stealing (Fork/Join):");
        for (int i = 0; i < workStealingResult.length; i++) {
            System.out.println("Column " + i + ": " + workStealingResult[i]);
        }
        System.out.println("Час виконання: " + (end - start) / 1e6 + " мс");

        // Work Dealing: ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(10);
        start = System.nanoTime();
        Future<int[]>[] futures = new Future[cols];
        for (int i = 0; i < cols; i++) {
            futures[i] = executor.submit(new ColumnSumWorker(matrix, i));
        }
        int[] workDealingResult = new int[cols];
        for (Future<int[]> future : futures) {
            int[] result = future.get();
            workDealingResult[result[0]] = result[1];
        }
        executor.shutdown();
        end = System.nanoTime();
        System.out.println("Work Dealing (ExecutorService):");
        for (int i = 0; i < workDealingResult.length; i++) {
            System.out.println("Column " + i + ": " + workDealingResult[i]);
        }
        System.out.println("Час виконання: " + (end - start) / 1e6 + " мс");
    }
}
