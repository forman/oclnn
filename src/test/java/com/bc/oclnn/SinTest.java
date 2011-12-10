package com.bc.oclnn;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Norman Fomferra
 */
public class SinTest {

    public static final double PI2 = 2 * Math.PI;

    @Test
    public void testSin() throws Exception {
        PatternList patternList = new PatternList();
        for (double y = 0.0; y <= 1.0; y += 0.1) {
            for (double x = 0.0; x <= 1.0; x += 0.1) {
                double z = f(PI2* x, PI2* y);
                System.out.println("z = " + z);
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

        Network network = new Network(2, 17, 17, 1);
        final double epsilon = 0.01;
        Training.trainNetwork(network, patternList, -1, -1, epsilon, 1000, 0.5, 0.5, -1, new Training.ErrorTrainer());

        final double[] output = new double[1];
        network.run(new double[]{
                Network.normalize(PI2 * 0.23, 0, PI2),
                Network.normalize(PI2 * 0.71, 0, PI2)
        }, output);
        Assert.assertEquals(f(PI2 * 0.23, PI2 * 0.71), Network.denormalize(output[0], -1, +1), epsilon);

    }

    private double f(double x, double y) {
        return Math.sin(x) * Math.sin(y);
    }

}
