package lou.ignite.servicegrid;

import java.math.BigInteger;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * Calls a deployed service. Before starting this, start the FiboServer and a
 */
public class FiboClient extends Base {

    public static void main(String[] args) {
        try (Ig ig = Ig.client()) {
            FiboService service = ig.serviceProxy(
                "fiboService", FiboService.class);

            for (int i = 0; i < 10; i++) {
                BigInteger n = service.fibo(i);
                println("Got fibo(" + i + ") = " + n);
            }
        }
    }
}