package service.message;

import service.core.Quotation;

import java.io.Serializable;

public class QuotationResponseMessage implements Serializable {
    public long id;
    public Quotation quotation;

    public QuotationResponseMessage(long id, Quotation quotation) {
        this.id = id;
        this.quotation = quotation;
    }
}
