
// Imports
package service.actor;

import akka.actor.*;
import service.core.*;
import service.message.*;


public class Quoter extends AbstractActor {

    // Variable Declarations
    private QuotationService service;
   
    // Handling all request types 
    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match( QuotationRequest.class, 
        msg -> {

            // Generate quotation, return to sender (broker)
            Quotation quotation = service.generateQuotation(msg.getInfo());
            getSender().tell( new QuotationResponse(msg.getId(), quotation), getSelf());

        })
        .match( Init.class, 
        msg -> {

            // Initializes AFQ Service
            service =  msg.getQuotationService();

        }).build();
    }
} 
