package lou.ignite.servicegrid;

import java.math.BigInteger;
import java.util.Objects;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

public class FiboServiceImpl implements Service, FiboService {

    // stable, should not have to change
    private static final long serialVersionUID = 1L;
    
    private final IgniteCache<Integer, BigInteger> fiboCache;

    public FiboServiceImpl(IgniteCache<Integer, BigInteger> fiboCache) {
        Objects.requireNonNull(fiboCache, "Null cache");
        this.fiboCache = fiboCache;
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        System.out.println("Init service... " + ctx);
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        System.out.println("Execute service... " + ctx);
    }

    @Override
    public void cancel(ServiceContext ctx) {
        System.out.println("Cancel service... " + ctx);
    }

    @Override
    public BigInteger fibo(int i) {
        return fiboCache.get(i);
    }

}
