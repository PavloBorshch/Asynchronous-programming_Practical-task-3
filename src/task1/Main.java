package task1;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Введення параметрів матриці користувачем
        System.out.print("Введіть кількість рядків матриці: ");
        int rows = scanner.nextInt(); // Кількість рядків

        System.out.print("Введіть кількість стовпців матриці: ");
        int cols = scanner.nextInt(); // Кількість стовпців

        System.out.print("Введіть мінімальне значення елементів матриці: ");
        int min = scanner.nextInt(); // Мінімальне значення елементів

        System.out.print("Введіть максимальне значення елементів матриці: ");
        int max = scanner.nextInt(); // Максимальне значення елементів

        // Перевірка коректності введених значень
        if (min > max) {
            System.out.println("Помилка: мінімальне значення не може бути більшим за максимальне.");
            return;
        }

        // Генерація матриці
        int[][] matrix = MatrixUtils.generateMatrix(rows, cols, min, max);

        // Вивід згенерованої матриці
        System.out.println("\nЗгенерована матриця:");
        MatrixUtils.printMatrix(matrix);

        // Виконання Work Stealing
        long start = System.nanoTime();
        int[] workStealingResult = WorkStealing.computeColumnSums(matrix);
        long end = System.nanoTime();
        System.out.println("\nWork Stealing (Fork/Join):");
        for (int i = 0; i < workStealingResult.length; i++) {
            System.out.println("Стовпець " + i + ": " + workStealingResult[i]);
        }
        System.out.println("\nЧас виконання: " + (end - start) / 1e6 + " мс\n");

        // Виконання Work Dealing
        start = System.nanoTime();
        int[] workDealingResult = WorkDealing.computeColumnSums(matrix);
        end = System.nanoTime();
        System.out.println("\nWork Dealing (ExecutorService):");
        for (int i = 0; i < workDealingResult.length; i++) {
            System.out.println("Стовпець " + i + ": " + workDealingResult[i]);
        }
        System.out.println("\nЧас виконання: " + (end - start) / 1e6 + " мс\n");
    }
}
