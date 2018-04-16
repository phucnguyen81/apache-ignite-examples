package lou.ignite.cache;

import org.apache.ignite.IgniteCache;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * Read entries from the numbers cache.
 */
public class CacheReader extends Base {

    public static void main(String[] args) {
        try (Ig ig = new Ig()) {
            IgniteCache<Integer, String> cache = ig.cacheNumbers();
            cache.forEach((e) -> {
                println(String.format("%s : %s", e.getKey(), e.getValue()));
            });
        }
    }
}
