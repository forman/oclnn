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

}
