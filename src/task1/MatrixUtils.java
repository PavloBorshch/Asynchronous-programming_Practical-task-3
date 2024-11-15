package task1;


import java.util.Random;

public class MatrixUtils {
    // Метод для генерації матриці з випадковими числами у заданому діапазоні
    public static int[][] generateMatrix(int rows, int cols, int min, int max) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(max - min + 1) + min; // Генерація випадкового числа
            }
        }
        return matrix;
    }

    // Метод для виведення матриці на екран
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println(); // Перехід на новий рядок після кожного рядка матриці
        }
    }
}