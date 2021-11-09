
// Imports
package service.message;
import service.core.Quotation;

public class QuotationResponse implements MyInterface {

    // Property declarations
    private long id;
    private Quotation quotation;

    // Constructors

    public QuotationResponse(long id, Quotation quotation) {
        this.id = id;
        this.quotation = quotation;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    
}
