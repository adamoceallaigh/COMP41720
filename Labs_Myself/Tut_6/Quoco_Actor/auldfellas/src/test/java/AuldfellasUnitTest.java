

// Imports
import org.junit.*;
import akka.actor.*;
import akka.testkit.javadsl.*;
import service.actor.Init;
import service.actor.Quoter;
import service.auldfellas.AFQService;
import service.core.ClientInfo;
import service.message.QuotationRequest;
import service.message.QuotationResponse;

public class AuldfellasUnitTest {

    // Variables for tests
    static ActorSystem system;

    /*
        Setup for Tests 
    */

    @BeforeClass
    public static void setup() {
         try {

            // Set up Actor System
            system = ActorSystem.create();

         } catch (Exception e) {

             // Error Handling
             System.out.println("Trouble: " + e);

         }
     }


    @AfterClass
    public static void teardown() {
         try {
 
            // Destroy Actor system
           TestKit.shutdownActorSystem(system);
           system = null;
 
         } catch (Exception e) {
 
             // Error Handling
             System.out.println("Trouble: " + e);
 
         }
     }


    /**
      * Tests
    */

    @Test
    public void testQuoter() {
        try {

            // Add Test ref to actor system
            ActorRef quoterRef = system.actorOf(Props.create(Quoter.class), "test");
            TestKit probe = new TestKit(system);

            // Testing test ref with quotation request
            quoterRef.tell(new Init(new AFQService()), null);
            quoterRef.tell(
                new QuotationRequest(1, new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1")), 
                probe.getRef()
            );

            // Checking returns Quotation Response object
            probe.awaitCond(probe::msgAvailable);
            probe.expectMsgClass(QuotationResponse.class);
            
        } catch (Exception e) {

            // Error Handling
            System.out.println(e.getMessage());
        }
        
    } 



    

    
 }
