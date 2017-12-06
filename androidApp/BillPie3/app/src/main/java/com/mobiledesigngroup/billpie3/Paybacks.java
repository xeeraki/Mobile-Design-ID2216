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
}
