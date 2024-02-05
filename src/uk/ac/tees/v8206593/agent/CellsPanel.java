package uk.ac.tees.v8206593.agent;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CellsPanel extends JPanel {
    private AgentWorld world;
    private final int initialPanelWidth = 480;
    private final int initialAgentsPerColor = 2;
    private int nAgentsPerColor = initialAgentsPerColor;
    private int cellsPerSide;
    private Timer tm;

    public CellsPanel(int cellsPerSide) {
        Dimension size = new Dimension(initialPanelWidth, initialPanelWidth);
        setPreferredSize(size);
        setMinimumSize(size);

        world = new AgentWorld(cellsPerSide, nAgentsPerColor);
        this.cellsPerSide = cellsPerSide;
        setBackground(Color.WHITE);

        tm = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                world.moveAgents();
                repaint();
            }
        });

        tm.start();
    }

    public void setSpeed(int gps) {    // Generations Per Second
        if (gps == 0)
            tm.stop();
        else  {
            tm.setDelay(1000 / gps);
            if (!tm.isRunning())
                tm.start();
        }
    }

    public void setSize(int size) {
        cellsPerSide = size;
        restart();
    }

    public void restart() {
        world.initialize(cellsPerSide, nAgentsPerColor);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    // Draws background

        Graphics2D g2d = (Graphics2D) g;

        int nCells = cellsPerSide * cellsPerSide;
        int panelWidth = Math.min(getWidth(), getHeight());
        double cellSize = (double) panelWidth / cellsPerSide;
        double cellWidth = cellSize - 2.0;
        double xOffset = 1.0, yOffset = 1.0;
        int col = 0;

        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Color color : world.getCells()) {
            g2d.setColor(color);

            g2d.fill(new Rectangle2D.Double(
                xOffset, yOffset, cellWidth, cellWidth));

            if (++col == cellsPerSide) {
                col = 0;
                xOffset = 1.0;
                yOffset += cellSize;
            }
            else
                xOffset += cellSize;
        }
    }
}

