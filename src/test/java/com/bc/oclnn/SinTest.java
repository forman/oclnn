package com.bc.oclnn;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class SinTest {

    public static final double PI2 = 2 * Math.PI;

    @Test
    public void testSin() throws Exception {
        PatternList patternList = new PatternList();
        for (int j = 0; j <= 10; j++) {
            for (int i = 0; i <= 10; i++) {
                double x = PI2 * i / 10.0;
                double y = PI2 * j / 10.0;
                double z = f(x, y);
                //System.out.println("f("+ x + ","+ y + ") = " + z);
                patternList.add(
                        new double[]{
                                Network.normalize(x, 0, PI2),
                                Network.normalize(y, 0, PI2)
                        },
                        new double[]{
                                Network.normalize(z, -1, +1)
                        });
            }
        }
        patternList.mix();

        Network network = new Network(new int[]{2, 10, 10, 1}, 5.0);
        final double epsilon = 0.005;
        Training.trainNetwork(network, patternList, -1, -1, epsilon, 1000, 0.1, 0.1, 997,
                              new Training.ErrorTrainer());

        final double[] output = new double[1];

        network.run(new double[]{
                Network.normalize(PI2 * 0.1, 0, PI2),
                Network.normalize(PI2 * 0.3, 0, PI2)
        }, output);
        assertEquals(f(PI2 * 0.1, PI2 * 0.3), Network.denormalize(output[0], -1, +1), 0.1);

        network.run(new double[]{
                Network.normalize(PI2 * 0.9, 0, PI2),
                Network.normalize(PI2 * 0.1, 0, PI2)
        }, output);
        assertEquals(f(PI2 * 0.9, PI2 * 0.1), Network.denormalize(output[0], -1, +1), 0.1);

        network.run(new double[]{
                Network.normalize(PI2 * 0.5, 0, PI2),
                Network.normalize(PI2 * 0.5, 0, PI2)
        }, output);
        assertEquals(f(PI2 * 0.5, PI2 * 0.5), Network.denormalize(output[0], -1, +1), 0.1);

    }

    private double f(double x, double y) {
        return Math.sin(x) * Math.sin(y);
    }

}
