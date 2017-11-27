package jade.wampus_world.environments;

import jade.util.Logger;
import jade.wampus_world.agents.WorldModelListener;
import jade.wampus_world.agents.NavigatorAgent;
import jade.wampus_world.agents.NavigatorListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private Logger log = Logger.getMyLogger(this.getClass().getName());

    private WorldNode[][] WorldMap = null;
    private List<WorldModelListener> listenersWorld;

    private List<NavigatorAgent> hunters;
    private List<NavigatorListener> listenersHunter;

    public World() {

        WorldMap = new WorldNode[4][4];
        listenersWorld = new ArrayList<WorldModelListener>();

        hunters = new ArrayList<>();
        listenersHunter = new ArrayList<>();

        initWorld();
    }

    private void initWorld() {

        for (int i = 0; i < WorldMap.length; i++ ) {
            for (int j = 0; j < WorldMap[i].length; j++) {
                WorldMap[i][j] = new WorldNode(i, j);
            }
        }

        Random r = new Random();
        int x, y;
        boolean placed = false;
        while(!placed){
            x = r.nextInt(WorldMap.length);
            y = r.nextInt(WorldMap[1].length);
            if(x != 0 || y != 0){
                WorldMap[x][y].setWumpus(true);
                placed = true;
            }
        }

        placed = false;
        while (!placed) {
            x = r.nextInt(WorldMap.length);
            y = r.nextInt(WorldMap[1].length);
            if ((x != 0 || y != 0) && WorldMap[x][y].hasNoWumpus()) {//isEmpty()) {
                WorldMap[x][y].setGold(true);
                placed = true;
            }
        }

        for (x = 0; x < WorldMap.length; x++ ) {
            for (y = 0; y < WorldMap[x].length; y++) {
                if ((x != 0 || y != 0) && WorldMap[x][y].isEmpty()){
                    if (Math.random() <= 0.2) {
                        WorldMap[x][y].setPit(true);
                    }
                }
            }
        }

        notifyWorldModelListeners();
    }

    public WorldNode[][] getWorldMap() {
        return this.WorldMap;
    }


    public void addWorldModelListener(final WorldModelListener listener) {
        if (listener != null && !listenersWorld.contains(listener)) {
            this.listenersWorld.add(listener);
        }
    }

    public void removeWorldModelListener(final WorldModelListener listener) {
        this.listenersWorld.remove(listener);
    }

    public void notifyWorldModelListeners() {
        for (final WorldModelListener listener : this.listenersWorld) {
            notifyWorldModelListener(listener);
        }
    }

    public void notifyWorldModelListener(final WorldModelListener listener) {
        listener.WorldModelChanged(this.getWorldMap());
    }

    public void addHunterListListener(final NavigatorListener listener) {
        if (listener != null && !listenersHunter.contains(listener)) {
            this.listenersHunter.add(listener);
        }
    }

    public void removeHunterListListener(final NavigatorListener listener) {
        this.listenersHunter.remove(listener);
    }

    public void notifyHunterListListeners() {
        for (final NavigatorListener listener : this.listenersHunter) {
            listener.navigatorListModelChanged(this.hunters);
        }
    }


}
