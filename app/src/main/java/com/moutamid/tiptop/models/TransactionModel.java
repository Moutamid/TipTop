package com.moutamid.tiptop.models;

public class TransactionModel {
    String ID, senderID, receiverID, price, senderName, receiverName, description;
    long timestamp;

    public TransactionModel() {
    }

    public TransactionModel(String ID, String senderID, String receiverID, String price, String senderName, String receiverName, String description, long timestamp) {
        this.ID = ID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.price = price;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
