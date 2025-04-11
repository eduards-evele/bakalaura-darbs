package evele;

import java.util.concurrent.*;

public class Matrix {
    public static int[][] multithreaded(int[][] a, int[][] b, ExecutorService pool) throws InterruptedException, ExecutionException {
        int rows = a.length;
        int cols = b[0].length;
        int[][] res = new int[rows][cols];

        Future<int[]>[] futures = new Future[rows];

        for (int i = 0; i < rows; i++) {
            final int row = i;
            futures[i] = pool.submit(() -> {
                int[] result_row = new int[cols];
                for (int j = 0; j < cols; j++) {
                    for (int k = 0; k < a[0].length; k++) {
                        result_row[j] += a[row][k] * b[k][j];
                    }
                }
                return result_row;
            });
        }

        for (int i = 0; i < rows; i++) {
            res[i] = futures[i].get(); 
        }
        
        return res;
    }

    public static int[][] singlethreaded(int[][] a, int[][] b) {
        int res_rows = a.length, res_cols = b[0].length;
        int[][] res = new int[res_rows][res_cols];
        for (int i = 0; i < res_rows; i++) {
            for (int j = 0; j < res_cols; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return res;
    }
}