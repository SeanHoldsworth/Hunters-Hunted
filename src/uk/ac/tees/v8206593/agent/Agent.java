package uk.ac.tees.v8206593.agent;

import java.awt.Color;

import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Agents come in three colors; RED, GREEN and BLUE. RED agents hunt GREEN
 * agents, GREEN agents hunt BLUE agents and BLUE agents hunt RED agents.
 *
 * If a hunter reachers a prey then the prey is destroyed and a new agent of
 * the same type as the destroyed prey is created at a random position.
 *
 * Agents perceive the area around them out to a radius distance of 10 cell
 * widths. If no other agent is within perception range then the agent will
 * move randomly.
 */

public class Agent {
    private Color color;
    private int position;
    private Random random = new Random();
    private Queue<Integer> trail;
    private int cellsPerSide;
    private boolean dead = false;
    private final int trailLength = 20;

    // The cellsPerSide parameterizes the world in which the agent exists
    // and is used for distance and move preference calculations.

    public Agent(int offset, Color color, int cellsPerSide) {
        position = offset;
        this.color = color;
        this.cellsPerSide = cellsPerSide;

        trail = new LinkedList<Integer>();
    }

    /*
     * Given a list of potential moves and other neighbors that are nearby
     * decide where the agent is going to move.
     */

    public void move(List<Integer> possibleMoves, List<Agent> nearby) {
        int destination;

        // Dead agents do not move.

        if (dead)
            return;

        // If all nearby agents are of the same color then there is no
        // conflict and the agents can move at random.
        
        boolean conflict = false;

        for (Agent other : nearby) {
            if (isPrey(other) || isPredator(other)) {
                conflict = true;
                break;
            }
        }

        // Narrow down the list of possible moves so as to avoid collisions
        // except with prey agents.
 
        ArrayList<Integer> openMoves = new ArrayList<>();

        for (int possible : possibleMoves) {
            boolean collision = false;

            for (Agent other : nearby)
                if (other.getPosition() == possible) {
                    if (isPrey(other)) {
                        other.kill();
                        if (trail.size() == trailLength)
                            trail.remove();
                        trail.add(position);
                        position = possible;
                        return;
                    }
                    else
                        collision = true;
                }

            if (!collision)
                openMoves.add(possible);
        }

        // Agents can't cross their own trails which further reduces options.
 
        openMoves.removeAll(trail);
        
        // It may be the case that no possible move is available in which case
        // the agent remains where it is.

        if (openMoves.size() == 0) {
            if (trail.size() > 0)
                trail.remove();
            return;
        }

        if (conflict)
            destination = chooseBestMove(openMoves, nearby);
        else
            destination = openMoves.get(random.nextInt(openMoves.size()));

        if (trail.size() == trailLength)
            trail.remove();

        trail.add(position);
        position = destination;
    }

    public double distance(Agent other) {
        int x = other.position % cellsPerSide;
        int y = other.position / cellsPerSide;

        return distance(x, y);
    }

    public double distance(int x, int y) {
        int x1 = this.position % cellsPerSide;
        int y1 = this.position / cellsPerSide;

        return Math.hypot(x - x1, y - y1);
    }

    public int getPosition() {
        return position;
    }

    public Queue<Integer> getTrail() {
        return trail;
    }

    public Color getColor() {
        return color;
    }

    int chooseBestMove(List<Integer> moves, List<Agent> nearby) {
        ArrayList<Integer> bestMoves = new ArrayList<>();
        double bestValue = 0.0;

        for (int destination : moves) {
            double value = evaluateMove(destination, nearby);

            if (bestMoves.size() == 0) {
                bestMoves.add(destination);
                bestValue = value;
            }
            else if (value > bestValue) {
                bestMoves.clear();
                bestMoves.add(destination);
                bestValue = value;
            }
            else if (value == bestValue) {
                bestMoves.add(destination);
            }
        }

        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    double evaluateMove(int destination, List<Agent> nearby) {
        double value = 0.0;

        int x = destination % cellsPerSide;
        int y = destination / cellsPerSide;

        for (Agent other : nearby)
            if (isPrey(other))
                value += 1.0 / other.distance(x, y);
            else
                if (isPredator(other))
                    value -= 1.0 / other.distance(x, y);

        return value;
    }

    public boolean isPrey(Agent other) {
       if (color == Color.RED && other.color == Color.GREEN)
           return true;

       if (color == Color.GREEN && other.color == Color.BLUE)
           return true;

       if (color == Color.BLUE && other.color == Color.RED)
           return true;

       return false;
    }

    public boolean isPredator(Agent other) {
       if (color == Color.GREEN && other.color == Color.RED)
           return true;

       if (color == Color.BLUE && other.color == Color.GREEN)
           return true;

       if (color == Color.RED && other.color == Color.BLUE)
           return true;

       return false;
    }

    private void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }
}
