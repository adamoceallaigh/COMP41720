
// Imports 
package service;

import akka.actor.*;
import service.actor.Init;
import service.actor.Quoter;
import service.girlpower.GPQService;

/* 
    Service's Job 
  1. Register GPQ service With broker
  2. Upon receiving of quotation request, return quotation response
*/

public class Main {
    
    public static void main(String[] args) {

        // Setting up the GPQ Service
        ActorSystem system = ActorSystem.create();

        // Initialization of Quoter class, allows us to handle requests on gpq service
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "girlpower");
        ref.tell(new Init(new GPQService()), null);

        // Registering Service with Broker
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        selection.tell("register", ref);
    } 
}
