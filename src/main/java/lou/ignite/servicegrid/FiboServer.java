package lou.ignite.servicegrid;

import java.math.BigInteger;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteServices;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * Starts an ignite instance that deploys a singleton FiboService for the
 * cluster.
 */
public class FiboServer extends Base {

    public static void main(String[] args) {
        Ig ig = new Ig();
        addShutdownHook(() -> ig.close()); // avoid resource warning
        
        // get the cache and put some entries in it if needed
        IgniteCache<Integer, BigInteger> cache = ig.cacheFibo();
        fibo().limit(100).forEach(e -> cache.putIfAbsent(e.getKey(), e.getValue()));   

        // deploy services only on server nodes
        FiboServiceImpl service = new FiboServiceImpl(ig.cacheFibo());
        IgniteServices services = ig.servicesForServers();
        services.deployClusterSingleton("fiboService", service);
    }
}
