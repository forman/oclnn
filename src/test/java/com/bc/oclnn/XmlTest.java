package com.bc.oclnn;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class XmlTest {

    @Test
    public void testNetworkXml() throws Exception {
        final Network network = new Network(3, 4, 2);

        network.initWeights(new Random(17));

        final String xml = network.toXml();
        // System.out.println("xml = " + xml);
        assertEquals("<network>\n" +
                             "  <layerSizes>\n" +
                             "    <int>3</int>\n" +
                             "    <int>4</int>\n" +
                             "    <int>2</int>\n" +
                             "  </layerSizes>\n" +
                             "  <values>\n" +
                             "    <double-array>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "    </double-array>\n" +
                             "    <double-array>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "    </double-array>\n" +
                             "    <double-array>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "    </double-array>\n" +
                             "  </values>\n" +
                             "  <errors>\n" +
                             "    <double-array>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "    </double-array>\n" +
                             "    <double-array>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "    </double-array>\n" +
                             "    <double-array>\n" +
                             "      <double>0.0</double>\n" +
                             "      <double>0.0</double>\n" +
                             "    </double-array>\n" +
                             "  </errors>\n" +
                             "  <weights>\n" +
                             "    <null/>\n" +
                             "    <double-array-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.4646230279194632</double>\n" +
                             "        <double>0.39474095672149945</double>\n" +
                             "        <double>-0.8340877770996586</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.6324729022114612</double>\n" +
                             "        <double>-0.9112281249922618</double>\n" +
                             "        <double>-0.5205267741270143</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.4149096433788919</double>\n" +
                             "        <double>0.31896738451801476</double>\n" +
                             "        <double>0.7179931612328361</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>-0.9924905148351728</double>\n" +
                             "        <double>-0.11670299477778556</double>\n" +
                             "        <double>0.6991675942396791</double>\n" +
                             "      </double-array>\n" +
                             "    </double-array-array>\n" +
                             "    <double-array-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.8695418461489006</double>\n" +
                             "        <double>0.5491114573252789</double>\n" +
                             "        <double>-0.9833405298381221</double>\n" +
                             "        <double>0.7475028710629705</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.5469206694261699</double>\n" +
                             "        <double>0.30227733103502397</double>\n" +
                             "        <double>0.7994173431171527</double>\n" +
                             "        <double>-0.35088734512862696</double>\n" +
                             "      </double-array>\n" +
                             "    </double-array-array>\n" +
                             "  </weights>\n" +
                             "  <deltas>\n" +
                             "    <null/>\n" +
                             "    <double-array-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "      </double-array>\n" +
                             "    </double-array-array>\n" +
                             "    <double-array-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "      </double-array>\n" +
                             "      <double-array>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "        <double>0.0</double>\n" +
                             "      </double-array>\n" +
                             "    </double-array-array>\n" +
                             "  </deltas>\n" +
                             "</network>", xml);
    }

    @Test
    public void testPatternListXml() throws Exception {
        final PatternList patternList = new PatternList();
        patternList.add(new double[]{0.1, 0.2, 0.3}, new double[]{0.4, 0.5});
        patternList.add(new double[]{0.2, 0.3, 0.4}, new double[]{0.5, 0.6});
        patternList.add(new double[]{0.3, 0.4, 0.5}, new double[]{0.6, 0.7});

        final String xml = patternList.toXml();
        // System.out.println("xml = " + xml);
        assertEquals("<patternList>\n" +
                             "  <list>\n" +
                             "    <pattern>\n" +
                             "      <input>\n" +
                             "        <double>0.1</double>\n" +
                             "        <double>0.2</double>\n" +
                             "        <double>0.3</double>\n" +
                             "      </input>\n" +
                             "      <output>\n" +
                             "        <double>0.4</double>\n" +
                             "        <double>0.5</double>\n" +
                             "      </output>\n" +
                             "    </pattern>\n" +
                             "    <pattern>\n" +
                             "      <input>\n" +
                             "        <double>0.2</double>\n" +
                             "        <double>0.3</double>\n" +
                             "        <double>0.4</double>\n" +
                             "      </input>\n" +
                             "      <output>\n" +
                             "        <double>0.5</double>\n" +
                             "        <double>0.6</double>\n" +
                             "      </output>\n" +
                             "    </pattern>\n" +
                             "    <pattern>\n" +
                             "      <input>\n" +
                             "        <double>0.3</double>\n" +
                             "        <double>0.4</double>\n" +
                             "        <double>0.5</double>\n" +
                             "      </input>\n" +
                             "      <output>\n" +
                             "        <double>0.6</double>\n" +
                             "        <double>0.7</double>\n" +
                             "      </output>\n" +
                             "    </pattern>\n" +
                             "  </list>\n" +
                             "</patternList>", xml);

    }
}
