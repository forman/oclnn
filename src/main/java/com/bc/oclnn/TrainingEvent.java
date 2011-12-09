package com.bc.oclnn;

import java.util.Date;

/**
* @author Norman Fomferra
*/
public class TrainingEvent {
    private final Date date;
    private final int cycleCount;
    private final int cycleCountLimit;
    private final int successCount;
    private final int successCountMax;
    private final int successCountLimit;
    private final int patternCount;
    private final long totalNanos;

    TrainingEvent(int cycleCount,
                  int cycleCountLimit,
                  int successCount,
                  int successCountMax,
                  int successCountLimit,
                  int patternCount,
                  long totalNanos) {
        this.successCountLimit = successCountLimit;
        this.date = new Date();
        this.cycleCount = cycleCount;
        this.cycleCountLimit = cycleCountLimit;
        this.successCount = successCount;
        this.successCountMax = successCountMax;
        this.patternCount = patternCount;
        this.totalNanos = totalNanos;
    }

    public Date getDate() {
        return date;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public int getCycleCountLimit() {
        return cycleCountLimit;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getSuccessCountMax() {
        return successCountMax;
    }

    public int getSuccessCountLimit() {
        return successCountLimit;
    }

    public int getPatternCount() {
        return patternCount;
    }

    public long getTotalNanos() {
        return totalNanos;
    }

    @Override
    public String toString() {
        return "TrainingEvent{" +
                "date=" + date +
                ", cycleCount=" + cycleCount +
                ", cycleCountLimit=" + cycleCountLimit +
                ", successCount=" + successCount +
                ", successCountMax=" + successCountMax +
                ", successCountLimit=" + successCountLimit +
                ", patternCount=" + patternCount +
                ", totalNanos=" + totalNanos +
                '}';
    }
}
