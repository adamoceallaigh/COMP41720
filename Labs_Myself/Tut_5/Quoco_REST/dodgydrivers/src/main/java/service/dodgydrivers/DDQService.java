package service.dodgydrivers; // Imports
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.Quotation;

/**
  * Implementation of the AuldFellas insurance quotation service.
  * @author Rem
  *
 */

 @RestController
 public class DDQService extends AbstractQuotationService {

	// All references are to be prefixed with an DD (e.g. DD001000)
    public static final String PREFIX = "DD";
    public static final String COMPANY = "Dodgy Drivers Corp.";
    private Map<String, Quotation> quotations = new HashMap<>();

    /*
      Quote generation:
      30% discount for being male
      2% discount per year over 60
      20% discount for less than 3 penalty points
      50% penalty (i.e. reduction in discount) for more than 60 penalty points
     */

    /**
     * Generates Quotation for the AuldFellas Service using the client's info
     * @return Quotation
     */
    public Quotation generateQuotation(ClientInfo client_info) {

        // Create an initial quotation between 600 and 1200
        double price = generatePrice(600, 600);

        // Automatic 30% discount for being male
        int discount = (client_info.getGender() == ClientInfo.MALE) ? 30:0;

        // Automatic 2% discount per year over 60...
        discount += (client_info.getAge() > 60) ? (2*(client_info.getAge()-60)) : 0;

        // Add a points discount
        discount += getPointsDiscount(client_info.getPoints());

        // Generate the quotation and send it back
        return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);

    }

    /**
     * Calculates the discount based on penalty points received
     * @return integer representing discount received
    */

    private int getPointsDiscount(int penalty_points_received) {

        // 20% discount for less than 3 penalty points
        if (penalty_points_received < 3) return 20;

        // No Discount for 3 - 6 penalty points
        if (penalty_points_received <= 6) return 0;

        // 50% added on for anything over
        return -50;
    }

    /*
     GET 
    */ 

    // Reference for Quotation

    @RequestMapping(value="/quotations/{reference}",method=RequestMethod.GET)

    public Quotation getResource(@PathVariable("reference") String reference) {

       Quotation quotation = quotations.get(reference);
       if (quotation == null) throw new NoSuchQuotationException();
       return quotation;

    } 

   
   /*
    POST 
   */

   // New Quotation

   @RequestMapping(value="/quotations",method=RequestMethod.POST)

   public ResponseEntity<Quotation> createQuotation(@RequestBody ClientInfo info) throws URISyntaxException {

       Quotation quotation = generateQuotation(info);
       quotations.put(quotation.getReference(), quotation);
       String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/quotations/"+quotation.getReference();
       HttpHeaders headers = new HttpHeaders();
       headers.setLocation(new URI(path));
       return new ResponseEntity<>(quotation, headers, HttpStatus.CREATED);

   } 



}
