package uk.ac.tees.v8206593.agent;

import java.awt.Color;

import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class AgentWorld {
    private final double perceptionRange = 10.0;
    private ArrayList<Agent> agents;
    private ArrayList<ArrayList<Integer>> neighborsByCell;
    private Color cells[];
    private int cellsPerSide;
    private int nCells;
    private Random random = new Random();

    private Color[] colors = { Color.RED, Color.GREEN, Color.BLUE };
    private int nColors = colors.length;

    public AgentWorld(int cellsPerSide, int nAgentsPerColor) {
        initialize(cellsPerSide, nAgentsPerColor);
    }

    public void initialize(int cellsPerSide, int nAgentsPerColor) {
        neighborsByCell = new ArrayList<ArrayList<Integer>>();
        this.cellsPerSide = cellsPerSide;
        nCells = cellsPerSide * cellsPerSide;
        cells = new Color[nCells];

        // Construct the array providing a list of neighbors for each cell.

        for (int i = 0; i < nCells; i++) {
            neighborsByCell.add(findNeighbors(i));
            cells[i] = Color.WHITE;
        }

        // Create the agents, assigning initial position and trail color.

        agents = new ArrayList<Agent>();

        for (Color color : colors)
            for (int i = 0; i < nAgentsPerColor; i++) 
                spawnAgent(color);
    }

    private void spawnAgent(Color color) {
        int offset = random.nextInt(nCells);

        while (cellIsOccupied(offset))
            offset = random.nextInt(nCells);

        agents.add(new Agent(offset, color, cellsPerSide));
    }

    private ArrayList<Integer> findNeighbors(int offset) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        final int modulo = offset % cellsPerSide;

        if (offset >= cellsPerSide) {
            if (modulo > 0)
                neighbors.add(offset - cellsPerSide - 1);

            neighbors.add(offset - cellsPerSide);

            if (modulo != cellsPerSide - 1)
                neighbors.add(offset - cellsPerSide + 1);
        }

        if (modulo != 0)
            neighbors.add(offset - 1);

        if (modulo != (cellsPerSide - 1))
            neighbors.add(offset + 1);

        if (offset < nCells - cellsPerSide) {
            if (modulo > 0)
                neighbors.add(offset + cellsPerSide - 1);

            neighbors.add(offset + cellsPerSide);

            if (modulo != (cellsPerSide - 1))
                neighbors.add(offset + cellsPerSide + 1);
        }

        return neighbors;
    }

    public boolean cellIsOccupied(int offset) {
        for (Agent agent : agents)
            if (agent.getPosition() == offset)
                return true;

        return false;
    }

    public ArrayList<Integer> possibleMoves(int offset) {
        return neighborsByCell.get(offset);
    }

    // Fill in the trails of each agent, making sure that when trails cross,
    // that newer trail cells overwrite older ones.

    private void addTrailCells() {
        HashMap<Integer,Integer> priorities = new HashMap<>();
        HashMap<Integer,Color> colors = new HashMap<>();

        for (int offset = 0; offset < nCells; offset++)
            cells[offset] = Color.WHITE;

        for (Agent agent : agents) {
            Queue<Integer> trail = agent.getTrail();
            Color color = agent.getColor();
            int priority = 0;
            
            for (int position : trail) {
                if (priorities.containsKey(position))
                    if (priorities.get(position) >= priority)
                        continue;

                priorities.put(position, priority);
                colors.put(position, color);
            }
        }

        for (Map.Entry<Integer,Color> entry : colors.entrySet()) {
            int position = entry.getKey();
            Color color = entry.getValue();
            cells[position] = color;
        }
    }

    public void moveAgents() {
        for (Agent agent : agents) {
            // Create a list of other nearby agents.

            ArrayList<Agent> nearby = new ArrayList<>();

            for (Agent other : agents) {
                if (agent == other)
                    continue;

                if (agent.distance(other) <= perceptionRange)
                    nearby.add(other);
            }

            int agentPosition = agent.getPosition();
            List<Integer> possibles = possibleMoves(agentPosition);
            agent.move(possibles, nearby);

            addTrailCells();
        }

        ArrayList<Agent> dead = new ArrayList<>();

        for (Agent agent : agents)
            if (agent.isDead())
                dead.add(agent);

        for (Agent corpse : dead) {
            Color color = corpse.getColor();
            agents.remove(corpse);
            spawnAgent(color);
        }

        for (Agent agent : agents) {
            int position = agent.getPosition();
            cells[position] = Color.BLACK;
        }
    }

    public Color[] getCells() {
        return cells;
    }

    public int getCellsPerSide() {
        return cellsPerSide;
    }
}
