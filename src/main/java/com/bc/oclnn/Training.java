package com.bc.oclnn;

import java.util.Date;
import java.util.Random;

/**
 * The training for a network.
 *
 * @author Norman
 */
public class Training {

    /**
     * Train this network using the supplied pattern list.
     *
     * @param network           the network to train
     * @param patternList       list of training patterns
     * @param successCountLimit quantity of patterns to match for training success, -1 = all
     * @param cycleCountLimit   maximum training cycles, -1 = no limit
     * @param threshold         limit near zero or one to count as zero or one
     * @param notifyPeriod      period in milliseconds between two notifications
     * @param learningRate      try 0.25...0.5
     * @param momentum          try 0...1
     * @param randomSeed        a random seed for initialising the neuron's weights, if -1 a new one will be generated
     * @param trainer           the trainer  @return quantity of trained patterns
     * @param listeners         listeners to be notified during training
     * @return the number of successfully trained patterns
     */
    public static int trainNetwork(Network network,
                                   PatternList patternList,
                                   int successCountLimit,
                                   int cycleCountLimit,
                                   double threshold,
                                   int notifyPeriod,
                                   double learningRate,
                                   double momentum,
                                   long randomSeed,
                                   Trainer trainer,
                                   TrainingListener... listeners) {

        if (listeners.length == 0) {
            listeners = new TrainingListener[]{new StdoutTrainingListener()};
        }

        final int patternCount = patternList.size();

        if (successCountLimit < 0) {
            successCountLimit = patternCount;
        }
        if (cycleCountLimit < 0) {
            cycleCountLimit = Integer.MAX_VALUE;
        }

        int cycleCount = 0;
        int successCount = 0;
        int successCountMax = 0;

        final long startTime = System.nanoTime();
        final long deltaTime = notifyPeriod * 1000L * 1000L;
        long lastTime = startTime;

        network.initWeights(randomSeed >= 0 ? new Random(randomSeed) : new Random());

        final double[] output = network.createOutputArray();
        fireStarted(new TrainingEvent(cycleCount, cycleCountLimit,
                                      successCount, successCountMax, successCountLimit,
                                      patternCount,
                                      System.nanoTime() - startTime), listeners);

        boolean trainingCompleted;
        do {
            successCount = 0;

            for (int i = 0; i < patternCount; i++) {
                Pattern pattern = patternList.get(i);

                network.run(pattern.getInput(), output);
                network.train(pattern.getOutput(), learningRate, momentum);

                boolean trained = trainer.isTrained(output, pattern.getOutput(), threshold);
                if (trained) {
                    successCount++;
                }
            }
            cycleCount++;

            successCountMax = Math.max(successCountMax, successCount);

            long nowTime = System.nanoTime();

            if (lastTime + deltaTime <= nowTime) {
                fireInProgress(new TrainingEvent(cycleCount,
                                                 cycleCountLimit,
                                                 successCount, successCountMax, successCountLimit,
                                                 patternCount,
                                                 nowTime - startTime), listeners);
                lastTime = nowTime;
            }

            trainingCompleted = successCount >= patternCount
                    || successCount >= successCountLimit
                    || cycleCount >= cycleCountLimit;
        }
        while (!trainingCompleted);

        fireCompleted(new TrainingEvent(cycleCount, cycleCountLimit,
                                        successCount, successCountMax, successCountLimit,
                                        patternCount,
                                        System.nanoTime() - startTime), listeners);
        return successCount;
    }

    private static void fireStarted(TrainingEvent trainingEvent, TrainingListener[] listeners) {
        for (TrainingListener listener : listeners) {
            listener.trainingStarted(trainingEvent);
        }
    }

    private static void fireInProgress(TrainingEvent trainingEvent, TrainingListener[] listeners) {
        for (TrainingListener listener : listeners) {
            listener.trainingInProgress(trainingEvent);
        }
    }

    private static void fireCompleted(TrainingEvent trainingEvent, TrainingListener[] listeners) {
        for (TrainingListener listener : listeners) {
            listener.trainingCompleted(trainingEvent);
        }
    }

    public interface Trainer {
        boolean isTrained(double[] actual, double[] expected, double threshold);
    }

    public static class ErrorTrainer implements Trainer {
        public boolean isTrained(double[] actual, double[] expected, double threshold) {
            for (int i = 0; i < expected.length; i++) {
                final double x1 = expected[i];
                final double x2 = actual[i];
                final double error = (x1 - x2) * (x1 - x2);
                if (error > threshold) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class BinaryTrainer implements Trainer {
        public boolean isTrained(double[] actual, double[] expected, double threshold) {
            final double lowerThreshold = 0.0 + threshold;
            final double upperThreshold = 1.0 - threshold;
            for (int i = 0; i < expected.length; i++) {
                final int c1 = classify(expected[i], lowerThreshold, upperThreshold);
                final int c2 = classify(actual[i], lowerThreshold, upperThreshold);
                if (c1 != c2) {
                    return false;
                }
            }
            return true;
        }

        private static int classify(double x, double lowerThreshold, double upperThreshold) {
            if (x > upperThreshold) {
                return 1;
            } else if (x < lowerThreshold) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    private static class StdoutTrainingListener implements TrainingListener {

        private Date startDate;

        @Override
        public void trainingStarted(TrainingEvent event) {
            startDate = event.getDate();
            System.out.println("Training started");
        }

        @Override
        public void trainingInProgress(TrainingEvent event) {
            System.out.printf("Training in progress: cycle: %d, success: %d/%d, avg. train time: %s ms%n",
                              event.getCycleCount(),
                              event.getSuccessCount(),
                              event.getPatternCount(),
                              event.getTotalNanos() / (event.getCycleCount() * 1.0E6));
        }

        @Override
        public void trainingCompleted(TrainingEvent event) {
            System.out.printf("Training completed after %s sec: cycle: %d, success: %d/%d, avg. train time: %s ms%n",
                              (event.getDate().getTime() - startDate.getTime()) / 1000.0,
                              event.getCycleCount(),
                              event.getSuccessCount(),
                              event.getPatternCount(),
                              event.getTotalNanos() / (event.getCycleCount() * 1.0E6));
        }
    }
}
