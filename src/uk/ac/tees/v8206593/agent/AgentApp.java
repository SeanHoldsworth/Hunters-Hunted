package uk.ac.tees.v8206593.agent;

import javax.swing.SwingUtilities;

public class AgentApp {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("Agents: The Trail").setVisible(true);
            }
        });
    }
}

