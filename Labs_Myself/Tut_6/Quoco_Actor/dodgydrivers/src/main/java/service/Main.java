
// Imports
package service;

import akka.actor.*;
import service.actor.Init;
import service.actor.Quoter;
import service.dodgydrivers.*;

/* 
    Service's Job 
  1. Register DDQ service With broker
  2. Upon receiving of quotation request, return quotation response
*/

public class Main {
    
    public static void main(String[] args) {

        // Setting up the DDQ Service
        ActorSystem system = ActorSystem.create();

        // Initialization of Quoter class, allows us to handle requests on ddq service
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "dodgydrivers");
        ref.tell(new Init(new DDQService()), null);

        // Registering Service with Broker
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        selection.tell("register", ref);
    } 
}
