package agh.cs.GUI;

import agh.cs.configuration.Configuration;
import agh.cs.mapElements.Animal;
import agh.cs.objectMapInformations.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Buttons extends JPanel implements ActionListener {
    private MainWindow gameMainFrame;
    private JButton start;
    private JButton stop;
    private JButton watch;
    private JButton show;
    private JButton save;

    private Animal animal;
    private String result;

    private boolean isPaused = false;
    private boolean firstPokemon = true;


    public Buttons(MainWindow gameMainFrame) {
        this.setGameMainFrame(gameMainFrame);
        setLayout(new GridLayout(2, 1));
        setSize(200, 200);
        this.setStart(new JButton("Simulate"));
        this.setStop(new JButton("Pause"));
        this.setWatch(new JButton("Catch a pokemon"));
        this.setShow(new JButton("Animals with dominating gene"));
        this.setSave(new JButton("Save to TXT"));

        this.getStart().addActionListener(this);
        this.getStop().addActionListener(this);
        this.getWatch().addActionListener(this);
        this.getShow().addActionListener(this);
        this.getSave().addActionListener(this);

        add(this.getStart());
        add(this.getStop());
        add(this.getSave());
        add(this.getShow());
        add(this.getWatch());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (this.isPaused()) {
            if (source == this.getStart()) {
                this.getGameMainFrame().getTimer().start();
                this.setPaused(false);
                Configuration.getInstance().setPinning(false);
            } else if (source == this.getSave()) {
                try {
                    FileWriter saveWriter = new FileWriter("Up_to_Day_" + getGameMainFrame().getSimulation().getDay() + "_save.txt");
                    saveWriter.write("Days: " + this.getGameMainFrame().getSimulation().getHistory().getDays()
                            + "\n" + "Animals: " + this.getGameMainFrame().getSimulation().getHistory().getNumberOfAnimals()
                            + "\n" + "Grasses: " + this.getGameMainFrame().getSimulation().getHistory().getNumberOfGrass()
                            + "\n" + "Average energy: " + getGameMainFrame().getSimulation().getHistory().getAverageEnergy()
                            + "\n" + "Dominating gene: " + getGameMainFrame().getSimulation().getHistory().getDominatingGene()
                            + "\n" + "Average age of dead: " + getGameMainFrame().getSimulation().getHistory().getAverageLifeLength()
                            + "\n" + "Average number of children: " + getGameMainFrame().getSimulation().getHistory().getAverageChildren());
                    saveWriter.close();
                    JOptionPane.showMessageDialog(null, "Success!!");
                } catch (IOException a) {
                    JOptionPane.showMessageDialog(null, "Booo... fail :'(");
                    a.printStackTrace();
                }
            } else if (source == this.getShow()) {
                Configuration.getInstance().setShow(true);
                getGameMainFrame().getGamePanel().repaint();
            } else if (source == this.getWatch()) {
                Configuration.getInstance().setPinning(true);
                if (this.isFirstPokemon()) {
                    getGameMainFrame().getFrame().addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (getAnimal() == null) {
                                int x = e.getX();
                                int y = e.getY();
                                x /= getGameMainFrame().getGamePanel().getWidthScale();
                                y = (y - Configuration.getInstance().getToolbarScale()) / getGameMainFrame().getGamePanel().getHeightScale();
                                List<Animal> animals = getGameMainFrame().getSimulation().getMap().getAnimalsMap().get(new Vector2d(x, y));
                                if (animals != null) {
                                    while (true) {
                                        setResult((String) JOptionPane.showInputDialog(
                                                null,
                                                "Name your pokemon",
                                                "Becoming a trainer",
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                null,
                                                ""
                                        ));
                                        if (getResult() != null && getResult().length() > 0) {
                                            setAnimal(animals.get(0));
                                            getAnimal().setPinned(true);
                                            getGameMainFrame().getDataPanel().updateData();
                                            break;
                                        }
                                        if (getResult() == null) {
                                            return;
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                }
                this.setFirstPokemon(false);
                if (getAnimal() != null) {
                    JOptionPane.showMessageDialog(null, "You have already pinned!");
                }
            }
        } else if (!isPaused()) {
            if (source == this.getStop()) {
                this.getGameMainFrame().getTimer().stop();
                this.setPaused(true);
                gameMainFrame.getFrame().addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isPaused && !Configuration.getInstance().isPinning()) {
                            int x = e.getX();
                            int y = e.getY();
                            x /= gameMainFrame.getGamePanel().getWidthScale();
                            y = (y - Configuration.getInstance().getToolbarScale()) / gameMainFrame.getGamePanel().getHeightScale();
                            List<Animal> animals = gameMainFrame.getSimulation().getMap().getAnimalsMap().get(new Vector2d(x, y));
                            if (animals != null) {
                                Animal animal = animals.get(ThreadLocalRandom.current().nextInt(animals.size()));
                                animal.sortGenes();
                                JOptionPane.showMessageDialog(null, "Genes: " + Arrays.toString(animal.getGenes())
                                        + "\nThis animal had " + animal.getChildren().size() + " children"
                                        + "\nand..."
                                        + "\nThis animal had " + animal.howManyAncestors() + " ancestors");

                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        }
    }


    public String getResult() {
        return this.result;
    }

    public Animal getAnimal() {
        return this.animal;
    }

    public MainWindow getGameMainFrame() {
        return gameMainFrame;
    }

    public void setGameMainFrame(MainWindow gameMainFrame) {
        this.gameMainFrame = gameMainFrame;
    }

    public JButton getStart() {
        return start;
    }

    public void setStart(JButton start) {
        this.start = start;
    }

    public JButton getStop() {
        return stop;
    }

    public void setStop(JButton stop) {
        this.stop = stop;
    }

    public JButton getWatch() {
        return watch;
    }

    public void setWatch(JButton watch) {
        this.watch = watch;
    }

    public JButton getShow() {
        return show;
    }

    public void setShow(JButton show) {
        this.show = show;
    }

    public JButton getSave() {
        return save;
    }

    public void setSave(JButton save) {
        this.save = save;
    }


    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isFirstPokemon() {
        return firstPokemon;
    }

    public void setFirstPokemon(boolean firstPokemon) {
        this.firstPokemon = firstPokemon;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setResult(String result) {
        this.result = result;
    }

}