package com.bc.oclnn;

import org.junit.Test;

import java.util.Random;

/**
 * @author Norman Fomferra
 */
public class SinTest {

    @Test
    public void testSin() throws Exception {
        PatternList patternList = new PatternList();
        for (int i = 0; i < 100; i++) {
            double x = 2 * Math.PI * Math.random();
            double y = 2 * Math.PI * Math.random();
            double z = Math.sin(x) * Math.sin(y);
            patternList.add(
                    new double[]{
                            Network.scaleIn(x, 0, 2 * Math.PI),
                            Network.scaleIn(y, 0, 2 * Math.PI)
                    },
                    new double[]{
                            Network.scaleIn(z, -1, +1)
                    });
        }

        Network network = new Network(2, 5, 5, 1);
        Training.trainNetwork(network, patternList, -1, -1, 0.001, 500, 0.25, 0.1, 6, new Training.ErrorTrainer());
    }

}
