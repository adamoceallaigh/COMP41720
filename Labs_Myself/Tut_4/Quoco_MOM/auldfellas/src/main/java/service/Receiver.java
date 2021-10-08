package main.java.service;

public class Receiver {

    static AFQService afq_service_instance = new AFQService();


    public static void main(String[] args) {

		// Function Variable Declarations
		String host = "";

		try {
			
			// Assigning host to host of the network
			host = args.length > 0 ?  args[0] : "0.0.0.0";

			// // Start up Auldfellas Quotation Service
			// Endpoint auldfellas_endpoint = Endpoint.create(new Quoter());
			// HttpServer auldfellas_server = HttpServer.create(new InetSocketAddress(9001) , 5);
			// auldfellas_server.setExecutor(Executors.newFixedThreadPool(5));
			// HttpContext auldfellas_context = auldfellas_server.createContext("/quotation");
			// auldfellas_endpoint.publish(auldfellas_context);
			// auldfellas_server.start();

            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
            Connection connection = factory.createConnection();
            connection.setClientID("auldfellas");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            Queue queue = session.createQueue("QUOTATIONS");
            Topic topic = session.createTopic("APPLICATIONS");
            MessageConsumer consumer = session.createConsumer(topic);
            MessageProducer producer = session.createProducer(queue); 

            connection.start();
            while (true) {
                // Get the next message from the APPLICATION topic
                Message message = consumer.receive();
                // Check it is the right type of message
                if (message instanceof ObjectMessage) {
                    // It’s an Object Message
                    Object content = ((ObjectMessage) message).getObject();
                    if (content instanceof QuotationRequestMessage) {
                        // It’s a Quotation Request Message
                        QuotationRequestMessage request = (QuotationRequestMessage) content;
                        // Generate a quotation and send a quotation response message…
                        Quotation quotation = afq_service_instance.generateQuotation(request.info);
                        Message response = session.createObjectMessage(new QuotationResponseMessage(request.id, quotation));
                        producer.send(response);
                    }
                } else {
                    System.out.println("Unknown message type: " +
                    message.getClass().getCanonicalName());
                }
            } 

	
		} catch (Exception e) {

			// Error Handling
			e.printStackTrace();

		}
	}
}
