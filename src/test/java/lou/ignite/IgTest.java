package lou.ignite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import lou.ignite.util.TestBase;

public class IgTest extends TestBase {

    private static Ig ig;

    @BeforeClass
    public static void createIgnite() {
        ig = new Ig();
    }

    @AfterClass
    public static void closeIgnite() {
        ig.close();
    }

    @Test
    public void persistenceShouldBeDisabled() {
        assertFalse(ig.isPersistenceEnabled());
    }

    @Test
    public void cacheNumbersExist() {
        assertNotNull(ig.cacheNumbers());
    }

}
