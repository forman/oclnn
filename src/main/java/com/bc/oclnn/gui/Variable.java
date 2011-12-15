package com.bc.oclnn.gui;

/**
* Represents a network input or output variable.
*
* @author Norman Fomferra
*/
public class Variable {
    int index;
    String name;
    double minValue;
    double maxValue;
    boolean logarithmic;

    public Variable(int index, String name) {
        this.index = index;
        this.name = name;
        this.minValue = 0.0;
        this.maxValue = 1.0;
        this.logarithmic = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        if (index != variable.index) return false;
        if (logarithmic != variable.logarithmic) return false;
        if (Double.compare(variable.maxValue, maxValue) != 0) return false;
        if (Double.compare(variable.minValue, minValue) != 0) return false;
        if (!name.equals(variable.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = index;
        result = 31 * result + name.hashCode();
        temp = minValue != +0.0d ? Double.doubleToLongBits(minValue) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = maxValue != +0.0d ? Double.doubleToLongBits(maxValue) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (logarithmic ? 1 : 0);
        return result;
    }
}
