// Imports
package service.core;

import java.util.ArrayList;

public class ClientApplication {
    public long client_application_message_id;
    public ClientInfo client_info;
    public ArrayList<Quotation> quotations;

    public ClientApplication(long client_application_message_id_init , ClientInfo client_info_init ){
        this.client_application_message_id = client_application_message_id_init;
        this.client_info = client_info_init;
        this.quotations = new ArrayList<>();
    }


}
