package com.mobiledesigngroup.billpie3;

/**
 * Created by eric.
 */

public class Paybacks {
    private String amount;
    private String event;
    private Boolean paid;
    private String payer;
    private String receiver;
    private String spending;
    private String date_paid;
    public Paybacks(){
        
    }

    public void setDate_paid(String date_paid) {
        this.date_paid = date_paid;
    }

    public String getDate_paid() {

        return date_paid;
    }

    public Paybacks(String amount, String event, Boolean paid, String payer, String receiver, String spending, String date_paid) {
        this.amount = amount;
        this.event = event;
        this.paid = paid;
        this.receiver = receiver;
        this.spending = spending;
        this.date_paid = date_paid;
        this.payer = payer;
    }

    public String getAmount() {
        return amount;
    }

    public String getEvent() {
        return event;
    }

    public Boolean getPaid() {
        return paid;
    }

    public String getPayer() {
        return payer;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSpending() {
        return spending;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSpending(String spending) {
        this.spending = spending;
    }

    public String toString() {
        return this.getPaid() + this.getReceiver() + this.getAmount() + this.getPayer() + this.getEvent() + this.getDate_paid() + this.getSpending();
    }
}
