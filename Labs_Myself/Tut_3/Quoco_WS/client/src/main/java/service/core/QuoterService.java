package service.core;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;
import javax.xml.ws.Endpoint;

@WebService
public interface QuoterService {
 	@WebMethod Quotation generateQuotation(ClientInfo info);
}