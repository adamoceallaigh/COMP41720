
// Imports
package service;

import akka.actor.*;
import service.actor.Broker;


public class Main {
    
    public static void main(String[] args) {

        // Setting up the Actor System
        ActorSystem system = ActorSystem.create();

        // Initialization of Broker class, allows us to handle requests on broker service
        system.actorOf(Props.create(Broker.class), "broker");
    } 
}

