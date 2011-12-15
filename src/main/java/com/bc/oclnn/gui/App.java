package com.bc.oclnn.gui;

import com.bc.oclnn.Pattern;
import com.bc.oclnn.PatternList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.Preferences;

/**
 * @author Norman Fomferra
 */
public class App {
    private final JFrame frame;
    private final PropertyChangeSupport propertyChangeSupport;
    private final AppPanel[] appPanels;

    private int[] layerSizes;
    private double activationSlope;
    private boolean autoMinMax;
    private Variable[] inputVariables;
    private Variable[] outputVariables;
    private PatternList patternList;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }

        final App app = new App();
        app.start();
    }

    public App() {
        layerSizes = new int[]{2, 3, 1};
        activationSlope = 1.0;
        autoMinMax = true;
        inputVariables = resizeVariableArray(new Variable[0], getInputLayerSize(), "input");
        outputVariables = resizeVariableArray(new Variable[0], getOutputLayerSize(), "output");

        propertyChangeSupport = new PropertyChangeSupport(this);
        frame = new JFrame("OCLNN 1.0");
        final JMenuBar menuBar = new JMenuBar();
        final JMenu fileMenu = new JMenu("File");
        fileMenu.add(new OpenNetworkAction());
        fileMenu.addSeparator();
        fileMenu.add(new SaveNetworkAction());
        fileMenu.add(new SaveNetworkAsAction());
        fileMenu.addSeparator();
        fileMenu.add(new LoadPatternListAction());
        fileMenu.addSeparator();
        fileMenu.add(new ExitAction());
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        appPanels = new AppPanel[] {
                new NetworkPanel(this),
                new TrainingPanel(this),
        };
        JTabbedPane tabbedPane = new JTabbedPane();
        for (AppPanel appPanel : appPanels) {
            tabbedPane.add(appPanel.getTitle(), appPanel);
        }
        tabbedPane.setSelectedIndex(0);
        frame.add(tabbedPane);
        frame.setBounds(getFrameBounds());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                for (AppPanel appPanel : appPanels) {
                    appPanel.handleAppWindowOpened();
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {
                new ExitAction().run();
            }
        });

    }

    private int getOutputLayerSize() {
        return layerSizes[layerSizes.length - 1];
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }


    private Rectangle getFrameBounds() {
        final DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        final int width = 640;
        final int height = 480;
        final int screenWidth = displayMode.getWidth();
        final int screenHeight = displayMode.getHeight();
        return new Rectangle((screenWidth - width) / 2, (screenHeight - height) / 2, width, height);
    }

    private void start() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public Preferences getPreferences() {
        return Preferences.userRoot();
    }

    private static Variable createVariable(int index, String baseName) {
        return new Variable(index, baseName + "_" + (index + 1));
    }

    public static JFileChooser createNetworkFileChooser(App app, String title) {
        final JFileChooser fileChooser = new JFileChooser(app.getPreferences().get("lastDir", "."));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new NetworkFileFilter());
        fileChooser.setDialogTitle(title);
        return fileChooser;
    }

    public int[] getLayerSizes() {
        return layerSizes.clone();
    }

    public void setLayerSizes(int[] layerSizes) {
        if (!Arrays.equals(this.layerSizes, layerSizes)) {
            int[] oldValue = this.layerSizes;
            this.layerSizes = layerSizes.clone();


            int inputSize = getInputLayerSize();
            if (inputSize != inputVariables.length) {
                setInputVariables(resizeVariableArray(inputVariables, inputSize, "input"));
            }
            int outputSize = layerSizes[layerSizes.length - 1];
            if (outputSize != outputVariables.length) {
                setOutputVariables(resizeVariableArray(outputVariables, outputSize, "output"));
            }

            propertyChangeSupport.firePropertyChange("layerSizes", oldValue, layerSizes);
        }
    }

    private int getInputLayerSize() {
        return this.layerSizes[0];
    }

    private Variable[] resizeVariableArray(Variable[] oldVariables, int newSize, String baseName) {
        Variable[] variables = new Variable[newSize];
        int n = Math.min(oldVariables.length, variables.length);
        System.arraycopy(oldVariables, 0, variables, 0, n);
        for (int i = n; i < variables.length; i++) {
            variables[i] = createVariable(i, baseName);
        }
        return variables;
    }

    public Variable[] getInputVariables() {
        return inputVariables;
    }

    public void setInputVariables(Variable[] inputVariables) {
        if (!Arrays.equals(this.inputVariables, inputVariables)) {
            Variable[] oldValue = this.inputVariables;
            this.inputVariables = inputVariables.clone();
            propertyChangeSupport.firePropertyChange("inputVariables", oldValue, inputVariables);
        }
    }

    public Variable[] getOutputVariables() {
        return outputVariables;
    }

    public void setOutputVariables(Variable[] outputVariables) {
        if (!Arrays.equals(this.outputVariables, outputVariables)) {
            Variable[] oldValue = this.outputVariables;
            this.outputVariables = outputVariables.clone();
            propertyChangeSupport.firePropertyChange("outputVariables", oldValue, outputVariables);
        }
    }

    public double getActivationSlope() {
        return activationSlope;
    }

    public void setActivationSlope(double activationSlope) {
        if (this.activationSlope != activationSlope) {
            double oldValue = this.activationSlope;
            this.activationSlope = activationSlope;
            propertyChangeSupport.firePropertyChange("activationSlope", oldValue, activationSlope);
        }
    }

    public boolean isAutoMinMax() {
        return autoMinMax;
    }

    public void setAutoMinMax(boolean autoMinMax) {
        if (this.autoMinMax != autoMinMax) {
            boolean oldValue = this.autoMinMax;
            this.autoMinMax = autoMinMax;
            propertyChangeSupport.firePropertyChange("autoMinMax", oldValue, autoMinMax);
        }
    }

    public abstract class AppAction extends AbstractAction {
        public AppAction(String name) {
            putValue(ACTION_COMMAND_KEY, getClass().getName());
            putValue(NAME, name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            run();
        }

        public abstract void run();
    }


    public class OpenNetworkAction extends AppAction {

        public OpenNetworkAction() {
            super("Open Network...");
        }

        public void run() {
            final JFileChooser fileChooser = createNetworkFileChooser(App.this, "Open Network File");
            final int response = fileChooser.showOpenDialog(frame);
            getPreferences().put("lastDir", fileChooser.getCurrentDirectory().getPath());
        }
    }

    public class SaveNetworkAction extends AppAction {
        public SaveNetworkAction() {
            super("Save Network");
        }

        public void run() {
            new SaveNetworkAsAction().run();
        }
    }

    public class SaveNetworkAsAction extends AppAction {
        public SaveNetworkAsAction() {
            super("Save Network As...");
        }

        public void run() {
            final String title = "Save Network File";
            final JFileChooser fileChooser = createNetworkFileChooser(App.this, title);
            final int response = fileChooser.showSaveDialog(frame);
            getPreferences().put("lastDir", fileChooser.getCurrentDirectory().getPath());
        }
    }

    public class LoadPatternListAction extends AppAction {
        public LoadPatternListAction() {
            super("Load Patterns...");
        }

        public void run() {
            final JFileChooser fileChooser = new JFileChooser(getPreferences().get("lastDir", "."));
            fileChooser.setDialogTitle("Load Pattern File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.addChoosableFileFilter(new PatternListXmlFileFilter());
            fileChooser.addChoosableFileFilter(new PatternListTextFileFilter());
            final int response = fileChooser.showOpenDialog(frame);
            getPreferences().put("lastDir", fileChooser.getCurrentDirectory().getPath());

            if (response == JFileChooser.APPROVE_OPTION) {
                FileFilter fileFilter = fileChooser.getFileFilter();
                PatternList patternList = null;
                if (fileFilter instanceof PatternListXmlFileFilter) {
                    try {
                         patternList = PatternList.readXml(fileChooser.getSelectedFile());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "I/O error:\n" + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(frame, "Illegal format (old version?):\n" + e.getMessage());
                    }
                } else {
                    try {
                        patternList = readPatternsFromTextFile(fileChooser.getSelectedFile());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "I/O error:\n" + e.getMessage());
                    }
                }
                if (patternList != null) {
                    setPatternList(patternList);
                    JOptionPane.showMessageDialog(frame, patternList.size() + " pattern(s) loaded.");
                }
            }
        }

        private PatternList readPatternsFromTextFile(File file) throws IOException {
            FileReader fileReader = new FileReader(file);
            try {
                return readPatterns(fileReader);
            } finally {
                fileReader.close();
            }
        }

        private PatternList readPatterns(Reader reader) throws IOException {
            StreamTokenizer st = new StreamTokenizer(reader);
            st.resetSyntax();
            st.commentChar('#');
            st.parseNumbers();
            st.whitespaceChars(0, 32);
            st.eolIsSignificant(false);
            ArrayList<Pattern> patterns = new ArrayList<Pattern>();
            int inputLayerSize = getInputLayerSize();
            int outputLayerSize = getOutputLayerSize();
            int i1 = 0, i2 = -1;
            double[] input = new double[inputLayerSize];
            double[] output = new double[outputLayerSize];
            while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                if (i1 >= 0) {
                    input[i1++] = st.nval;
                } else if (i2 >= 0) {
                    output[i2++] = st.nval;
                } else {
                    throw new IllegalStateException();
                }
                if (i1 == inputLayerSize) {
                    i1 = -1;
                    i2 = 0;
                }
                if (i2 == outputLayerSize) {
                    i1 = 0;
                    i2 = -1;
                    patterns.add(new Pattern(input, output));
                }
            }
            if (i1 == 0 && i2 == -1) {
                return new PatternList(patterns);
            } else {
                JOptionPane.showMessageDialog(frame,
                                              String.format("Number of values in this file does not match current network structure:\n" +
                                                                    "Expected n patterns of size %d (= %d inputs + %d outputs).",
                                                            inputLayerSize + outputLayerSize,
                                                            inputLayerSize, outputLayerSize));
                return null;
            }
        }

    }

    public PatternList getPatternList() {
        return patternList;
    }

    public void setPatternList(PatternList patternList) {
        PatternList oldValue = this.patternList;
        this.patternList = patternList;
        propertyChangeSupport.firePropertyChange("patternList", oldValue, this.patternList);
    }

    public class ExitAction extends AppAction {

        public ExitAction() {
            super("Exit");
        }

        public void run() {
            System.exit(0);
        }
    }

    public static class NetworkFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            final String name = f.getName().toLowerCase();
            return name.endsWith("-network.xml");
        }

        @Override
        public String getDescription() {
            return "Network XML files (*-network.xml)";
        }
    }

    public static class PatternListXmlFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            final String name = f.getName().toLowerCase();
            return name.endsWith("-pattern.xml");
        }

        @Override
        public String getDescription() {
            return "Pattern XML files (*-pattern.xml)";
        }
    }

    public static class PatternListTextFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            final String name = f.getName().toLowerCase();
            return name.endsWith(".txt") || name.endsWith(".csv");
        }

        @Override
        public String getDescription() {
            return "Pattern text files (*.txt, *.csv)";
        }
    }
}
