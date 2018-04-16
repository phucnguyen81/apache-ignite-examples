package lou.ignite.util;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.Assert;

public class Base extends Assert {

    private final Logger log = Logger.getLogger(getClass().getSimpleName());

    public static void addShutdownHook(Runnable hook) {
        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }

    public static void println(Object... x) {
        if (x.length == 0) System.out.println();
        else Arrays.stream(x).forEach(System.out::println);
    }

    public void log() {
        log.info(System.lineSeparator());
    }

    public void log(Object msg) {
        log.info(String.valueOf(msg));
    }

    public void logError(Object msg) {
        log.log(Level.SEVERE, String.valueOf(msg));
    }

    public void logError(Object msg, Throwable error) {
        log.log(Level.SEVERE, String.valueOf(msg), error);
    }

    public static Stream<Entry<Integer, BigInteger>> fibo() {
        AtomicInteger count = new AtomicInteger(0);
        return fibo(BigInteger.ONE, BigInteger.ONE).map(
            n -> new SimpleEntry<Integer, BigInteger>(
                count.getAndIncrement(), n));
    }

    private static Stream<BigInteger> fibo(BigInteger f1, BigInteger f2) {
        return Stream.iterate(new BigInteger[] { f1, f2 },
            pair -> new BigInteger[] { pair[1], pair[0].add(pair[1]) })
            .map(pair -> pair[0]);
    }

}
