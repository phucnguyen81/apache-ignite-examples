package lou.ignite.datagrid;

import org.apache.ignite.IgniteCache;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * A partitioned cache is distributed among cluster nodes.
 */
public class CollocationRead extends Base {

    public static void main(String[] args) {
        try (Ig ig = new Ig()) {
            IgniteCache<Integer, String> cache = ig.cacheNumbers();

            /* Store keys in cache (values will end up on different cache nodes
             * if cache is partitioned) */
            for (int i = 0; i < 10; i++) {
                cache.put(i, Integer.toString(i));
            }

            for (int i = 0; i < 10; i++) {
                final Integer key = i;
                // read the entries on the node where the keys are
                ig.affinityRun(cache, i, () -> {
                    String val = cache.localPeek(key);
                    println("Got [key=" + key + ", val=" + val + ']');
                });
            }
        }
    }
}
