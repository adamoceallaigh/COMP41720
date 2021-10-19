
// Imports
package service.broker;

import java.net.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.core.*;

/*
    Broker REST Controller Functionalities
    - Return Single Client Application Referenced By ID inputted
    - Return All Client Applications from cache
    - Post Single Quotation to services for quotes
*/

@RestController
public class LocalBrokerService {

    // Static instance Variable Declarations
    static HashMap<Long, ClientApplication> cache = new HashMap<>();

    // Retrieving service urls from Spring - application.properties
    @Value("#{'${server.service_urls}'.split(',\\s*')}")
    List<String> service_urls;



    /*
     GET 
    */ 

    // Quotation By ID
    @RequestMapping(value="/quotation" , method=RequestMethod.GET)
    public ResponseEntity<ClientApplication> getQuotationByID(@RequestParam("client_application_id") Optional<String> client_application_id){

        try {

            // Variables for process
            Long client_application_id_long;
            ClientApplication found_client_application = null;
    
            // Check url contains a request param
            if(!client_application_id.isPresent()) return null;
            else client_application_id_long = Long.parseLong(client_application_id.get());
            

            // Cache Contains Id ?
            if(!cache.containsKey(client_application_id_long)) return null;
            found_client_application = cache.get(client_application_id_long);


            // Return Client Application Matching ID
            String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/quotation/";
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(path));
            return new ResponseEntity<>(found_client_application, headers, HttpStatus.CREATED);            
            
        } catch (Exception e) {

            // Error Handling
            System.out.println(e);
        }

        return null;
    }
    

    // All Quotations
    @RequestMapping(value="/quotations" , method=RequestMethod.GET)
    public ResponseEntity<List<ClientApplication>> getQuotations(){

        try {

           // Variables for process
           List<ClientApplication> client_applications_in_cache = new ArrayList<ClientApplication>();

            /*
                1. Loop through Cache
                2. Add Client Applications to Array
            */
           for(ClientApplication temp_client_application : cache.values()){
               client_applications_in_cache.add(temp_client_application);
           }

           // Return All Client Applications
            String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/quotations/";
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(path));
            return new ResponseEntity<>(client_applications_in_cache, headers, HttpStatus.CREATED);            
            
        } catch (Exception e) {

            // Error Handling
            System.out.println(e);
        }

        return null;
    }


   /*
    POST 
   */

   // Quotation
   @RequestMapping(value="/applications", method=RequestMethod.POST)
    public ResponseEntity<ClientApplication> postQuotation(@RequestBody ClientApplication info) {

        try{

            // Variables for process
            RestTemplate restTemplate = new RestTemplate();
            ClientApplication temporary_client_application_message = null;
            HttpEntity<ClientApplication> quotation_request = new HttpEntity<>(info);
    
    
            // Loop through service urls passed into broker
            for (String url : service_urls){
    
                // Set up ClientApplicationMessage in cache for service verification later
                if(!cache.containsKey(info.client_application_message_id)) {
                    cache.put(info.client_application_message_id, new ClientApplication(info.client_application_message_id, info.client_info));
                }
    
                // Retrieve Quotation from each of the services
                Quotation quotation = restTemplate.postForObject(url + "quotations", quotation_request, Quotation.class);
    
                 /*
                    Check quotation response id against cache
                        - If present, add quotation received to it's quotations array
                */
                
                if(cache.get(info.client_application_message_id) != null){
                    temporary_client_application_message = cache.get(info.client_application_message_id);
                    temporary_client_application_message.quotations.add(quotation);
                }
            }
    
            // Return Client Application Created
            String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/applications/";
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(path));
            return new ResponseEntity<>(temporary_client_application_message, headers, HttpStatus.CREATED);

        } catch (URISyntaxException uri_error){

              // Error Handling
              System.out.println(uri_error);

        }

        return null;

    }

}
