package evele;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 15)
@Fork(1)
public class MatrixBenchmark {

    private static final int SIZE = 1000;
    private int[][] a;
    private int[][] b;
    private ExecutorService pool;

    @Setup(Level.Trial)
    public void setUpPool() {
        pool = Executors.newFixedThreadPool(4);
        //pool = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Setup(Level.Invocation)
    public void setUp() {
        a = generateMatrix(SIZE, SIZE);
        b = generateMatrix(SIZE, SIZE);
    }

    @Benchmark
    public void singleThreadedMultiply(Blackhole bh) {
        try {
        //int[][] result = Matrix.singlethreaded.multiply(a,b);
        int[][] result = Matrix.multithreaded(a, b, pool);
        bh.consume(result);
        }catch(Exception e ) {}
        
    }


    @TearDown(Level.Trial)
    public void tearDownPool() {
        pool.shutdown();
    }

    private int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(-100, 101);
            }
        }
        return matrix;
    }


}