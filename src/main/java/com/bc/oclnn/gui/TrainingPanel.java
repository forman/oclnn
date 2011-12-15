package com.bc.oclnn.gui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Norman Fomferra
 */
public class TrainingPanel extends AppPanel {
    private ArrayList<Job> jobs;
    private final JPanel jobMonitorPanel;
    private final Random random = new Random();

    public TrainingPanel(App app) {
        super(app, "Training");

        jobs = new ArrayList<Job>();

        final JComboBox learningRateField = createComboBox(new String[]{"0.1", "0.2", "0.25", "0.5"});
        final JComboBox momentumField = createComboBox(new String[]{"0.0", "0.1", "0.2", "0.25", "0.5"});
        final JComboBox thresholdField = createComboBox(new String[]{"0.001", "0.005", "0.01", "0.05",});
        final JComboBox randomSeedField = createComboBox(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});

        JPanel paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        paramPanel.add(new JLabel("Learning rate:"));
        paramPanel.add(learningRateField);
        paramPanel.add(new JLabel("  "));
        paramPanel.add(new JLabel("Momentum:"));
        paramPanel.add(momentumField);
        paramPanel.add(new JLabel("  "));
        paramPanel.add(new JLabel("Threshold:"));
        paramPanel.add(thresholdField);
        paramPanel.add(new JLabel("  "));
        paramPanel.add(new JLabel("Random seed:"));
        paramPanel.add(randomSeedField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    startJob(Double.parseDouble(learningRateField.getSelectedItem().toString()),
                             Double.parseDouble(momentumField.getSelectedItem().toString()),
                             Double.parseDouble(thresholdField.getSelectedItem().toString()),
                             Integer.parseInt(randomSeedField.getSelectedItem().toString()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(TrainingPanel.this, "Not a number: " + e.getMessage());
                }
            }
        });
        buttonPanel.add(startButton);

        JPanel jobStartPanel = new JPanel(new BorderLayout(4, 4));
        jobStartPanel.add(paramPanel, BorderLayout.WEST);
        jobStartPanel.add(buttonPanel, BorderLayout.EAST);

        jobMonitorPanel = new JPanel(new GridLayout(-1, 1, 2, 2));

        setLayout(new BorderLayout(4, 4));
        add(jobStartPanel, BorderLayout.NORTH);
        add(new JScrollPane(jobMonitorPanel), BorderLayout.CENTER);
    }

    private void startJob(double learningRate, double momentum, double threshold, int randomSeed) {
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new DateAxis("Time"));

        final TimeSeriesCollection seriesCollection = new TimeSeriesCollection(new TimeSeries("Job " + (jobs.size() + 1)));
        NumberAxis rangeAxis = new NumberAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        XYPlot subplot = new XYPlot(seriesCollection, null, rangeAxis, new StandardXYItemRenderer());
        subplot.setBackgroundPaint(Color.lightGray);
        subplot.setDomainGridlinePaint(Color.white);
        subplot.setRangeGridlinePaint(Color.white);
        plot.add(subplot);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds

        JFreeChart chart = new JFreeChart(plot);
        chart.removeSubtitle(chart.getSubtitle(0));
        chart.setBorderPaint(Color.black);
        chart.setBorderVisible(true);
        chart.setBackgroundPaint(Color.white);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(512, 128));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JPanel buttonPanel = new JPanel(new BorderLayout(2, 2));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JButton removeButton = new JButton("Remove");
        buttonPanel.add(removeButton, BorderLayout.SOUTH);

        JPanel jobPanel = new JPanel(new BorderLayout(2, 2));
        jobPanel.add(chartPanel, BorderLayout.CENTER);
        jobPanel.add(buttonPanel, BorderLayout.EAST);

        final Job job = new Job(learningRate, momentum, threshold, randomSeed, seriesCollection, jobPanel);
        jobs.add(job);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobs.remove(job);
                jobMonitorPanel.remove(job.jobPanel);
                updatePanelUI();
            }
        });

        jobMonitorPanel.add(jobPanel);
        updatePanelUI();

    }

    private void updatePanelUI() {
        invalidate();
        validate();
        repaint();
    }

    private JComboBox createComboBox(String[] items) {
        JComboBox comboBox = new JComboBox(items);
        comboBox.setEditable(true);
        Component editorComponent = comboBox.getEditor().getEditorComponent();
        if (editorComponent instanceof JTextField) {
            JTextField textComponent = (JTextField) editorComponent;
            textComponent.setColumns(4);
            textComponent.setHorizontalAlignment(JTextField.RIGHT);
        }
        return comboBox;
    }

    @Override
    public void handleAppWindowOpened() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateState();
                    }
                });
                timer.setRepeats(true);
                timer.start();
            }
        });

    }


    private void updateState() {
        final Millisecond t = new Millisecond();
        for (Job job : jobs) {
            job.dataset.getSeries(0).add(t, random.nextGaussian());
        }
    }

    private class Job {
        final double learningRate;
        final double momentum;
        final double threshold;
        final long randomSeed;
        final TimeSeriesCollection dataset;
        final JPanel jobPanel;

        private Job(double learningRate, double momentum, double threshold, long randomSeed, TimeSeriesCollection dataset, JPanel jobPanel) {
            this.learningRate = learningRate;
            this.momentum = momentum;
            this.threshold = threshold;
            this.randomSeed = randomSeed;
            this.dataset = dataset;
            this.jobPanel = jobPanel;
        }
    }


}
