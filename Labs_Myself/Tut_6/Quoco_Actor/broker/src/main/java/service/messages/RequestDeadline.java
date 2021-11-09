
// Imports
package service.messages;

import akka.actor.ActorRef;

public class RequestDeadline {

    // Property declarations
    private long SEED_ID;
    private ActorRef client_ref;


    // Constructors

    public RequestDeadline(long application_request_id, ActorRef client_ref_init) {
        this.SEED_ID = application_request_id;
        this.client_ref = client_ref_init;

    }

    // Getters and Setters 

    public long getSEED_ID() {
        return SEED_ID;
    }

    public void setSEED_ID(long sEED_ID) {
        SEED_ID = sEED_ID;
    }

    public ActorRef get_client_ref() {
        return client_ref;
    }

    public void set_client_ref(ActorRef client_ref) {
        this.client_ref = client_ref;
    }

    
    
    
}
