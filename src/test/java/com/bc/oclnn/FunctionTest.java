package com.bc.oclnn;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class FunctionTest {
    public static final double eps = 0.0000000001;
    public static final double below = 0.0 + eps;
    public static final double above = 1.0 - eps;
    public static final double ambiguous = 0.5;
    public static final int MAX_CANDIDATES = 250;

    @Test
    public void testIt() throws Exception {
        PatternList pl = createTrainingSet(MAX_CANDIDATES, 4);
        Network network = new Network(2, 7, 1);
        int maxCycles = -1;
        final Training.BinaryTrainer trainer = new Training.BinaryTrainer();

        Training.trainNetwork(network, pl, pl.size(), maxCycles, 0.2, 1000, 0.25, 0.9, 3, trainer);

        for (double x = 0.0; x <= 1.0; x += 0.1) {
            for (double y = 0.0; y <= 1.0; y += 0.1) {
                double actual = classify(network, x, y);
                double expected = g(x, y);
                assertEquals("x=" + x + ",y=" + y, expected, actual, 0.5);
            }
        }

    }

    public static PatternList createTrainingSet(int n, long seed) throws IOException {
        Random r = new Random(seed);

        PatternList pl = new PatternList();
        for (int i = 0; i < n; i++) {

            double x = r.nextDouble();
            double y = r.nextDouble();

            double[] input = new double[2];
            input[0] = x;
            input[1] = y;

            double[] output = new double[1];
            output[0] = g(x, y);

            pl.add(input, output);
        }


        return pl;
    }

    public double classify(Network network, double x, double y) {
        double[] input = new double[2];
        input[0] = x;
        input[1] = y;

        double[] output = new double[1];
        network.run(input, output);

        if (output[0] > 0.85) {
            return above;
        } else if (output[0] < 0.15) {
            return below;
        }

        return ambiguous;
    }

    public static double g(double x, double y) {
        return (y < f(x)) ? below : above;
    }

    public static double f(double x) {
        return -5 * x + 2;
    }

    public static void main(String[] args) throws Exception {
        new FunctionTest().testIt();
    }
}
