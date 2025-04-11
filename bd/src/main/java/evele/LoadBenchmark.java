package com.evele;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 15)
@Fork(1)
public class LoadBenchmark {

    ExecutorService pool;
    Blackhole bh;

    @Setup(Level.Trial)
    public void setup() {
        //pool = Executors.newCachedThreadPool();
        pool = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Setup(Level.Invocation)
    public void setupInvocation(Blackhole blackhole) {
        this.bh = blackhole;
    }

    @Benchmark
    public void asyncHttpBenchmark() throws Exception {
        List<Callable<Double>> tasks = new ArrayList<Callable<Double>>();
        for(int i = 0; i < 15000; i++) {
            final int j = i;
            tasks.add(()-> {
                double x = Math.random() * 10;
                bh.consume(x);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {}
                return x;
            });
        }

        pool.invokeAll(tasks);
    }

    @TearDown(Level.Trial)
    public void tearDownPool() {
        pool.shutdown();
    }
}

