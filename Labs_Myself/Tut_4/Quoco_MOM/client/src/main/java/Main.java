
// Imports
import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;
import service.message.ClientApplicationMessage;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import javax.jms.*;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/*
 * Client's Job
 *  - Set up Requests Queue ( producer )
 *  - Listen to the Response Queue ( consumer )
 *  - Match Request ID with Response ID
 *  - Display Quotations, if received
 */

public class Main {

    private static long SEED_ID = 0;
    private static Map<Long, ClientInfo> cache = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // Variables for process
         String host = args.length > 0 ? args[0] : "0.0.0.0";
         int port = 9000;

         //  More Advanced flag-based configuration
         for (int i=0; i < args.length; i++) {
             switch (args[i]) {
                 case "-h":
                     host = args[++i];
                     break;
                 case "-p":
                     port = Integer.parseInt(args[++i]);
                     break;
                 default:
                     System.out.println("Unknown flag: " + args[i] +"\n");
                     System.out.println("Valid flags are:");
                     System.out.println("\t-h <host>\tSpecify the hostname of the target service");
                     System.out.println("\t-p <port>\tSpecify the port number of the target service");
                     System.exit(0);
             }
         }

        // Sets up Connection Factory on TCP port of ActiveMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");

        // Allows Serialization of Object from Classes
        ((ActiveMQConnectionFactory) factory).setTrustAllPackages(true);

        // Creates connection, Assigns Client ID
        Connection connection = factory.createConnection();
        connection.setClientID("client");

        // Connection creates Session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // Creating Consumer(s) and Producer(s) and their respective destinations
        Queue requests_queue = session.createQueue("REQUESTS");
        Queue responses_queue = session.createQueue("RESPONSES");

        MessageProducer request_producer = session.createProducer(requests_queue);
        MessageConsumer response_consumer = session.createConsumer(responses_queue);

        // Start connection so messages can be transmitted across nodes
        connection.start();


        /*
            1. Listen to Responses queue for Quotation Responses
            2. Match Client ID against response ID to identify discrepancies
            3. Display Quotation, if any are received
        */
        new Thread(() -> {

            while (true){
                try{

                    Message final_quotation_message = response_consumer.receive();

                    if (final_quotation_message instanceof ObjectMessage) {
                        Object content = ((ObjectMessage) final_quotation_message).getObject();
                        if (content instanceof ClientApplicationMessage) {
                            ClientApplicationMessage final_quotation_response = (ClientApplicationMessage) content;
                            ClientInfo info = cache.get(final_quotation_response.client_application_message_id);
                            displayProfile(info);

                            for (Quotation quotation: final_quotation_response.quotations ){
                                displayQuotation(quotation);
                            }

                            System.out.println("\n");
                        }
                        final_quotation_message.acknowledge();
                    } else {
                        System.out.println("Unknown message type: " +
                                final_quotation_message.getClass().getCanonicalName());
                    }

                } catch (Exception e) {

                    // Error Handling
                    System.out.println(e);

                }
            }
        }).start();

        /*
            1. Loop through each client in test data
            2. Set up new Request Quotation Message Object for each client
            3. Send to Requests Queue
        */
        for (ClientInfo info : clients) {

            try {
                QuotationRequestMessage initial_quotation_request = new QuotationRequestMessage(SEED_ID++, info);
                Message request = session.createObjectMessage(initial_quotation_request);
                cache.put(initial_quotation_request.id, initial_quotation_request.info);
                request_producer.send(request);

            } catch (Exception e){

                // Error Handling
                System.out.println(e);
            }

        }

        // Start connection so messages can be transmitted across nodes
        connection.start();




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
