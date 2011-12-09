package com.bc.oclnn;

/**
* @author Norman Fomferra
*/
public interface TrainingListener {
    void trainingStarted(TrainingEvent trainingEvent);

    void trainingInProgress(TrainingEvent trainingEvent);

    void trainingCompleted(TrainingEvent trainingEvent);
}
