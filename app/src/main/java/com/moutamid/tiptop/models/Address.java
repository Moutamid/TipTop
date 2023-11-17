package com.moutamid.tiptop.models;

public class Address {
    String country, city, address, state, postalCode;

    public Address() {
    }

    public Address(String country, String city, String address, String state, String postalCode) {
        this.country = country;
        this.city = city;
        this.address = address;
        this.state = state;
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
