package agh.cs.GUI;

import agh.cs.simulationFiles.DarwinsGame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow implements ActionListener {

    private DarwinsGame simulation;
    private JFrame frame;
    private Timer timer;
    private GamePanel gamePanel;
    private Data dataPanel;

    public MainWindow(DarwinsGame simulation){
        this.setSimulation(simulation);
        setTimer(new Timer(simulation.getDelay(),this));

        setFrame(new JFrame("Darwins Game"));
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setPreferredSize(new Dimension(1000,500));
        getFrame().setResizable(false);
        getFrame().setVisible(true);
        getFrame().setLocation(50,50);

        setGamePanel(new GamePanel(this.getSimulation(),this));
        setDataPanel(new Data(this.getSimulation(),this));

        getFrame().setLayout(new GridLayout(1,2));
        getFrame().add(getGamePanel());
        getFrame().add(getDataPanel());
        getFrame().pack();
    }

    public void startSimulation(){
        getTimer().start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getSimulation().simulate();
        getGamePanel().repaint();
        getDataPanel().updateData();
    }

    public DarwinsGame getSimulation() {
        return simulation;
    }

    public void setSimulation(DarwinsGame simulation) {
        this.simulation = simulation;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public Data getDataPanel() {
        return dataPanel;
    }

    public void setDataPanel(Data dataPanel) {
        this.dataPanel = dataPanel;
    }
}
