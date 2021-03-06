package lou.ignite.cache;

import org.apache.ignite.IgniteCache;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * Writes some entries to the numbers cache.
 */
public class CacheLoader extends Base {
    // how many keys to load
    private static final int KEY_COUNT = 10;

    public static void main(String[] args) {
        try (Ig ignite = new Ig()) {
            IgniteCache<Integer, String> cache = ignite.cacheNumbers();
            for (int i = 0; i < KEY_COUNT; i++) {
                cache.put(i, Integer.toString(i));
                println("Load " + i + " = " + cache.get(i));
            }
        }
    }
}
