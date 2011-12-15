package com.bc.oclnn.gui;

import javax.swing.*;

/**
 * @author Norman Fomferra
 */
public abstract class AppPanel extends JPanel {
    private final App app;
    private final String title;

    public AppPanel(App app,  String title) {
        this.app = app;
        this.title = title;
    }

    public App getApp() {
        return app;
    }

    public String getTitle() {
        return title;
    }

    public abstract void handleAppWindowOpened();
}
