
// Imports
package service.message;
import service.core.ClientInfo;

public class ApplicationRequest implements MyInterface {

    // Property Declarations
    private long application_request_id;
    private ClientInfo client_info;
    

    // Constructors

    public ApplicationRequest(long application_request_id_init, ClientInfo client_info_init) {
        this.application_request_id = application_request_id_init;
        this.client_info = client_info_init;
    }
    

    // Getters and Setters

    public ClientInfo getClientInfo() {
        return client_info;
    }

    public void setClientInfo(ClientInfo client_info) {
        this.client_info = client_info;
    }


    public long get_application_request_id() {
        return application_request_id;
    }


    public void set_application_request_id(long application_request_id) {
        this.application_request_id = application_request_id;
    }

    
}
