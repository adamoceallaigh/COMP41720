package service;
// Imports
import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.*;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/*
 Note:
 1. ConnectionFactory creates Connection
 2. Connection creates the Session
 3. Produces a Message Consumer and Producer
 4. Communicate messages through Session
 5. Consumer and Producer Endpoints are Destinations - Topic or Queues
*/

/*
* Broker's Job
*  - Listen to Quotations Queue ( consumer )
*  - Listen to Requests Queue ( consumer )
*  - Set Up the Applications Topic for services to subscribe to ( producer )
*  - Set up the Response Queue ( producer )
*/



public class Broker implements LocalBrokerService {

    private static long SEED_ID = 0;
    static HashMap<Long, ClientApplicationMessage> cache = new HashMap<>();

    public static void main(String[] args) {

        try {

            // Variables for process
            String host = args.length > 0 ? args[0] : "0.0.0.0";
            int port = 9000;
            List<QuotationResponseMessage> quotations_final_all_services = new LinkedList<>();

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

            // Creates connection, Assigns Broker ID
            Connection connection = factory.createConnection();
            connection.setClientID("broker");

            // Connection creates Session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Creating Consumer(s) and Producer(s) and their respective destinations
            Queue requests_queue = session.createQueue("REQUESTS");
            Queue responses_queue = session.createQueue("RESPONSES");
            Queue quotations_queue = session.createQueue("QUOTATIONS");
            Topic applications_topic = session.createTopic("APPLICATIONS");

            MessageConsumer requests_consumer = session.createConsumer(requests_queue);
            MessageProducer responses_producer = session.createProducer(responses_queue);
            MessageProducer applications_producer = session.createProducer(applications_topic);
            MessageConsumer quotations_consumer = session.createConsumer(quotations_queue);

            // Start connection so messages can be transmitted across nodes
            connection.start();

            new Thread(() -> {
                while(true){
                    try{

                        /*
                            Listen constantly to Requests Queue for Quotation Requests
                            - If received, send onto Applications topic
                        */

                        // Get the next message from the Requests queue
                        Message received_request_message = requests_consumer.receive();
                        System.out.println("Recieved Request");

                        if (received_request_message instanceof ObjectMessage) {
                            Object content = ((ObjectMessage) received_request_message).getObject();
                            if (content instanceof QuotationRequestMessage) {
                                QuotationRequestMessage quotation_request = (QuotationRequestMessage) content;
                                Message quotation_response_message = session.createObjectMessage(quotation_request);


//                                System.out.println(cache.get(quotation_response_message).client_application_message_id);
//                                System.out.println(quotation_request);
                                if(!cache.containsKey(quotation_request.id)) {
                                    cache.put(quotation_request.id, new ClientApplicationMessage(quotation_request.id , quotation_request.info));
                                }

                                applications_producer.send(quotation_response_message);

                                System.out.println(cache);

                                // Wait for 10 seconds
                                Thread.sleep(10000);


                                // Check quotations array for service quotations received

                                // Send Client Application Message back to Response Queue
                                Message final_quotation_response_sent = session.createObjectMessage(cache.get(quotation_request.id));
                                responses_producer.send(final_quotation_response_sent);
                            }
                            received_request_message.acknowledge();
                        } else {
                            System.out.println("Unknown received_quotation_response type: " +
                                    received_request_message.getClass().getCanonicalName());
                        }




                    } catch (Exception e){

                        // Error Handling
                        System.out.println(e);

                    }
                }
            }).start();



            new Thread(() -> {
                while(true){
                    try{

                    /*
                        Listen constantly to Quotations Queue for Quotation Responses
                        - If received, send onto response queue
                    */

                        // Get the next message from the Quotations queue
                        Message received_quotation_response = quotations_consumer.receive();
                        System.out.println("Recieved Quotation");

                        if (received_quotation_response instanceof ObjectMessage) {
                            Object content = ((ObjectMessage) received_quotation_response).getObject();
                            if (content instanceof QuotationResponseMessage) {
                                QuotationResponseMessage quotation_response_object = (QuotationResponseMessage) content;
//                            System.out.println(cache);

                              if(cache.get(quotation_response_object.id) != null){
                                  ClientApplicationMessage temporary_client_application_message = cache.get(quotation_response_object.id);
                                  temporary_client_application_message.quotations.add(quotation_response_object.quotation);
                                  System.out.println(temporary_client_application_message.quotations);
                              }

                            }
                            received_quotation_response.acknowledge();
                        } else {
                            System.out.println("Unknown received_quotation_response type: " +
                                    received_quotation_response.getClass().getCanonicalName());
                        }


                    } catch(Exception e){

                        // Error Handling
                        System.out.println(e);
                        System.out.println(e.getMessage());

                    }
                }

            }).start();



        } catch (Exception e) {

            // Error Handling
            System.out.println("Trouble: " + e);

        }
    }

    @Override
    public List<Quotation> getQuotations(ClientInfo info) {
        return null;
    }
}
