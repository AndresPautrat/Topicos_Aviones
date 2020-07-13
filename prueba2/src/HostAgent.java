import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class HostAgent extends Agent {
    public static int numero_aviones = 5;

    @Override
    public void setup() {
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            addBehaviour(new OneShotBehaviour() {

                @Override
                public void action() {
                    PlatformController container = getContainerController();
                    try {
                        String localName = "Torre";
                        AgentController ac = container.createNewAgent(localName, "TorreAgent", null);
                        ac.start();
                    } catch (Exception e) {
                        System.err.println("exceptiion " + e);
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 5; i++) {
                        try {
                            String localName = "Avion" + i;
                            AgentController ac = container.createNewAgent(localName, "AvionAgent", null);
                            ac.start();
                        } catch (Exception e) {
                            System.err.println("exceptiion " + e);
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("exceptiion " + e);
            e.printStackTrace();
        }
    }

    private void crearAvion(PlatformController container) {
        try {
            String localName = "Torre";
            AgentController ac = container.createNewAgent(localName, "AvionAgent", null);
            ac.start();
        } catch (Exception e) {
            System.err.println("exceptiion " + e);
            e.printStackTrace();
        }
    }
}
