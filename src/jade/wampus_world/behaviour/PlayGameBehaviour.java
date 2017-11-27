package jade.wampus_world.behaviour;

import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.wampus_world.agents.WumpusAgent;

public class PlayGameBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 1L;

    private Logger log = Logger.getMyLogger(this.getClass().getName());

    private int transition = GameBehaviour.TRANS_GAME_OVER;

    ACLMessage reply = null;
    String content = null;
    AID senderAID = null;

    private Ontology ontology = null;
    private ContentManager man = null;
    private SLCodec codec = null;

    public void onStart() {
        if(log.isLoggable(Logger.INFO))
            log.log(Logger.INFO, "onStart()");

        man = myAgent.getContentManager();
        codec = new SLCodec();
        man.registerOntology(ontology);
        man.registerLanguage(codec);
    }

    public void action() {
        if (log.isLoggable(Logger.INFO))
            log.log(Logger.INFO, "action()");

        ACLMessage msg;
        do {
            msg = myAgent.blockingReceive();
            if (log.isLoggable(Logger.INFO)) {
                log.log(Logger.INFO, "Message-Content: " + msg.getContent());
                log.log(Logger.INFO, "Message-Performative: " + msg.getPerformative());
                log.log(Logger.INFO, "Message-Perfomrative (geraten): " + ACLMessage.REQUEST);
                log.log(Logger.INFO, "Message-Sender: " + msg.getSender().toString());
                log.log(Logger.INFO, "Message-Sender-Name: " + msg.getSender().getName());
                log.log(Logger.INFO, "Message-Sender-LocalName: " + msg.getSender().getLocalName());
            }
            content = msg.getContent().trim();
            senderAID = msg.getSender();
            reply = msg.createReply();
            reply.setPerformative(ACLMessage.REFUSE);
            reply.setOntology(ontology.getName());
            reply.setLanguage(codec.getName());
            if ((msg.getPerformative() == ACLMessage.REQUEST)
                    && (content.equals(WumpusConsts.ACTION_REGISTER))
                    && (!((WumpusAgent) myAgent).maximumSpectologsReached()))
            reply.setPerformative(ACLMessage.INFORM);

        if ((((WumpusAgent) myAgent).isRegisteredSpectologs(senderAID))
                && (msg.getPerformative() == ACLMessage.REQUEST)
                && (isContentValid(content))
                && (((WumpusAgent) myAgent).isSpectologsAlive(senderAID))) {

            if (log.isLoggable(Logger.INFO))
                log.log(Logger.INFO, "Hunter wants to: " + content);

            if (content.equals(WumpusConsts.ACTION_DEREGISTER)) {

                ((WumpusAgent) myAgent).processAction(senderAID, content);
                reply.setPerformative(ACLMessage.AGREE);

            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        myAgent.send(reply);

    } while (true);
    }

    private boolean isContentValid(String content) {

        if (content.equals(WumpusConsts.ACTION_DEREGISTER)
                || content.equals(WumpusConsts.ACTION_REGISTER)
                || content.equals(WumpusConsts.ACTION_GRAB)
                || content.equals(WumpusConsts.ACTION_MOVE)
                || content.equals(WumpusConsts.ACTION_SHOOT)
                || content.equals(WumpusConsts.ACTION_TURN_LEFT)
                || content.equals(WumpusConsts.ACTION_TURN_RIGHT))
            return true;

        return false;
    }

    public int onEnd() {

        return transition;
    }

    public boolean done() {
        return true;
    }
}
