package agh.cs.GUI;

import agh.cs.configuration.Configuration;
import agh.cs.simulationFiles.DarwinsGame;


public class GUI {
    public static void main(String[] args) {
        for (int i = 0; i < Configuration.getInstance().getMaps(); i++) {
            MainWindow gameMainFrame = new MainWindow(new DarwinsGame(Configuration.getInstance().getMapWidth(), Configuration.getInstance().getMapHeight(), Configuration.getInstance().getStartingAnimals(), Configuration.getInstance().getAnimalEnergy(), Configuration.getInstance().getGrassEnergy(), Configuration.getInstance().getMoveCost(), Configuration.getInstance().getJunglePercentage(), Configuration.getInstance().getDelay()));
            gameMainFrame.startSimulation();
        }
    }
}