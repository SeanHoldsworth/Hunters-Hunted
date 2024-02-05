package uk.ac.tees.v8206593.agent;

import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

class MainFrame extends JFrame {
    private final int cellsPerSide = 40;
    private CellsPanel cellsPanel;
    private ControlPanel controlPanel;

    public MainFrame(String name) {
        super(name);

        cellsPanel = new CellsPanel(cellsPerSide);
        controlPanel = new ControlPanel();

        controlPanel.addRestartListener(new RestartListener() {
            @Override
            public void restartButtonPressed() {
                cellsPanel.restart();
            };
        });

        controlPanel.addSpeedChangeListener(new SpeedChangeListener() {
            @Override
            public void valueChanged(int gps) {
                cellsPanel.setSpeed(gps);
            }
        });

        controlPanel.addSizeChangeListener(new SizeChangeListener() {
            @Override
            public void sizeChanged(int size) {
                cellsPanel.setSize(size);
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1; gc.weighty = 8;
        add(cellsPanel, gc);

        gc.gridx = 0; gc.gridy = 1;
        gc.weightx = 1; gc.weighty = 1;
        add(controlPanel, gc);

        pack();
        
        // Do not let the frame be resized smaller that this minimum size.

        setMinimumSize(getSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

