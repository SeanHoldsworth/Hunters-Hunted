package uk.ac.tees.v8206593.agent;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

interface RestartListener {
    public void restartButtonPressed();
}

interface SpeedChangeListener {
    public void valueChanged(int gps);
}

interface SizeChangeListener {
    public void sizeChanged(int size);
}

public class ControlPanel extends JPanel {
    private JButton restartButton;
    private JSlider speedSlider;
    private JSpinner sizeSpinner;
    private ArrayList<RestartListener> restartListeners;
    private ArrayList<SpeedChangeListener> speedChangeListeners;
    private ArrayList<SizeChangeListener> sizeChangeListeners;
    private boolean running = true;

    // Moves Per Second
    private static final int MIN_MPS = 0;
    private static final int MAX_MPS = 10;
    private static final int INIT_MPS = 3;

    // Component padding
    private static final int hGap = 5;
    private static final int vGap = 5;

    public ControlPanel() {
        // Create components

        restartButton = new JButton("Restart");

        speedSlider = new JSlider(
            JSlider.HORIZONTAL, MIN_MPS, MAX_MPS, INIT_MPS);

        speedSlider.setMajorTickSpacing(5);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        SpinnerNumberModel gridSizes = new SpinnerNumberModel(40, 20, 50, 1);
        sizeSpinner = new JSpinner(gridSizes);
        sizeSpinner.setEditor(new JSpinner.DefaultEditor(sizeSpinner));

        // Set up action listeners

        restartListeners = new ArrayList<RestartListener>();
        speedChangeListeners = new ArrayList<SpeedChangeListener>();
        sizeChangeListeners = new ArrayList<SizeChangeListener>();

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (RestartListener listener : restartListeners)
                    listener.restartButtonPressed();
            }
        });

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!speedSlider.getValueIsAdjusting()) {
                    int gps = speedSlider.getValue();

                    for (SpeedChangeListener listener : speedChangeListeners)
                        listener.valueChanged(gps);
                }
            }
        });

        sizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int size = Integer.parseInt(sizeSpinner.getValue().toString());

                for (SizeChangeListener listener : sizeChangeListeners)
                    listener.sizeChanged(size);
            }
        });

        // Layout components

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(hGap, vGap, hGap, vGap);
        gc.weightx = 1; gc.weighty = 1;
        //gc.anchor = GridBagConstraints.LINE_START;

        gc.gridx = 0; gc.gridy = 0;
        gc.gridwidth = 2; gc.gridheight = 1;
        add(restartButton, gc);

        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth = 1; gc.gridheight = 1;
        add(new JLabel("Cells per Side:"), gc);

        gc.gridx = 1; gc.gridy = 1;
        gc.gridwidth = 1; gc.gridheight = 1;
        add(sizeSpinner, gc);

        gc.gridx = 2; gc.gridy = 0;
        gc.gridwidth = 1; gc.gridheight = 1;
        add(new JLabel("Moves per second"), gc);

        gc.gridx = 2; gc.gridy = 1;
        gc.gridwidth = 1; gc.gridheight = 1;
        add(speedSlider, gc);
    }

    public void addRestartListener(RestartListener listener) {
        restartListeners.add(listener);
    }

    public void addSpeedChangeListener(SpeedChangeListener listener) {
        speedChangeListeners.add(listener);
    }

    public void addSizeChangeListener(SizeChangeListener listener) {
        sizeChangeListeners.add(listener);
    }
}
