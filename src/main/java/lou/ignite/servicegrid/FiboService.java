package lou.ignite.servicegrid;

import java.math.BigInteger;

public interface FiboService {
    /**
     * Returns the ith number in fibonacci sequence if available, else
     * returns null.
     */
    BigInteger fibo(int i);
}
