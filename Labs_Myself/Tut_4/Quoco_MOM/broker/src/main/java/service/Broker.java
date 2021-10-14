// Imports
package service;
import org.apache.activemq.ActiveMQConnectionFactory;
import service.message.*;

import javax.jms.*;
import java.util.HashMap;


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



public class Broker {

    // Static instance Variable Declarations
    static HashMap<Long, ClientApplicationMessage> cache = new HashMap<>();

    public static void main(String[] args) {

        try {

            // Variables for process
            String host = args.length > 0 ? args[0] : "0.0.0.0";
            int port = 61616;

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
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":"+port+"");

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

                        // Check it is the right type of message
                        if (received_request_message instanceof ObjectMessage) {

                            // It’s an Object Message
                            Object content = ((ObjectMessage) received_request_message).getObject();

                            if (content instanceof QuotationRequestMessage) {

                                // It’s a Quotation Request Message
                                QuotationRequestMessage quotation_request = (QuotationRequestMessage) content;
                                Message quotation_response_message = session.createObjectMessage(quotation_request);

                                // Set up ClientApplicationMessage in cache for service verification later
                                if(!cache.containsKey(quotation_request.id)) {
                                    cache.put(quotation_request.id, new ClientApplicationMessage(quotation_request.id , quotation_request.info));
                                }

                                // Send quotation request message on to applications topic
                                applications_producer.send(quotation_response_message);

                                // Wait for 10 seconds - for service quotations received in other thread
                                Thread.sleep(10000);    

                                // Send Client Application Message back to Response Queue
                                Message final_quotation_response_sent = session.createObjectMessage(cache.get(quotation_request.id));
                                responses_producer.send(final_quotation_response_sent);

                            }

                            // Acknowledge the quotation request has been received
                            received_request_message.acknowledge();

                        } else {

                            // Error Handling
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

                        // Check it is the right type of message
                        if (received_quotation_response instanceof ObjectMessage) {

                            // It’s an Object Message
                            Object content = ((ObjectMessage) received_quotation_response).getObject();

                            if (content instanceof QuotationResponseMessage) {

                                // It’s a Quotation Response Message
                                QuotationResponseMessage quotation_response_object = (QuotationResponseMessage) content;

                                /*
                                    Check quotation response id against cache
                                        - If present, add quotation received to it's quotations array
                                */
                              if(cache.get(quotation_response_object.id) != null){
                                  ClientApplicationMessage temporary_client_application_message = cache.get(quotation_response_object.id);
                                  temporary_client_application_message.quotations.add(quotation_response_object.quotation);
                              }

                            }

                            // Acknowledge the quotation response has been received
                            received_quotation_response.acknowledge();

                        } else {

                            // Error Handling
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

}
