package com.moutamid.tiptop.models;

public class BankDetails {

    String userID, name, email, phone, accountNumber, routingNumber, last4Digit, DOB, last4SSN;

    Address address;

    public BankDetails() {
    }

    public BankDetails(String userID, String name, String email, String phone, String accountNumber, String routingNumber, String last4Digit, String DOB, String last4SSN, Address address) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.last4Digit = last4Digit;
        this.DOB = DOB;
        this.last4SSN = last4SSN;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getLast4Digit() {
        return last4Digit;
    }

    public void setLast4Digit(String last4Digit) {
        this.last4Digit = last4Digit;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getLast4SSN() {
        return last4SSN;
    }

    public void setLast4SSN(String last4SSN) {
        this.last4SSN = last4SSN;
    }
}
