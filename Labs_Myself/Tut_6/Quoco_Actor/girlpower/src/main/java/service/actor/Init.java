 
 // Imports
package service.actor;
import service.girlpower.GPQService;

 public class Init {

     // Static instance Variable Declarations
    private GPQService afq_service_instance;


	// Constructors

    public Init(GPQService afq_service_instance_init ) {
		this.afq_service_instance = afq_service_instance_init;
	}

	// Getters and Setters

	public GPQService getQuotationService() {
		return this.afq_service_instance;
	}


	public void setQuotationService(GPQService afq_service_instance) {
		this.afq_service_instance = afq_service_instance;
	}
 }
