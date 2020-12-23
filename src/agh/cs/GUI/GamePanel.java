package agh.cs.GUI;

import agh.cs.configuration.Configuration;
import agh.cs.map.GrassField;
import agh.cs.mapElements.Animal;
import agh.cs.mapElements.Grass;
import agh.cs.objectMapInformations.Vector2d;
import agh.cs.simulationFiles.DarwinsGame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class GamePanel extends JPanel {
    private DarwinsGame simulation;
    private MainWindow gameMainFrame;
    private GrassField map;

    private int width = 500;
    private int height = 500;
    private int widthScale;
    private int heightScale;


    public GamePanel(DarwinsGame simulation, MainWindow gameMainFrame) {
        this.setSimulation(simulation);
        this.setGameMainFrame(gameMainFrame);
        setWidthScale((getWidth() / this.getSimulation().getWidth()));
        setHeightScale((getHeight() / this.getSimulation().getHeight()));
        this.setWidth(getWidthScale() * this.getSimulation().getWidth());
        this.setHeight(getHeightScale() * this.getSimulation().getHeight());
        setSize(new Dimension(this.getWidth(), this.getHeight()));
        gameMainFrame.getFrame().setPreferredSize(new Dimension(500 + getWidth(), getHeight() + Configuration.getInstance().getToolbarSize())); //tutaj prosze wpisac wysokosc paska narzedzi, inaczej sie zepsuje :))
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setMap(getSimulation().getMap());
        int domGen = getSimulation().getDominatingGene();
        g.setColor(new Color(255, 111, 0));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(0, 160, 7));
        g.fillRect(getSimulation().getJungleArea().getBottom_left().x * getWidthScale(),
                getSimulation().getJungleArea().getBottom_left().y * getHeightScale(),
                getSimulation().getJungleArea().getWidth() * getWidthScale(),
                getSimulation().getJungleArea().getHeight() * getHeightScale());
        for (Grass grass : getMap().getGrassList().values()) {
            if (grass.getPosition() != null) {
                g.setColor(grass.toColor());
                int x = grass.getPosition().x * getWidthScale();
                int y = grass.getPosition().y * getHeightScale();
                g.fillRect(x, y, getWidthScale(), getHeightScale());
            }
        }


        for (Map.Entry<Vector2d, List<Animal>> entry : getMap().getAnimalsMap().entrySet()) {
            g.setColor(entry.getValue().get(0).toColor());
            if (getGameMainFrame().getTimer().isRunning() || !Configuration.getInstance().isShow()) {
                if (entry.getValue().contains(getGameMainFrame().getDataPanel().getButtonPanel().getAnimal())) {
                    g.setColor(new Color(0, 0, 255));
                }
            } else if (!getGameMainFrame().getTimer().isRunning() && Configuration.getInstance().isShow()) {
                for (Animal animal : entry.getValue()) {
                    if (animal.getBestGene() == domGen) {
                        g.setColor(new Color(255, 0, 255));
                        break;
                    }
                }
            }
            int y = entry.getValue().get(0).getPosition().y * getHeightScale();
            int x = entry.getValue().get(0).getPosition().x * getWidthScale();
            g.fillRect(x, y, getWidthScale(), getHeightScale());
        }

        Configuration.getInstance().setShow(false);

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

    public GrassField getMap() {
        return map;
    }

    public void setMap(GrassField map) {
        this.map = map;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(int widthScale) {
        this.widthScale = widthScale;
    }

    public int getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(int heightScale) {
        this.heightScale = heightScale;
    }
}

