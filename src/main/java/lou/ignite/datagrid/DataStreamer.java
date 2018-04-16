package lou.ignite.datagrid;

import java.math.BigInteger;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * Demonstrates how cache can be populated with data utilizing
 * {@link IgniteDataStreamer} API. {@link IgniteDataStreamer} is a lot more
 * efficient to use than standard {@code put(...)} operation as it properly
 * buffers cache requests together and properly manages load on remote nodes.
 * 
 * @author phuc
 */
public class DataStreamer extends Base {

    private static final int ENTRY_COUNT = 1000;

    public static void main(String[] args) {
        // data to load
        List<Entry<Integer, BigInteger>> fibos = fibo()
            .limit(ENTRY_COUNT).collect(Collectors.toList());

        try (Ig ignite = new Ig()) {
            println();
            println(">>> Cache data streamer example started.");

            IgniteCache<Integer, BigInteger> cache = ignite.cacheFibo();
            long start = System.currentTimeMillis();
            try (IgniteDataStreamer<Integer, BigInteger> stmr = ignite
                .dataStreamer(cache)) {
                addData(stmr, fibos);
            }
            long end = System.currentTimeMillis();

            println(">>> Loaded " + ENTRY_COUNT + " keys in "
                + (end - start) + "ms.");
        }
    }

    private static void addData(
        IgniteDataStreamer<Integer, BigInteger> streamer,
        List<Entry<Integer, BigInteger>> fibos)
    {
        // configure loader
        streamer.perNodeBufferSize(100);
        streamer.perNodeParallelOperations(4);

        fibos.stream().forEach(e -> {
            streamer.addData(e.getKey(), e.getValue());

            // show progress while loading cache
            int loaded = e.getKey();
            if (loaded % 100 == 0)
                println("Loaded " + loaded + " keys.");
        });
    }
}
