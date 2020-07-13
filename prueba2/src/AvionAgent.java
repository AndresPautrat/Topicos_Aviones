import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AvionAgent extends Agent {
    @Override
    public void setup() {
        try {
            ServiceDescription sd = new ServiceDescription();
            sd.setType("AvionAgent");
            sd.setName("AvionAgentDescription");
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);

            ParallelBehaviour parallel = new ParallelBehaviour();
            parallel.addSubBehaviour(new TickerBehaviour(this, 1000) {

                @Override
                protected void onTick() {
                    System.out.println("(" + getLocalName() + ",volando)");
                }
            });

            parallel.addSubBehaviour(new TickerBehaviour(this, 5000) {

                @Override
                protected void onTick() {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent("volando");
                    msg.addReceiver(new AID("Torre", AID.ISLOCALNAME));
                    send(msg);
                }
            });

            parallel.addSubBehaviour(new CyclicBehaviour() {

                @Override
                public void action() {
                    ACLMessage msg = myAgent.receive();
                    if (msg != null) {
                        switch (msg.getContent()) {
                            case "recibido":
                                System.out.print("-> " + " confirmacion de la torre" + getLocalName());
                                break;
                            case "aceptado":
                                addBehaviour(new OneShotBehaviour() {

                                    @Override
                                    public void action() {
                                        System.out.println("=> " + getLocalName() + " (inicio)");
                                        block(2000);
                                        System.out.println("=> " + getLocalName() + " (aterrizaje)");
                                        doDelete();
                                        TorreAgent.pista = false;
                                    }
                                });
                                break;
                        }
                    }
                }
            });

            parallel.addSubBehaviour(new TickerBehaviour(this, 10000) {

                @Override
                protected void onTick() {
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.setContent("permiso_aterrizar");
                    msg.addReceiver(new AID("Torre", AID.ISLOCALNAME));
                    send(msg);
                }
            });

            addBehaviour(parallel);
        } catch (Exception e) {
            System.err.println("exception " + e);
            e.printStackTrace();
        }
    }
}