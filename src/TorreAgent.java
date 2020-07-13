import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class TorreAgent extends Agent {
    public static Boolean pista = false;

    @Override
    protected void setup() {
        try {
            ServiceDescription sd = new ServiceDescription();
            sd.setType("TorrentAgent");
            sd.setName("TorrentAgentDescription");
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);

            addBehaviour(new CyclicBehaviour() {

                @Override
                public void action() {
                    // TODO Auto-generated method stub

                    ACLMessage msg = myAgent.receive();
                    if (msg != null) {
                        tratarMensajes(msg);
                    } else
                        block();
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected void tratarMensajes(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        switch (msg.getContent()) {
            case "volando":
                System.out.println(getLocalName() + ":mensaje de " + msg.getSender().getLocalName());
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent("recibido");
                break;
            case "permiso_aterrizar":
                if (pista) {
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reply.setContent(("rechazo"));
                } else {
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent("aceptado");
                    pista = true;
                }
                break;
            default:
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                reply.setContent("no Entendi");
        }
        send(reply);
    }
}
