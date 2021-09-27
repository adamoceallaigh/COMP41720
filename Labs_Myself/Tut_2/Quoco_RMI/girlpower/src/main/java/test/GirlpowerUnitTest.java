package test;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import service.core.Constants;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;
import girlpower.GPQService;

import org.junit.*;
import static org.junit.Assert.assertNotNull;

public class GirlpowerUnitTest {

    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService gpqService = new GPQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService)
            UnicastRemoteObject.exportObject(gpqService,0);
            registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    } 

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
        registry.lookup(Constants.GIRL_POWER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {

        // Retrieve the quotation service
        QuotationService service = (QuotationService) registry.lookup(Constants.GIRL_POWER_SERVICE);

        // Test generation of a quotation from service using test data
        Quotation generated_quotation = service.generateQuotation(new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"));

        // Test whether generation has been created and returned
        System.out.println(generated_quotation.company);
        assertNotNull(generated_quotation);
        
    }
} 
