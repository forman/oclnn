package com.bc.oclnn;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class NormalisationTest {

    @Test
    public void testNormalisation() throws Exception {

        assertEquals(0.01, Network.normalize(0.0, 0.0, 1.0), 0.0);
        assertEquals(0.5, Network.normalize(0.5, 0.0, 1.0), 0.0);
        assertEquals(0.99, Network.normalize(1.0, 0.0, 1.0), 0.0);

        assertEquals(0.01, Network.normalize(-1.0, -1.0, +1.0), 0.0);
        assertEquals(0.5, Network.normalize(0.0, -1.0, +1.0), 0.0);
        assertEquals(0.99, Network.normalize(+1.0, -1.0, +1.0), 0.0);

        assertEquals(0.01, Network.normalize(0.0, 0.0, 4.0), 0.0);
        assertEquals(0.255, Network.normalize(1.0, 0.0, 4.0), 0.0);
        assertEquals(0.5, Network.normalize(2.0, 0.0, 4.0), 0.0);
        assertEquals(0.745, Network.normalize(3.0, 0.0, 4.0), 0.0);
        assertEquals(0.99, Network.normalize(4.0, 0.0, 4.0), 0.0);
    }

    @Test
    public void testDeNormalisation() throws Exception {

        assertEquals(0.0, Network.denormalize(0.01, 0.0, 1.0), 0.0);
        assertEquals(0.5, Network.denormalize(0.5, 0.0, 1.0), 0.0);
        assertEquals(1.0, Network.denormalize(0.99, 0.0, 1.0), 0.0);

        assertEquals(-1.0, Network.denormalize(0.01, -1.0, +1.0), 0.0);
        assertEquals(0.0, Network.denormalize(0.5, -1.0, +1.0), 0.0);
        assertEquals(+1.0, Network.denormalize(0.99, -1.0, +1.0), 0.0);

        assertEquals(0.0, Network.denormalize(0.01, 0.0, 4.0), 0.0);
        assertEquals(1.0, Network.denormalize(0.255, 0.0, 4.0), 0.0);
        assertEquals(2.0, Network.denormalize(0.5, 0.0, 4.0), 0.0);
        assertEquals(3.0, Network.denormalize(0.745, 0.0, 4.0), 0.0);
        assertEquals(4.0, Network.denormalize(0.99, 0.0, 4.0), 0.0);
    }
}
