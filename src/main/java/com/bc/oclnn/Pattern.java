package com.bc.oclnn;

/**
 * A training pattern.
 *
 * @author Norman
 */
public class Pattern {

    private final double[] input;
    private final double[] output;

    public Pattern(double[] input, double[] output) {
        this.input = input.clone();
        this.output = output.clone();
    }

    public double[] getInput() {
        return input;
    }

    public double[] getOutput() {
        return output;
    }
}
