package main.java.service.message;

public class QuotationResponseMessage implements Serializable {
 public long id;
 public Quotation quotation;

 public QuotationResponseMessage(long id, Quotation quotation) {
    this.id = id;
    this.quotation = quotation;
 }
 
} 
