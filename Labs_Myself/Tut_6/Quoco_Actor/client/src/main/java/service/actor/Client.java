
// Imports
package service.actor;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import akka.actor.*;
import service.core.*;
import service.message.*;

/*
 * Client's Job
 *  - Send Quotation Requests for Client Info
 *  - Listen for Application Responses
 *  - Match Request ID with Response ID
 *  - Display Quotations, if received
 */


public class Client extends AbstractActor {

    // Variable Declarations
    public QuotationService service;
    public ActorSelection broker_url;
    private static long SEED_ID = 0;
    private static Map<Long, ClientInfo> cache = new HashMap<>();

    // Constructors

    public Client(ActorSelection broker_url_init){
        this.broker_url = broker_url_init;
    }
 
   
    // Handling all request types 
    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match( String.class, 
        msg -> {

            if (!msg.equals("start")) return;

            /*
                1. Loop through each client in test data
                2. Set up new Quotation Request Object for each client
                3. Send to Broker URL
            */
            for (ClientInfo info : clients) {

                try {
                    
                    ApplicationRequest initial_quotation_request = new ApplicationRequest(SEED_ID++, info);
                    cache.put(initial_quotation_request.get_application_request_id(), initial_quotation_request.getClientInfo());
                    broker_url.tell(initial_quotation_request, getSelf());

                } catch (Exception e){

                    // Error Handling
                    System.out.println(e);
                }

            }

        })
        .match( ApplicationResponse.class, 
        final_quotation_response -> {

            // Receive the id of message from cache
            ClientInfo info = cache.get(final_quotation_response.get_application_response_id());
            displayProfile(info);

            /*
                1. Loop through Client Application Message's Quotations Array, 
                2. Display Quotation
            */
            for (Quotation quotation: final_quotation_response.getQuotations() ){
                displayQuotation(quotation);
            }

        })
        .build();
    }

    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
                        " | Age: " + String.format("%1$-30s", info.age)+" |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.licenseNumber) +
                        " | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };
} 
