
// Imports
import service.actor.*;

import akka.actor.*;


public class Main{

    public static void main(String[] args) {

        try {
            
             // Setting up the Client Service
            ActorSystem system = ActorSystem.create();

            // Definining url of broker for interaction
            ActorSelection broker_url = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
            
            // Initialization of Client class, allows us to handle requests on client actor
            ActorRef ref = system.actorOf(Props.create(Client.class, broker_url),  "client");
            ref.tell("start", null);
            


        } catch (Exception e) {

            // Error Handling
            System.out.println(e.getMessage());
        }

        
    }

}
