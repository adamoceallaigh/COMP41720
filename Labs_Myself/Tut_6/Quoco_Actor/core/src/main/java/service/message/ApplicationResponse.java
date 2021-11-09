
// Imports
package service.message;
import java.util.ArrayList;
import java.util.List;
import service.core.ClientInfo;
import service.core.Quotation;

public class ApplicationResponse implements MyInterface {

    // Property Declarations
    private long application_response_id;
    private ClientInfo client_info;
    private List<Quotation> quotations;
    
    // Constructors

    public ApplicationResponse(long application_response_id_init, ClientInfo client_info_init) {
        this.application_response_id = application_response_id_init;
        this.client_info = client_info_init;
        this.quotations = new ArrayList<>();
    }

    // Getters and Setters

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }


    public ClientInfo getClientInfo() {
        return client_info;
    }

    public void setClientInfo(ClientInfo client_info) {
        this.client_info = client_info;
    }

    public long get_application_response_id() {
        return application_response_id;
    }

    public void set_application_response_id(long application_response_id) {
        this.application_response_id = application_response_id;
    }

    
    
}
