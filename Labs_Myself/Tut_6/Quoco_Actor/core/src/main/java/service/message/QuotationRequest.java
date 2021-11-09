
// Imports 
package service.message;
import service.core.ClientInfo;

public class QuotationRequest implements MyInterface {

    // Property Declarations
    public long id;
    public ClientInfo info;


    // Constructors

    public QuotationRequest(long id, ClientInfo info) {
        this.id = id;
        this.info = info;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ClientInfo getInfo() {
        return info;
    }

    public void setInfo(ClientInfo info) {
        this.info = info;
    }

    
}
