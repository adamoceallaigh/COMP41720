
// Imports
// import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


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

         RestTemplate restTemplate = new RestTemplate();
         for (ClientInfo temp_client: clients) {

            ClientApplication initial_quotation_request = new ClientApplication(SEED_ID++, temp_client);
            cache.put(initial_quotation_request.client_application_message_id, initial_quotation_request.client_info);
            // HttpEntity<ClientApplication> request = new HttpEntity<>(initial_quotation_request);

            Object quotation = restTemplate.getForObject("http://"+host+":"+ port +"/applications", ClientApplication.class);
            if (quotation instanceof ClientApplication) {

                // It’s a ClientApplication
                ClientApplication final_quotation_response = (ClientApplication) quotation;

                // Receive the id of message from cache
                ClientInfo info = cache.get(final_quotation_response.client_application_message_id);
                displayProfile(info);

                /*
                    1. Loop through Client Application Message's Quotations Array, 
                    2. Display Quotation
                */
                for (Quotation temp_quotation: final_quotation_response.quotations ){
                    displayQuotation(temp_quotation);
                }


                System.out.println("\n");

            }


        }



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
                "| Name: " + String.format("%1$-29s", info.getName()) +
                        " | Gender: " + String.format("%1$-27s", (info.getGender()==ClientInfo.MALE?"Male":"Female")) +
                        " | Age: " + String.format("%1$-30s", info.getAge())+" |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) +
                        " | No Claims: " + String.format("%1$-24s", info.getNoClaims()+" years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.getPoints())+" |");
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
                "| Company: " + String.format("%1$-26s", quotation.getCompany()) +
                        " | Reference: " + String.format("%1$-24s", quotation.getReference()) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
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
