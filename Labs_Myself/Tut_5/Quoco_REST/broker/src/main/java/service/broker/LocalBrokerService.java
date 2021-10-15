package service.broker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import service.core.*;

public class LocalBrokerService {

    // Static instance Variable Declarations
    static HashMap<Long, ClientApplication> cache = new HashMap<>();


    /*
     GET 
    */ 

    // Quotations

    @RequestMapping(value="/applications",method=RequestMethod.GET)
    public List<Quotation> getQuotations(ClientApplication info) {

        // Function Variable Declarations
		List<String> urls = new LinkedList<String>(Arrays.asList(
            "http://localhost:8081",
            "http://localhost:8082",
            "http://localhost:8083"
        ));
        RestTemplate restTemplate = new RestTemplate();
        ClientApplication temporary_client_application_message = null;

        for (String url : urls){

            // HttpEntity<ClientApplication> quotation_request = new HttpEntity<>(info);

            // Set up ClientApplicationMessage in cache for service verification later
            if(!cache.containsKey(info.client_application_message_id)) {
                cache.put(info.client_application_message_id, new ClientApplication(info.client_application_message_id, info.client_info));
            }

            
            Quotation quotation = restTemplate.getForObject(url + "quotations", Quotation.class);

             /*
                Check quotation response id against cache
                    - If present, add quotation received to it's quotations array
            */
            
            if(cache.get(info.client_application_message_id) != null){
                temporary_client_application_message = cache.get(info.client_application_message_id);
                temporary_client_application_message.quotations.add(quotation);
            }
        }

        return temporary_client_application_message.quotations;


    }


   /*
    POST 
   */

   // New Quotation

//    @RequestMapping(value="/applications",method=RequestMethod.POST)

//    public List<Quotation> createQuotation(@RequestBody ClientInfo info)  {

//     for (String url : urls){
//         HttpEntity<ClientInfo> request = new HttpEntity<>(info);
//         Quotation quotation = restTemplate.postForObject(url + "quotations", request, Quotation.class);
//         quotations.add(quotation);
//     }

//    } 
}
