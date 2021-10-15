// Imports
package service.broker;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import service.core.ClientInfo;
import service.core.Quotation;

/*
* Broker's Job
*  - Loop through all the Quotation Service URL's
*  - Obtain Quotations for each service 
*  - Send Them bacck to Client
*/


public class Application {

    private Map<String, Quotation> quotations = new HashMap<>();

    public static void main(String[] args) {



        RestTemplate restTemplate = new RestTemplate();

        // for (ClientInfo temp_client: clients) {


        //     HttpEntity<ClientInfo> request = new HttpEntity<>(temp_client);
        //     Quotation quotation = restTemplate.postForObject("http://localhost:8083/quotations",
        //     request, Quotation.class);
        //     displayProfile(clients[0]);
        //     displayQuotation(quotation); 

        // }

        
        
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
