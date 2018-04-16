package lou.ignite;

import lou.ignite.util.Base;

public class IgMain extends Base {

    /**
     * Launch the main ignite node, which should be kept running until JVM
     * shutdowns.
     */
    public static void main(String[] args) {
        Ig ig = new Ig();
        addShutdownHook(() -> ig.close());
    }

}
