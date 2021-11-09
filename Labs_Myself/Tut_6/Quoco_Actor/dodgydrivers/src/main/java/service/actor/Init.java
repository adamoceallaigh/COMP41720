 
 // Imports
package service.actor;
import service.dodgydrivers.DDQService;

 public class Init {

     // Static instance Variable Declarations
    private DDQService afq_service_instance;


	// Constructors
	
    public Init(DDQService afq_service_instance_init ) {
		this.afq_service_instance = afq_service_instance_init;
	}

	// Getters and Setters

	public DDQService getQuotationService() {
		return this.afq_service_instance;
	}


	public void setQuotationService(DDQService afq_service_instance) {
		this.afq_service_instance = afq_service_instance;
	}

 }
