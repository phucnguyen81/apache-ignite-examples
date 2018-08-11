package lou.ignite;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

import lou.ignite.util.Base;

/**
 * Facade over ignite.
 * 
 * @author phuc
 */
public class Ig extends Base implements AutoCloseable {

    /**
     * Launch the main ignite node, which should be kept running until JVM
     * shutdowns.
     */
    public static void main(String[] args) {
        Ig ig = new Ig();
        addShutdownHook(() -> ig.close());
    }

    private static final String NUMBERS_CACHE_NAME = "Numbers";

    private static final String FIBO_CACHE_NAME = "Fibo";

    private final Ignite ignite;

    /**
     * Starts the default ignite instance in client mode.
     */
    public static Ig client() {
        return new Ig("/lou-client.xml");
    }

    /**
     * Starts default ignite instance.
     */
    public Ig() {
        this("/lou-ignite.xml");
    }

    /**
     * The path to the Spring configuration file can be absolute or relative to
     * either IGNITE_HOME (Ignite installation folder) or META-INF folder in
     * classpath.
     */
    public Ig(String springCfgPath) {
        this(Ignition.start(springCfgPath));
    }

    /** Wraps an ignite instance */
    public Ig(Ignite ignite) {
        checkEnvironment();
        this.ignite = ignite;
    }

    /* IGNITE_HOME environment variable must be set to the ignite installation
     * folder */
    private static void checkEnvironment() {
        String igniteHome = System.getenv("IGNITE_HOME");
        if (igniteHome == null)
            throw new IllegalStateException(
                "IGNITE_HOME environment variable is not set");
        if (Files.notExists(Paths.get(igniteHome)))
            throw new IllegalStateException(
                "IGNITE_HOME not exist at " + igniteHome);
    }

    public boolean isPersistenceEnabled() {
        return ignite.configuration().getDataStorageConfiguration()
            .getDefaultDataRegionConfiguration().isPersistenceEnabled();
    }

    /** Whether there are server nodes in the cluster */
    public boolean hasServerNodes() {
        return !ignite.cluster().forServers().nodes().isEmpty();
    }

    /** Returns services for the cluster group of server nodes only */
    public IgniteServices servicesForServers() {
        return ignite.services(ignite.cluster().forServers());
    }

    /**
     * Gets a remote handle on the service. If service is available locally,
     * then local instance is returned, otherwise, a remote proxy is dynamically
     * created and provided for the specified service. The proxy is sticky,
     * i.e. it always contact the same remote service.
     */
    public <T> T serviceProxy(
        String serviceName,
        Class<? super T> serviceInterface)
    {
        boolean stickyFlag = true;
        return ignite.services().serviceProxy(
            serviceName, serviceInterface, stickyFlag);
    }

    /**
     * Closes this instance of grid. This method is identical to calling
     * G.stop(igniteInstanceName, true).
     */
    @Override
    public void close() {
        log("Shutting down ignite instance " + ignite);
        ignite.close();
    }

    /**
     * A cache that maps numbers to strings.
     */
    public IgniteCache<Integer, String> cacheNumbers() {
        IgniteCache<Integer, String> cache = ignite.cache(NUMBERS_CACHE_NAME);
        return cache;
    }

    /**
     * A cache containing fibonacci sequence.
     */
    public IgniteCache<Integer, BigInteger> cacheFibo() {
        IgniteCache<Integer, BigInteger> cache = ignite.cache(FIBO_CACHE_NAME);
        return cache;
    }

    /**
     * Executes a job on the remote node where data with the given key is
     * located. This assumes we can get a cache instance on the server side.
     */
    public <K, V> void affinityRun(
        IgniteCache<K, V> cache,
        K key,
        IgniteRunnable job)
    {
        ignite.compute().affinityRun(cache.getName(), key, job);
    }

    /**
     * Executes collection of jobs on nodes within the underlying cluster group.
     * Collection of all returned job results is returned from the result
     * future.
     */
    public <R> Collection<R> computeCall(
        Collection<? extends IgniteCallable<R>> jobs)
    {
        return ignite.compute().call(jobs);
    }

    /**
     * Executes collection of jobs on grid nodes within the underlying cluster
     * group.
     */
    public void computeRun(Collection<? extends IgniteRunnable> jobs) {
        ignite.compute().run(jobs);
    }

    /**
     * Gets a new instance of data streamer associated with given cache name.
     * Data streamer is responsible for loading external data into in-memory
     * data grid. For more information refer to {@link IgniteDataStreamer}
     * documentation.
     */
    public <K, V> IgniteDataStreamer<K, V> dataStreamer(
        IgniteCache<K, V> cache)
    {
        return ignite.dataStreamer(cache.getName());
    }

}
