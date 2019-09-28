import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

public class RecursionVsLoop {
    private static final int BOUND = 93; // Exclusive upper bound for fibonacci
    //    private static final int BOUND = 21; // Exclusive upper bound for factorial
    private static final int LOOP_COUNT = 4_000_000;
    //    private static final long SEED = 9378;
    private static final long SEED = 6535635;
    private static final long SLEEP = 100;
    private static BigInteger checksum;

    public static void main(String[] args) throws InterruptedException {

//        System.out.println(Long.MAX_VALUE);
//        System.out.println(FibonacciMemo.fibRecursionMemo(BOUND));
        Random randomGen = new Random(SEED);

        long loopTotal = 0;
        long recursionTotal = 0;

        for (int i = 0; i < 5; i++) {
            System.out.println("Pass: " + (i + 1));
            long seed = randomGen.nextLong();

            long loopTime = funcRunner(RecursionVsLoop::fibLoop, seed);
//            System.out.println("Loop checksum      : " + checksum);
            BigInteger prevChecksum = checksum;
            long recursionTime = funcRunner(FibonacciMemo::fibRecursionMemo, seed);
//            System.out.println("Recursion checksum : " + checksum);

            assert checksum.equals(prevChecksum);

            System.out.println("Loop time          : " + loopTime);
            System.out.println("Recursion time     : " + recursionTime);
            loopTotal += loopTime;
            recursionTotal += recursionTime;
        }
        System.out.println("\nLoop total time      : " + loopTotal);
        System.out.println("Recursion total time : " + recursionTotal);
        System.out.println("Loop/Recursion ratio : " + (double) loopTotal / recursionTotal);
    }

    private static long funcRunner(Function<Integer, Long> func, long seed) throws InterruptedException {
        Random randomGen = new Random(seed);
        BigInteger res = BigInteger.valueOf(0);
        Thread.sleep(SLEEP);
        long startTime = System.nanoTime();
        for (int i = 0; i < LOOP_COUNT; i++) {
            res = res.add(BigInteger.valueOf(func.apply(randomGen.nextInt(BOUND))));
        }
        checksum = res;
        long endTime = System.nanoTime();

        Thread.sleep(SLEEP);
        return endTime - startTime;
    }

    private static long factLoop(int n) {
        long res = 1L;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    private static long factRecursion(int n) {
        return (n > 1) ? n * factRecursion(n - 1) : 1L;
    }


    private static long fibLoop(int n) {
        if (n < 2) {
            return n;
        }
        long previous = 1;
        long prePrevious = 0;
        for (int i = 2; i <= n; i++) {
            long current = previous + prePrevious;
            prePrevious = previous;
            previous = current;
        }
        return previous;
    }

    private static long fibRecursionNaive(int n) {
        return (n > 1) ? fibRecursionNaive(n - 1) + fibRecursionNaive(n - 2) : n;
    }

    private static class FibonacciMemo {
        private static Map<Integer, Long> fibMemo = new HashMap<>();

        private static long fibRecursionMemo(int n) {
            if (n < 2) {
                return n;
                /* or safely replaced to: */
//                return fibMemo.computeIfAbsent(n, Long::valueOf);
            }
            return fibMemo.computeIfAbsent(n, v -> fibRecursionMemo(v - 1) + fibRecursionMemo(v - 2));
        }
    }
}
