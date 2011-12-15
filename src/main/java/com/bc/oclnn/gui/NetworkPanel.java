package com.bc.oclnn.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * @author Norman Fomferra
 */
public class NetworkPanel extends AppPanel {

    private final JComboBox layerSizesField;
    private final JComboBox activationSlopeField;
    private final JCheckBox autoMinMaxCheckBox;
    private ParameterTableModel inputParameterTableModel;
    private ParameterTableModel outputParameterTableModel;

    public NetworkPanel(App app) {
        super(app, "Network");
        inputParameterTableModel = new ParameterTableModel(app.getInputVariables());
        outputParameterTableModel = new ParameterTableModel(app.getOutputVariables());
        JTable inputTable = new JTable(inputParameterTableModel);
        JTable outputTable = new JTable(outputParameterTableModel);

        JPanel parameterPanel = new JPanel(new GridLayout(1, 2, 4, 4));
        parameterPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        parameterPanel.add(createTitledTable("Input layer:", inputTable));
        parameterPanel.add(createTitledTable("Output layer:", outputTable));

        JPanel structurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        structurePanel.add(new JLabel("Layer sizes:"));
        layerSizesField = new JComboBox(new String[]{"2, 3, 1"});
        layerSizesField.setEditable(true);
        layerSizesField.addActionListener(new LayerSizesActionListener());
        structurePanel.add(layerSizesField);
        structurePanel.add(new JLabel("   "));
        structurePanel.add(new JLabel("Activation slope:"));
        activationSlopeField = new JComboBox(new String[]{"1.0", "2.0", "2.5", "5.0"});
        activationSlopeField.setEditable(true);
        activationSlopeField.addActionListener(new ActivationSlopeActionListener());
        structurePanel.add(activationSlopeField);
        structurePanel.add(new JLabel("   "));
        autoMinMaxCheckBox = new JCheckBox("Auto-min/max from patterns");
        autoMinMaxCheckBox.addActionListener(new AutoMinMaxActionListener());
        structurePanel.add(autoMinMaxCheckBox);

        setLayout(new BorderLayout(4, 4));
        add(structurePanel, BorderLayout.NORTH);
        add(parameterPanel, BorderLayout.CENTER);

        updateLayerSizesField();
        updateActivationSlopeField();
        updateAutoMinMaxBox();
        updateInputParameters();
        updateOutputParameters();

        app.getPropertyChangeSupport().addPropertyChangeListener("layerSizes", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateLayerSizesField();
             }
        });
        app.getPropertyChangeSupport().addPropertyChangeListener("activationSlope", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateActivationSlopeField();
            }
        });
       app.getPropertyChangeSupport().addPropertyChangeListener("autoMinMax", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateAutoMinMaxBox();
            }
        });
        app.getPropertyChangeSupport().addPropertyChangeListener("inputParameters", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateInputParameters();
            }
        });
        app.getPropertyChangeSupport().addPropertyChangeListener("outputParameters", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateOutputParameters();
            }
        });
    }

    @Override
    public void handleAppWindowOpened() {

    }

    private void updateInputParameters() {
        inputParameterTableModel.setVariables(getApp().getInputVariables());
    }

    private void updateOutputParameters() {
        outputParameterTableModel.setVariables(getApp().getOutputVariables());
    }

    private void updateActivationSlopeField() {
        activationSlopeField.setSelectedItem(getApp().getActivationSlope() + "");
    }

    private void updateAutoMinMaxBox() {
        autoMinMaxCheckBox.setSelected(getApp().isAutoMinMax());
    }

    private void updateLayerSizesField() {
        layerSizesField.setSelectedItem(Arrays.toString(getApp().getLayerSizes()).replace("[", " ").replace("]", " "));
    }


    private JPanel createTitledTable(String title, JTable table) {
        JPanel inputPanel = new JPanel(new BorderLayout(2, 2));
        inputPanel.add(new JLabel(title), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return inputPanel;
    }

    private class ParameterTableModel extends AbstractTableModel {
        private static final int ID_INDEX = 0;
        private static final int NAME_INDEX = 1;
        private static final int LOG_INDEX = 2;
        private static final int MIN_INDEX = 3;
        private static final int MAX_INDEX = 4;

        private  Variable[] variables;

        public ParameterTableModel(Variable[] variables) {
            this.variables = variables;
        }

        public Variable[] getVariables() {
            return variables;
        }

        public void setVariables(Variable[] variables) {
            this.variables = variables;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return variables.length;
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            boolean autoMinMax = autoMinMaxCheckBox.isSelected();
            if (columnIndex == ID_INDEX) {
                return false;
            } else if (columnIndex == NAME_INDEX) {
                return true;
            } else if (columnIndex == LOG_INDEX) {
                return true;
            } else if (columnIndex == MIN_INDEX) {
                return !autoMinMax;
            } else if (columnIndex == MAX_INDEX) {
                return !autoMinMax;
            }
            return false;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == ID_INDEX) {
                return "#";
            } else if (columnIndex == NAME_INDEX) {
                return "Name";
            } else if (columnIndex == MIN_INDEX) {
                return "Min";
            } else if (columnIndex == LOG_INDEX) {
                return "Log";
            } else if (columnIndex == MAX_INDEX) {
                return "Max";
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == ID_INDEX) {
                return Integer.class;
            } else if (columnIndex == NAME_INDEX) {
                return String.class;
            } else if (columnIndex == LOG_INDEX) {
                return Boolean.class;
            } else if (columnIndex == MIN_INDEX) {
                return Double.class;
            } else if (columnIndex == MAX_INDEX) {
                return Double.class;
            }
            return Object.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == ID_INDEX) {
                return rowIndex + 1;
            } else if (columnIndex == NAME_INDEX) {
                return variables[rowIndex].name;
            } else if (columnIndex == LOG_INDEX) {
                return variables[rowIndex].logarithmic;
            } else if (columnIndex == MIN_INDEX) {
                return variables[rowIndex].minValue;
            } else if (columnIndex == MAX_INDEX) {
                return variables[rowIndex].maxValue;
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == NAME_INDEX) {
                variables[rowIndex].name = (String) aValue;
            } else if (columnIndex == LOG_INDEX) {
                variables[rowIndex].logarithmic = (Boolean) aValue;
            } else if (columnIndex == MIN_INDEX) {
                variables[rowIndex].minValue = (Double) aValue;
            } else if (columnIndex == MAX_INDEX) {
                variables[rowIndex].maxValue = (Double) aValue;
            }
        }
    }

    private class AutoMinMaxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getApp().setAutoMinMax(autoMinMaxCheckBox.isSelected());
        }
    }

    private class LayerSizesActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if ("comboBoxChanged".equals(e.getActionCommand())) {
                String layerSizesText = (String) layerSizesField.getSelectedItem();
                final String[] split = layerSizesText.split(",");
                int[] layerSizes = new int[split.length];
                if (layerSizes.length < 3) {
                    JOptionPane.showMessageDialog(NetworkPanel.this, "There must be at least 3 layers.");
                    return;
                }
                for (int i = 0; i < split.length; i++) {
                    int layerSize = Integer.parseInt(split[i].trim());
                    if (layerSize <= 0) {
                        JOptionPane.showMessageDialog(NetworkPanel.this, "All layer sizes must be > 0.");
                        return;
                    }
                    layerSizes[i] = layerSize;
                }

                getApp().setLayerSizes(layerSizes);
            }
        }
    }

    private class ActivationSlopeActionListener implements ActionListener {

        public ActivationSlopeActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String item = (String) activationSlopeField.getEditor().getItem();
            try {
                double activationSlope = Double.parseDouble(item);
                if (activationSlope <= 0) {
                    JOptionPane.showMessageDialog(NetworkPanel.this, "Slope must be > 0.");
                    return;
                }
                getApp().setActivationSlope(activationSlope);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(NetworkPanel.this, "Not a number.");
            }
        }
    }
}
