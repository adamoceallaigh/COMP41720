
// Imports
package service.message;
import service.core.ClientInfo;
import service.core.Quotation;
import java.io.Serializable;
import java.util.ArrayList;

public class ClientApplicationMessage implements Serializable {
    public long client_application_message_id;
    public ClientInfo client_info;
    public ArrayList<Quotation> quotations;

    public ClientApplicationMessage(long client_application_message_id_init , ClientInfo client_info_init ){
        this.client_application_message_id = client_application_message_id_init;
        this.client_info = client_info_init;
        this.quotations = new ArrayList<>();
    }


}
