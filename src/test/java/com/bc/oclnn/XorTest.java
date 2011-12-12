package com.bc.oclnn;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class XorTest {
    public static final double ONE = 0.9999999999;
    public static final double ZERO = 0.0000000001;

    @Test
    public void testIt() throws Exception {
        PatternList patternList = new PatternList();
        patternList.add(new double[]{ZERO, ZERO}, new double[]{ZERO});
        patternList.add(new double[]{ZERO, ONE}, new double[]{ONE});
        patternList.add(new double[]{ONE, ZERO}, new double[]{ONE});
        patternList.add(new double[]{ONE, ONE}, new double[]{ZERO});

        Network network = new Network(new int[] {2, 3, 1}, 1.0);
        final Training.BinaryTrainer trainer = new Training.BinaryTrainer();

        Training.trainNetwork(network, patternList, patternList.size(), -1, 0.005, 500, 0.25, 0.5, 3, trainer);


        for (double y = 0.0; y <= 1.0; y += 0.25) {
            for (double x = 0.0; x <= 1.0; x += 0.25) {
                //System.out.println("assertEquals(" + classifier(network, x, y) + ", classifier(network, " + x + ", " + y + "));");
            }
        }

        assertEquals(false, classifier(network, 0.0, 0.0));
        assertEquals(false, classifier(network, 0.25, 0.0));
        assertEquals(false, classifier(network, 0.5, 0.0));
        assertEquals(true, classifier(network, 0.75, 0.0));
        assertEquals(true, classifier(network, 1.0, 0.0));

        assertEquals(false, classifier(network, 0.0, 0.25));
        assertEquals(false, classifier(network, 0.25, 0.25));
        assertEquals(false, classifier(network, 0.5, 0.25));
        assertEquals(true, classifier(network, 0.75, 0.25));
        assertEquals(true, classifier(network, 1.0, 0.25));

        assertEquals(false, classifier(network, 0.0, 0.5));
        assertEquals(false, classifier(network, 0.25, 0.5));
        assertEquals(false, classifier(network, 0.5, 0.5));
        assertEquals(false, classifier(network, 0.75, 0.5));
        assertEquals(false, classifier(network, 1.0, 0.5));

        assertEquals(true, classifier(network, 0.0, 0.75));
        assertEquals(true, classifier(network, 0.25, 0.75));
        assertEquals(false, classifier(network, 0.5, 0.75));
        assertEquals(false, classifier(network, 0.75, 0.75));
        assertEquals(false, classifier(network, 1.0, 0.75));

        assertEquals(true, classifier(network, 0.0, 1.0));
        assertEquals(true, classifier(network, 0.25, 1.0));
        assertEquals(false, classifier(network, 0.5, 1.0));
        assertEquals(false, classifier(network, 0.75, 1.0));
        assertEquals(false, classifier(network, 1.0, 1.0));
    }

    public boolean classifier(Network network, double x, double y) {
        double[] input = new double[2];
        input[0] = x;
        input[1] = y;

        double[] output = new double[1];
        network.run(input, output);

        if (output[0] >= 0.5) {
            return (true);
        }

        return (false);
    }


}
