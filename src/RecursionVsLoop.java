import java.math.BigInteger;
import java.util.Random;
import java.util.function.Function;

public class RecursionVsLoop {
    private static final int BOUND = 20;
    private static final int LOOP_COUNT = 5000000;
    private static final long SEED = 6535635;
    private static final long SLEEP = 2000;
    private static BigInteger checksum;

    public static void main(String[] args) throws InterruptedException {

        Random randomGen = new Random(SEED);

        long loopTotal = 0;
        long recursionTotal = 0;

        for (int i = 0; i < 5; i++) {
            System.out.println("Pass: " + (i + 1));
            long seed = randomGen.nextLong();

            long loopTime = funcRunner(RecursionVsLoop::factLoop, seed);
//            System.out.println("Loop checksum      : " + checksum);
            BigInteger prevChecksum = checksum;
            long recursionTime = funcRunner(RecursionVsLoop::factRecursion, seed);
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
        for (int i = 0; i < RecursionVsLoop.LOOP_COUNT; i++) {
            res = res.add(BigInteger.valueOf(func.apply(randomGen.nextInt(RecursionVsLoop.BOUND))));
        }
        checksum = res;

        Thread.sleep(SLEEP);
        return System.nanoTime() - startTime;
    }

    private static long factLoop(int n) {
        if (n < 3) {
            return n;
        }
        long res = 1;
        for (int i = 2; i <= n; ) {
            res *= i++;
        }
        return res;
    }

    private static long factRecursion(int n) {
        return (n < 3) ? n : n * factRecursion(n - 1);
    }
}