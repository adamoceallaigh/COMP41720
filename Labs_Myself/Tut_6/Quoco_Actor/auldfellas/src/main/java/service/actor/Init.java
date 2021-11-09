 
 // Imports
package service.actor;

import service.auldfellas.AFQService;

 public class Init {

     // Static instance Variable Declarations
    private AFQService afq_service_instance;


	// Constructor

    public Init(AFQService afq_service_instance_init ) {
		this.afq_service_instance = afq_service_instance_init;
	}


	// Getters and Setters 

	public AFQService getQuotationService() {
		return this.afq_service_instance;
	}


	public void setQuotationService(AFQService afq_service_instance) {
		this.afq_service_instance = afq_service_instance;
	}
	
 }
