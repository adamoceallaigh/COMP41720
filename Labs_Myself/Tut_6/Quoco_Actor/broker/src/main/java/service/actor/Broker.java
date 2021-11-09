
// Imports
package service.actor;
import service.message.*;
import service.messages.RequestDeadline;
import java.util.*;
import akka.actor.*;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;

/*
* Broker's Job
*  1. Implement Actor Reference (Services) Directory
*  2. Listen for Quotation Requests ( consumer )
*   - Set up Cache with ClientApplication Requests
*  3. Send quotation request onto Services for quotations
*  4. Listen for Quotation Responses (services)
    - Check against cache for matching id's
*  5. Send ApplicationResponse back to consumer
*/


public class Broker extends AbstractActor {

    // Static instance Variable Declarations
    static HashMap<Long, ApplicationResponse> cache = new HashMap<>();
    static List<ActorRef> actorRefs = new ArrayList<>(); 

    @Override
    public Receive createReceive() {
    
        return receiveBuilder()
                .match(String.class,
                    msg -> {

                        /*
                            1. Listen for message containing "register" from each service
                            2. Register each service Quoter class as reference for later use
                        */
                        if (!msg.equals("register")) return;
                        System.out.println(getSender());
                        actorRefs.add(getSender());

                    })
                .match(ApplicationRequest.class,
                    quotation_request -> {   
                        
                        try {

                            // Set up Application Response in cache for service verification later
                            if(!cache.containsKey(quotation_request.get_application_request_id())) {
                                cache.put(quotation_request.get_application_request_id(), new ApplicationResponse(quotation_request.get_application_request_id() , quotation_request.getClientInfo()));
                            }
                            
                            /*
                                1. Loop through all actor refs (services) in directory
                                2. Send Quotation Request for Quotation from each service
                            */
                            for (ActorRef ref : actorRefs) {

                                ref.tell(
                                    new QuotationRequest(quotation_request.get_application_request_id(), quotation_request.getClientInfo()), getSelf()
                                );
                                
                            } 


                            /*
                                1. Wait for quotations from each service to return
                                2. Send a wait-over instruction to broker to send what's in cache
                            */
                            getContext().system().scheduler().scheduleOnce( 
                                Duration.create(2, TimeUnit.SECONDS), 
                                getSelf(), 
                                new RequestDeadline(quotation_request.get_application_request_id(), getSender()),
                                getContext().dispatcher(), 
                                null
                            );
                            
                        } catch (Exception e) {
                            
                            // Error Handling
                            System.out.println(e.getMessage());

                        }
                        

                        



                    }
                ).match(RequestDeadline.class,
                    send_instruction -> {

                        /* 
                            1. Obtains the relevant quotations pertaining to client info
                            2. Returns quotations to client
                        */
                        ApplicationResponse final_quotation_response_sent = cache.get(send_instruction.getSEED_ID());
                        send_instruction.get_client_ref().tell(final_quotation_response_sent, getSelf());
                    }
                )
                .match(QuotationResponse.class,
                    quotation_response -> {

                                /*
                                    Check quotation response id against cache
                                        - If present, add quotation received to it's quotations array
                                */
                                if(cache.get(quotation_response.getId()) != null){
                                    ApplicationResponse temporary_application_response = cache.get(quotation_response.getId());
                                    temporary_application_response.getQuotations().add(quotation_response.getQuotation());
                                }
                    }
                )
                .build();
    
    }

}
