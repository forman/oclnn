package com.bc.oclnn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

import static java.lang.Math.exp;
import static java.lang.System.arraycopy;

/**
 * The backpropagation network.
 *
 * @author Norman
 */
public final class Network {

    public static final double NORM_MIN = 0.01;
    public static final double NORM_MAX = 0.99;

    private final int[] layerSizes;
    private final double slope;
    private final double[][] values;
    private final double[][] errors;
    private final double[][][] weights;
    private final double[][][] deltas;

    /**
     * Constructor for a standard 3-layer backpropagation network.
     *
     * @param layerSizes The sizes of each layer, where the input layer is the first and
     *                   the output layer is the last in the array.
     *                   Any number of hidden layers may be in between. At least the number
     *                   of input and output layers must be given.
     */
    public Network(int ... layerSizes) {
          this(layerSizes, 1.0);
    }

   /**
     * Constructor for a standard 3-layer backpropagation network.
     *
     * @param slope      Slope of the sigmoid activation function, try 1.0 ... 10.0
     * @param layerSizes The sizes of each layer, where the input layer is the first and
     *                   the output layer is the last in the array.
     *                   Any number of hidden layers may be in between. At least the number
     *                   of input and output layers must be given.
     */
    public Network(int[] layerSizes, double slope) {

        this.layerSizes = layerSizes.clone();
        this.slope = slope;

        final int layerCount = layerSizes.length;

        values = new double[layerCount][];
        errors = new double[layerCount][];
        weights = new double[layerCount][][];
        deltas = new double[layerCount][][];
        for (int k = 0; k < layerCount; k++) {
            final int layerSize = layerSizes[k];
            values[k] = new double[layerSize];
            errors[k] = new double[layerSize];
            weights[k] = k > 0 ? new double[layerSize][layerSizes[k - 1]] : null;
            deltas[k] = k > 0 ? new double[layerSize][layerSizes[k - 1]] : null;
        }
    }

    public void initWeights(Random random) {
        for (int k = 1; k < layerSizes.length; k++) {
            for (int j = 0; j < layerSizes[k]; j++) {
                for (int i = 0; i < layerSizes[k - 1]; i++) {
                    weights[k][j][i] = 2.0 * random.nextDouble() - 1.0;
                }
            }
        }
    }

    public double[] createOutputArray() {
        return new double[layerSizes[layerSizes.length - 1]];
    }

    public void run(double[] input, double[] output) {

        arraycopy(input, 0, values[0], 0, values[0].length);

        for (int k = 1; k < layerSizes.length; k++) {
            for (int j = 0; j < layerSizes[k]; j++) {
                double net = 0.0;
                for (int i = 0; i < layerSizes[k - 1]; i++) {
                    net += values[k - 1][i] * weights[k][j][i];
                }
                values[k][j] = activation(net);
            }
        }

        arraycopy(values[layerSizes.length - 1], 0, output, 0, values[layerSizes.length - 1].length);
    }

    public void train(double[] truth, double learningRate, double momentum) {

        final int kOutput = layerSizes.length - 1;

        for (int j = 0; j < layerSizes[kOutput]; j++) {
            train(kOutput, j, truth[j] - values[kOutput][j], learningRate, momentum);
        }

        for (int k = kOutput - 1; k > 0; k--) {
            for (int j = 0; j < layerSizes[k]; j++) {
                double errorSum = 0.0;
                for (int i = 0; i < layerSizes[k + 1]; i++) {
                    errorSum += errors[k + 1][i] * weights[k + 1][i][j];
                }
                train(k, j, errorSum, learningRate, momentum);
            }
        }
    }

    private void train(int k, int j, double errorSum, double learningRate, double momentum) {
        final double v1 = values[k][j];
        final double v2 = v1 * (1.0 - v1) * errorSum;
        errors[k][j] = v2;
        for (int i = 0; i < layerSizes[k - 1]; i++) {
            final double newDelta = learningRate * v2 * values[k - 1][i];
            final double oldDelta = deltas[k][j][i];
            weights[k][j][i] += newDelta + momentum * oldDelta;
            deltas[k][j][i] = newDelta;
        }
    }

    public static double normalize(double value, double minValue, double maxValue) {
        double ratio = (value - minValue) / (maxValue - minValue);
        return NORM_MIN + ratio * (NORM_MAX - NORM_MIN);
    }

    public static double denormalize(double normValue, double minValue, double maxValue) {
        double ratio = (normValue - NORM_MIN) / (NORM_MAX - NORM_MIN);
        return minValue + ratio * (maxValue - minValue);
    }

    public double activation(double x) {
        return 1.0 / (1.0 + exp(-slope *x));
    }


    public static Network readXml(File file) {
        return (Network) createXStream().fromXML(file);
    }

    public void writeXml(File file) throws IOException {
        Writer writer = new FileWriter(file);
        try {
            writer.write(toXml());
        } finally {
            writer.close();
        }
    }

    public static Network fromXml(String xml) {
        return (Network) createXStream().fromXML(xml);
    }

    public String toXml() {
        return createXStream().toXML(this);
    }

    private static XStream createXStream() {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("network", Network.class);
        return xStream;
    }

}

