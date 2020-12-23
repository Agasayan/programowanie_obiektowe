package agh.cs.GUI;


import agh.cs.simulationFiles.DarwinsGame;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class Data extends JPanel {
    private DarwinsGame simulation;
    private MainWindow gameMainFrame;
    private JLabel days = new JLabel();
    private JLabel animals = new JLabel();
    private JLabel grasses = new JLabel();
    private JLabel dominatingGenotype = new JLabel();
    private JLabel averageAlive = new JLabel();
    private JLabel averageDead = new JLabel();
    private JLabel childs = new JLabel();
    private JLabel pinnedChilds = new JLabel();
    private JLabel pinnedDead = new JLabel();
    private JLabel pinnedAncestors = new JLabel();
    private JLabel pinnedEnergy = new JLabel();
    private Buttons buttonPanel;
    private JLabel pinned = new JLabel();


    public Data(DarwinsGame simulation, MainWindow gameMainFrame) {
        this.setSimulation(simulation);
        this.setGameMainFrame(gameMainFrame);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setSize(300, 300);
        setBackground(new Color(245, 240, 240));

        this.getDays().setText("Day: " + this.getSimulation().getDay());
        this.getAnimals().setText("Animals: " + this.getSimulation().getHowManyAnimals());
        this.getGrasses().setText("Grasses: " + this.getSimulation().getMap().getGrassList().size());
        this.getDominatingGenotype().setText("Dominating gene: " + this.getSimulation().getDominatingGene());
        this.getAverageAlive().setText("Average energy of alive animals: " + simulation.getAverageEnergy());
        this.getAverageDead().setText("Average age of dead animals: " + simulation.getAverageDaysForDeadAnimals());
        this.getChilds().setText("Average number of children of alive animals: " + simulation.getAverageChild());

        add(this.getDays());
        add(this.getAnimals());
        add(this.getGrasses());
        add(this.getDominatingGenotype());
        add(this.getAverageAlive());
        add(this.getAverageDead());
        add(this.getChilds());


        add(this.getPinned());
        add(this.getPinnedEnergy());
        add(this.getPinnedChilds());
        add(this.getPinnedAncestors());
        add(this.getPinnedDead());

        this.setButtonPanel(new Buttons(this.getGameMainFrame()));
        add(this.getButtonPanel());
    }

    public void updateData() {
        setFocusable(true);
        setBackground(new Color(245, 240, 240));
        this.getDays().setText("Day: " + this.getSimulation().getDay());
        this.getAnimals().setText("Animals: " + this.getSimulation().getHowManyAnimals());
        this.getGrasses().setText("Grasses: " + this.getSimulation().getMap().getGrassList().size());
        this.getAverageAlive().setText("Average energy of alive animals: " + getSimulation().getAverageEnergy());
        this.getAverageDead().setText("Average age of dead animals: " + getSimulation().getAverageDaysForDeadAnimals());
        this.getChilds().setText("Children: " + getSimulation().getAverageChild());
        this.getDominatingGenotype().setText("Dominating gene: " + this.getSimulation().getDominatingGene());
        if (getButtonPanel().getResult() != null) {
            this.getPinnedEnergy().setText(getButtonPanel().getResult() + "'s energy: " + getButtonPanel().getAnimal().getEnergy());
            this.getPinned().setText("Pokemon:" + getButtonPanel().getResult());
            this.getPinnedChilds().setText(getButtonPanel().getResult() + " had " + getButtonPanel().getAnimal().getChildren().size() + " Pokechildren");
            this.getPinnedAncestors().setText(getButtonPanel().getResult() + " had " + getButtonPanel().getAnimal().howManyAncestors() + " Pokancestors");
            if (getButtonPanel().getAnimal().dead())
                this.getPinnedDead().setText(getButtonPanel().getResult() + " fought hard until: " + getButtonPanel().getAnimal().getDeathDay());
            else this.getPinnedDead().setText("Pokemon's adventure is still going on");
        }
    }

    public DarwinsGame getSimulation() {
        return simulation;
    }

    public void setSimulation(DarwinsGame simulation) {
        this.simulation = simulation;
    }

    public MainWindow getGameMainFrame() {
        return gameMainFrame;
    }

    public void setGameMainFrame(MainWindow gameMainFrame) {
        this.gameMainFrame = gameMainFrame;
    }

    public JLabel getDays() {
        return days;
    }

    public void setDays(JLabel days) {
        this.days = days;
    }

    public JLabel getAnimals() {
        return animals;
    }

    public void setAnimals(JLabel animals) {
        this.animals = animals;
    }

    public JLabel getGrasses() {
        return grasses;
    }

    public void setGrasses(JLabel grasses) {
        this.grasses = grasses;
    }

    public JLabel getDominatingGenotype() {
        return dominatingGenotype;
    }

    public void setDominatingGenotype(JLabel dominatingGenotype) {
        this.dominatingGenotype = dominatingGenotype;
    }

    public JLabel getAverageAlive() {
        return averageAlive;
    }

    public void setAverageAlive(JLabel averageAlive) {
        this.averageAlive = averageAlive;
    }

    public JLabel getAverageDead() {
        return averageDead;
    }

    public void setAverageDead(JLabel averageDead) {
        this.averageDead = averageDead;
    }

    public JLabel getChilds() {
        return childs;
    }

    public void setChilds(JLabel childs) {
        this.childs = childs;
    }

    public JLabel getPinnedChilds() {
        return pinnedChilds;
    }

    public void setPinnedChilds(JLabel pinnedChilds) {
        this.pinnedChilds = pinnedChilds;
    }

    public JLabel getPinnedDead() {
        return pinnedDead;
    }

    public void setPinnedDead(JLabel pinnedDead) {
        this.pinnedDead = pinnedDead;
    }

    public JLabel getPinnedAncestors() {
        return pinnedAncestors;
    }

    public void setPinnedAncestors(JLabel pinnedAncestors) {
        this.pinnedAncestors = pinnedAncestors;
    }

    public JLabel getPinnedEnergy() {
        return pinnedEnergy;
    }

    public void setPinnedEnergy(JLabel pinnedEnergy) {
        this.pinnedEnergy = pinnedEnergy;
    }

    public Buttons getButtonPanel() {
        return buttonPanel;
    }

    public void setButtonPanel(Buttons buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public JLabel getPinned() {
        return pinned;
    }

    public void setPinned(JLabel pinned) {
        this.pinned = pinned;
    }
}

