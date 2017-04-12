package org.sample.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by songyigui on 2016/11/22.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.SampleTime)
@Warmup(iterations = 5)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(24)
public class SecureRandomTest {

    private SecureRandom random;

    @Setup(Level.Trial)
    public void setup() {
        random = new SecureRandom();
    }

    @Benchmark
    public long randomWithNative() {
        return random.nextLong();
    }

    @Benchmark
    @Fork(jvmArgsAppend = "-Djava.security.egd=file:/dev/./urandom")
    public long randomWithSHA1() {
        return random.nextLong();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SecureRandomTest.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
