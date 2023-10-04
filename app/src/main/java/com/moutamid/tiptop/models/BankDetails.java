package com.moutamid.tiptop.models;

public class BankDetails {

    String userID, bankID, name, email;

    public BankDetails() {
    }

    public BankDetails(String userID, String bankID, String name, String email) {
        this.userID = userID;
        this.bankID = bankID;
        this.name = name;
        this.email = email;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
