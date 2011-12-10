package com.bc.oclnn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A list of patterns.
 *
 * @author Norman
 */
public class PatternList {
    private ArrayList<Pattern> list = new ArrayList<Pattern>();

    public PatternList() {
    }

    public void add(double[] input, double[] output) {
        list.add(new Pattern(input, output));
    }

    public Pattern get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public void writeXml(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        try {
            String xml = toXml();
            writer.write(xml);
        } finally {
            writer.close();
        }
    }

    public String toXml() {
        return createXStream().toXML(this);
    }

    public static PatternList readXml(File file) throws IOException, ClassNotFoundException {
        return (PatternList) createXStream().fromXML(file);
    }

    public static PatternList fromXml(String xml) {
        return (PatternList) createXStream().fromXML(xml);
    }

    private static XStream createXStream() {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("pattern", Pattern.class);
        xStream.alias("patternList", PatternList.class);
        return xStream;
    }

    public void mix() {
        final int size = list.size();
        for (int i = 0; i< size; i++) {
            int i1 = (int)(size * Math.random());
            int i2 = (int)(size * Math.random());
            final Pattern p1 = list.get(i1);
            final Pattern p2 = list.get(i2);
            list.set(i1, p2);
            list.set(i2, p1);
        }

    }
}
