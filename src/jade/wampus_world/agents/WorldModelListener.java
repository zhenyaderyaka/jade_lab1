package jade.wampus_world.agents;

import jade.wampus_world.enviroments.WorldNode;

public interface WorldModelListener {
    void WorldModelChanged(WorldNode[][] worldMap);
}
