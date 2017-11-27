package jade.wampus_world.agents;

import jade.core.Agent;

import java.util.List;

public class NavigatorAgent extends Agent implements NavigatorListener {

    @Override
    public void navigatorListModelChanged(List<NavigatorAgent> navigatorAgent) {
           updateNavigatorAgentList(navigatorAgent);
    }

    private void updateNavigatorAgentList(List<NavigatorAgent> navigatorAgent) {

    }

}
